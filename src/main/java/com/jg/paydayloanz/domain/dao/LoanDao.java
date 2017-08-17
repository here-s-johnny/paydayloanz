package com.jg.paydayloanz.domain.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jg.paydayloanz.domain.Loan;

@Repository("Loan Repository")
@Transactional
public interface LoanDao extends CrudRepository<Loan, Integer>{

	public Loan findByUid(int uid);
	
	public List<Loan> findByUserId(int userId);
	
	public List<Loan> findByIp(String ip);

}