package com.googlecode.jplurk.behavior;

import static junit.framework.Assert.*;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.jplurk.IPlurkAgent;
import com.googlecode.jplurk.PlurkAgent;
import com.googlecode.jplurk.model.Account;
import com.googlecode.jplurk.model.Qualifier;
import com.googlecode.jplurk.net.Result;

public class SessionBehaviorTest {

	IPlurkAgent agent;

	@Before
	public void setUpUserInfo() throws Exception {
		Account account = Account.createWithDynamicProperties();
		assertFalse("user name sholud be set",
				StringUtils.isBlank(account	.getName()));
		assertFalse("password sholud be set",
				StringUtils.isBlank(account	.getPassword()));

		agent = new PlurkAgent(account);
		Result result = agent.login();
		assertTrue("login failed, check your user name and password", result.isOk());
	}

	@Test
	public void testAddAndRemovePlurk() throws Exception {


		Result result = agent.addPlurk(Qualifier.SHARES, RandomStringUtils.randomAscii(100));
		assertTrue(result.isOk());

		String plurkId = (String) result.getAttachement().get("plurk_id");
		assertTrue(StringUtils.isNotBlank(plurkId));

		result = agent.deletePlurk(NumberUtils.toInt(plurkId));
		assertTrue(result.isOk());

	}

}
