package FrameworkSpringJava.metier;

import FrameworkSpringJava.dao.Autowired;
import FrameworkSpringJava.dao.Bean;
import FrameworkSpringJava.dao.Property;
import FrameworkSpringJava.dao.Service;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


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
