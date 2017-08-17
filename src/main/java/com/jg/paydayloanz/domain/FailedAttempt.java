package com.jg.paydayloanz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "failedAttempt")
public class FailedAttempt {

	@Column(name = "id", unique = true)
	@NotNull
	@Id
	@GeneratedValue
	private int uid;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "userId")
	private int userId;
	
	//no argument constructor - hibernate requires it
	public FailedAttempt() {}
	
	public FailedAttempt(int userId, String ip) {
		this.userId = userId;
		this.ip = ip;
		this.timestamp = new Date();
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
