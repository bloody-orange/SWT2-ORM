package swt6.orm.dao.bitronix;

import swt6.orm.dao.IssueDao;
import swt6.orm.domain.*;

import javax.persistence.criteria.*;
import java.util.List;

public class IssueDaoImpl extends AbstractBaseDao<Issue, Long> implements IssueDao {
    public IssueDaoImpl() {
        super(Issue.class);
    }

    @Override
    public List<Issue> findByState(IssueState... states) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Issue> issueQry = cb.createQuery(Issue.class);
        Root<Issue> issues = issueQry.from(Issue.class);
        return mgr.getQuery(issueQry
                        .select(issues)
                        .where(issues.get(Issue_.state).in((Object[]) states)))
                .getResultList();
    }

    @Override
    public List<Issue> findByEmployee(Employee empl, IssueState... state) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Issue> issueQry = cb.createQuery(Issue.class);
        Root<Issue> issues = issueQry.from(Issue.class);
        Join<Issue, Employee> assignee = issues.join(Issue_.assignee);

        Predicate pred = cb.and(
                issues.get(Issue_.state).in((Object[]) state),
                cb.equal(assignee.get(Employee_.id), empl.getId()));

        return mgr.getQuery(issueQry
                        .select(issues)
                        .where(pred))
                .getResultList();
    }

    @Override
    public List<Issue> findByEmployee(Employee empl) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Issue> issueQry = cb.createQuery(Issue.class);
        Root<Issue> issues = issueQry.from(Issue.class);
        Join<Issue, Employee> assignee = issues.join(Issue_.assignee);

        Predicate pred = cb.and(cb.equal(assignee.get(Employee_.id), empl.getId()));

        return mgr.getQuery(issueQry
                        .select(issues)
                        .where(pred))
                .getResultList();
    }
}
