package swt6.orm.dao.impl;

import swt6.orm.dao.BaseDao;
import swt6.orm.domain.BaseEntity;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import java.util.List;

public abstract class AbstractBaseDao<T extends BaseEntity<IdT>, IdT> implements BaseDao<T, IdT> {
    private final Class<T> entityType;
    protected final PersistenceManager mgr = PersistenceManagerFactory.getManager();

    AbstractBaseDao(Class<T> entityType) {
        this.entityType = entityType;
    }

    protected Class<T> getEntityType() {
        return entityType;
    }

    @Override
    public T addOrUpdate(T entity) {
        return mgr.merge(entity); // creates obj if not existing, throws if it does
    }

    @Override
    public T getById(IdT id) {
        return mgr.find(entityType, id);
    }

    @Override
    public List<T> getAll() {
        return mgr.query("select e from " + getEntityType().getSimpleName() + " e", getEntityType());
    }

    @Override
    public void removeById(IdT id) {
        remove(getById(id));
    }

    @Override
    public void remove(T entity) {
        mgr.remove(entity);
    }
}