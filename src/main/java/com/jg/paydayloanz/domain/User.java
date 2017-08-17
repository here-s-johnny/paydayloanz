package com.jg.paydayloanz.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {
	
	
	@Id
	@NotNull
	@GeneratedValue
	@Column(name = "id")
	private int uid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "password")
	@NotNull
	private String password;
	
	@Column(name = "email")
	@NotNull
	private String email;
	
	//no argument constructor for hibernate
	public User() {}
	
	public User(String name, String password, String email) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
