package com.googlecode.jplurk.model;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jplurk.model.Message;

public class MessageModelTest{

	Message mesg;
	@Before
	public void setUp() throws Exception {
		mesg = new Message();
	}

	@Test
	public void test_() throws Exception {
		// how to test posted ?
		// i have no idea about its specification

//		TimeZone zone = TimeZone.getTimeZone("GMT 0:00");
//	    Calendar now = Calendar.getInstance(zone);
//
//		mesg.generatePosted();
//		System.out.println(mesg.getPosted());
//
//		SimpleDateFormat year = new SimpleDateFormat("yyyy-M-d");
//		SimpleDateFormat time = new SimpleDateFormat("H:k:m");
//
//		Date current = now.getTime();
//		System.out.println(now.getTimeZone());
//		System.out.println(year.format(current) + "T" + time.format(current));
	}

}
