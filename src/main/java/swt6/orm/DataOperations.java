package swt6.orm;

import com.ninja_squad.dbsetup.operation.Operation;
import swt6.util.DateUtil;

import static com.ninja_squad.dbsetup.Operations.*;

public class DataOperations {
    public static final Operation DELETE_ALL =
            deleteAllFrom(
                    "EMPLOYEE",
                    "TEMPORARYEMPLOYEE",
                    "PERMANENTEMPLOYEE",
                    "ISSUE",
                    "PHASE",
                    "LOGBOOKENTRY",
                    "PROJECT",
                    "PROJECTEMPLOYEE",
                    "MODULE",
                    "ADDRESS");


    public static final Operation INSERT_EMPLOYEES =
            insertInto("EMPLOYEE")
                    .columns("ID", "FIRSTNAME", "LASTNAME", "DATEOFBIRTH", "CITY", "ZIPCODE", "STREET")
                    .values(1L, "Herbert", "Steiner", DateUtil.getDate(1972, 12, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(2L, "Max", "Geier", DateUtil.getDate(1999, 11, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(3L, "Haribo", "Maier", DateUtil.getDate(1968, 12, 1), "Hagenberg", "4232", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PERMANENTEMPLOYEES =
            insertInto("PERMANENTEMPLOYEE")
                    .columns("ID", "SALARY", "WORKINGHOURS")
                    .values(2L, 85000D, 40D)
                    .build();

    public static final Operation INSERT_TEMPORARYEMPLOYEES =
            insertInto("TEMPORARYEMPLOYEE")
                    .columns("ID", "RENTER", "HOURLYRATE", "STARTDATE", "ENDDATE")
                    .values(3L, "Good Employees Inc.", 15D, DateUtil.getDate(2015, 11, 13), DateUtil.getDate(2017, 1, 13))
                    .build();

    public static final Operation INSERT_ADDRESSES =
            insertInto("ADDRESS")
                    .columns("ZIPCODE", "CITY", "STREET")
                    .values("4232", "Hagenberg", "Softwarepark 14")
                    .values("4232", "Hagenberg", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PROJECTS =
            insertInto("PROJECT")
                    .columns("ID", "NAME", "LEADER_ID")
                    .values(1L, "Mission Impossible", 1L)
                    .values(2L, "Space Elevator", null)
                    .build();

    public static final Operation INSERT_PROJECTEMPLOYEE =
            insertInto("PROJECT_EMPLOYEE")
                    .columns("PROJECT_ID", "EMPLOYEE_ID")
                    .values(1L, 1L)
                    .values(1L, 2L)
                    .values(2L, 2L)
                    .values(2L, 0L)
                    .build();

    public static final Operation INSERT_MODULES =
            insertInto("MODULE")
                    .columns("ID", "NAME", "PROJECT_ID")
                    .values(1L, "Gather Information of target", 1L)
                    .values(2L, "Break in", 1L)
                    .values(3L, "Great escape", 1L)
                    .values(4L, "Gather materials", 2L)
                    .values(5L, "Build Elevator", 2L)
                    .build();

    public static final Operation INSERT_LOGBOOKENTRIES =
            insertInto("LOGBOOKENTRY")
                    .columns("ID", "EMPLOYEE_ID", "ACTIVITY", "STARTTIME", "ENDTIME", "MODULE_ID")
                    .values(1L, 1L, "Museum ausspähen", DateUtil.getTime(2015, 11, 21, 1, 24), DateUtil.getTime(2015, 11, 21, 1, 43), 1L)
                    .values(2L, 1L, "Zelt kaufen", DateUtil.getTime(2016, 1, 1, 17, 0), DateUtil.getTime(2016, 1, 1, 18, 0), 2L)
                    .values(3L, 2L, "Zelt kaufen", DateUtil.getTime(2016, 1, 1, 17, 0), DateUtil.getTime(2016, 1, 1, 18, 0), 2L)
                    .values(4L, 1L, "Zelt aufbauen", DateUtil.getTime(2016, 1, 2, 8, 0), DateUtil.getTime(2016, 1, 2, 17, 0), 3L)
                    .values(5L, 2L, "Zelt aufbauen", DateUtil.getTime(2016, 1, 2, 8, 0), DateUtil.getTime(2016, 1, 2, 19, 0), 3L)
                    .values(6L, 2L, "Heizung aufbauen", DateUtil.getTime(2016, 1, 3, 8, 0), DateUtil.getTime(2016, 1, 3, 12, 0), 4L)
                    .values(7L, 1L, "Heizung einschalten", DateUtil.getTime(2016, 1, 4, 8, 0), DateUtil.getTime(2016, 1, 4, 9, 0), 4L)
                    .values(8L, 1L, "Getränke kaufen", DateUtil.getTime(2016, 1, 17, 8, 0), DateUtil.getTime(2016, 1, 17, 12, 0), 5L)
                    .values(9L, 2L, "Getränke einkühlen", DateUtil.getTime(2016, 1, 18, 8, 0), DateUtil.getTime(2016, 1, 18, 10, 0), 6L)
                    .build();

    public static final Operation INSERT_ALL = sequenceOf(
            INSERT_ADDRESSES,
            INSERT_EMPLOYEES,
            INSERT_TEMPORARYEMPLOYEES,
            INSERT_PERMANENTEMPLOYEES,
            INSERT_PROJECTS,
            INSERT_MODULES,
            INSERT_PROJECTEMPLOYEE,
            INSERT_LOGBOOKENTRIES
    );

    public static final Operation DELETE_INSERT_ALL = sequenceOf(
            DELETE_ALL,
            INSERT_ALL
    );
}