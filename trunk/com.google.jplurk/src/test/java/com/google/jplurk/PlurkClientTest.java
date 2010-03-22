package com.google.jplurk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.google.jplurk.exception.PlurkException;

import junit.framework.TestCase;

public class PlurkClientTest extends TestCase {

	private static Log logger = LogFactory.getLog(PlurkClientTest.class);
	private PlurkClient client;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			client = new PlurkClient(new PlurkSettings());
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void testSearchUser() throws Exception {
		JSONObject ret = client.searchUser("plurkbuddy");
		assertNotNull(ret);
	}

}
