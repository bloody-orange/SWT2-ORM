package swt6.orm.dao.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddressDaoTest.class,
        EmployeeDaoTest.class,
        ProjectDaoTest.class,
        ModuleDaoTest.class,
        PhaseDaoTest.class,
        IssueDaoTest.class
})
public class DaoTestSuite {
}