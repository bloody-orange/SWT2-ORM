package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.AddressDao;
import swt6.orm.dao.EmployeeDao;
import swt6.orm.dao.impl.AddressDaoImpl;
import swt6.orm.dao.impl.EmployeeDaoImpl;
import swt6.orm.domain.Address;
import swt6.orm.domain.AddressId;
import swt6.orm.domain.Employee;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EmployeeDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 1001L;
    private static final Long id2 = 1002L;
    private static final EmployeeDao dao = new EmployeeDaoImpl();
    private static final PersistenceManager mgr = PersistenceManagerFactory.getManager();

    @BeforeClass
    public static void init() {
        mgr.initFactory();
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
                DataOperations.INSERT_PROJECTEMPLOYEE);

        DbSetup dbSetup = new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true", "app", "derby"), operation);
        tracker.launchIfNecessary(dbSetup);
    }


    @Test
    public void testAdd() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertEquals(3, dao.getAll().size());
                    Employee e = new Employee("hi", "du", DateUtil.getDate(1993, 3, 4));
                    e = dao.addOrUpdate(e);
                    Address a = new Address("4232", "Hagenberg", "Teststraße 42");
                    e.setAddress(a);
                    assertNotNull(e.getId());
                    assertEquals(4, dao.getAll().size());
                }));
    }

    @Test
    public void testGetById() {
        assertTrue(
                mgr.executeTransaction(() -> assertNotNull(dao.getById(id1))));
    }

    @Test
    public void testUpdate() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    final String otherName = "OtherName";
                    Employee empl = dao.getById(id1);
                    assertNotNull(empl);
                    assertNotEquals(empl.getFirstName(), otherName);
                    empl.setFirstName(otherName);
                    empl = dao.addOrUpdate(empl);
                    Employee otherEmpl = dao.getById(empl.getId());
                    assertEquals(otherEmpl.getFirstName(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Employee empl = dao.getById(id1);
                    assertNotNull(empl);
                    dao.remove(empl);
                    Employee nullEmpl = dao.getById(id1);
                    assertNull(nullEmpl);

                    empl = dao.getById(id2);
                    assertNotNull(empl);
                    dao.removeById(id2);
                    nullEmpl = dao.getById(id2);
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
                    assertTrue(dao.findByProject(10001L).size() == 2);
                    assertTrue(dao.findByProject(10002L).size() == 2);
                }));
    }
}
