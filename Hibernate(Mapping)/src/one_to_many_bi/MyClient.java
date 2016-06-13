package one_to_many_bi;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MyClient {

	public static void main(String[] args) {
		Configuration cnf = new Configuration();
		cnf.addAnnotatedClass(Cart.class);
		cnf.addAnnotatedClass(Item.class);
		cnf.configure("/one_to_many_bi/hibernate.conf.xml");
		System.out.println("MyClient.main(): XML CONFIGURED");
		Cart cart = new Cart();
		
		cart.setCart_total(100);
		List<Item> cartItems = new ArrayList<Item>();
		Item itm1 = new Item();
		itm1.setItemQty((short) 3);
		itm1.setCart(cart);
		itm1.setItemId(10);
		Item itm2 = new Item();
		
		itm2.setItemQty((short) 4);
		itm2.setCart(cart);
		itm2.setItemId(20);
		cartItems.add(itm1);
		cartItems.add(itm2);
		cart.setCartItems(cartItems);
		SessionFactory factory = cnf.buildSessionFactory();
		Session session = factory.getCurrentSession();
		Transaction tx = (Transaction) session.getTransaction();
		tx.begin();
		session.save(cart);
		tx.commit();
		System.out.println("MyClient.main(): END OF PROCESS");
		
	}
}
