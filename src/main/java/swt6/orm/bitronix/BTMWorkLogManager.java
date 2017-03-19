package swt6.orm.bitronix;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import swt6.orm.DataOperations;
import swt6.orm.dao.*;
import swt6.orm.dao.impl.*;
import swt6.orm.domain.*;
import swt6.orm.persistence.PersistenceManager;
import swt6.orm.persistence.PersistenceManagerFactory;
import swt6.util.DateUtil;

import java.util.List;

public class BTMWorkLogManager {
    private static PersistenceManager mgr = PersistenceManagerFactory.getManager();

    public static void main(String[] args) {
        try {
            mgr.initFactory();
            new DbSetup(new DriverManagerDestination("jdbc:derby://localhost:1527/worklogdb;create=true",
                    "app", "derby"), DataOperations.DELETE_INSERT_ALL)
                    .launch();

            printTitle("<< WORKFLOWS >>");
            testIssue();
            testEntries();

            System.out.println();
            System.out.println();
            printTitle("<< QUERIES >>");
            findIssuesByState();
            findTimeWorkedOnProject();
            findIssuesForEmployee();
            findTimePerPhase();
        } finally {
            mgr.closeFactory();
        }
    }

    private static void testEntries() {
        markTransactionStart("LogbookEntry Operations");
        mgr.executeTransaction(() -> {
            ProjectDao projectDao = new ProjectDaoImpl();
            IssueDao issueDao = new IssueDaoImpl();
            LogbookEntryDao entryDao = new LogbookEntryDaoImpl();
            EmployeeDao emplDao = new EmployeeDaoImpl();

            Employee empl = emplDao.findById(101L);
            Project proj = projectDao.findById(101L);
            Issue issue = issueDao.findById(101L);
            Phase phase = new PhaseDaoImpl().findById(101L);
            Module module = new ModuleDaoImpl().findByPredicate(m -> m.getProject().equals(proj)).get(0);
            printLine();

            printTitle("Creating logbook entry");
            LogbookEntry entry = new LogbookEntry(
                    "Testing logbook",
                    DateUtil.getTime(2017, 3, 1, 12, 3, 4),
                    DateUtil.getTime(2017, 3, 1, 13, 3, 4),
                    empl,
                    phase,
                    module,
                    issue
            );
            entry = entryDao.addOrUpdate(entry);
            printLine(entry.toString());
            printLine();

            printTitle("View corresponding issue");
            issue = entry.getIssue();
            printLine(issue.toString());
            printLine();

            printTitle("Change corresponding issue");
            issue = entry.getIssue();
            issue.setEstimatedMinutes(480);
            issue.setPercentageDone(25);
            printLine(issue.toString());
            printLine();
        });
        markTransactionEnd();
    }

    private static void testIssue() {
        markTransactionStart("Issue Operations");
        mgr.executeTransaction(() -> {
            ProjectDao projectDao = new ProjectDaoImpl();
            IssueDao issueDao = new IssueDaoImpl();
            EmployeeDao emplDao = new EmployeeDaoImpl();

            Employee empl = emplDao.findById(101L);
            Project proj = projectDao.findById(101L);
            printLine();

            printTitle("Creating issue");
            Issue issue = new Issue("Testing a workflow", proj, IssueState.NEW, IssuePriority.HIGH, 30, 0);
            issue = issueDao.addOrUpdate(issue);
            printLine(issue.toString());
            printLine();

            printTitle("Adding assignee to issue");
            issue.setAssignee(empl);
            printLine(issue.toString());
            printLine();

            printTitle("Changing issue");
            issue.setState(IssueState.REJECTED);
            issue.setPercentageDone(100);
            issue.setEstimatedMinutes(0);
            printLine(issue.toString());
            printLine();
        });
        markTransactionEnd();
    }

    private static void findTimePerPhase() {
        markTransactionStart("Time per phase");
        mgr.executeTransaction(() -> {
            ProjectDao projectDao = new ProjectDaoImpl();
            LogbookEntryDao entryDao = new LogbookEntryDaoImpl();
            PhaseDao phaseDao = new PhaseDaoImpl();

            List<Project> projects = projectDao.findAll();
            for (Project project: projects) {
                printLine("Times for " + project.getName() + ":");
                List<Phase> phases = phaseDao.findAll();
                for (Phase phase: phases) {
                    Long phaseTotal = 0L;
                    List<LogbookEntry> entries = entryDao.findByProjectAndPhase(project, phase);
                    for (LogbookEntry entry: entries) {
                        Long timeSpent = (entry.getStopTime().getTime() - entry.getStartTime().getTime()) / 60000;
                        phaseTotal += timeSpent;
                    }
                    printLine("Phase " + phase.getName() + ": " + phaseTotal + " minutes");
                }
                printLine();
            }
        });
        markTransactionEnd();
    }

    private static void findIssuesForEmployee() {
        markTransactionStart("Issues by Employee");
        mgr.executeTransaction(() -> {
            IssueDao issueDao = new IssueDaoImpl();
            EmployeeDao emplDao = new EmployeeDaoImpl();

            //Predicate<Issue> pred = (Issue issue) -> issue.getState() == IssueState.NEW || issue.getState() == IssueState.OPEN || issue.getState() == IssueState.RESOLVED;
            List<Employee> empls = emplDao.findAll();
            for (Employee empl: empls) {
                printLine("New/open/resolved issues by employee " + empl.getLastName() + ":");
                List<Issue> issues = issueDao.findByEmployee(empl, IssueState.NEW, IssueState.OPEN, IssueState.RESOLVED);
                Long total = 0L;
                for (Issue issue: issues) {
                    List<LogbookEntry> entries = new LogbookEntryDaoImpl().findByPredicate(entry -> entry.getIssue().equals(issue));
                    Long issueTotal = 0L;
                    for (LogbookEntry entry: entries) {
                        Long timeSpent = (entry.getStopTime().getTime() - entry.getStartTime().getTime()) / 60000;
                        issueTotal += timeSpent;
                    }
                    total += issueTotal;
                    printLine(issue.toString());
                    printLine("He spent " + issueTotal + " minutes on this issue.");
                }
                printLine("Total time spent on new/open/resolved issues: " + total + " minutes.\n");
            }
        });
        markTransactionEnd();
    }

    private static void findTimeWorkedOnProject() {
        markTransactionStart("Time worked on project");
        mgr.executeTransaction(() -> {
            ProjectDao projDao = new ProjectDaoImpl();
            EmployeeDao emplDao = new EmployeeDaoImpl();
            Project proj = projDao.findById(101L);

            printLine(proj.toString());
            printLine("Total time: " + projDao.getTotalMinutesSpentOnProject(proj) + " minutes.");
            List<Employee> empls = emplDao.findAll();
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
            List<Issue> issues = issueDao.findByState(IssueState.OPEN, IssueState.NEW, IssueState.RESOLVED);
            for (Issue issue : issues) {
                printLine(issue.toString());
            }
        });
        markTransactionEnd();
    }

    private static void markTransactionStart(String name) {
        printSeperator();
        printTitle(name.toUpperCase());
        printTitle("TRANSACTION START");
        printSeperator();
    }

    private static void markTransactionEnd() {
        printTitle("TRANSACTION END");
        System.out.println();
        System.out.println();
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

    private static void printLine() {
        printLine("");
    }

    private static void printLine(String line) {
        System.out.println("!>> " + line);
    }
    private static void printSeperator() {
        System.out.println("!-------------------------------------------------------!");
    }
}
