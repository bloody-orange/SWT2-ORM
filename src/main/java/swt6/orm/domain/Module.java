package swt6.orm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Module implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 50)
    private String name;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "module")
    private Set<LogbookEntry> logbookEntries = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLogbookEntry(LogbookEntry entry) {
        if (entry == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }

        if (entry.getModule() != null) {
            entry.getModule().getLogbookEntries().remove(entry);
        }

        this.logbookEntries.add(entry);
        entry.setModule(this);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<LogbookEntry> getLogbookEntries() {
        return logbookEntries;
    }

    public void setLogbookEntries(Set<LogbookEntry> entries) {
        this.logbookEntries = entries;
    }
}
