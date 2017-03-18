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
        assertTrue(
                mgr.executeTransaction(() -> {
                    List<Address> list = dao.findAll();
                    assertEquals(2, list.size());
                }));
        tracker.skipNextLaunch();
    }

    @Test
    public void testAdd() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    assertEquals(2, dao.findAll().size());
                    Address a = new Address("4232", "Hagenberg", "Softwarepark 12345");
                    a = dao.addOrUpdate(a);
                    assertNotNull(a.getId());
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
                    Address addr = dao.findById(id1);
                    assertNotNull(addr);
                    assertNotEquals(addr.getCity(), "NotHagenberg");
                    addr.setCity("NotHagenberg");
                    addr = dao.addOrUpdate(addr);
                    assertEquals(addr.getCity(), "NotHagenberg");
                }));
    }

    @Test
    public void testDelete() {
        assertTrue(
                mgr.executeTransaction(() -> {
                    Address a = dao.findById(id1);
                    assertNotNull(a);
                    dao.remove(a);
                    Address nullA = dao.findById(id1);
                    assertNull(nullA);

                    a = dao.findById(id2);
                    assertNotNull(a);
                    dao.removeById(id2);
                    nullA = dao.findById(id2);
                    assertNull(nullA);
                }));
    }
}
