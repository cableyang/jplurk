package com.google.jplurk.validator;

import org.apache.commons.lang.math.NumberUtils;

public class NonNegativeIntegerValidator implements IValidator {

	public boolean validate(String value) {
		return NumberUtils.toInt(value, -1) >= 0;
	}

}
