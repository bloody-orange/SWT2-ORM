package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.LogbookEntryDao;
import swt6.orm.dao.bitronix.*;
import swt6.util.DbSetupUtil;
import swt6.orm.domain.*;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LogbookEntryDaoTest {

    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final LogbookEntryDao dao = new LogbookEntryDaoImpl();
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
                    LogbookEntry entry = dao.addOrUpdate(getTestEntry());
                    assertNotNull(entry.getId());
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
                    LogbookEntry entry = dao.findById(id1);
                    assertNotNull(entry);
                    assertNotEquals(entry.getActivity(), otherName);
                    entry.setActivity(otherName);
                    entry = dao.addOrUpdate(entry);
                    LogbookEntry otherProj = dao.findById(entry.getId());
                    assertEquals(otherProj.getActivity(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    LogbookEntry entry = dao.findById(id1);
                    assertNotNull(entry);
                    dao.remove(entry);
                    LogbookEntry nullEntity = dao.findById(id1);
                    assertNull(nullEntity);

                    LogbookEntry otherEntity = dao.findById(id2);
                    assertNotNull(otherEntity);
                    dao.removeById(otherEntity.getId());
                    nullEntity = dao.findById(id2);
                    assertNull(nullEntity);
                }));
    }

    @Test
    public void testFindByProjectAndPhase() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Project proj = new ProjectDaoImpl().findById(101L);
                    Phase phase = new PhaseDaoImpl().findById(101L);
                    List<LogbookEntry> entries = dao.findByProjectAndPhase(proj, phase);
                    assertNotNull(entries);
                    assertEquals(2, entries.size());
                }));
    }

    private LogbookEntry getTestEntry() {
        Employee empl = new EmployeeDaoImpl().findById(101L);
        Project proj = new ProjectDaoImpl().findById(101L);
        Issue issue = new IssueDaoImpl().findById(101L);
        Phase phase = new PhaseDaoImpl().findById(101L);
        Module module = new ModuleDaoImpl().findByPredicate(m -> m.getProject().equals(proj)).get(0);
        return new LogbookEntry(
                "Testing logbook",
                DateUtil.getTime(2017, 3, 1, 12, 3, 4),
                DateUtil.getTime(2017, 3, 1, 13, 3, 4),
                empl,
                phase,
                module,
                issue
        );
    }

}
