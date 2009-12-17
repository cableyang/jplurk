package com.google.jplurk.exception;

public class PlurkException extends Throwable {

	private static final long serialVersionUID = 1L;

	public PlurkException() {

	}

	public PlurkException(String message) {
		super(message);
	}

	public PlurkException(Throwable t) {
		super(t);
	}

	public PlurkException(String message, Throwable t) {
		super(message, t);
	}
}
