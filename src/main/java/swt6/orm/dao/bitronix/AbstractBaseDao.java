package swt6.orm.dao.bitronix;

import swt6.orm.dao.BaseDao;
import swt6.orm.domain.BaseEntity;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import java.util.function.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

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
    public T findById(IdT id) {
        return mgr.find(entityType, id);
    }

    @Override
    public List<T> findAll() {
        return mgr.query("select e from " + getEntityType().getSimpleName() + " e", getEntityType());
    }

    @Override
    public void removeById(IdT id) {
        remove(findById(id));
    }

    @Override
    public void remove(T entity) {
        entity.removeDependencies();
        mgr.remove(entity);
    }

    @Override
    public List<T> findByPredicate(Predicate<T> pred) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<T> qry = cb.createQuery(entityType);
        Root<T> entities = qry.from(entityType);

        return mgr.getQuery(qry.select(entities))
                .getResultList().stream().filter(pred).collect(Collectors.toList());
    }
}