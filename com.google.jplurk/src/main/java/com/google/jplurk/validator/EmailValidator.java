package com.google.jplurk.validator;

import com.google.jplurk.exception.PlurkException;

public class EmailValidator implements IValidator {


	public boolean validate(String value) {
		return value.matches("(\\w+)(\\.\\w+)*@(\\w+\\.)(\\w+)(\\.\\w+)*");
	}

	public static void main(String[] args) throws PlurkException {
		boolean xdxd = IValidator.ValidatorUtils.validate(EmailValidator.class, "xd");
		System.out.println(xdxd);
	}

}
