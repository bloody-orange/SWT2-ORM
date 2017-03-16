package swt6.orm.dao;

import swt6.orm.domain.Issue;

public interface IssueDao {
    Long add(Issue e);
    void remove(Long id, Class<Issue> entityType);
}
