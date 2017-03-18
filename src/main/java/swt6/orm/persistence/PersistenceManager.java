package swt6.orm.persistence;

import swt6.orm.domain.BaseEntity;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

public interface PersistenceManager {
    boolean executeTransaction(Transactor transactor);
    void startTransaction();
    void commit();
    void rollback();
    void initFactory();
    void closeFactory();
    <T extends BaseEntity> T merge(T entity);
    <T extends BaseEntity> void remove(T entity);
    <T extends BaseEntity> void persist(T entity);
    <T extends BaseEntity<IdT>, IdT> T find(Class<T> entityType, IdT id);
    <T extends BaseEntity> List<T> query(String query, Class<T> entityType);
    <T extends BaseEntity> List<T> parametrizedQuery(String query, Class<T> entityType, Map<String, Object> params);
    <T extends BaseEntity> CriteriaBuilder getCriteriaBuilder();
    <T> TypedQuery<T> getQuery(CriteriaQuery<T> qry);
}
