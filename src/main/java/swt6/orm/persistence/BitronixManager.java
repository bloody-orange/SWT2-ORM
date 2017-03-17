package swt6.orm.persistence;

import swt6.orm.domain.BaseEntity;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Map;

class BitronixManager implements PersistenceManager {
    private EntityManagerFactory emFactory = null;
    private Context ctx = null;
    private ThreadLocal<EntityManager> emThread = new ThreadLocal<>();

    BitronixManager() {
    }

    public boolean executeTransaction(Transactor transactor) {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() != Status.STATUS_ACTIVE) {
                tx.begin();
            }
            transactor.executeTransaction();
            tx.commit();
            closeManager();
            return true;
        } catch (Exception e) {
            rollback();
            return false;
        }
    }

    @Override
    public void startTransaction() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() != Status.STATUS_ACTIVE) {
                tx.begin();
            }
        } catch (NamingException | NotSupportedException | SystemException e) {
            e.printStackTrace();
        }
    }

    private EntityManager getManager() {
        if (emThread.get() == null) {
            emThread.set(getFactory().createEntityManager());
        }
        return emThread.get();
    }

    private void closeManager() {
        if (emThread.get() != null) {
            emThread.get().close();
            emThread.set(null);
        }
    }

    public void commit() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE ||
                    tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                tx.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeManager();
    }

    public void rollback() {
        try {
            UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE ||
                    tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                tx.rollback();
            }
        } catch (NamingException | SystemException e) {
            e.printStackTrace();
        }
        closeManager();
    }

    public void closeFactory() {
        if (emFactory != null) {
            emFactory.close();
            emFactory = null;
        }
    }

    @Override
    public <T extends BaseEntity> T merge(T entity) {
        return getManager().merge(entity);
    }

    @Override
    public <T extends BaseEntity> void remove(T entity) {
        getManager().remove(entity);
    }

    @Override
    public <T extends BaseEntity> void persist(T entity) {
        getManager().persist(entity);
    }

    @Override
    public <T extends BaseEntity<IdT>, IdT> T find(Class<T> entityType, IdT id) {
        return getManager().find(entityType, id);
    }

    @Override
    public <T extends BaseEntity> List<T> query(String query, Class<T> entityType) {
        return getManager().createQuery(query, entityType).getResultList();
    }

    @Override
    public <T extends BaseEntity> List<T> parametrizedQuery(String query, Class<T> entityType, Map<String, Object> params) {
        TypedQuery<T> qry = getManager().createQuery(query, entityType);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            qry.setParameter(entry.getKey(), entry.getValue());
        }
        return qry.getResultList();
    }

    @Override
    public void initFactory() {
        getFactory();
    }

    private EntityManagerFactory getFactory() {
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
