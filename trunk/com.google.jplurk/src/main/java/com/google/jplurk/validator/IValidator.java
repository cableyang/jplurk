package com.google.jplurk.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;

import com.google.jplurk.exception.PlurkException;

public interface IValidator {

	public boolean validate(String value);

	static class ValidatorUtils {
		public static boolean validate(Class<? extends IValidator> validatorClazz, String value) throws PlurkException{

				Object v = null;
				try {
					v = ConstructorUtils.invokeConstructor(validatorClazz, new Object[0]);
				} catch (Exception e) {
					throw new PlurkException(
						"cannot create validator from [" + validatorClazz + "]", e);
				}

				Boolean passValidation = false;
				try {
					passValidation = (Boolean) MethodUtils.invokeMethod(v, "validate", value);
				} catch (Exception e) {
					throw new PlurkException(
						"cannot invoke method from [" + validatorClazz + "]", e);
				}

				return passValidation;
		}
	}

}
