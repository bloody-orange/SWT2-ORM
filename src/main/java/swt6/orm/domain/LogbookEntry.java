package swt6.orm.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LogbookEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private Long id;
	private String activity;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date stopTime;
	
	@ManyToOne // owning side, employee defines the foreign key -> bidirectional
	private Employee employee;
	
	public LogbookEntry() {
		
	}
	
	public LogbookEntry(String activity, Date startTime, Date stopTime) {
		this.activity = activity;
		this.startTime = startTime;
		this.stopTime = stopTime;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		DateFormat format = DateFormat.getDateTimeInstance();
		
		return String.format(
				"[%d] %s [%s, %s] (employee: %s)", 
				id, 
				activity,
				format.format(startTime), 
				format.format(stopTime),
				employee == null ? "<null>" : employee.getLastName());
	}
}
