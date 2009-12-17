package com.google.jplurk.validator;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.jplurk.exception.PlurkException;

public interface IValidator {

	public boolean validate(String value);

	static class ValidatorUtils {

		static Logger logger = LoggerFactory.getLogger(ValidatorUtils.class);

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

				if (!passValidation) {
					logger.warn("value[" + value + "] cannot pass the validator[" + validatorClazz + "]");
				}

				return passValidation;
		}
	}

}
