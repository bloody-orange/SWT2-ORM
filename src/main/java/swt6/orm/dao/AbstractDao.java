package swt6.orm.dao;

import org.hibernate.Session;
import swt6.orm.domain.BaseEntity;
import swt6.orm.domain.Employee;
import swt6.orm.hibernate.HibernateUtil;

import javax.persistence.EntityManager;

public abstract class AbstractDao<T extends BaseEntity<IdT>, IdT> {
    public IdT add(T entity) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        try {
            T persisted = em.merge(entity); // creates obj if not existing, throws if it does
            return persisted.getId();
        } catch(Exception e) {
            BTMUtil.rollback();
            throw e;
        }
    }

    public void remove(IdT id, Class<T> entityType) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        try {
            T persistent = em.find(entityType, id);
            em.remove(persistent);
        } catch(Exception e) {
            BTMUtil.rollback();
            throw e;
        }
    }
}