package swt6.orm.dao.impl;

import swt6.orm.bitronix.BTMUtil;
import swt6.orm.dao.EmployeeDao;
import swt6.orm.domain.Employee;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeDaoImpl extends AbstractBaseDao<Employee, Long> implements EmployeeDao {
    public EmployeeDaoImpl() {
        super(Employee.class);
    }

    @Override
    public List<Employee> findByName(String name) {
        EntityManager em = BTMUtil.getEntityManager();
        TypedQuery<Employee> qry = em.createQuery(
                "select e from Employee e where e.firstName like :name or e.lastName like :name", Employee.class);
        qry.setParameter("name", "%" + name + "%");
        return qry.getResultList();
    }

    @Override
    public List<Employee> findByProject(Long projectId) {
        EntityManager em = BTMUtil.getEntityManager();
        TypedQuery<Employee> qry = em.createQuery(
                "select e from Employee e join e.projects p where p.id = :id", Employee.class);
        qry.setParameter("id", projectId);
        return qry.getResultList();
    }
}
