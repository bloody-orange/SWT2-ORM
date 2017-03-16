package swt6.orm.dao;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Project;

import java.util.List;

public interface EmployeeDao {
    Long add(Employee e);
    void remove(Long id, Class<Employee> entityType);
    void updateLastName(long employeeId, String lastname);
    void updateFirstName(long employeeId, String firstname);
    void addLogbookEntry(long employeeId, LogbookEntry entry);
    void setAddress(long employeeId, Address address);
    List<Employee> findByName(String name);
    List<Employee> findByProject(long projectId);
}
