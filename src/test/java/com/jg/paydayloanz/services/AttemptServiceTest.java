package com.jg.paydayloanz.services;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jg.paydayloanz.domain.FailedAttempt;
import com.jg.paydayloanz.domain.dao.AttemptDao;

public class AttemptServiceTest {
	
	@Mock
	private AttemptDao attemptDao;
	
	@Mock
	private FailedAttempt attempt;
	
	@InjectMocks
	private AttemptService attemptService = new AttemptService();
	
	 @Before
	    public void setupMock() {
	       MockitoAnnotations.initMocks(this);
	 }

	@Test
	public void testSavingAttempt() {
		attemptService.saveAttempt(attempt);
		verify(attemptDao).save(attempt);
	}

}
