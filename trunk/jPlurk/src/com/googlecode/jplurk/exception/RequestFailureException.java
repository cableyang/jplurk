package com.googlecode.jplurk.exception;

public class RequestFailureException extends RuntimeException{

	private static final long serialVersionUID = 2405924251357922059L;

	public RequestFailureException() {
		super("request is failure. please check the log messages.");
	}
}
