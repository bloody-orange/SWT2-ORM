package swt6.orm.dao.impl;

import swt6.orm.dao.LogbookEntryDao;
import swt6.orm.domain.LogbookEntry;

public class LogbookEntryDaoImpl extends AbstractBaseDao<LogbookEntry, Long> implements LogbookEntryDao {
    public LogbookEntryDaoImpl() {
        super(LogbookEntry.class);
    }
}
