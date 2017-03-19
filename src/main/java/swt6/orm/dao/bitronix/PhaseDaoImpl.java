package swt6.orm.dao.bitronix;

import swt6.orm.dao.PhaseDao;
import swt6.orm.domain.Phase;

public class PhaseDaoImpl extends AbstractBaseDao<Phase, Long> implements PhaseDao {
    public PhaseDaoImpl() {
        super(Phase.class);
    }
}
