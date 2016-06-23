package Inheritance.table_per_concrete_class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="employee_inher")
public class Employee extends Person{

	@Column(name="SALARY")
	private long salary;

	public long getSalary() {
		return salary;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	
	
	
}
