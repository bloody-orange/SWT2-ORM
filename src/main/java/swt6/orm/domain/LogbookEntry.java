package swt6.orm.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

@Entity
public class LogbookEntry implements BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String activity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date stopTime;

    // you often need at least the name of the writer -> eager fetch
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.JOIN)
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    private Issue issue;

    public LogbookEntry() {
    }

    public LogbookEntry(String activity, Date startTime, Date stopTime, Employee employee, Phase phase, Module module) {
        this.activity = activity;
        this.startTime = startTime;
        this.stopTime = stopTime;
        employee.addLogbookEntry(this);
        phase.addLogbookEntry(this);
        module.addLogbookEntry(this);
    }

    public LogbookEntry(String activity, Date startTime, Date stopTime, Employee employee, Phase phase, Module module, Issue issue) {
        this(activity, startTime, stopTime, employee, phase, module);
        issue.addLogbookEntry(this);
    }

    public Issue getIssue() {
        return issue;
    }

    void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void removeDependencies() {
        employee.getLogbookEntries().remove(this);
        phase.getEntries().remove(this);
        module.getEntries().remove(this);
        issue.getEntries().remove(this);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public Phase getPhase() {
        return phase;
    }

    void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Module getModule() {
        return module;
    }

    void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        DateFormat format = DateFormat.getDateTimeInstance();

        return String.format(
                "Entry [%d]: %s [%s, %s] (employee: %s) (issue: #%s)",
                id,
                activity,
                format.format(startTime),
                format.format(stopTime),
                employee == null ? "<null>" : employee.getLastName(),
                issue == null ? "<null>" : issue.getId());
    }
}
