package com.jg.paydayloanz.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jg.paydayloanz.domain.FailedAttempt;
import com.jg.paydayloanz.domain.Loan;
import com.jg.paydayloanz.domain.User;
import com.jg.paydayloanz.exceptions.NoLoggedInUserException;
import com.jg.paydayloanz.services.AttemptService;
import com.jg.paydayloanz.services.LoanService;

@Controller
public class LoansController {
	
	@Autowired
	private LoanService loanService;
	
	@Autowired
	private AttemptService attemptService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value = "/myloans", method = RequestMethod.GET)
	public ModelAndView showLoans(HttpSession session) {
		ModelAndView mav = new ModelAndView("myloans");
		User user = (User)session.getAttribute("user");
		
		if (user == null) throw new NoLoggedInUserException();
		
		List<Loan> loans = loanService.findByUserId(user.getUid());
		
		mav.addObject("loans", loans);
		mav.addObject("interest", env.getProperty("INTEREST"));
		return mav;
	}

	@RequestMapping(value = "/payoff", method = RequestMethod.GET)
	public ModelAndView payoff(HttpSession session,@RequestParam(value="loan", required=false) Integer loan, 
								RedirectAttributes redirectAttributes) {
		
		ModelAndView mav = null;
				
		if (session.getAttribute("user") == null || loan == null) throw new NoLoggedInUserException();
		
		
		Loan loanToUpdate = loanService.findByUid(loan);
		loanToUpdate.setPaidOff(true);
		loanService.saveLoan(loanToUpdate);
		
	    redirectAttributes.addFlashAttribute("message", "You have paid back your loan, thank you!");
		
		mav = new ModelAndView("redirect:/myloans");

		
		
		return mav;
	}
	
	@RequestMapping(value = "/apply", method = RequestMethod.GET)
	public ModelAndView apply(HttpSession session) {
		
		if (session.getAttribute("user") == null) throw new NoLoggedInUserException();
		
		ModelAndView mav = new ModelAndView("apply");
		
		mav.addObject("maxAmount", env.getProperty("MAX_AMOUNT"));
		mav.addObject("maxDays", env.getProperty("MAX_DAYS"));
		mav.addObject("minAmount", env.getProperty("MIN_AMOUNT"));
		mav.addObject("minDays", env.getProperty("MIN_DAYS"));
		
		return mav;
	}
	
	@RequestMapping(value = "/applyProcess", method = RequestMethod.POST)
	public ModelAndView applyProcess(HttpServletRequest request, HttpSession session,
									RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("user") == null) throw new NoLoggedInUserException();
		
		double amount = Double.parseDouble(request.getParameter("amount"));
		
		int term = Integer.parseInt(request.getParameter("term"));
		
		User user = (User)session.getAttribute("user");
		
		String ip = request.getRemoteAddr();
		
		double amountWithInterest = loanService.calculateAmountWithInterestAndCommission(term, amount);
		
		if (loanService.applyForLoan(ip, amount, user, term)) {
			
			Loan loan = new Loan(amount, amountWithInterest, user.getUid(), ip, true, term);
			loanService.saveLoan(loan);
			ModelAndView mav = new ModelAndView("redirect:/myloans");
		    redirectAttributes.addFlashAttribute("message", "Congratulations, you have successfully taken a loan!");
		    return mav;
		    
		} else {
			
			FailedAttempt attempt = new FailedAttempt(user.getUid(), ip);
			attemptService.saveAttempt(attempt);
			ModelAndView mav = new ModelAndView("redirect:/apply");
			redirectAttributes.addFlashAttribute("message", "You application has been rejected due to one or more of the following reasons: <br>"
					+ "The values you entered exceeded the maximum allowed amount or days<br>"
					+ "This ip address has been used to issue a loan request today more times than allowed<br>"
					+ "It is too late at night to apply for a loan with the maximum amount<br>"
					+ "You still have an unpaid loan pending");
			return mav;
			
		}
				
	}
	
	@RequestMapping(value = "/extend", method = RequestMethod.GET)
	public ModelAndView extend(HttpSession session, @RequestParam(value="loan", required=false) int loan,
								RedirectAttributes redirectAttributes) {
		
		if (session.getAttribute("user") == null) throw new NoLoggedInUserException();
		
		ModelAndView mav = null;
		
		Loan loanToUpdate = loanService.findByUid(loan);
		
		double amountWithInterest = loanService.calculateAmountWithExtension(loanToUpdate);
				
		Date previousDate = loanToUpdate.getDeadline();
		int days = Integer.parseInt(env.getProperty("EXTENSION_TIME"));
		
		loanToUpdate.setDeadline(loanToUpdate.produceDeadline(previousDate, days));
		loanToUpdate.setExtendedAt(new Date());
		loanToUpdate.setAmountWithInterest(amountWithInterest);
		
		loanService.saveLoan(loanToUpdate);
		
	    redirectAttributes.addFlashAttribute("message", "You have successfully extended your loan! You now have one more week to pay it back, but the amount has increased as well.");
		
		mav = new ModelAndView("redirect:/myloans");
		
		
		return mav;
	}
	
	@ExceptionHandler
	public String exceptionHandler(NoLoggedInUserException exception) {
		return "redirect:/hello";
	}
	
}
