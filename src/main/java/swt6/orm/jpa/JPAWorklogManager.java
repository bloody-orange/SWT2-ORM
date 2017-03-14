package swt6.orm.jpa;

import org.hibernate.Session;
import org.hibernate.Transaction;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Project;
import swt6.orm.hibernate.HibernateUtil;
import swt6.util.DateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPAWorklogManager {
    public static void main(String[] args) {
        JPAUtil.getEntityManagerFactory();
        try {
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

            System.out.println("------------- insertEmployee ----------------");
            insertEntity(empl1);

            JPAUtil.commit();

            System.out.println("------------- listAllEmployee ----------------");
            listAllEmployees();
        }
        finally {
            JPAUtil.closeEntityManagerFactory();
        }
    }

    /*
    private static void insertEmployee(Employee empl) {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("WorkLogPU");
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        em.persist(empl);

        tx.commit();

        em.close();
        emFactory.close();
    }

    private static void insertEmployee(Employee empl) {
        EntityManager em = JPAUtil.getTransactedEntityManager();
        em.persist(empl);
        JPAUtil.commit();
    }

    private static <T> void insertEntity(T entity) {
        EntityManager em = JPAUtil.getTransactedEntityManager();
        em.persist(entity);
        JPAUtil.commit();
    }*/

    private static <T> T insertEntity(T entity) {
        EntityManager em = null;

        try {
            em = JPAUtil.getTransactedEntityManager();
            return em.merge(entity);
        } catch (Exception e) {
            JPAUtil.rollback();
            throw e;
        }
    }


    private static void listAllEmployees() {
        EntityManager em = null;
        try {
            em = JPAUtil.getTransactedEntityManager();
            List<Employee> allEmpls = em.createQuery("select e from Employee e", Employee.class)
                    .getResultList();
            for (Employee employee : allEmpls) {
                System.out.println(employee);
                if (employee.getAddress() != null) {
                    System.out.println("  Address: " + employee.getAddress());
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

            JPAUtil.commit();
        } catch (Exception e) {
            JPAUtil.rollback();
            throw e;
        }
    }
}
