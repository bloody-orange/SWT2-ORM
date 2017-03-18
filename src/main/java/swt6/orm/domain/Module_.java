package swt6.orm.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Module.class)
public class Module_ {
    public static volatile SingularAttribute<Module, Project> project;
}
