package Inheritance.table_per_subclass;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Subject.class);
		conf.addAnnotatedClass(Physics.class);
		conf.addAnnotatedClass(Chemistry.class);
		conf.configure("/Inheritance/hibernate.conf.xml");
		System.out.println("Client.main(): XML configured Successfully");
		/*Subject sub = new Subject();
		sub.setSubjectCost("100");
		sub.setSubjectId(100L);*/
		
		Physics pp = new Physics();
		pp.setParticipants(1000);
		pp.setSubjectCost("200");
		pp.setSubjectId(200L);
		pp.setSubjectName("PP");
		
		Chemistry chemistry = new Chemistry();
		chemistry.setParticipants(2000);
		chemistry.setSubjectCost("300");
		chemistry.setSubjectId(300L);
		chemistry.setSubjectName("CC");
		
		SessionFactory fact = conf.buildSessionFactory();
		Session sess = fact.getCurrentSession();
		sess.getTransaction().begin();
		//sess.save(sub);
		sess.save(pp);
		sess.save(chemistry);
		sess.getTransaction().commit();
		
	}
}
