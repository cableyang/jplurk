package com.googlecode.jplurk.exception;

import com.googlecode.jplurk.model.Account;

public class LoginFailureException extends RuntimeException {
	private static final long serialVersionUID = 8154874004798387253L;

	public LoginFailureException(Account account) {
		super("Cannot login with account: " + account.getName()
				+ ". Please check the user name and password");
	}
}
