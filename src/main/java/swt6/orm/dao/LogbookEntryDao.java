package swt6.orm.dao;

import swt6.orm.domain.LogbookEntry;

import java.util.List;

public interface LogbookEntryDao {
    Long add(LogbookEntry e);
    void remove(Long id, Class<LogbookEntry> entityType);
}
