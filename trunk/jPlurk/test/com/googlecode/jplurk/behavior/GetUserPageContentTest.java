package com.googlecode.jplurk.behavior;

import junit.framework.Assert;

import org.junit.Test;


import com.googlecode.jplurk.PlurkTemplate;
import com.googlecode.jplurk.net.Result;
import com.googlecode.jplurk.utils.PatternUtils;

public class GetUserPageContentTest {

	@Test
	public void testUserIdToUID() throws Exception {
		PlurkTemplate template = PlurkTemplate.guest();
		Result result = template.doAction(GetUserPageContent.class, "qrtt1");
		Assert.assertEquals(Long.valueOf(3146394L), 
			PatternUtils.parseUserUidFromUserpage(result.getResponseBody()));
	}

	@Test
	public void testUserIdToUIDWhenNoFound() throws Exception {
		PlurkTemplate template = PlurkTemplate.guest();
		Result result = template.doAction(GetUserPageContent.class, "qrtt1qrtt1");
		Assert.assertEquals(Long.valueOf(0L), 
			PatternUtils.parseUserUidFromUserpage(result.getResponseBody()));
	}
}
