package com.jg.paydayloanz.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jg.paydayloanz.domain.LoginForm;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.services.UserService;

@Controller
public class LoginController {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView showLogin() {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("login", new LoginForm());
		return mav;
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public ModelAndView loginProcess(@ModelAttribute("login") LoginForm login, HttpSession session,
									RedirectAttributes redirectAttributes) {

		ModelAndView mav = null;
		User user = userService.validateUser(login);
		if (null != user) {
			session.setAttribute("user", user);
			mav = new ModelAndView("redirect:/hello");
		} else {
			mav = new ModelAndView("redirect:/login");
		    redirectAttributes.addFlashAttribute("message", "Username or Password is wrong!!");
		}
		return mav;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		
		session.setAttribute("user", null);
		ModelAndView mav = new ModelAndView("redirect:/hello");
		
		return mav;
	}
}