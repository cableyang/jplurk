package com.google.jplurk;

import junit.framework.TestCase;

public class PlurkUtilsTest extends TestCase {

	public void testBirthday() throws Exception {
		assertEquals("1911-01-01", PlurkUtils.birthday(1911, 1, 1));
	}
}
