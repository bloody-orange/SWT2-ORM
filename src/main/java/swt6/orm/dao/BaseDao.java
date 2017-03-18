package swt6.orm.dao;

import swt6.orm.domain.BaseEntity;

import java.util.List;
import java.util.function.Predicate;

public interface BaseDao<T extends BaseEntity<IdT>, IdT> {
    T addOrUpdate(T obj);
    T findById(IdT id);
    List<T> findAll();
    void remove(T obj);
    void removeById(IdT id);
    List<T> findByPredicate(Predicate<T> pred);
}
