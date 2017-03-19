package swt6.orm.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
// Strategy 1
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
// Strategy 2
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue("E")
// Strategy 3
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee implements BaseEntity<Long> {
    private static final long serialVersionUID = -6726960404716047785L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    // all other members are mapped automatically
    @Column(length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    // date types must be mapped explicitly
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    // default FetchType.LAZY
    @OneToMany(mappedBy = "employee", cascade = {CascadeType.ALL}, orphanRemoval = true,
                fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<LogbookEntry> logbookEntries = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumns({
            @JoinColumn(name = "zipCode", referencedColumnName = "zipCode"),
            @JoinColumn(name = "city", referencedColumnName = "city"),
            @JoinColumn(name = "street", referencedColumnName = "street")
    })
    private Address address;

    @OneToMany(mappedBy = "leader", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private List<Project> leadingProjects;

    public List<Project> getLeadingProjects() {
        return leadingProjects;
    }

    public void setLeadingProjects(List<Project> leadingProjects) {
        this.leadingProjects = leadingProjects;
    }

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Project> projects = new HashSet<>();

    public Employee() {

    }

    public Employee(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<LogbookEntry> getLogbookEntries() {
        return logbookEntries;
    }

    public void setLogbookEntries(Set<LogbookEntry> logbookEntries) {
        this.logbookEntries = logbookEntries;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalStateException("Address was null");
        }
        if (this.address != null) {
            this.address.removeInhabitant(this);
        }
        this.address = address;
        address.addInhabitant(this);
    }

    public void addLogbookEntry(LogbookEntry entry) {
        if (entry == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }

        if (entry.getEmployee() != null) {
            entry.getEmployee().getLogbookEntries().remove(entry);
        }

        this.logbookEntries.add(entry);
        entry.setEmployee(this);
    }

    public void addLeadingProject(Project project) {
        if (project == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }

        if (project.getLeader() != null) {
            project.getLeader().removeLeadingProject(project);
        }

        this.leadingProjects.add(project);
        project.setLeader(this);
    }

    public void removeLeadingProject(Project project) {
        if (project == null) {
            throw new IllegalStateException("LogbookEntry was null");
        }

        if (project.getLeader() == this) {
            project.setLeader(null);
            this.leadingProjects.remove(project);
        }
    }

    @Override
    public void removeDependencies() {
        while (this.getProjects().size() > 0) {
            this.getProjects().iterator().next().removeMember(this);
        }

        while (this.getLeadingProjects().size() > 0) {
            this.removeLeadingProject(this.getLeadingProjects().iterator().next());
        }
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return String.format("[%d]: %s %s (%4$tY-%4$tm-%4$td)", id, firstName, lastName, dateOfBirth);
    }
}
