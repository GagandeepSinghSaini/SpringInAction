package one_to_one;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MyClient {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Person.class);
		conf.addAnnotatedClass(Address.class);
		conf.configure("/one_to_one/hibernate.conf.xml");
		System.out.println("MyClient.main(): Configeration Done");
		Transaction transaction = null;
		Person pp = new Person();
		pp.setName("GAGAN");
		Address address = new Address();
		address.setPerson(pp);
		address.setAddress("ABCD");
		SessionFactory factory = conf.buildSessionFactory();
		Session session = factory.getCurrentSession();
		transaction = session.beginTransaction();
		session.save(pp);
		transaction.commit();
		if(session.isOpen()) {
			session.close();
		}
		System.out.println("MyClient.main(): END");
		
	}
}
