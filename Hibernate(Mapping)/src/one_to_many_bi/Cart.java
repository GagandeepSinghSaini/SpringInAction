package one_to_many_bi;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Cart {

	@Id
	@GeneratedValue
	@Column(name="CartId")
	private int cartId;
	@Column(name="CartTotal")
	private double cart_total;
	@OneToMany(mappedBy="cart" ,cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Item> cartItems;
	
	public List<Item> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<Item> cartItems) {
		this.cartItems = cartItems;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public double getCart_total() {
		return cart_total;
	}
	public void setCart_total(double cart_total) {
		this.cart_total = cart_total;
	}
	
	
	
}
