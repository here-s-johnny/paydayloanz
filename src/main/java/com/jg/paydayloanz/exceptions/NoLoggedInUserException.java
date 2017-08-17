package com.jg.paydayloanz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="no logged in user")
public class NoLoggedInUserException extends RuntimeException {
	private static final long serialVersionUID = 7807233797509714275L;
}
