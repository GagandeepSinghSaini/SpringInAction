package Inheritance.table_per_hierarchy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Client {

	public static void main(String[] args) {
		System.out.println("Client.main(): ONE TABLE PER HIERARCHY");
		Configuration conf = new Configuration();
		conf.addAnnotatedClass(Animal.class);
		conf.addAnnotatedClass(LandAnimal.class);
		conf.configure("Inheritance/hibernate.conf.xml");
		System.out.println("Client.main(): XML LOADED SUCCESSFULLY");
		Animal animal = new Animal();
		animal.setAnimalId(100L);
		animal.setColor("nitin");
		
		LandAnimal landAnimal = new LandAnimal();
		landAnimal.setAnimalId(200L);
		landAnimal.setAnimalName("SACHIN");
		landAnimal.setColor("Blue");
		
		SessionFactory fact = conf.buildSessionFactory();
		Session sess = fact.getCurrentSession();
		sess.getTransaction().begin();
		sess.save(animal);
		sess.save(landAnimal);
		sess.getTransaction().commit();
		
	}
}
