package swt6.orm.dao.impl;

import swt6.orm.bitronix.BTMUtil;
import swt6.orm.dao.BaseDao;
import swt6.orm.domain.BaseEntity;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class AbstractBaseDao<T extends BaseEntity<IdT>, IdT> implements BaseDao<T, IdT> {
    private final Class<T> entityType;

    AbstractBaseDao(Class<T> entityType) {
        this.entityType = entityType;
    }

    protected Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public T addOrUpdate(T entity) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        return em.merge(entity); // creates obj if not existing, throws if it does
    }

    @Override
    public T getById(IdT id) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        return em.find(entityType, id);
    }

    @Override
    public List<T> getAll() {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        return em.createQuery("select e from " + getEntityType().getSimpleName() + " e", getEntityType()).getResultList();
    }

    @Override
    public void removeById(IdT id) {
        remove(getById(id));
    }

    @Override
    public void remove(T entity) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        em.remove(entity);
    }
}