package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.PhaseDao;
import swt6.orm.dao.bitronix.PhaseDaoImpl;
import swt6.util.DbSetupUtil;
import swt6.orm.domain.Phase;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import static org.junit.Assert.*;

public class PhaseDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final PhaseDao dao = new PhaseDaoImpl();
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
                    assertEquals(3, dao.findAll().size());
                    Phase phase = new Phase("Test Phase");
                    dao.addOrUpdate(phase);
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
                    Phase phase = dao.findById(id1);
                    assertNotNull(phase);
                    assertNotEquals(phase.getName(), otherName);
                    phase.setName(otherName);
                    phase = dao.addOrUpdate(phase);
                    Phase otherProj = dao.findById(phase.getId());
                    assertEquals(otherProj.getName(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Phase phase = dao.findById(id1);
                    assertNotNull(phase);
                    dao.remove(phase);
                    Phase nullEntity = dao.findById(id1);
                    assertNull(nullEntity);

                    Phase otherEntity = dao.findById(id2);
                    assertNotNull(otherEntity);
                    dao.removeById(otherEntity.getId());
                    nullEntity = dao.findById(id2);
                    assertNull(nullEntity);
                }));
    }
}
