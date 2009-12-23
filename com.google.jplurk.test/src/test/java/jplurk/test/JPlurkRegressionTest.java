package jplurk.test;

import javax.swing.JOptionPane;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.jplurk.PlurkClient;
import com.google.jplurk.PlurkSettings;
import com.google.jplurk.Qualifier;
import com.google.jplurk.exception.PlurkException;

/**
 * Unit test for simple App.
 */
public class JPlurkRegressionTest extends TestCase {

	static Logger logger = LoggerFactory.getLogger(JPlurkRegressionTest.class);
	
	private void gracefulWait(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException ignored) {
		}
	}

	public void testJPlurk() throws Exception {
		PlurkSettings config = null;
		try {
			config = new PlurkSettings();
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		}
		PlurkClient client = new PlurkClient(config);

		// do login
		JSONObject ret = client.login(JOptionPane.showInputDialog("id"), JOptionPane.showInputDialog("password"));
		assertNotNull(ret);
		gracefulWait();
		
		// do plurk add
		String content = RandomStringUtils.random(100, "1234567890abcdef");
		ret = client.plurkAdd(content, Qualifier.LOVES);
		int plurkId = 0;
		String responsedContent = "";
		try {
			plurkId = NumberUtils.toInt("" + ret.get("plurk_id"));
			responsedContent = ret.getString("content_raw");
			assertEquals(content, responsedContent);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}		
		gracefulWait();
		
		// do plurk edit
		ret = client.plurkEdit("" + plurkId, StringUtils.reverse(responsedContent));
		assertNotNull(ret);
		assertEquals(StringUtils.reverse(responsedContent), ret.getString("content_raw"));
		gracefulWait();
		
		// do mute plurk
		ret = client.mutePlurks(new String[]{"" + plurkId});
		assertNotNull(ret);
		assertEquals("ok", ret.getString("success_text"));
		gracefulWait();
		
		// do unmute plurk
		ret = client.unmutePlurks(new String[]{"" + plurkId});
		assertNotNull(ret);
		assertEquals("ok", ret.getString("success_text"));
		gracefulWait();
		
		// do plurk delete
		ret = client.plurkDelete("" + plurkId);
		assertNotNull(ret);
		assertEquals("ok", ret.getString("success_text"));
		gracefulWait();
		
		// do get plurks
		ret = client.getPlurks(null, 0, 0, true, false);
		assertNotNull(ret);
		
	}

}
