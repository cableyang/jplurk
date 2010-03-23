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
	
	public void testGetPublicProfile() throws Exception {
		JSONObject ret = client.getPublicProfile("qrtt1");
		assertNotNull(ret);
		assertEquals("qrtt1", ret.getJSONObject("user_info").getString("nick_name"));
	}

}
