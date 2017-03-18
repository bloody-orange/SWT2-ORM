package swt6.orm.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Project.class)
public class Project_ {
    public static volatile SetAttribute<Project, Issue> issues;
    public static volatile SingularAttribute<Project, Long> id;
    public static volatile SetAttribute<Project, Employee> members;
}
