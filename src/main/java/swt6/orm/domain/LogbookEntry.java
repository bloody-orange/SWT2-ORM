package swt6.orm.domain;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.org.apache.bcel.internal.generic.CASTORE;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.engine.spi.CascadeStyle;
import sun.security.util.Length;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.*;

@Entity
public class LogbookEntry implements Serializable {
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

    // you often need at least the name of the assignee -> eager fetch
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.JOIN)
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Module module;

    public LogbookEntry() {

    }

    public LogbookEntry(String activity, Date startTime, Date stopTime) {
        this.activity = activity;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public Long getId() {
        return id;
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

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        DateFormat format = DateFormat.getDateTimeInstance();

        return String.format(
                "[%d] %s [%s, %s] (employee: %s)",
                id,
                activity,
                format.format(startTime),
                format.format(stopTime),
                employee == null ? "<null>" : employee.getLastName());
    }
}
