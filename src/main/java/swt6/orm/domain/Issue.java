package swt6.orm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Period;

public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public enum IssueState {
        NEW, OPEN, RESOLVED, CLOSED, REJECTED
    }

    @Column(nullable = false)
    private IssueState state;

    public enum IssuePriority {
        LOW, NORMAL, HIGH
    }

    @Column(nullable = false)
    private IssuePriority priority;

    @Column
    private int estimatedMinutes;

    @Column
    private int percentageDone;

    public Issue() {
    }

    public Issue(IssueState state, IssuePriority priority, int estimatedMinutes) {
        this.percentageDone = 0;
        this.state = state;
        this.priority = priority;
        this.estimatedMinutes = estimatedMinutes;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public int getPercentageDone() {
        return percentageDone;
    }

    public void setPercentageDone(int percentageDone) {
        if (percentageDone > 100 || percentageDone < 0) {
            throw new IllegalArgumentException("percentage out of bounds");
        }
        this.percentageDone = percentageDone;
    }
}
