package swt6.orm.dao.impl;

import swt6.orm.dao.ModuleDao;
import swt6.orm.domain.Module;

public class ModuleDaoImpl extends AbstractBaseDao<Module, Long> implements ModuleDao {
    public ModuleDaoImpl() {
        super(Module.class);
    }
}
