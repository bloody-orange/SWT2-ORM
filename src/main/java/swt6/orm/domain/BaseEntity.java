package swt6.orm.domain;

import java.io.Serializable;

public interface BaseEntity<IdT> extends Serializable {
    IdT getId();
    void removeDependencies();
}
