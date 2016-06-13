package one_to_many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Employer.class);
		conf.addAnnotatedClass(Employee.class);
		conf.configure("/one_to_many/hibernate.conf.xml");
		System.out.println("Client.main(): XML LOADED");
		Employer emp = new Employer();
		emp.setEmployerName("TRANTOR");
		Employer emp1 = new Employer();
		emp1.setEmployerName("SUFI");
		Employee e1 = new Employee();
		Employee e2 = new Employee();
		Employee e3 = new Employee();
		e1.setEmployeeId(205);
		e1.setEmployeeName("SACHIN");
		e1.setEmployer(emp);
		e2.setEmployeeId(211);
		e2.setEmployeeName("ASHOK");
		e2.setEmployer(emp);
		e3.setEmployeeId(210);
		e3.setEmployeeName("RAVI");
		e3.setEmployer(emp1);
		SessionFactory fact = conf.buildSessionFactory();
		Session session = fact.getCurrentSession();
		session.beginTransaction();
		session.save(e1);
		session.save(e2);
		session.save(e3);
		session.getTransaction().commit();
		System.out.println("Client.main(): DONE");
		
	}
}
