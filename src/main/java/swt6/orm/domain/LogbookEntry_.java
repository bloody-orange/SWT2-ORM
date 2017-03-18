package swt6.orm.domain;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(LogbookEntry.class)
public class LogbookEntry_ {
    public static volatile SingularAttribute<LogbookEntry, Date> startTime;
    public static volatile SingularAttribute<LogbookEntry, Date> stopTime;
    public static volatile SingularAttribute<LogbookEntry, Employee> employee;
    public static volatile SingularAttribute<LogbookEntry, Phase> phase;
    public static volatile SingularAttribute<LogbookEntry, Module> module;
}
