package swt6.orm.bitronix;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.*;

public class BTMUtil {
    private static EntityManagerFactory emFactory = null;
    private static Context ctx = null;
    private static ThreadLocal<EntityManager> emThread = new ThreadLocal<>();

    public static boolean executeTransaction(Transactor transactor) {
        try {
            EntityManager em = getTransactedEntityManager();
            transactor.executeTransaction();
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE) {
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            rollback();
            return false;
        }
    }

    public static void startTransaction() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() != Status.STATUS_ACTIVE) {
                tx.begin();
            }
            getEntityManager();
        } catch (NamingException | NotSupportedException | SystemException e) {
            e.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        if (emThread.get() == null) {
            emThread.set(getFactory().createEntityManager());
        }
        return emThread.get();
    }

    public static void closeEntityManager() {
        if (emThread.get() != null) {
            emThread.get().close();
            emThread.set(null);
        }
    }

    public static EntityManager getTransactedEntityManager() {
        startTransaction();
        return getEntityManager();
    }

    public static void commit() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE) {
                tx.commit();
            }
        } catch (NamingException | SystemException | HeuristicMixedException | RollbackException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        closeEntityManager();
    }

    public static void rollback() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE ||
                    tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                tx.rollback();
            }
        } catch (NamingException | SystemException e) {
            e.printStackTrace();
        }
        closeEntityManager();
    }

    public static void closeFactory() {
        if (emFactory != null) {
            emFactory.close();
            emFactory = null;
        }
    }

    public static EntityManagerFactory getFactory() {
        try {
            if (ctx == null)
                ctx = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (emFactory == null) {
            emFactory = Persistence.createEntityManagerFactory("WorkLogPU");
        }
        return emFactory;
    }


}
