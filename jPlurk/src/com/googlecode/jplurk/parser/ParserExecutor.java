package com.googlecode.jplurk.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ParserExecutor {

	static Log logger = LogFactory.getLog(ParserExecutor.class);

	@SuppressWarnings("unchecked")
	public final static Map<String, String> parse(
			Class<? extends IParser> parserClass, String text) {
		try {
			Object o = parserClass.newInstance();
			Method m = o.getClass().getMethod("parse",
					new Class[] { String.class });
			return (Map<String, String>) m.invoke(o, text);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new HashMap<String, String>();
	}

	public final static String parseSingleValue(
			Class<? extends IParser> parserClass, String text) {
		return parse(parserClass, text).get("value");
	}

	@SuppressWarnings("serial")
	public static Map<String, String> createSingleValue(final Object value) {
		return new HashMap<String, String>() {
			{
				put("value", "" + value);
			}
		};
	}

}
