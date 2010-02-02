package com.google.jplurk;

import junit.framework.TestCase;

public class DateTimeTest extends TestCase{

	public void testFoo() throws Exception {
//		Sat, 09 Jan 2010 08:22:53 GMT
//		INFO : [Thread-12] (PlurkFacade.java:62) getPlurks | --> Sat, 09 Jan 2010 08:22:52 GMT
//		INFO : [Thread-12] (PlurkFacade.java:62) getPlurks | --> Sat, 09 Jan 2010 08:22:52 GMT
//		INFO : [Thread-12] (PlurkFacade.java:62) getPlurks | --> Mon, 28 Dec 2009 02:24:28 GMT
//		INFO : [Thread-12] (PlurkFacade.java:62) getPlurks | --> Tue, 22 Dec 2009 00:46:47 GMT
		System.out.println(DateTime.create("Sat, 09 Jan 2010 08:22:53 GMT").toCalendar().getTime());
	}
}
