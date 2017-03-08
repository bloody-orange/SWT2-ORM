package swt6.orm.simple.hibernate;

import static org.hamcrest.CoreMatchers.nullValue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import net.bytebuddy.asm.Advice.This;;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static Session session = null;
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) { // create new instance
		  Configuration config = new Configuration().configure("hibernate-simple.cfg.xml");
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
