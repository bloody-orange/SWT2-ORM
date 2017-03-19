package swt6.orm.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Module implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 50)
    private String name;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "module")
    private Set<LogbookEntry> entries = new HashSet<>();

    public Module(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    public Module() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public void removeDependencies() {
        while (this.getEntries().size() > 0) {
            this.removeLogbookEntry(this.getEntries().iterator().next());
        }

        project.getModules().remove(this);
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
            entry.getModule().getEntries().remove(entry);
        }

        this.entries.add(entry);
        entry.setModule(this);
    }

    public void removeLogbookEntry(LogbookEntry entry) {
        if (entry == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }
        if (entry.getModule() == this) {
            entry.setModule(null);
            this.entries.remove(entry);
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<LogbookEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<LogbookEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Module [" + id + "]: " + name;
    }
}
