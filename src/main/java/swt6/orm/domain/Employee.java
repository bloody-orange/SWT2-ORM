package swt6.orm.domain;

import org.hibernate.engine.spi.CascadeStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
public class Employee implements Serializable {
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

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<LogbookEntry> logbookEntries = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "zipCode", referencedColumnName = "zipCode"),
            @JoinColumn(name = "city", referencedColumnName = "city"),
            @JoinColumn(name = "street", referencedColumnName = "street")
    })
    private Address address;

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
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
