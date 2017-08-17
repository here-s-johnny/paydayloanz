package com.jg.paydayloanz.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.jg.paydayloanz.domain.dao.AttemptDao;
import com.jg.paydayloanz.domain.dao.LoanDao;

@Component
@PropertySource("file:src/main/resources/config.properties")
public class LoanRequestVerification {

	@Autowired
	private LoanDao loanDao;
	
	@Autowired
	private AttemptDao attemptDao;

	@Autowired
	private Environment env;

	public boolean verifyApplication(String ip, Date date, double amount, User user, int term) {
		
		return checkAmount(amount) && 
			   checkTerm(term) &&
			   checkIfIpNotOverloaded(ip) && 
			   checkIfNotMaxAtNight(date, amount) &&
			   checkIfNoLoansPending(user);
	}
	
	
	// double checking if the amount entered is within the allowed interval
	// it should also be checked at the front end level
	private boolean checkAmount(double amount) {
		
		return amount <= Integer.parseInt(env.getProperty("MAX_AMOUNT")) &&
				amount >= Integer.parseInt(env.getProperty("MIN_AMOUNT"));
	}
	
	// double checking if the term entered is within the allowed interval
	// it should also be checked at the front end level
	private boolean checkTerm(int term) {
		
		return term <= Integer.parseInt(env.getProperty("MAX_DAYS")) &&
				term >= Integer.parseInt(env.getProperty("MIN_DAYS"));
	}

	// checking if the ip used to apply for a loan hasn't been used more 
	// than the allowed number of times in the last 24h
	private boolean checkIfIpNotOverloaded(String ip) {

		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate today = LocalDate.now(ZoneId.of(env.getProperty("TIMEZONE")));
		LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
		Date start = Date.from(todayMidnight.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(tomorrowMidnight.atZone(ZoneId.systemDefault()).toInstant());

		int counter = 0;
		List<Loan> loans = loanDao.findByIp(ip);
		for (Loan l : loans) {
			if (!(l.getTimestamp().before(start) || l.getTimestamp().after(end))) {
				counter++;
			}
		}
		List<FailedAttempt> attempts = attemptDao.findByIp(ip);
		for (FailedAttempt a : attempts) {
			if (!(a.getTimestamp().before(start) || a.getTimestamp().after(end))) {
				counter++;
			}
		}
		
		return (counter < Integer.parseInt(env.getProperty("MAX_APPLICATIONS")));
	}

	// checking the constraint that the maximal amount should not be permitted for loan
	// at certain time period during the day (namely from 00:00 till SUNRISE)
	private boolean checkIfNotMaxAtNight(Date date, double value) {

		if (value == Integer.parseInt(env.getProperty("MAX_AMOUNT"))) {

			LocalTime midnight = LocalTime.MIDNIGHT;
			LocalDate today = LocalDate.now(ZoneId.of(env.getProperty("TIMEZONE")));
			LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

			LocalTime sunrise = midnight.plusHours(Integer.parseInt(env.getProperty("SUNRISE")));
			LocalDateTime todaySunrise = LocalDateTime.of(today, sunrise);

			Date start = Date.from(todayMidnight.atZone(ZoneId.systemDefault()).toInstant());
			Date end = Date.from(todaySunrise.atZone(ZoneId.systemDefault()).toInstant());

			if (!(date.before(start) || date.after(end))) {
				return false;
			}
			
		}

		return true;
	}
	
	private boolean checkIfNoLoansPending(User user) {
		List<Loan> loans = loanDao.findByUserId(user.getUid());
		
		List<Loan> loansFiltered = loans.stream().filter(l -> (!l.isPaidOff())).collect(Collectors.toList());
		
		return loansFiltered.size() == 0;
		
		
	}

}
