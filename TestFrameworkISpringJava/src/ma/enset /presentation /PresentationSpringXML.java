package ma.enset.presentation;

import FrameworkSpringJava.metier.Context;
import FrameworkSpringJava.metier.ContextXML;
import ma.enset.dao.IDao;
import ma.enset.metier.Imetier;


public class PresentationSpringXML {
    public static void main(String[] args) {
        Context context = new ContextXML("config.xml");
        Imetier imetier =(Imetier) context.getBean("metier");
        System.out.println(imetier.calcul());
    }
}
