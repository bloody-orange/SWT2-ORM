package swt6.orm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// map 1:1 with one table
@Entity
public class Address implements BaseEntity<AddressId> {
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "zipCode")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "street", column = @Column(name = "street")),
    })
    @EmbeddedId
    private AddressId id;


    @OneToMany(mappedBy = "address", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Employee> inhabitants = new HashSet<>();

    public Set<Employee> getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(Set<Employee> inhabitants) {
        this.inhabitants = inhabitants;
    }

    public void addInhabitant(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee was null");
        }
        this.inhabitants.add(employee);
    }

    public void removeInhabitant(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee was null");
        }
        this.inhabitants.remove(employee);
    }

    public AddressId getId() {
        return id;
    }

    public void setId(AddressId addressId) {
        this.id = addressId;
    }

    public Address() {
    }

    public Address(String zipCode, String city, String street) {
        this.id = new AddressId(zipCode, city, street);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
