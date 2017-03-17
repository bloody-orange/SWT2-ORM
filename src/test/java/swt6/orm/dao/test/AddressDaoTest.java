package swt6.orm.dao.test;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.*;
import swt6.orm.DataOperations;
import swt6.orm.dao.AddressDao;
import swt6.orm.dao.impl.AddressDaoImpl;
import swt6.orm.domain.Address;
import swt6.orm.domain.AddressId;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import java.util.List;

import static org.junit.Assert.*;

public class AddressDaoTest {
    private static DbSetupTracker tracker = new DbSetupTracker();
    private static final AddressId id1 = new AddressId("4232", "Hagenberg", "Softwarepark 14");
    private static final AddressId id2 = new AddressId("4232", "Hagenberg", "Softwarepark 105");
    private static final AddressDao dao = new AddressDaoImpl();
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
        Operation operation = Operations.sequenceOf(DataOperations.DELETE_ALL, DataOperations.INSERT_ADDRESSES);

        DbSetup dbSetup = new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true", "app", "derby"), operation);
        tracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testGetAll() {
        mgr.executeTransaction(() -> {
            List<Address> list = dao.getAll();
            assertEquals(2, list.size());
        });
        tracker.skipNextLaunch();
    }

    @Test
    public void testAdd() {
        mgr.executeTransaction(() -> {
            assertEquals(2, dao.getAll().size());
            Address a = new Address("4232", "Hagenberg", "Softwarepark 12345");
            a = dao.addOrUpdate(a);
            assertNotNull(a.getId());
            assertEquals(3, dao.getAll().size());
        });
    }

    @Test
    public void testGetById() {
        mgr.executeTransaction(() -> assertNotNull(dao.getById(new AddressId("4232", "Hagenberg", "Softwarepark 14"))));
    }

    @Test
    public void testUpdate() {
        mgr.executeTransaction(() -> {
            Address addr = dao.getById(id1);
            assertNotEquals(addr.getCity(), "NotHagenberg");
            addr.setCity("NotHagenberg");
            addr = dao.addOrUpdate(addr);
            Address newA = dao.getById(addr.getId());
            assertEquals(newA.getCity(), "NotHagenberg");
        });
    }

    @Test
    public void testDelete() {
        mgr.executeTransaction(() -> {
            Address a = dao.getById(id1);
            assertNotNull(a);
            dao.remove(a);
            Address nullA = dao.getById(id1);
            assertNull(nullA);

            a = dao.getById(id2);
            assertNotNull(a);
            dao.removeById(id2);
            nullA = dao.getById(id2);
            assertNull(nullA);
        });
    }
}
