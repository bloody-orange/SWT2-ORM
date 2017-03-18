package swt6.orm.dao.impl;

import swt6.orm.dao.ProjectDao;
import swt6.orm.domain.*;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDaoImpl extends AbstractBaseDao<Project, Long> implements ProjectDao {
    public ProjectDaoImpl() {
        super(Project.class);
    }

    @Override
    public Long getTotalMinutesSpentOnProject(Project project) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Tuple> projQuery = cb.createQuery(Tuple.class);
        Root<Project> projects = projQuery.from(Project.class);
        Join<Project, Issue> issues = projects.join(Project_.issues);
        Join<Issue, LogbookEntry> entries = issues.join(Issue_.entries);

        ArrayList<IssueState> states = new ArrayList<>();
        states.add(IssueState.OPEN);
        states.add(IssueState.RESOLVED);

        Predicate where = cb.and(
                cb.equal(projects.get(Project_.id), project.getId()),
                issues.get(Issue_.state).in(states)
                );

        List<Tuple> times = mgr.getQuery(
                projQuery
                    .multiselect(entries.get(LogbookEntry_.startTime), entries.get(LogbookEntry_.stopTime))
                    .where(where)
        )
                .getResultList();
        Long minutes = 0L;
        for (Tuple tuple: times) {
            Date startTime = (Date) tuple.get(0);
            Date stopTime = (Date) tuple.get(1);
            minutes += (stopTime.getTime() - startTime.getTime()) / 60000;
        }
        return minutes;
    }

    @Override
    public Long getMinutesSpentOnProjectByEmployee(Project project, Employee empl) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<Tuple> projQuery = cb.createQuery(Tuple.class);
        Root<Project> projects = projQuery.from(Project.class);
        Join<Project, Employee> members = projects.join(Project_.members);
        Join<Project, Issue> issues = projects.join(Project_.issues);
        Join<Issue, LogbookEntry> entries = issues.join(Issue_.entries);
        Join<LogbookEntry, Employee> assignee = entries.join(LogbookEntry_.employee);

        ArrayList<IssueState> states = new ArrayList<>();
        states.add(IssueState.OPEN);
        states.add(IssueState.RESOLVED);

        Predicate where = cb.and(
                cb.equal(projects.get(Project_.id), project.getId()),
                cb.equal(assignee.get(Employee_.id), empl.getId()),
                cb.equal(members.get(Employee_.id), assignee.get(Employee_.id)),
                issues.get(Issue_.state).in(states)
        );

        List<Tuple> times = mgr.getQuery(
                projQuery
                .multiselect(entries.get(LogbookEntry_.startTime), entries.get(LogbookEntry_.stopTime))
                .where(where))
                .getResultList();
        Long minutes = 0L;
        for (Tuple tuple: times) {
            Date startTime = (Date) tuple.get(0);
            Date stopTime = (Date) tuple.get(1);
            minutes += (stopTime.getTime() - startTime.getTime()) / 60000;
        }
        return minutes;
    }
}
