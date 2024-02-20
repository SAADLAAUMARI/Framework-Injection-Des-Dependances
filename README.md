# Framework-Injection-Des-Dependances
## Objectifs 

> Ce travail a pour objectif de créer un mini framework d’injection des dépendances, afin de l’utiliser en créant des applications Java légère à l'aide de notre propre implémentation d'injection de dépendance.

> Dans ce travail on va concevoir et créer un mini Framework d'injection des dépendances similaire à Spring IOC.

> Le mini Framework va permettre de faire l'injection des dépendances entre les différents composant de son application respectant les possibilités suivantes :

1. A travers un fichier XML de configuration 
2. En utilisant les annotations
3. Possibilité d'injection via :
    - Le constructeur
    - Le Setter
    - Attribut (accès direct à l'attribut : Field)

## les dependances 

Dans le cadre de notre projet, l'intégration de la dépendance JAXB a été essentielle.

Mais qu'est-ce que JAXB?

JAXB, abréviation de Java Architecture for XML Binding, représente une API Java permettant de réaliser un mapping entre les classes Java et leurs équivalents en XML, et inversement. Cette architecture offre une solution pratique pour la sérialisation (conversion des objets Java en XML) et la désérialisation (conversion du XML en objets Java) des données.

Un aspect clé de l'API JAXB réside dans ses annotations, qui offrent la possibilité de personnaliser la manière dont les classes Java et leurs propriétés sont représentées en XML. En appliquant ces annotations aux classes et aux propriétés, les développeurs ont la possibilité de spécifier avec précision comment les objets doivent être convertis en XML, ainsi que la méthode de conversion des éléments XML en objets Java.


## Les entites 
dans le package ```FrameworkSpringJava.dao``` nous avons cree nos classe ;
 - Bean : représente une instance d'une classe qui est créée, configurée et gérée par le conteneur.
 - Beans : fait référence à l'élément racine d'une configuration de contexte (généralement définie dans un fichier XML ou annotée dans une classe Java).
 - Property : fait référence à la méthode d'injection de dépendances utilisée pour définir les valeurs des propriétés d'un Bean.
 
 - ```Beans.java```
 ``` java
 
@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Beans {
    @XmlElement(name = "bean")
    private List<Bean> beanList=null;

    public List<Bean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<Bean> beanList) {
        this.beanList = beanList;
    }
}
 ```
 - ```Bean.java```
 ``` java
 @XmlRootElement(name = "bean")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "class")
    private String className;

    @XmlElement(name = "property")
    private Property property;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property= property;
    }
}

 ```
 - ```Property.java```
 ``` java
 @XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "value")
    private String value;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
 ```
## Créer des annotations
Nous avons creer deux annotations :
- ``` @Service ``` : annotation utilisée pour marquer une classe en tant que composant de service dans une application.
- ``` @Autowired ``` : une annotation utilisée pour l'injection automatique de dépendances.

- ``` Interface Authowrid ```
``` java 
  @Target({METHOD,CONSTRUCTOR,FIELD,PARAMETER,ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  public @interface Autowired {
      boolean required() default true;
  }
```
- ``` Interface Service ```
``` java
  @Retention(RUNTIME)
  @Target(TYPE)
  public @interface Service {
      String value() default "";
  }
```

## Injection A travers Fichier XML.

Nous avons creer dans le package ```ContextXML``` qui va nous permet de faire l'inejection de dependance a travers un fichier xml.

- ``` ContextXML.java ```
``` java 
public class ContextXML extends Context{
    String path;

    public ContextXML(String path) {
        this.path = path;
        loadAll();
    }

    public void loadAll() {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Beans beans = (Beans) unmarshaller.unmarshal(Context.class.getResourceAsStream("/"+path));
            for (Bean bean : beans.getBeanList()) {
                beanList.add(bean);}
        }catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## Injection A travers Annotation.


Nous avons creer dans le package ```ContextAnn``` qui va nous permet de faire l'inejection de dependance a travers les annotations.

- ``` ContextAnn.java ```
``` java 
public class ContextAnn extends Context{

    ArrayList<String> packages = new ArrayList<>();

    public ContextAnn(String... packages) {
        for (String packag:packages
             ) {
            this.packages.add(packag);
        }
        loadAll();
    }
    public void loadAll() {
            for(String pack :packages){
                ClasseLoader classeLoader = new ClasseLoader();
                ArrayList<Class> classes = classeLoader.findAllClassesUsingClassLoader(pack);
                for (Class clss:classes){
                    // chercher toute les notations de classe
                    if(clss.isAnnotationPresent(Service.class)){
                        Service service =(Service) clss.getAnnotation(Service.class);
                        Bean bean = new Bean();
                        if (!service.value().equals(""))bean.setId(service.value());
                        Boolean isDone = false;
                        // chercher Autowired annotation
                        Method[] methods=clss.getMethods();

                        for(Method method:methods){
                            if(method.isAnnotationPresent(Autowired.class)){
                                Property property = new Property();
                                // si la methodes est setter
                                String nom = method.getName().substring(3);
                                property.setName(nom);
                                property.setValue(nom.toLowerCase());
                                isDone=true;
                            }
                        }
                        // si la methodes est no authowired
                        if (!isDone){
                            Field[] fields = clss.getDeclaredFields();
                            for (Field field:fields){
                                if(field.isAnnotationPresent(Autowired.class)){
                                    Property property = new Property();
                                    property.setName(field.getName());
                                    property.setValue(field.getName().toLowerCase());
                                    bean.setProperty(property);
                                    isDone=true;
                                }
                            }
                        }
                        // Constructeur
                        if (!isDone){
                            Constructor[] constructors = clss.getConstructors();
                            for (Constructor constructor:constructors){
                                if(constructor.isAnnotationPresent(Autowired.class)){
                                    Property property = new Property();
                                    property.setName(constructor.getName());
                                    property.setValue(constructor.getName().toLowerCase());
                                    bean.setProperty(property);
                                    isDone=true;
                                }
                            }
                        }
                        bean.setClassName(clss.getName());
                        beanList.add(bean);

                    }
                }
            }
    }
}
```
## Generer un fichier Jar
Afin d'utiliser ce framework il faut generer un fichier jar.
1. Dans le menu principal, sélectionnez  File | Project Structure  ``` Ctrl+Alt+Shift+S ``` et cliquez sur Artifacts.
2. Cliquez sur + , pointez sur JAR et sélectionnez From modules with dependencies.
3. Cliquer sur ``` ok ```. 
4. Finalement, Dans le menu principal, sélectionnez Build | Build Artifacts.
5. Si vous regardez maintenant le dossier out/artifacts , vous y trouverez votre fichier .jar.

# Test de Framework Injection des dépendance

En renprend le code de l'activite 1. 
Ajouter le fichier jar generer comme un dependance. 
Cree un fichier conf.xml pour utliser Injection par fichier XML 

- ```Config.xml```
``` xml 
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<beans>
    <bean id="dao" class="ma.enset.ext.DaoImplVWeb">
    </bean>
    <bean id="metier" class="ma.enset.metier.MetierImpl">
        <property name="Dao" value="dao">

        </property>
    </bean>
</beans>
```
## Resultat Injection par fichier XML.

![237512975-6bc38019-39f5-4089-b7fa-6bea349d95c6](https://github.com/SAADLAAUMARI/Framework-Injection-Des-Dependances/assets/133123024/9fc93a07-8230-4f5a-9144-908628c08e61)


## Resultat Injection par Annotation.

![237512975-6bc38019-39f5-4089-b7fa-6bea349d95c6 (1)](https://github.com/SAADLAAUMARI/Framework-Injection-Des-Dependances/assets/133123024/48f760fe-362f-4efb-b904-7bc071b04362)

