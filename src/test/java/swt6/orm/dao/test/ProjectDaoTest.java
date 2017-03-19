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
import swt6.orm.dao.EmployeeDao;
import swt6.orm.dao.ProjectDao;
import swt6.orm.dao.impl.EmployeeDaoImpl;
import swt6.orm.dao.impl.ProjectDaoImpl;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.Project;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProjectDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final ProjectDao dao = new ProjectDaoImpl();
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
        Operation operation = DataOperations.DELETE_INSERT_ALL;

        DbSetup dbSetup = new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true", "app", "derby"), operation);
        tracker.launchIfNecessary(dbSetup);
    }


    @Test
    public void testAdd() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertEquals(2, dao.findAll().size());
                    Project proj = new Project("hi", null);
                    dao.addOrUpdate(proj);
                    assertEquals(3, dao.findAll().size());
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
                    Project proj = dao.findById(id1);
                    assertNotNull(proj);
                    assertNotEquals(proj.getName(), otherName);
                    proj.setName(otherName);
                    proj = dao.addOrUpdate(proj);
                    Project otherProj = dao.findById(proj.getId());
                    assertEquals(otherProj.getName(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Project proj = dao.findById(id1);
                    assertNotNull(proj);
                    dao.remove(proj);
                    Project nullProj = dao.findById(id1);
                    assertNull(nullProj);

                    Project otherProj = dao.findById(id2);
                    assertNotNull(otherProj);
                    dao.removeById(otherProj.getId());
                    nullProj = dao.findById(id2);
                    assertNull(nullProj);
                }));
    }

    @Test
    public void testTotalMinutesSpent() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Project proj = dao.findById(id1);
                    assertNotNull(proj);
                    long minutes = dao.getTotalMinutesSpentOnProject(proj);
                    assertEquals(1476L, minutes);
                }));
    }


    @Test
    public void testMinutesSpentByEmployee() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Project proj = dao.findById(id1);
                    assertNotNull(proj);
                    Employee empl = new EmployeeDaoImpl().findById(101L);
                    assertNotNull(empl);
                    long minutes = dao.getMinutesSpentOnProjectByEmployee(proj, empl);
                    assertEquals(549L, minutes);
                }));
    }
}
