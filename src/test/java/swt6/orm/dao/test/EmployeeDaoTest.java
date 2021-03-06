package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.EmployeeDao;
import swt6.orm.dao.bitronix.EmployeeDaoImpl;
import swt6.util.DbSetupUtil;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EmployeeDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final Long id3 = 103L;
    private static final EmployeeDao dao = new EmployeeDaoImpl();
    private static final PersistenceManager mgr = PersistenceManagerFactory.getManager();

    @BeforeClass
    public static void init() {
        mgr.initFactory("WorkLogTestPU");
    }

    @AfterClass
    public static void close() {
        mgr.closeFactory();
    }

    @Before
    public void prepare() throws Exception {
        Operation operation = Operations.sequenceOf(
                DataOperations.DELETE_ALL,
                DataOperations.INSERT_ADDRESSES,
                DataOperations.INSERT_EMPLOYEES,
                DataOperations.INSERT_PROJECTS,
                DataOperations.INSERT_PROJECTEMPLOYEE);
        tracker.launchIfNecessary(DbSetupUtil.getTestDbSetup(operation));
    }


    @Test
    public void testAdd() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertEquals(3, dao.findAll().size());
                    Employee e = new Employee("goi", "dsfu", DateUtil.getDate(1994, 3, 4));
                    Address a = new Address("4232", "Hagenberg", "Softwarepark 14");
                    e.setAddress(a);
                    e = dao.addOrUpdate(e);
                    assertNotNull(e.getId());
                    assertEquals(4, dao.findAll().size());
                }));
    }

    @Test
    public void testGetById() {
        assertTrue(
                mgr.executeTransaction(() -> assertNotNull(dao.findById(id1))));
    }

    @Test
    public void testUpdate() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    final String otherName = "OtherName";
                    Employee empl = dao.findById(id1);
                    assertNotNull(empl);
                    assertNotEquals(empl.getFirstName(), otherName);
                    empl.setFirstName(otherName);
                    empl = dao.addOrUpdate(empl);
                    Employee otherEmpl = dao.findById(empl.getId());
                    assertEquals(otherEmpl.getFirstName(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Employee empl = dao.findById(id2);
                    assertNotNull(empl);
                    dao.remove(empl);
                    Employee nullEmpl = dao.findById(id2);
                    assertNull(nullEmpl);

                    empl = dao.findById(id3);
                    assertNotNull(empl);
                    dao.removeById(id3);
                    nullEmpl = dao.findById(id3);
                    assertNull(nullEmpl);
                }));
    }

    @Test
    public void testFindByName() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertTrue(dao.findByName("Herbert").size() == 1);
                    assertTrue(dao.findByName("ier").size() == 2);
                    assertNotNull(dao.findByName("Haribo").get(0));
                }));
    }

    @Test
    public void testFindByProject() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertTrue(dao.findByProject(id1).size() == 2);
                    assertTrue(dao.findByProject(id2).size() == 2);
                }));
    }
}
