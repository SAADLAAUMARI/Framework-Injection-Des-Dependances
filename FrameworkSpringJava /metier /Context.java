package FrameworkSpringJava.metier;

import FrameworkSpringJava.dao.Bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Context implements IContext {

    List<Bean> beanList = new ArrayList<>();
    protected Context()
    {

    }
    @Override
    public Object getBean(String id) {
        for (Bean bean:beanList) {
            if(bean.getId().equals(id)){
                try {
                    return beanFactory(bean);
                }catch (InstantiationException | IllegalAccessException| ClassNotFoundException e)
                {
                    throw  new RuntimeException(e);
                }
            }
        }
        return null;
    }

    @Override
    public Object beanFactory(Bean bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class className = Class.forName(bean.getClassName());
        Object object = className.newInstance();
        if (bean.getProperty()!=null) {

            String name = bean.getProperty().getName();
            String value =bean.getProperty().getValue();
            Boolean isDone = false;

            // injection par Setter
            if(!isDone){
                try {
                    BySetter(className,object,name,value);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }

            // injection par Constructeur

            if(!isDone){
                try {
                    byConstructor(className,object,name,value);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }

            // injection par attribut (Field)

            if(!isDone){
                try {
                   byField(className,object,name,value);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return object;
    }

    @Override
    public void byField(Class className, Object object, String name, String ref) {
        Field[]  fields = className.getDeclaredFields();
        for(Field field:fields){
            if(field.getName().equals(name)|| field.getName().equals(name.toLowerCase())){
                try{
                    Object objectTemp=getBean(ref);
                    field.setAccessible(true);
                    field.set(object,objectTemp);
                }catch (Exception e)
                {
                    throw  new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void byConstructor(Class className, Object object, String name, String ref) {
        Constructor[] constructors = className.getConstructors();
        for(Constructor constructor:constructors){
            if(constructor.getParameterCount()==1)
            {
                try{
                    Object objectTmp=getBean(ref);
                    object=constructor.newInstance(objectTmp);
                }
                catch (Exception e){
                    throw new RuntimeException();
                }
            }
        }
    }

    @Override
    public void BySetter(Class className, Object object, String name, String ref) {
        Method[] methods = className.getMethods();
        for (Method method:methods){
            if(method.getName().equals("set"+name)){
                try {
                   Object objectTemp= getBean(ref);
                   method.invoke(object,objectTemp);
                 }
                catch (Exception e){
                    throw new RuntimeException(e);
                 }
            }
        }
    }
}
