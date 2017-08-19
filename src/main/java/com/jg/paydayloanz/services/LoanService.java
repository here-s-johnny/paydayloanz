package com.jg.paydayloanz.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jg.paydayloanz.domain.Loan;
import com.jg.paydayloanz.domain.LoanRequestVerification;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.domain.dao.LoanDao;

@Service("LoanService")
@PropertySource("file:src/main/resources/config.properties")
public class LoanService {

	@Autowired
	private LoanDao loanDao;
	
	@Autowired
	private LoanRequestVerification request;

	@Autowired
	private Environment env;

	public Loan findByUid(int uid) {
		return loanDao.findByUid(uid);
	}

	public List<Loan> findByUserId(int uid) {
		return loanDao.findByUserId(uid);
	}

	public void saveLoan(Loan loan) {
		loanDao.save(loan);
	}
	
	public boolean applyForLoan(String ip, double value, User user, int term) {

		Date date = new Date();

		return request.verifyApplication(ip, date, value, user, term);

	}

	public double calculateAmountWithInterestAndCommission(int term, double amount, boolean extension) {
		
		double interestRegular = Double.parseDouble(env.getProperty("INTEREST"));
		double interestExtended = Double.parseDouble(env.getProperty("EXTENSION")) *
									Double.parseDouble(env.getProperty("INTEREST"));
		
		double interestPercent = (extension) ? interestExtended :
											   interestRegular;
		
		double commission = Double.parseDouble(env.getProperty("COMMISSION"));
		double interest = interestPercent * amount;
		
		return commission + amount + (interest * ((double)term/365));
	}

}
