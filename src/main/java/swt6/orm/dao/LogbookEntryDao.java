package swt6.orm.dao;

import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Phase;
import swt6.orm.domain.Project;

import java.util.List;

public interface LogbookEntryDao extends BaseDao<LogbookEntry, Long>  {
    List<LogbookEntry> findByProjectAndPhase(Project project, Phase... phases);
}
