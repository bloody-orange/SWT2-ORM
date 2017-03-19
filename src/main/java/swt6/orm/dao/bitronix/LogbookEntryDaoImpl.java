package swt6.orm.dao.bitronix;

import swt6.orm.dao.LogbookEntryDao;
import swt6.orm.domain.*;

import javax.persistence.criteria.*;
import java.util.List;

public class LogbookEntryDaoImpl extends AbstractBaseDao<LogbookEntry, Long> implements LogbookEntryDao {
    public LogbookEntryDaoImpl() {
        super(LogbookEntry.class);
    }

    @Override
    public List<LogbookEntry> findByProjectAndPhase(Project project, Phase ... phases) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<LogbookEntry> entryQuery = cb.createQuery(LogbookEntry.class);
        Root<LogbookEntry> entries = entryQuery.from(LogbookEntry.class);
        Join<LogbookEntry, Module> modules = entries.join(LogbookEntry_.module);
        Join<Module, Project> projects = modules.join(Module_.project);
        Join<LogbookEntry, Phase> phasesRoot = entries.join(LogbookEntry_.phase);

        Predicate pred = cb.and(
                cb.equal(projects.get(Project_.id), project.getId()),
                phasesRoot.in((Object[]) phases));

        return mgr.getQuery(entryQuery
                .select(entries)
                .where(pred)
        )
                .getResultList();
    }
}
