package swt6.orm.dao;

import swt6.orm.domain.BaseEntity;

import java.util.List;

public interface BaseDao<T extends BaseEntity<IdT>, IdT> {
    T addOrUpdate(T obj);
    T getById(IdT id);
    List<T> getAll();
    void remove(T obj);
    void removeById(IdT id);
}
