package tw.idv.askeing.jPlurk;

import junit.framework.TestCase;

import org.apache.commons.httpclient.methods.PostMethod;

import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.net.HttpMethodUtil;

public class CookieGetterTest extends TestCase{

	AccountModel user;
	String cookie = "_optional_cookie";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		user = new AccountModel("xd", "orz");
	}

	public void testCreateGetCookieRequestWithOptCookie() throws Exception {
		String uri = "/foo";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpMethodUtil.prepareForQueryCookie(user, uri, cookie);

		assertEquals(post1.getURI(), post2.getURI());
		assertEquals(post1.getParameter("Cookie"), post2.getParameter("Cookie"));
	}

	public void testCreateGetCookieRequestWithUsername() throws Exception {
		String uri = "/m/login";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpMethodUtil.prepareForQueryCookie(user, uri, cookie);

		assertCookie(post1, post2, "username");
	}

	public void testCreateGetCookieRequestWithNickname() throws Exception {
		String uri = "/Users/login";
		PostMethod post1 = CookieGetter.createGetCookieRequest(user, uri, cookie);
		PostMethod post2 = HttpMethodUtil.prepareForQueryCookie(user, uri, cookie);

		assertCookie(post1, post2, "nick_name");
	}

	private void assertCookie(PostMethod post1, PostMethod post2, String keyName) {
		assertNull(post1.getParameter("Cookie"));
		assertNull(post2.getParameter("Cookie"));
		assertEquals(post1.getParameter(keyName).getValue(), user.getName());
		assertEquals(post2.getParameter(keyName).getValue(), user.getName());
		assertEquals(post1.getParameter(keyName).getValue(), post2.getParameter(keyName).getValue());
	}
}
