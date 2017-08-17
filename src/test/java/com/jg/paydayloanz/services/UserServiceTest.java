package com.jg.paydayloanz.services;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.jg.paydayloanz.domain.LoginForm;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.domain.dao.UserDao;

public class UserServiceTest {

	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;
	
	@InjectMocks
	private UserService userService = new UserService();
	
	 @Before
	    public void setupMock() {
	       MockitoAnnotations.initMocks(this);
	    }
	
	@Test
	public void testFindingUserByEmail() {
		
		List<User> users = Arrays.asList(new User(), new User());
		List<User> wrongUsers = Arrays.asList(new User(), new User());
		
		when(userDao.findByEmail(user.getEmail())).thenReturn(users);
		assertEquals(users,userDao.findByEmail(user.getEmail()));
		
		List<User> testUsers = userService.findByEmail(user.getEmail());
		verify(userDao, atLeastOnce()).findByEmail(user.getEmail());
		assertEquals(testUsers, users);
		assertNotSame(testUsers,wrongUsers);
	}
	
	@Test
	public void testFindingUserByUid() {
		
		User u1 = new User();
		User u2 = new User();
		
		when(userDao.findByUid(user.getUid())).thenReturn(u1);
		assertEquals(u1,userDao.findByUid(user.getUid()));
		
		User testUser = userService.findByUid(user.getUid());
		verify(userDao, atLeastOnce()).findByUid(user.getUid());
		assertEquals(testUser, u1);
		assertNotSame(testUser,u2);	
	}

	@Test
	public void testSavingUser() {
		userService.saveUser(user);
		verify(userDao).save(user);
	}
	
	@Test
	public void testValidateUser_Successful() {
		
		LoginForm form = new LoginForm("testName", "testPswd", "testEmail");
		List<User> users = Arrays.asList(new User("testName", "testPswd", "testEmail"), 
										new User("testName", "b", "c"), 
										new User("testName", "c", "testEmail"));
		
		when(userDao.findByName(anyString())).thenReturn(users);
		
		User testUser = userService.validateUser(form);
		verify(userDao).findByName("testName");
		assertNotNull(testUser);
		assertEquals(testUser.getName(), "testName");
		assertEquals(testUser.getPassword(), "testPswd");
		assertEquals(testUser.getEmail(), "testEmail");
		
		
	}
	
	@Test
	public void testValidateUser_Unsuccessful_NoUserInDB() {
		
		LoginForm form = new LoginForm("testName", "testPswd", "testEmail");
		List<User> users = Arrays.asList(new User("testName", "b", "c"), 
										new User("testName", "c", "testEmail"));
		
		when(userDao.findByName(anyString())).thenReturn(users);
		
		User testUser = userService.validateUser(form);
		verify(userDao).findByName("testName");
		
		assertNull(testUser);
	}
	
	@Test
	public void testValidateUser_Unsuccessful_EmptyDB() {
		
		LoginForm form = new LoginForm("testName", "testPswd", "testEmail");
		List<User> users = Arrays.asList();
		
		when(userDao.findByName(anyString())).thenReturn(users);
		
		User testUser = userService.validateUser(form);
		verify(userDao).findByName("testName");
		
		assertNull(testUser);
	}
	
	@Test
	public void testValidateRegistration_Successful() {
		
		User user = new User("test", "test", "test");
		
		List<User> users = Arrays.asList();
		
		when(userDao.findByName(anyString())).thenReturn(users);
		when(userDao.findByEmail(anyString())).thenReturn(users);

		boolean check = userService.validateRegistration(user);
		verify(userDao).findByName("test");
		verify(userDao).findByEmail("test");

		assertTrue(check);
	}
	
	@Test
	public void testValidateRegistration_Unsuccessful_UsernameExists() {
		
		User user = new User("test", "test", "test");
		
		List<User> users1 = Arrays.asList(new User("test", "b", "c"), 
										 new User("test", "c", "testEmail"),
										 new User("test", "test", "c"));
		List<User> users2 = Arrays.asList();
		
		when(userDao.findByName(anyString())).thenReturn(users1);
		when(userDao.findByEmail(anyString())).thenReturn(users2);

		boolean check = userService.validateRegistration(user);
		verify(userDao).findByName("test");
		verify(userDao).findByEmail("test");

		assertFalse(check);
	}
	
	@Test
	public void testValidateRegistration_Unsuccessful_EmailExists() {
		
		User user = new User("test", "test", "test");
		
		List<User> users1 = Arrays.asList();
		
		List<User> users2 = Arrays.asList(new User("testName", "b", "testEmail"), 
				 						new User("a", "c", "testEmail"),
				 						new User("a", "test", "testEmail"));
		
		when(userDao.findByName(anyString())).thenReturn(users1);
		when(userDao.findByEmail(anyString())).thenReturn(users2);

		boolean check = userService.validateRegistration(user);
		verify(userDao).findByName("test");
		verify(userDao).findByEmail("test");

		assertFalse(check);
	}
	
}
