package swt6.orm.bitronix;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import swt6.orm.DataOperations;
import swt6.orm.dao.*;
import swt6.orm.dao.impl.*;
import swt6.orm.domain.*;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class BTMWorkLogManager {
    private static PersistenceManager mgr = PersistenceManagerFactory.getManager();

    public static void main(String[] args) {
        try {
            mgr.initFactory();
            new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true",
                    "app", "derby"), DataOperations.DELETE_INSERT_ALL)
                    .launch();
            findIssuesByState();
            getTimeWorkedOnProject();
        } finally {
            mgr.closeFactory();
        }
    }

    private static void getTimeWorkedOnProject() {
        markTransactionStart("Time worked on project");
        mgr.executeTransaction(() -> {
            ProjectDao projDao = new ProjectDaoImpl();
            EmployeeDao emplDao = new EmployeeDaoImpl();
            Project proj = projDao.getById(101L);

            printLine(proj.toString());
            printLine("Total time: " + projDao.getTotalMinutesSpentOnProject(proj) + " minutes.");
            List<Employee> empls = emplDao.getAll();
            for (Employee empl: empls) {
                printLine("Time by Employee " + empl.getLastName() +": " + projDao.getMinutesSpentOnProjectByEmployee(proj, empl) + " minutes.");
            }
        });
        markTransactionEnd();
    }

    private static void findIssuesByState() {
        markTransactionStart("New/Open/Resolved issues");
        mgr.executeTransaction(() -> {
            IssueDao issueDao = new IssueDaoImpl();
            List<Issue> issues = new ArrayList<>();
            issues.addAll(issueDao.findByState(IssueState.OPEN));
            issues.addAll(issueDao.findByState(IssueState.NEW));
            issues.addAll(issueDao.findByState(IssueState.RESOLVED));
            for (Issue issue : issues) {
                printLine(issue.toString());
            }
        });
        markTransactionEnd();
    }

    private static void markTransactionStart(String name) {
        printTitle("TRANSACTION START");
        printTitle(name.toUpperCase());
    }

    private static void markTransactionEnd() {
        printTitle("TRANSACTION END");
    }

    private static void printTitle(String title) {

        String leftIndent = "";
        String rightIndent = "";
        for (int i = 0; i < (25 - title.length()) / 2; ++i) {
            leftIndent += " ";
        }
        for (int i = 0; i < (26 - title.length()) / 2; ++i) {
            rightIndent += " ";
        }

        System.out.println("!-------------- " + leftIndent + title + rightIndent + " --------------!");
    }

    private static void printLine(String line) {
        System.out.println(">> " + line);
    }
}
