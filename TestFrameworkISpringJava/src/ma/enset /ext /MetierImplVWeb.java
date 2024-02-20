package ma.enset.ext;

import FrameworkSpringJava.dao.Service;
import ma.enset.dao.IDao;
import ma.enset.metier.Imetier;

@Service("metier")
public class MetierImplVWeb implements Imetier {
    IDao dao =null;
    @Override
    public double calcul() {
        System.out.println("Version web Service");
        return 90;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
