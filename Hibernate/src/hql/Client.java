package hql;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Employee.class);
		conf.configure("/hql/hibernate.conf.xml");
		System.out.println("Client.main(): XML File successfully loaded");
		
		Employee emp  = new Employee();
		emp.setName("E1");
		emp.setAddress("#111, Hello City, US");
		Employee emp1  = new Employee();
		emp1.setName("E2");
		emp1.setAddress("#222, Hello City, US");
		Employee emp2 = new Employee();
		emp2.setName("E3");
		emp2.setAddress("#333, Hello City, US");
		
		Session sess = conf.buildSessionFactory().getCurrentSession();
		sess.beginTransaction();
		sess.save(emp);
		sess.save(emp1);
		sess.save(emp2);
		sess.getTransaction().commit();
	}
}
