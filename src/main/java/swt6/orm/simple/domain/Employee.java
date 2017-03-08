package swt6.orm.simple.domain;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {
	private static final long serialVersionUID = -2714162115572046349L;

	private Long id;

	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	
	public Employee() {
		
	}
	public Employee(String firstName, String lastName, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return String.format("[%d]: %s %s (%4$tY-%4$tm-%4$td)", id, firstName, lastName, dateOfBirth);
	}
	
}
