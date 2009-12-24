package com.google.jplurk.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.jplurk.validator.IValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validation {

	Validator[] value();

	@interface Validator{
		String field();
		Class<? extends IValidator> validator();
	}

}
