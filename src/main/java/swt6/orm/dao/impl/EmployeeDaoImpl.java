package swt6.orm.dao.impl;

import swt6.orm.dao.BTMUtil;
import swt6.orm.dao.EmployeeDao;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public void add(Employee employee) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        em.persist(employee);
    }

    @Override
    public void updateLastName(long employeeId, String lastname) {
        EntityManager em = BTMUtil.getTransactedEntityManager();

    }

    @Override
    public void updateFirstName(long employeeId, String firstname) {

    }

    @Override
    public void addLogbookEntry(long employeeId, LogbookEntry entry) {

    }

    @Override
    public void setAddress(long employeeId, Address address) {

    }

    @Override
    public Employee findByName(String name) {
        return null;
    }

    @Override
    public List<Employee> findByProject(long projectId) {
        return null;
    }
}
