package com.jg.paydayloanz.domain.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jg.paydayloanz.domain.FailedAttempt;

@Repository("Attempt Repository")
@Transactional
public interface AttemptDao extends CrudRepository<FailedAttempt, Integer>{
	
	public List<FailedAttempt> findByIp(String ip);

}
