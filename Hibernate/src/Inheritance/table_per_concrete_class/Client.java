package Inheritance.table_per_concrete_class;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Person.class);
		conf.addAnnotatedClass(Employee.class);
		conf.addAnnotatedClass(Owner.class);
		conf.configure("/Inheritance/hibernate.conf.xml");
		System.out.println("XML Configured Successfully");
		Person p1 = new Person();
		p1.setPersonId(1L);
		p1.setPersonName("AA");
		
		Employee e1 = new Employee();
		e1.setPersonId(2L);
		e1.setPersonName("BB");
		e1.setSalary(1000L);
		
		Owner o1 = new Owner();
		o1.setPersonId(3L);
		o1.setPersonName("CC");
		o1.setShareCount(20);
		
		SessionFactory fact = conf.buildSessionFactory();
		Session session = fact.getCurrentSession();
		session.getTransaction().begin();
		session.save(p1);
		session.save(e1);
		session.save(o1);
		session.getTransaction().commit();
		System.out.println("Client.main(): SESSION OBJECT: "+session);
		fact.close();
		System.out.println("Client.main(): SESSION FACTORY: "+fact);
	}
}
