package many_to_many_bi;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Author.class);
		conf.addAnnotatedClass(Books.class);
		conf.configure("/many_to_many_bi/hibernate.conf.xml");
		System.out.println("Client.main(): XML FILE LOADED SUCCESS");
		Author author1 = new Author();
		Author author2 = new Author();
		Author author3 = new Author();
		
		author1.setAuthorName("A1.1");
		author2.setAuthorName("A2.1");
		author3.setAuthorName("A3.1");
		Books bk1 = new Books();
		Books bk2 = new Books();
		
		bk1.setBookName("B1.1");
		bk2.setBookName("B1.2");
		
		Set bookSet = new HashSet();
		bookSet.add(bk1);
		bookSet.add(bk2);
		
		Set authorSet = new HashSet();
		authorSet.add(author3);
		authorSet.add(author2);
		
		bk1.setAuthor(authorSet);
		bk2.setAuthor(authorSet);
		
		author1.setBooks(bookSet);
		
		
		SessionFactory fact = conf.buildSessionFactory();
		Session sess = fact.getCurrentSession();
		sess.getTransaction().begin();
		//sess.save(author1);
		sess.save(bk1);
		sess.getTransaction().commit();
		System.out.println("Client.main(): DONE");
		
		
	}
}
