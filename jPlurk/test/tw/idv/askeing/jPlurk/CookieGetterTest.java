package tw.idv.askeing.jPlurk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;

import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.net.HttpUtil;

public class CookieGetterTest{

	AccountModel user;
	String cookie = "_optional_cookie";

	@Before
	public void setUp() throws Exception {
		user = new AccountModel("xd", "orz");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateGetCookieRequestWithOptCookie() throws Exception {
		String uri = "/foo";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpUtil.createGetCookieHttpMethod(user, uri, cookie);

		assertEquals(post1.getURI(), post2.getURI());
		assertEquals(post1.getParameter("Cookie"), post2.getParameter("Cookie"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateGetCookieRequestWithUsername() throws Exception {
		String uri = "/m/login";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpUtil.createGetCookieHttpMethod(user, uri, cookie);

		assertCookie(post1, post2, "username");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateGetCookieRequestWithNickname() throws Exception {
		String uri = "/Users/login";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpUtil.createGetCookieHttpMethod(user, uri, cookie);

		assertCookie(post1, post2, "nick_name");
	}

	private void assertCookie(PostMethod post1, PostMethod post2, String keyName) {
		assertNull(post1.getParameter("Cookie"));
		assertNull(post2.getParameter("Cookie"));
		assertEquals(post1.getParameter(keyName).getValue(), user.getName());
		assertEquals(post2.getParameter(keyName).getValue(), user.getName());
		assertEquals(post1.getParameter(keyName).getValue(), post2.getParameter(keyName).getValue());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testParseSetCookieHeader() throws Exception {
		String cookie = "plurkcookie=hbuu4hs1X4CtOZa2bnPUDAIg9+A=?chk=STg1MDA2NTY1KAou&user_id=TDNxNDYpOTRMCi4=; Domain=.plurk.com; expires=Thu, 19-Mar-2009 15:47:06 GMT; Max-Age=1209600; Path=/";
		List<Header> headerList = new ArrayList<Header>();
		for (int i = 0; i < 5; i++) {
			// make some dummy data
			headerList.add(new Header("Header_" + i , "__"+ i + cookie));
		}
		headerList.add(new Header("Set-Cookie", cookie));
		Header[] headers = new Header[headerList.size()];
		headerList.toArray(headers);

		assertEquals(
			CookieGetter.parseSetCookieHeader(headers),
			HttpUtil.parseSetCookieHeader(headers));
	}
}
