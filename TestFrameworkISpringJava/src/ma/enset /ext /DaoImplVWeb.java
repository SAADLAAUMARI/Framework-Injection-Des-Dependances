package ma.enset.ext;

import FrameworkSpringJava.dao.Service;
import ma.enset.dao.IDao;


@Service("dao")
public class DaoImplVWeb implements IDao {
    @Override
    public double getdate() {
        System.out.println("Version web Service");
        return 90;
    }
}
