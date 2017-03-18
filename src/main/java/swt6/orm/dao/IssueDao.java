package swt6.orm.dao;

import swt6.orm.domain.Employee;
import swt6.orm.domain.Issue;
import swt6.orm.domain.IssueState;
import swt6.orm.domain.Project;

import java.util.List;

public interface IssueDao extends BaseDao<Issue, Long> {
    List<Issue> findByState(IssueState ... states);
    List<Issue> findByEmployee(Employee empl);
    List<Issue> findByEmployee(Employee empl, IssueState ... state);
}
