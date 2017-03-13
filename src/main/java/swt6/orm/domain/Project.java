package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> members = new HashSet<>();


    @OneToMany(mappedBy = "project")
    private Set<Module> modules;

    public Long getId() {
        return id;
    }

    public Project() {
    }

    public Project(String name) {
        this.name = name;
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
        return name;
    }
}
