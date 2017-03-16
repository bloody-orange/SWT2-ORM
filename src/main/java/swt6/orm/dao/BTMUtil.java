package swt6.orm.dao;

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
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() != Status.STATUS_ACTIVE) {
                tx.begin();
            }
        } catch (NamingException | NotSupportedException | SystemException e) {
            e.printStackTrace();
        }
        EntityManager em = getEntityManager();
        em.joinTransaction();
        return em;
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

    public static void closeEntityManagerFactory() {
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
