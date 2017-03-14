package swt6.orm.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import swt6.orm.domain.*;
import swt6.util.DateUtil;

public class HibernateWorklogManager {

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("-- create database schema ---");

            Employee empl1 = new Employee("Franz", "Kammer", DateUtil.getDate(1950, 1, 23));
            Address addr1 = new Address("4232", "Hagenberg", "Softwarepark 104");
            empl1.setAddress(addr1);
            Employee empl2 = new Employee("Karli", "Schanz", DateUtil.getDate(1940, 5, 1));
            Address addr2 = new Address("4232", "Hagenberg", "Softwarepark 11");
            empl2.setAddress(addr2);
            Employee empl3 = new Employee("Annemarie", "Moser-Pröll", DateUtil.getDate(1945, 10, 25));
            Address addr3 = new Address("5020", "Salzburg", "Sterneckstraße 33");
            empl3.setAddress(addr3);

            Project proj1 = new Project("Testprojekt");
            Project proj2 = new Project("Testübung");

            LogbookEntry entry1 = new LogbookEntry("Müsli essen", DateUtil.getTime(8, 0), DateUtil.getTime(9, 0));
            LogbookEntry entry2 = new LogbookEntry("Krafttraining", DateUtil.getTime(9, 30), DateUtil.getTime(10, 30));
            LogbookEntry entry3 = new LogbookEntry("Slalomtraining", DateUtil.getTime(11, 0), DateUtil.getTime(13, 0));
            LogbookEntry entry4 = new LogbookEntry("Massage", DateUtil.getTime(15, 0), DateUtil.getTime(16, 30));

            Issue issue1 = new Issue(IssueState.NEW, IssuePriority.LOW, 30, 0);

            System.out.println("---  save employees  ---");
            empl1 = saveEntity(empl1);
            empl2 = saveEntity(empl2);
            empl3 = saveEntity(empl3);

            System.out.println("--- add logbookentry ---");
            empl1 = addLogbookEntry(empl1, entry1, entry2);
            empl2 = addLogbookEntry(empl2, entry3);
            empl3 = addLogbookEntry(empl3, entry4);

            System.out.println("--- add projects ---");
            proj1 = saveEntity(proj1);
            proj2 = saveEntity(proj2);
            System.out.println("-- add members to projects --");
            proj1 = addProject(proj1, empl1, empl2);
            proj2 = addProject(proj2, empl3);

            System.out.println("--- list ---");
            listEmployees();
        } finally {
            HibernateUtil.closeSessionFactory();
        }
    }

    private static Employee addLogbookEntry(Employee empl, LogbookEntry... entries) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();

        empl = (Employee) session.merge(empl);
        for (LogbookEntry entry : entries) {
            empl.addLogbookEntry(entry);
        }

        tx.commit();

        return empl;
    }

    private static Project addProject(Project project, Employee... employees) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();

        project = (Project) session.merge(project);
        for (Employee empl : employees) {
            empl = (Employee) session.merge(empl); // important, otherwise employee does not get updated!
            project.addMember(empl);
        }

        tx.commit();

        return project;
    }

    // generic approach
    @SuppressWarnings("unchecked")
    private static <T> T saveEntity(T entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();

        entity = (T) session.merge(entity); // creates obj if not existing, updates if exists

        tx.commit(); // closes session also if getCurrentSession was used
        return entity;

    }

    private static void listEmployees() {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();

        List<Employee> allEmpls = session.createQuery("select e from Employee e", Employee.class).getResultList();
        for (Employee employee : allEmpls) {
            System.out.println(employee);

            if (employee.getAddress() != null) {
                System.out.println("  Address");
                System.out.println("    " + employee.getAddress());
            }
            if (employee.getLogbookEntries().size() > 0) {
                System.out.println("  LogbookEntries");
                for (LogbookEntry entry : employee.getLogbookEntries()) {
                    System.out.println("    " + entry);
                }
            }
            if (employee.getProjects().size() > 0) {
                System.out.println("  Projects");
                for (Project project : employee.getProjects()) {
                    System.out.println("    " + project);
                }
            }
        }

        tx.commit();
    }


}
