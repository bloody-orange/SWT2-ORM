package swt6.orm.domain;

import javax.persistence.Column;
import java.util.Date;

public class PermanentEmployee extends Employee {
    @Column
    private double salary;

    private PermanentEmployee() {
        super();
    }

    private PermanentEmployee(String firstName, String lastName, Date dateOfBirth, double salary) {
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
