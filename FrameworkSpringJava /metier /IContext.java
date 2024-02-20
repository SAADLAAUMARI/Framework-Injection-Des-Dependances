package FrameworkSpringJava.metier;

import FrameworkSpringJava.dao.Bean;


public interface IContext {
    Object getBean(String id);
    Object beanFactory(Bean bean) throws  InstantiationException,IllegalAccessException,ClassNotFoundException;
    void byField(Class className,Object object,String name,String ref);
    void byConstructor(Class className,Object object,String name,String ref);
    void BySetter(Class className,Object object,String name,String ref);
}
