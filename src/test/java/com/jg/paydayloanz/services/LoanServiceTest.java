package com.jg.paydayloanz.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import com.jg.paydayloanz.domain.Loan;
import com.jg.paydayloanz.domain.LoanRequestVerification;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.domain.dao.LoanDao;

public class LoanServiceTest {

	@Mock
	private LoanDao loanDao;

	@Mock
	private Loan loan;

	@Mock
	private LoanRequestVerification request;

	@Mock
	private Environment env;

	@InjectMocks
	private LoanService loanService = new LoanService();

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindingLoanByUid() {

		Loan l1 = new Loan();
		Loan l2 = new Loan();

		when(loanDao.findByUid(loan.getUid())).thenReturn(l1);
		assertEquals(l1, loanDao.findByUid(loan.getUid()));

		Loan testLoan = loanService.findByUid(loan.getUid());
		verify(loanDao, atLeastOnce()).findByUid(loan.getUid());
		assertEquals(testLoan, l1);
		assertNotSame(testLoan, l2);
	}

	@Test
	public void testSavingLoan() {
		loanService.saveLoan(loan);
		verify(loanDao).save(loan);
	}

	@Test
	public void testApplyingForLoan() {

		String ip = "1.1.1.1";
		double amount = 10;
		User user = new User();
		int term = 5;

		when(request.verifyApplication(anyString(), any(), anyDouble(), any(), anyInt())).thenReturn(true);

		boolean check = loanService.applyForLoan(ip, amount, user, term);
		verify(request).verifyApplication(anyString(), any(), anyDouble(), any(), anyInt());
		assertTrue(check);
	}

	@Test
	public void testCalculatingAmountWithInterestAndCommission() {

		int term = 10;
		double amount = 1000;

		double termYearly = ((double)10/365);
		
		double result = 500 + 1000 + (0.4 * amount * termYearly);

		when(env.getProperty("EXTENSION")).thenReturn("1.5");
		when(env.getProperty("INTEREST")).thenReturn("0.4");
		when(env.getProperty("COMMISSION")).thenReturn("500");

		double test = loanService.calculateAmountWithInterestAndCommission(term, amount, false);

		assertEquals(test, result, 0);

	}
	
	@Test
	public void testCalculatingAmountWithExtension() {

		int term = 10;
		double amount = 1000;

		double termYearly = ((double)10/365);
		
		double result = 500 + 1000 + (0.4 * 1.5 * amount * termYearly);
		
		when(env.getProperty("EXTENSION")).thenReturn("1.5");
		when(env.getProperty("COMMISSION")).thenReturn("500");
		when(env.getProperty("INTEREST")).thenReturn("0.4");
		
		double test = loanService.calculateAmountWithInterestAndCommission(term, amount, true);
		
		assertEquals(test,result,0);
		
	}
}
