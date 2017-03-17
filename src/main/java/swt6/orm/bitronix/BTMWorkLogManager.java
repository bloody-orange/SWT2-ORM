package swt6.orm.bitronix;

import swt6.orm.dao.*;
import swt6.orm.dao.impl.*;
import swt6.orm.domain.*;
import swt6.util.DateUtil;

public class BTMWorkLogManager {
    public static void main(String[] args) {
        try {
            BTMUtil.getFactory();
            addEntities();
        } finally {
            BTMUtil.closeFactory();
        }
    }

    private static void addEntities() {
        try {
            BTMUtil.startTransaction();
            EmployeeDao emplDao = new EmployeeDaoImpl();
            Employee e1 = emplDao.addOrUpdate(new Employee("Hans",
                    "Goldner", DateUtil.getDate(1995, 3, 5)));
            Employee e2 = emplDao.addOrUpdate(new PermanentEmployee("Herbert",
                    "Maier", DateUtil.getDate(1993, 1, 5), 21.5));
            Employee e3 = emplDao.addOrUpdate(new TemporaryEmployee("Gerti",
                    "Müller", DateUtil.getDate(1994, 3, 15), "Amazing Workers Corp", 23.4,
                    DateUtil.getDate(2016, 3, 5), DateUtil.getDate(2017, 12, 4)));

            AddressDao aDao = new AddressDaoImpl();
            Address a1 = aDao.addOrUpdate(new Address("4232", "Hagenberg", "Softwarepark 14"));
            Address a2 = aDao.addOrUpdate(new Address("4232", "Hagenberg", "Radler Straße 15"));

            e1.setAddress(a1);
            e2.setAddress(a2);
            e3.setAddress(a1);

            ProjectDao projDao = new ProjectDaoImpl();
            Project p1 = projDao.addOrUpdate(new Project("Mission Impossible", e1));
            Project p2 = projDao.addOrUpdate(new Project("Space elevator", e2));

            ModuleDao modDao = new ModuleDaoImpl();
            Module mod1 = modDao.addOrUpdate(new Module("Break into Mansion", p1));
            Module mod2 = modDao.addOrUpdate(new Module("Sneak through ventilation shaft", p1));
            Module mod3 = modDao.addOrUpdate(new Module("Build elevator", p2));
            Module mod4 = modDao.addOrUpdate(new Module("Profit", p2));

            PhaseDao phaseDao = new PhaseDaoImpl();
            Phase phase1 = phaseDao.addOrUpdate(new Phase("Implementation"));
            Phase phase2 = phaseDao.addOrUpdate(new Phase("Testing"));
            Phase phase3 = phaseDao.addOrUpdate(new Phase("Maintenance"));

            BTMUtil.commit();
        } catch (Exception e) {
            BTMUtil.rollback();
        } finally {
            BTMUtil.closeEntityManager();
        }
    }
}
