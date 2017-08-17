package com.jg.paydayloanz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jg.paydayloanz.domain.FailedAttempt;
import com.jg.paydayloanz.domain.dao.AttemptDao;

@Service
public class AttemptService {
	
	@Autowired
	private AttemptDao attemptDao;

	public void saveAttempt(FailedAttempt attempt) {
		attemptDao.save(attempt);
	}
}
