package sg.edu.nus.sms.model;

public class UserSession {
	
	private int id;
	private String userType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public UserSession() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserSession(int id, String userType) {
		super();
		this.id = id;
		this.userType = userType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSession other = (UserSession) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	

}
