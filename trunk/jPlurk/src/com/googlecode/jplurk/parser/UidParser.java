package com.googlecode.jplurk.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UidParser implements IParser {

	static Pattern patt = Pattern.compile(".*\"user_id\"\\s*:\\s*\\d+,.*");

	@Override
	public Map<String, String> parse(String text) {
		String line = StringUtils.substringBetween(text, "var SETTINGS =", ";");
		if(line == null){
			return new HashMap<String, String>();
		}
		JSONObject obj = (JSONObject) JSONValue.parse(line);
		return ParserExecutor.createSingleValue(obj.get("user_id"));
	}

}
