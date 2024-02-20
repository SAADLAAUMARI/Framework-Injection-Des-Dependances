package ma.enset.metier;

import FrameworkSpringJava.dao.Autowired;
import FrameworkSpringJava.dao.Service;
import ma.enset.dao.IDao;


@Service("metier")
public class MetierImpl implements Imetier {
    @Autowired

    private IDao dao=null; //coublage faible
    @Override
    public double calcul() {
        double tmp=dao.getdate();
        double resultat =tmp*420 / Math.tan(tmp*Math.PI);
        return resultat;
    }
  /*
   Inejecter dans la variable dao  un object une classe qui implémente l'interface IDao
   */
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
