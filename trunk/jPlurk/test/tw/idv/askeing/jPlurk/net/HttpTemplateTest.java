package tw.idv.askeing.jPlurk.net;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpTemplateTest extends TestCase {
	public void testCheckAcceptStatus() throws Exception {
		HttpTemplate t = new HttpTemplate((PostMethod)null);
		assertTrue(t.checkAcceptStatus(new int[] { 5, 4, 3, 2, 1 }, 1));
		assertTrue(t.checkAcceptStatus(new int[] {
				HttpStatus.SC_MOVED_TEMPORARILY, HttpStatus.SC_OK },
				HttpStatus.SC_OK));
		assertFalse(t.checkAcceptStatus(new int[] {}, 3));
		assertFalse(t.checkAcceptStatus(new int[] { 1 }, 3));
	}
}
