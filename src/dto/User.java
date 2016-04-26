package dto;

import java.util.Date;

public class User {
	private String username;
	private String password;
	private String name;
	private String surname;
	private String eMail;
	private String bio;
	private Date brDate;
	private String group;
	
	public User() {
		super();
		this.group = "";
	}


	public User(String username, String password, String name, String surname, String eMail, String bio, Date brDate,
			String group) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.eMail = eMail;
		this.bio = bio;
		this.brDate = brDate;
		this.group = group;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String geteMail() {
		return eMail;
	}


	public void seteMail(String eMail) {
		this.eMail = eMail;
	}


	public String getBio() {
		return bio;
	}


	public void setBio(String bio) {
		this.bio = bio;
	}


	public Date getBrDate() {
		return brDate;
	}


	public void setBrDate(Date brDate) {
		this.brDate = brDate;
	}


	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}
	
}
