package com.jg.paydayloanz.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jg.paydayloanz.domain.User;

@Controller  
public class HelloController {  
	
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ModelAndView hello(HttpSession session){  
    	
		ModelAndView mav = new ModelAndView("index");
		
    	if (session.getAttribute("user") != null) {
    		User user = (User)session.getAttribute("user");
    		mav.addObject("username", user.getName());
    	} else {
    		mav.addObject("username", "guest");
    	}
        return mav;
    }  
    
}  