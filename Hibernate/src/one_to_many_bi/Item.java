package one_to_many_bi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Item {

	@Id
	@Column(name="ItemId")
	private int itemId;
	@Column(name="ItemQty")
	private short itemQty;
	@ManyToOne
	@JoinColumn(name="CartId")
	private Cart cart;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public short getItemQty() {
		return itemQty;
	}
	public void setItemQty(short itemQty) {
		this.itemQty = itemQty;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	
}
