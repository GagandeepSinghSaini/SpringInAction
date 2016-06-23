package Inheritance.table_per_subclass;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="Subject")
@Inheritance(strategy=InheritanceType.JOINED)
public class Subject {

	@Id
	private long subjectId;
	@Column(name="subjectCost")
	private String subjectCost;
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectCost() {
		return subjectCost;
	}
	public void setSubjectCost(String subjectCost) {
		this.subjectCost = subjectCost;
	}
	
}
