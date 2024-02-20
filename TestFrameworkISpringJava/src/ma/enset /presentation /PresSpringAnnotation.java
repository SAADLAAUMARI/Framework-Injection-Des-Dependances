package ma.enset.presentation;

import FrameworkSpringJava.metier.Context;
import FrameworkSpringJava.metier.ContextAnn;
import ma.enset.metier.Imetier;


public class PresSpringAnnotation {
    public static void main(String[] args) {
        Context context = new ContextAnn("ma.enset.metier","ma.enset.ext");
        Imetier imetier = (Imetier) context.getBean("metier");
        System.out.println(imetier.calcul());

    }
}
