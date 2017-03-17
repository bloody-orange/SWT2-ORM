package swt6.orm.dao.impl;

import swt6.orm.dao.ProjectDao;
import swt6.orm.domain.Project;

public class ProjectDaoImpl extends AbstractBaseDao<Project, Long> implements ProjectDao {
    public ProjectDaoImpl() {
        super(Project.class);
    }
}
