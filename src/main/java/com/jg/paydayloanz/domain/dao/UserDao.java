package com.jg.paydayloanz.domain.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jg.paydayloanz.domain.User;

@Repository("User Repository")
@Transactional
public interface UserDao extends CrudRepository<User, Integer>{

	public User findByUid(int uid);
	
	public List<User> findByEmail(String email);	

	public List<User> findByName(String name);
}
