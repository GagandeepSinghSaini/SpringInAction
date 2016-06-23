package Inheritance.table_per_concrete_class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="owner_inher")
public class Owner extends Person{

	@Column(name="ShareCount")
	private int shareCount;

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	
}
