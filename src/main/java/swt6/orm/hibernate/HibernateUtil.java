package swt6.orm.hibernate;

import static org.hamcrest.CoreMatchers.nullValue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import net.bytebuddy.asm.Advice.This;
import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Project;;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static Session session = null;
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) { // create new instance
		  Configuration config = new Configuration().configure(); // uses default cfg "hibernate.cfg.cml"
		  
		  // add employee as an entity, type safe
		  config.addAnnotatedClass(Employee.class);
		  config.addAnnotatedClass(LogbookEntry.class);
		  //config.addAnnotatedClass(Address.class);
		  config.addAnnotatedClass(Project.class);
		  sessionFactory = config.buildSessionFactory();
		}
		
		return sessionFactory;
	}
	public static void closeSessionFactory() {
		if(sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
	}
	
	public static Session openSession() {
		if(session == null) {
			session = getSessionFactory().openSession();
		}
		
		return session;
	}
	
	public static void closeSession() {
		if(session != null) {
			session.close();
			session = null;
		}
	}
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
}