package swt6.orm.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("PE")
public class PermanentEmployee extends Employee {
    @Column
    private double salary;

    public PermanentEmployee() {
        super();
    }

    public PermanentEmployee(String firstName, String lastName, Date dateOfBirth, double salary) {
        super(firstName, lastName, dateOfBirth);
        this.salary = salary;

    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + "\n     salary: " + salary;
    }

}
