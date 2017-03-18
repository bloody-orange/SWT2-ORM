package swt6.orm.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project implements BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "ProjectEmployee",
            joinColumns = {
                @JoinColumn(name = "projectId", referencedColumnName="id")},
            inverseJoinColumns = {
                @JoinColumn(name = "employeeId", referencedColumnName="id")})
    private Set<Employee> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "leaderId")
    private Employee leader;

    @OneToMany(mappedBy = "project")
    private Set<Module> modules;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Issue> issues;

    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public Long getId() {
        return id;
    }

    public Project() {
    }

    public Project(String name, Employee leader) {
        this.name = name;
        this.leader = leader;
    }


    public Employee getLeader() {
        return leader;
    }

    public void setLeader(Employee leader) {
        this.leader = leader;
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


    public void addIssue(Issue issue) {
        if (issue == null) {
            throw new IllegalArgumentException("Issue was null");
        }
        this.issues.add(issue);
        issue.setProject(this); // bidirectional relation
    }

    public void removeIssue(Issue issue) {
        if (issue == null) {
            throw new IllegalArgumentException("Issue was null");
        }
        this.issues.remove(issue);
        issue.setProject(null);
    }

    public Set<Employee> getMembers() {
        return members;
    }

    public void setMembers(Set<Employee> members) {
        this.members = members;
    }

    public void addMember(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee was null");
        }
        this.members.add(employee);
        employee.getProjects().add(this); // bidirectional relation
    }

    public void removeMember(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee was null");
        }
        this.members.remove(employee);
        employee.getProjects().remove(this);
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public void addModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module was null");
        }
        this.modules.add(module);
        if (module.getProject() != null) {
            module.getProject().removeModule(module);
        }
        module.setProject(this);
    }

    public void removeModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module was null");
        }
        this.modules.remove(module);
        module.setProject(null);
    }

    public String toString() {
        return "Project [" + id + "]: " + name;
    }
}
