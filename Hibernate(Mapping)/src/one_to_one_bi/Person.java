package one_to_one_bi;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Person_bi")
public class Person {

	@Id
	@GeneratedValue
	@Column(name="Person_Id")
	private int id;
	@Column(name="Person_Name")
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="AddressId")
	Address addressId;
	
	
	public Address getAddressId() {
		return addressId;
	}
	public void setAddress(Address addressID) {
		this.addressId = addressID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
