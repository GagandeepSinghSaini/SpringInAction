package one_to_many;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class Employer {

	@Id
	@GeneratedValue
	private int EmployerId;
	private String EmployerName;
	
	public int getEmployerId() {
		return EmployerId;
	}
	public void setEmployerId(int employerId) {
		EmployerId = employerId;
	}
	public String getEmployerName() {
		return EmployerName;
	}
	public void setEmployerName(String employerName) {
		EmployerName = employerName;
	}
	
}
