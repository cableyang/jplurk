package com.google.jplurk.validator;

import com.google.jplurk.exception.PlurkException;

import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {

	private boolean validate(String email) throws PlurkException{
		return IValidator.ValidatorUtils.validate(EmailValidator.class, email);
	}

	public void testValidate() throws Exception, PlurkException {
		assertTrue(validate("qrtt1@hoooootmail.com"));
		assertTrue(validate("qrtt123.d34@hoooootmail.com"));
		assertTrue(validate("qrtt123.tw@hoooootmail.com"));
		assertTrue(validate("qrtt123.tw@hoooootmail.com.dotdotdot"));
	}

}
