package com.google.jplurk;

import java.util.Map;

public class MapHelper {

	Map<String, String> map;

	private String key;
	private String value;

	public MapHelper(Map<String, String> map) {
		this.map = map;
	}

	public MapHelper k(String key) {
		this.key = key;
		checkAndPush();
		return this;
	}

	public MapHelper v(String value) {
		this.value = value;
		checkAndPush();
		return this;
	}

	private void checkAndPush() {
		if (key != null && value != null) {
			map.put(key, value);
			this.key = null;
			this.value = null;
		}
	}

	public Map<String, String> getMap() {
		return map;
	}
}
