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
                    .values(1001L, "Herbert", "Steiner", DateUtil.getDate(1972, 12, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(1002L, "Max", "Geier", DateUtil.getDate(1999, 11, 11), "Hagenberg", "4232", "Softwarepark 14")
                    .values(1003L, "Haribo", "Maier", DateUtil.getDate(1968, 12, 1), "Hagenberg", "4232", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PERMANENTEMPLOYEES =
            insertInto("PERMANENTEMPLOYEE")
                    .columns("ID", "SALARY", "WORKINGHOURS")
                    .values(1002L, 85000D, 40D)
                    .build();

    public static final Operation INSERT_TEMPORARYEMPLOYEES =
            insertInto("TEMPORARYEMPLOYEE")
                    .columns("ID", "RENTER", "HOURLYRATE", "STARTDATE", "ENDDATE")
                    .values(1003L, "Good Employees Inc.", 15D, DateUtil.getDate(2015, 11, 13), DateUtil.getDate(2017, 12, 11))
                    .build();

    public static final Operation INSERT_ADDRESSES =
            insertInto("ADDRESS")
                    .columns("ZIPCODE", "CITY", "STREET")
                    .values("Hagenberg", "4232", "Softwarepark 14")
                    .values("Hagenberg", "4232", "Softwarepark 105")
                    .build();

    public static final Operation INSERT_PROJECTS =
            insertInto("PROJECT")
                    .columns("ID", "NAME", "LEADER_ID")
                    .values(10001L, "Mission Impossible", 1001L)
                    .values(10002L, "Space Elevator", null)
                    .build();

    public static final Operation INSERT_PROJECTEMPLOYEE =
            insertInto("PROJECT_EMPLOYEE")
                    .columns("PROJECT_ID", "EMPLOYEE_ID")
                    .values(10001L, 10001L)
                    .values(10001L, 10002L)
                    .values(10002L, 10002L)
                    .values(10002L, 10003L)
                    .build();

    public static final Operation INSERT_LOGBOOKENTRIES =
            insertInto("LOGBOOKENTRY")
                    .columns("ID", "EMPLOYEE_ID", "ACTIVITY", "STARTTIME", "ENDTIME", "TASK_ID")
                    .values(10001L, 10001L, "Zelt aussuchen", DateUtil.getTime(2016, 1, 1, 12, 00), DateUtil.getTime(2016, 1, 1, 17, 00), 10001L)
                    .values(10002L, 10001L, "Zelt kaufen", DateUtil.getTime(2016, 1, 1, 17, 00), DateUtil.getTime(2016, 1, 1, 18, 00), 10002L)
                    .values(10003L, 10002L, "Zelt kaufen", DateUtil.getTime(2016, 1, 1, 17, 00), DateUtil.getTime(2016, 1, 1, 18, 00), 10002L)
                    .values(10004L, 10001L, "Zelt aufbauen", DateUtil.getTime(2016, 1, 2, 8, 00), DateUtil.getTime(2016, 1, 2, 17, 00), 10003L)
                    .values(10005L, 10002L, "Zelt aufbauen", DateUtil.getTime(2016, 1, 2, 8, 00), DateUtil.getTime(2016, 1, 2, 19, 00), 10003L)
                    .values(10006L, 10002L, "Heizung aufbauen", DateUtil.getTime(2016, 1, 3, 8, 00), DateUtil.getTime(2016, 1, 3, 12, 00), 10004L)
                    .values(10007L, 10001L, "Heizung einschalten", DateUtil.getTime(2016, 1, 4, 8, 00), DateUtil.getTime(2016, 1, 4, 9, 00), 10004L)
                    .values(10008L, 10001L, "Getränke kaufen", DateUtil.getTime(2016, 1, 17, 8, 00), DateUtil.getTime(2016, 1, 17, 12, 00), 10005L)
                    .values(10009L, 10002L, "Getränke einkühlen", DateUtil.getTime(2016, 1, 18, 8, 00), DateUtil.getTime(2016, 1, 18, 10, 00), 10006L)
                    .build();

    public static final Operation INSERT_ALL = sequenceOf(
            INSERT_ADDRESSES,
            INSERT_EMPLOYEES,
            INSERT_TEMPORARYEMPLOYEES,
            INSERT_PERMANENTEMPLOYEES,
            INSERT_PROJECTS,
            INSERT_PROJECTEMPLOYEE,
            INSERT_LOGBOOKENTRIES
    );

    public static final Operation DELETE_INSERT_ALL = sequenceOf(
            DELETE_ALL,
            INSERT_ALL
    );
}