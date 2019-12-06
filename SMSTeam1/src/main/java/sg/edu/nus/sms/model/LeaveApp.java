package sg.edu.nus.sms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
public class LeaveApp {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	private Faculty faculty;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	
	private String status;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	

	
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	public LeaveApp() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeaveApp(int id, Faculty faculty, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.faculty = faculty;
		this.startDate = startDate;
		this.endDate = endDate;
	}



	
	

}
