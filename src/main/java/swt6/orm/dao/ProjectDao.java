package swt6.orm.dao;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Project;

public interface ProjectDao extends BaseDao<Project, Long> {
    Long getTotalMinutesSpentOnProject(Project project);
    Long getMinutesSpentOnProjectByEmployee(Project project, Employee empl);
}
