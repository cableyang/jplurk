package com.googlecode.jplurk.exception;

public class NotLoginException extends RuntimeException{

	private static final long serialVersionUID = -4616796686134343587L;

	public NotLoginException() {
		super("You must do login before other actions.");
	}
}
