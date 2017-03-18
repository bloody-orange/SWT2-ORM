package swt6.orm.dao.impl;

import swt6.orm.dao.IssueDao;
import swt6.orm.domain.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class IssueDaoImpl extends AbstractBaseDao<Issue, Long> implements IssueDao {
    public IssueDaoImpl() {
        super(Issue.class);
    }

    @Override
    public List<Issue> findByState(IssueState state) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Issue> issueQry = cb.createQuery(getEntityType());
        Root<Issue> issues = issueQry.from(getEntityType());
        return mgr.getQuery(issueQry.select(issues).where(cb.equal(issues.get(Issue_.state), state))).getResultList();
    }

    @Override
    public List<Issue> findByEmployee(Employee empl) {
        return null;
    }
}
