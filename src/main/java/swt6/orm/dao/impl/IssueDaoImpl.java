package swt6.orm.dao.impl;

import swt6.orm.dao.IssueDao;
import swt6.orm.domain.Issue;

public class IssueDaoImpl extends AbstractBaseDao<Issue, Long> implements IssueDao {
    public IssueDaoImpl() {
        super(Issue.class);
    }
}
