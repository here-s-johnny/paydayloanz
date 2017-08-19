package com.jg.paydayloanz.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.core.env.Environment;

import com.jg.paydayloanz.domain.dao.AttemptDao;
import com.jg.paydayloanz.domain.dao.LoanDao;

public class LoanRequestVerificationTest {
	
	@Mock
	private LoanDao loanDao;
	
	@Mock
	private AttemptDao attemptDao;
	
	@Mock
	private User user;
	
	@Mock
	private Environment env;
	
	@InjectMocks
	private LoanRequestVerification request = new LoanRequestVerification();
	
	@Before
    public void setupMock() {
       MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testVerifyingApplication_Successful() {
		
		double amount = 100;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 10:58
		Date date = new Date(1503046680000L);
		int term = 10;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("0");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertTrue(test);
		
	}
	
	@Test
	public void testVerifyingApplication_Successful_CornerCases() {
		
		double amount = 1000;
		String ip = "1.1.1.1";
		
		// this is 17.08.2017 23:59:59
		Date date = new Date(1503007199000L);

		int term = 1;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("1000");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertTrue(test);
		
		amount = 3000;
		term = 30;
		
		// this is 18.08.2017 06:01:00
		date = new Date(1503028860000L);
		
		test = request.verifyApplication(ip, date, amount, user, term);
		assertTrue(test);
		
	}
	
	@Test
	public void testVerifyingApplication_Unsuccessful_AmountNotWithinRange() {
		
		double amount = 10000;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 10:58
		Date date = new Date(1503046680000L);
		int term = 10;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("1000");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
		
		amount = 500;
		
		test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
	}

	@Test
	public void testVerifyingApplication_Unsuccessful_TermNotWithinRange() {
		
		double amount = 1000;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 10:58
		Date date = new Date(1503046680000L);
		int term = 31;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("0");
		when(env.getProperty("MIN_DAYS")).thenReturn("10");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
		
		term = 9;
		
		test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
	}
	
	@Test
	public void testVerifyingApplication_Unsuccessful_IpOverloaded() {
		
		double amount = 100;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 10:58
		Date date = new Date(1503046680000L);
		int term = 10;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true, ip), new Loan(new Date(), true, ip), new Loan(new Date(), true, ip));
		List<FailedAttempt> attempts = Arrays.asList();
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("0");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("3");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
		
		loans = Arrays.asList();
		attempts = Arrays.asList(new FailedAttempt(new Date(), ip), new FailedAttempt(new Date(), ip), new FailedAttempt(new Date(), ip));
		assertFalse(test);
		
		loans = Arrays.asList(new Loan(new Date(), true, ip), new Loan(new Date(), true, ip), new Loan(new Date(), true, ip));
		attempts = Arrays.asList(new FailedAttempt(new Date(), ip), new FailedAttempt(new Date(), ip), new FailedAttempt(new Date(), ip));
		assertFalse(test);

	}
	
	
	@Test
	public void testVerifyingApplication_Unsuccessful_MaxAmountWithinTheForbiddenTimeScope() {
		
		double amount = 3000;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 00:05:00
		Date date = new Date(1503007500000L);
		int term = 10;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), true));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("0");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
		
	}
	
	@Test
	public void testVerifyingApplication_Unsuccessful_LoanPending() {
		
		double amount = 3000;
		String ip = "1.1.1.1";
		
		// this is 18.08.2017 10:58
		Date date = new Date(1503046680000L);
		int term = 10;
		
		List<Loan> loans = Arrays.asList(new Loan(new Date(), false));
		List<FailedAttempt> attempts = Arrays.asList(new FailedAttempt(new Date()));
		
		when(env.getProperty("MAX_AMOUNT")).thenReturn("3000");
		when(env.getProperty("MIN_AMOUNT")).thenReturn("0");
		when(env.getProperty("MIN_DAYS")).thenReturn("1");
		when(env.getProperty("MAX_DAYS")).thenReturn("30");
		when(env.getProperty("TIMEZONE")).thenReturn("Europe/Warsaw");
		when(env.getProperty("SUNRISE")).thenReturn("6");
		when(env.getProperty("MAX_APPLICATIONS")).thenReturn("10");
		when(loanDao.findByIp(ip)).thenReturn(loans);
		when(attemptDao.findByIp(ip)).thenReturn(attempts);
		when(loanDao.findByUserId(user.getUid())).thenReturn(loans);
		
		boolean test = request.verifyApplication(ip, date, amount, user, term);
		assertFalse(test);
		
	}
	
}
