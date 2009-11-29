package tw.idv.askeing.jPlurk.net;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

public class HttpTemplateTest{
	@Test
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
