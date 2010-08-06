package com.google.jplurk;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlurkClientTest extends AbstractJPlurkSessionTestCase {

    static List<JSONObject> plurks = new LinkedList<JSONObject>();
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // prepare the plurks for testing
        if (plurks.isEmpty()) {
            JSONArray plurkData = client.getPlurks(DateTime.now(), 0, false,
                    false, false).getJSONArray("plurks");
            assertNotNull(plurkData);
            for (int i = 0; i < plurkData.length(); i++) {
                plurks.add(plurkData.getJSONObject(i));
            }
            assertFalse(plurks.isEmpty());
        }
    }

    private JSONObject pickTestData() throws JSONException {
        Collections.shuffle(plurks);
        // return client.getPlurk("" + plurks.get(0).getLong("plurk_id"));
        return plurks.get(0);
    }

    public void testMute() throws Exception {
        JSONObject ret = client.mutePlurks(new String[] { ""
                + pickTestData().getLong("plurk_id") });
        assertEquals("ok", ret.get("success_text"));

        ret = client.unmutePlurks(new String[] { ""
                + pickTestData().getLong("plurk_id") });
        assertEquals("ok", ret.get("success_text"));
    }
    
    public void testFavortie() throws Exception {
        JSONObject ret = client.favoritePlurks(new String[] { ""
                + pickTestData().getLong("plurk_id") });
        assertEquals("ok", ret.get("success_text"));

        ret = client.unfavoritePlurks(new String[] { ""
                + pickTestData().getLong("plurk_id") });
        assertEquals("ok", ret.get("success_text"));
    }

    public void testSearchUser() throws Exception {
        JSONObject ret = client.searchUser("plurkbuddy");
        assertNotNull(ret);
    }

    public void testGetPublicProfile() throws Exception {
        JSONObject ret = client.getPublicProfile("qrtt1");
        assertNotNull(ret);
        assertEquals("qrtt1", ret.getJSONObject("user_info").getString(
                "nick_name"));
    }

}
