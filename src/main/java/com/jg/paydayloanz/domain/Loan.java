package com.jg.paydayloanz.domain;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "loan")
public class Loan {

	@Column(name = "id", unique = true)
	@NotNull
	@Id
	@GeneratedValue
	private int uid;
	
	@Column(name = "timestamp")
	private Date timestamp;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "amountWithInterest")
	private double amountWithInterest;
	
	@Column(name = "extendedAt")
	private Date extendedAt;

	@Column(name = "ip")
	private String ip;
	
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "paidOff")
	private boolean paidOff;
	
	@Column(name = "deadline")
	private Date deadline;
	
	public Loan(double amount) {
		this.amount = amount;
	}
	
	//no argument constructor - hibernate requires it
	public Loan() {}
	
	public Loan(double amount, double amountWithInterest, int userId, String ip, boolean accepted, int deadline) {
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		this.amount = amount;
		this.amountWithInterest = Double.valueOf(df.format(amountWithInterest));
		this.userId = userId;
		this.ip = ip;
		this.timestamp = new Date();
		this.paidOff = false;
		this.extendedAt = null;
		
		this.deadline = produceDeadline(new Date(), deadline);
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
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

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public double getAmount() {
		
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isPaidOff() {
		return paidOff;
	}

	public void setPaidOff(boolean paidOff) {
		this.paidOff = paidOff;
	}

	public Date getExtendedAt() {
		return extendedAt;
	}

	public void setExtendedAt(Date extendedAt) {
		this.extendedAt = extendedAt;
	}
	
	public double getAmountWithInterest() {
		return amountWithInterest;
	}

	public void setAmountWithInterest(double amountWithInterest) {
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		this.amountWithInterest = Double.valueOf(df.format(amountWithInterest))
;
	}
	
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	

	public Date produceDeadline(Date date, int days) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		
		c.add(Calendar.DATE, days);
		date = c.getTime();
		
		return date;
	}
}
