package com.booking.dto;

public class Admin {
	
	String ID;
	String passwd;
	String email;
	String name;
	
	public Admin(String iD, String passwd, String email, String name) {
		super();
		ID = iD;
		this.passwd = passwd;
		this.email = email;
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
