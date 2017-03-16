package swt6.orm.dao.impl;

import swt6.orm.dao.AbstractDao;
import swt6.orm.dao.BTMUtil;
import swt6.orm.dao.EmployeeDao;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeDaoImpl extends AbstractDao<Employee, Long> implements EmployeeDao {
    @Override
    public void updateLastName(long employeeId, String lastname) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        Employee e = em.find(Employee.class, employeeId);
        e.setLastName(lastname);
    }

    @Override
    public void updateFirstName(long employeeId, String firstname) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        Employee e = em.find(Employee.class, employeeId);
        e.setFirstName(firstname);
    }

    @Override
    public void addLogbookEntry(long employeeId, LogbookEntry entry) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        Employee e = em.find(Employee.class, employeeId);
        e.addLogbookEntry(entry);
    }

    @Override
    public void setAddress(long employeeId, Address address) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        Employee e = em.find(Employee.class, employeeId);
        e.setAddress(address);
    }

    @Override
    public List<Employee> findByName(String name) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        TypedQuery<Employee> qry = em.createQuery(
                "select e from Employee e where e.firstName like :name or e.lastName like :name", Employee.class);
        qry.setParameter("name", "%" + name + "%");
        return qry.getResultList();
    }

    @Override
    public List<Employee> findByProject(long projectId) {
        EntityManager em = BTMUtil.getTransactedEntityManager();
        TypedQuery<Employee> qry = em.createQuery(
                "select e from Employee e join e.projects p where p.id = :id", Employee.class);
        qry.setParameter("id", projectId);
        return qry.getResultList();
    }
}
