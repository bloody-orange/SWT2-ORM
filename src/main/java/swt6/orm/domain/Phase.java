package swt6.orm.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Phase implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "phase")
    private Set<LogbookEntry> entries = new HashSet<>();

    public Phase() {
    }
    public Phase(String name) {

        this.name = name;
    }

    public Long getId() {

        return id;
    }

    @Override
    public void removeDependencies() {
        while (this.getEntries().size() > 0) {
            this.removeLogbookEntry(this.getEntries().iterator().next());
        }
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

    public Set<LogbookEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<LogbookEntry> logbookEntries) {
        this.entries = logbookEntries;
    }

    public void addLogbookEntry(LogbookEntry entry) {
        if (entry == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }

        if (entry.getPhase() != null) {
            entry.getPhase().getEntries().remove(entry);
        }

        this.entries.add(entry);
        entry.setPhase(this);
    }

    public void removeLogbookEntry(LogbookEntry entry) {
        if (entry == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }
        if (entry.getPhase() == this) {
            entry.setPhase(null);
            this.entries.remove(entry);
        }
    }
}
