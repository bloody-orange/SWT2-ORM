package swt6.orm.dao.impl;

import swt6.orm.dao.EmployeeDao;
import swt6.orm.domain.Employee;

import java.util.HashMap;
import java.util.List;

public class EmployeeDaoImpl extends AbstractBaseDao<Employee, Long> implements EmployeeDao {
    public EmployeeDaoImpl() {
        super(Employee.class);
    }

    @Override
    public List<Employee> findByName(String name) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "%" + name + "%");
        return mgr.parametrizedQuery(
                "select e from Employee e where e.firstName like :name or e.lastName like :name", Employee.class, params);
    }

    @Override
    public List<Employee> findByProject(Long projectId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", projectId);
        return mgr.parametrizedQuery(
                "select e from Employee e join e.projects p where p.id = :id", Employee.class, params);
    }


}
