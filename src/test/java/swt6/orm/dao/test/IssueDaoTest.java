package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.IssueDao;
import swt6.orm.dao.bitronix.*;
import swt6.orm.dao.bitronix.IssueDaoImpl;
import swt6.util.DbSetupUtil;
import swt6.orm.domain.*;
import swt6.orm.domain.Issue;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IssueDaoTest {

    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final IssueDao dao = new IssueDaoImpl();
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
        Operation operation = DataOperations.DELETE_INSERT_ALL;
        tracker.launchIfNecessary(DbSetupUtil.getTestDbSetup(operation));
    }

    @Test
    public void testAdd() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertEquals(5, dao.findAll().size());
                    Project proj = new ProjectDaoImpl().findById(101L);
                    Employee empl = new EmployeeDaoImpl().findById(101L);
                    Issue issue = new Issue("Testissue", proj, IssueState.OPEN,
                            IssuePriority.HIGH, 0, 0);
                    dao.addOrUpdate(issue);
                    assertEquals(6, dao.findAll().size());
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
                    Issue issue = dao.findById(id1);
                    assertNotNull(issue);
                    assertNotEquals(issue.getDescription(), otherName);
                    issue.setDescription(otherName);
                    issue = dao.addOrUpdate(issue);
                    Issue otherProj = dao.findById(issue.getId());
                    assertEquals(otherProj.getDescription(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Issue issue = dao.findById(id1);
                    assertNotNull(issue);
                    dao.remove(issue);
                    Issue nullEntity = dao.findById(id1);
                    assertNull(nullEntity);

                    Issue otherEntity = dao.findById(id2);
                    assertNotNull(otherEntity);
                    dao.removeById(otherEntity.getId());
                    nullEntity = dao.findById(id2);
                    assertNull(nullEntity);
                }));
    }

    @Test
    public void findByState() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    List<Issue> issues = dao.findByState(IssueState.NEW);
                    assertNotNull(issues);
                    assertEquals(1, issues.size());

                    issues = dao.findByState(IssueState.REJECTED, IssueState.OPEN);
                    assertNotNull(issues);
                    assertEquals(2, issues.size());
                }));
    }

    @Test
    public void findByEmployee() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Employee empl = new EmployeeDaoImpl().findById(101L);
                    assertNotNull(empl);
                    List<Issue> issues = dao.findByEmployee(empl);
                    assertNotNull(issues);
                    assertEquals(2, issues.size());
                }));
    }

    @Test
    public void findByEmployeeAndState() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Employee empl = new EmployeeDaoImpl().findById(101L);
                    assertNotNull(empl);
                    List<Issue> issues = dao.findByEmployee(empl, IssueState.NEW, IssueState.CLOSED);
                    assertNotNull(issues);
                    assertEquals(1, issues.size());
                }));
    }
}
