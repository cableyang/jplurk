package com.google.jplurk.validator;

import org.apache.commons.lang.StringUtils;

public class TrueFalseLiteralValidator implements IValidator {

	public boolean validate(String value) {
		return StringUtils.equals("true", value)
				|| StringUtils.equals("false", value);
	}

}
