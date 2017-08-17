package com.jg.paydayloanz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jg.paydayloanz.domain.LoginForm;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.domain.dao.UserDao;

@Service("userService")
public class UserService {

	@Autowired
	private UserDao userDao;

	public User findByUid(int uid) {
		return userDao.findByUid(uid);
	}
	
	public List<User> findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public void saveUser(User user) {
		userDao.save(user);
	}

	// checks credentials during login - namely checks if a user with given username and password exists
	public User validateUser(LoginForm login) {
		
		List<User> users = userDao.findByName(login.getUsername());
		
		List<User> usersFiltered = users.stream().filter(u -> (u.getPassword().equals(login.getPassword()))).collect(Collectors.toList());

	    return usersFiltered.size() > 0 ? usersFiltered.get(0) : null;
	}
	
	// checks if the provided username and email are not already in use
	public boolean validateRegistration(User user) {
		
		List<User> users = userDao.findByName(user.getName());
		
		List<User> emails = userDao.findByEmail(user.getEmail());
		
		return users.size() == 0 && emails.size() == 0;
	}

}