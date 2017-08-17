package com.jg.paydayloanz.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
			assertEquals(l1,loanDao.findByUid(loan.getUid()));
			
			Loan testLoan = loanService.findByUid(loan.getUid());
			verify(loanDao, atLeastOnce()).findByUid(loan.getUid());
			assertEquals(testLoan, l1);
			assertNotSame(testLoan,l2);	
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
		
		public void testCalculatingAmountWithInterestAndCommission() {
			
		}
}



