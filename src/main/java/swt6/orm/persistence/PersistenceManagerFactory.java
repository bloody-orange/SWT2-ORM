package swt6.orm.persistence;

public class PersistenceManagerFactory {
    private static PersistenceManager manager = null;

    public static PersistenceManager getManager() {
        if (manager == null) {
            manager = new BitronixManager();
        }
        return manager;
    }
}
