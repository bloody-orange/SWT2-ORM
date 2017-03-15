package swt6.orm.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Period;

public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private IssueState state;

    @Column(nullable = false)
    private IssuePriority priority;

    @Column
    private int estimatedMinutes;

    @Column
    private int percentageDone;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Employee assignee;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.JOIN)
    private Project project;

    public Issue() {
    }

    public Issue(String description, Project project, IssueState state, IssuePriority priority, int estimatedMinutes, int percentageDone) {
        this.description = description;
        this.project = project;
        this.percentageDone = percentageDone;
        this.state = state;
        this.priority = priority;
        this.estimatedMinutes = estimatedMinutes;
    }

    public Long getId() {

        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Employee getAssignee() {
        return assignee;
    }

    public void setAssignee(Employee assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
