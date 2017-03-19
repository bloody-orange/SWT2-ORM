package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swt6.orm.DataOperations;
import swt6.orm.dao.ModuleDao;
import swt6.orm.dao.bitronix.ModuleDaoImpl;
import swt6.orm.dao.bitronix.ProjectDaoImpl;
import swt6.util.DbSetupUtil;
import swt6.orm.domain.Module;
import swt6.orm.domain.Project;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ModuleDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final Long id1 = 101L;
    private static final Long id2 = 102L;
    private static final ModuleDao dao = new ModuleDaoImpl();
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
                    Module module = new Module("Testmodule", proj);
                    dao.addOrUpdate(module);
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
                    Module module = dao.findById(id1);
                    assertNotNull(module);
                    assertNotEquals(module.getName(), otherName);
                    module.setName(otherName);
                    module = dao.addOrUpdate(module);
                    Module otherProj = dao.findById(module.getId());
                    assertEquals(otherProj.getName(), otherName);
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Module module = dao.findById(id1);
                    assertNotNull(module);
                    dao.remove(module);
                    Module nullEntity = dao.findById(id1);
                    assertNull(nullEntity);

                    Module otherEntity = dao.findById(id2);
                    assertNotNull(otherEntity);
                    dao.removeById(otherEntity.getId());
                    nullEntity = dao.findById(id2);
                    assertNull(nullEntity);
                }));
    }
}
