package swt6.orm;

import com.ninja_squad.dbsetup.operation.Operation;
import swt6.util.DateUtil;

import static com.ninja_squad.dbsetup.Operations.*;

public class DataOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom(
                    "LOGBOOKENTRY",
                    "ISSUE",
                    "PROJECTEMPLOYEE",
                    "PHASE",
                    "MODULE",
                    "PROJECT",
                    "EMPLOYEE",
                    "TEMPORARYEMPLOYEE",
                    "PERMANENTEMPLOYEE",
                    "ADDRESS");


    public static final Operation INSERT_EMPLOYEES =
            insertInto("EMPLOYEE")
                    .columns("ID", "FIRSTNAME", "LASTNAME", "DATEOFBIRTH", "CITY", "ZIPCODE", "STREET")
                    .values(101L, "Herbert", "Steiner", DateUtil.getDate(1972, 12, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(102L, "Max", "Geier", DateUtil.getDate(1999, 11, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(103L, "Haribo", "Maier", DateUtil.getDate(1968, 12, 1), "Hagenberg", "4232", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PERMANENTEMPLOYEES =
            insertInto("PERMANENTEMPLOYEE")
                    .columns("ID", "SALARY")
                    .values(102L, 85000D)
                    .build();

    public static final Operation INSERT_TEMPORARYEMPLOYEES =
            insertInto("TEMPORARYEMPLOYEE")
                    .columns("ID", "RENTER", "HOURLYRATE", "STARTDATE", "ENDDATE")
                    .values(103L, "Good Employees Inc.", 15D, DateUtil.getDate(2015, 11, 13), DateUtil.getDate(2017, 1, 13))
                    .build();

    public static final Operation INSERT_ADDRESSES =
            insertInto("ADDRESS")
                    .columns("ZIPCODE", "CITY", "STREET")
                    .values("4232", "Hagenberg", "Softwarepark 14")
                    .values("4232", "Hagenberg", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PROJECTS =
            insertInto("PROJECT")
                    .columns("ID", "NAME", "LEADERID")
                    .values(101L, "Mission Impossible", 101L)
                    .values(102L, "Space Elevator", 101L)
                    .build();

    public static final Operation INSERT_PROJECTEMPLOYEE =
            insertInto("PROJECTEMPLOYEE")
                    .columns("PROJECTID", "EMPLOYEEID")
                    .values(101L, 101L)
                    .values(101L, 102L)
                    .values(102L, 102L)
                    .values(102L, 103L)
                    .build();

    public static final Operation INSERT_MODULES =
            insertInto("MODULE")
                    .columns("ID", "NAME", "PROJECT_ID")
                    .values(101L, "Gather Information of target", 101L)
                    .values(102L, "Break in", 101L)
                    .values(103L, "Great escape", 101L)
                    .values(104L, "Gather materials", 102L)
                    .values(105L, "Build Elevator", 102L)
                    .build();

    public static final Operation INSERT_PHASES =
            insertInto("PHASE")
                .columns("ID", "NAME")
                .values(101L, "Implementation")
                .values(102L, "Testing")
                .values(103L, "Maintenance")
                .build();

    public static final Operation INSERT_LOGBOOKENTRIES =
            insertInto("LOGBOOKENTRY")
                    .columns("ID", "EMPLOYEE_ID", "ACTIVITY", "STARTTIME", "STOPTIME", "MODULE_ID", "PHASE_ID", "ISSUE_ID")
                    .values(101L, 101L, "Spy house", DateUtil.getTime(2015, 11, 21, 1, 24), DateUtil.getTime(2015, 11, 21, 2, 43), 101L, 101L, 101L)
                    .values(102L, 101L, "Spy guard 1", DateUtil.getTime(2015, 11, 21, 17, 10), DateUtil.getTime(2015, 11, 22, 1, 0), 101L, 101L, 101L)
                    .values(103L, 102L, "Drill into wall", DateUtil.getTime(2015, 12, 1, 3, 13), DateUtil.getTime(2015, 12, 1, 18, 13), 102L, 102L, 102L)
                    .values(104L, 102L, "Triggered Alarm", DateUtil.getTime(2015, 12, 2, 5, 30), DateUtil.getTime(2015, 12, 2, 5, 31), 102L, 102L, 103L)
                    .values(105L, 102L, "Run away", DateUtil.getTime(2015, 12, 2, 5, 31), DateUtil.getTime(2015, 12, 2, 5, 58), 103L, 103L, 102L)
                    .values(106L, 102L, "Get resources", DateUtil.getTime(2016, 1, 3, 15, 20), DateUtil.getTime(2016, 1, 3, 12, 0), 104L, 101L, 105L)
                    .values(107L, 103L, "Check resources", DateUtil.getTime(2016, 1, 4, 15, 10), DateUtil.getTime(2016, 1, 4, 9, 0), 104L, 102L, 105L)
                    .values(108L, 103L, "Work on part 1", DateUtil.getTime(2017, 1, 7, 13, 0), DateUtil.getTime(2017, 1, 17, 15, 0), 105L, 101L, 104L)
                    .values(109L, 102L, "Repair part 2", DateUtil.getTime(2017, 2, 13, 14, 0), DateUtil.getTime(2017, 2, 18, 15, 0), 105L, 103L, 104L)
                    .build();

    public static final Operation INSERT_ISSUES =
            insertInto("ISSUE")
                .columns("ID", "DESCRIPTION", "ESTIMATEDMINUTES", "PERCENTAGEDONE", "PRIORITY", "STATE", "ASSIGNEE_ID", "PROJECT_ID")
                .values(101L, "Get building plan", 540, 20, 1, 2, 101L, 101L)
                .values(102L, "Steal diamond", 1000, 30, 0, 1, null, 101L)
                .values(103L, "Steal gold", 1000, 0, 0, 4, null, 101L)
                .values(104L, "Build stairs", 6000, 46, 2, 3, 103L, 102L)
                .values(105L, "Buy more material", 300, 0, 0, 0, 101L, 102L)
                .build();

    public static final Operation INSERT_ALL = sequenceOf(
            INSERT_ADDRESSES,
            INSERT_EMPLOYEES,
            INSERT_TEMPORARYEMPLOYEES,
            INSERT_PERMANENTEMPLOYEES,
            INSERT_PROJECTS,
            INSERT_MODULES,
            INSERT_PROJECTEMPLOYEE,
            INSERT_PHASES,
            INSERT_ISSUES,
            INSERT_LOGBOOKENTRIES
    );

    public static final Operation DELETE_INSERT_ALL = sequenceOf(
            DELETE_ALL,
            INSERT_ALL
    );
}