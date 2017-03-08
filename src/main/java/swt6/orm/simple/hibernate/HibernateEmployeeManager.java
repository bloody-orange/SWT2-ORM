package swt6.orm.simple.hibernate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import swt6.orm.simple.domain.Employee;

public class HibernateEmployeeManager {

  static String promptFor(BufferedReader in, String p) {
    System.out.print(p + "> ");
    try {
      return in.readLine();
    }
    catch (Exception e) {
      return promptFor(in, p);
    }
  }
  
  private static void saveEmployee1(Employee empl) {
	  // create new SessionFactory for every database
	  Configuration config = new Configuration().configure("hibernate-simple.cfg.xml");
	  SessionFactory sessionFactory = config.buildSessionFactory();
	  
	  Session session = sessionFactory.openSession();
	  Transaction tx = session.beginTransaction();
	  
	  session.save(empl);
	  
	  tx.commit();
	  session.close();
	  sessionFactory.close();
  }
  
  private static void saveEmployee(Employee empl) {
	  Session session = HibernateUtil.getCurrentSession();
	  Transaction tx = session.beginTransaction();
	  
	  session.save(empl);
	  
	  tx.commit();
	  //HibernateUtil.closeSession();
  }
  
  private static List<Employee> getAllEmployees() {
	  Session session = HibernateUtil.getCurrentSession();
	  Transaction tx = session.beginTransaction();
	  
	  Query<Employee> query = session.createQuery("select e from Employee e", Employee.class);
	  // query.setParameter(...); for prepared statements
	  List<Employee> employees = query.getResultList();

	  tx.commit();
	  //HibernateUtil.closeSession();
	  
	  return employees;
  }
  private static void updateEmployee(long emplId, String firstName, String lastName, Date dob) {
	  Session session = HibernateUtil.getCurrentSession();
	  Transaction tx = session.beginTransaction();
	  
	  Employee empl = session.find(Employee.class, emplId);
	  empl.setFirstName(firstName);
	  empl.setLastName(lastName);
	  empl.setDateOfBirth(dob);
	  
	  tx.commit();
	  //HibernateUtil.closeSession();

  }
  
  public static void main(String[] args) {
    DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String availCmds = "commands: quit, insert, list, update";
    
    System.out.println("Hibernate Employee Admin");
    System.out.println(availCmds);
    String userCmd = promptFor(in, "");

    try {
    	System.out.println("--- create schema ---");
    	HibernateUtil.getSessionFactory();
    	
	    while(!userCmd.equals("quit")) {
	    	switch(userCmd) {
		    	case "insert":
					try {
						saveEmployee(new Employee(
			    				promptFor(in, "firstName"), 
			    				promptFor(in, "lastName"), 
			    				dfmt.parse(promptFor(in, "dob"))
			    				));
					} catch (ParseException e) {
						System.err.println("Invalid date format");
					}
		    		break;
		    	case "list":
		    		for(Employee employee : getAllEmployees()) {
		    			System.out.println(employee);
		    		}
		    		break;
		    	case "update":
					try {
						updateEmployee(Long.parseLong(promptFor(in, "id")), 
			    				promptFor(in, "new firstName"), 
			    				promptFor(in, "new lastName"), 
			    				dfmt.parse(promptFor(in, "new dateOfBirth")));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
		    	default:
		    		System.err.printf("%s is not a valid command.\n", userCmd);
		    		break;
	    	}
	    	
	    	System.out.println(availCmds);
	    	userCmd = promptFor(in, "");
	    }
    }
    finally {
		HibernateUtil.closeSessionFactory();
	}
  }
}
