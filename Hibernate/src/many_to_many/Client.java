package many_to_many;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Author.class);
		conf.addAnnotatedClass(Book.class);
		conf.configure("/many_to_many/hibernate.conf.xml");
		System.out.println("Client.main(): xml file is configured successfully");
		Set<Author> authSet1 = new HashSet<Author>();
		Set<Author> authSet2 = new HashSet<Author>();
		Author auth1 = new Author();
		auth1.setAuthorName("A1");
		Author auth2 = new Author();
		auth2.setAuthorName("A2");
		Author auth3 = new Author();
		auth3.setAuthorName("A3");
		Author auth4 = new Author();
		auth4.setAuthorName("A4");
		Author auth5 = new Author();
		auth5.setAuthorName("A5");
		authSet1.add(auth1);
		authSet1.add(auth2);
		authSet2.add(auth3);
		authSet2.add(auth4);
		authSet2.add(auth5);
		Book b1 = new Book();
		b1.setBookName("B1");
		Book b2 = new Book();
		b2.setBookName("B2");
		b1.setAuthor(authSet1);
		b2.setAuthor(authSet2);
		SessionFactory factory = conf.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		session.save(b1);
		session.save(b2);
		System.out.println("COMMITING TRANSACTION");
		session.getTransaction().commit();
		
	}
}
