package com.googlecode.jplurk.impl;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.util.TimeUtil;

import com.googlecode.jplurk.Behavior;
import com.googlecode.jplurk.net.Request;

public class GetPlurks implements Behavior {

	/************************************
    def get_plurks(uid=nil, date_from=Time.now, date_offset=Time.now, fetch_responses=false)
    return false unless @logged_in
    uid ||= @uid
    params = {
      :user_id => uid,
      :from_date => date_from.getgm.strftime("%Y-%m-%dT%H:%M:%S"),
      :date_offset => date_offset.getgm.strftime("%Y-%m-%dT%H:%M:%S"),
      :fetch_responses => fetch_responses,
    }
    data = statuses(plurk_to_json(request("/TimeLine/getPlurks", :method => :post, :params => params )))
    return data
  end

  **/

	@Override
	public boolean action(Request params, Object arg) {
		// 為什麼不成功呢?
		/**

post successful !? :: false
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<title>400 Bad Request</title>
<h1>Bad Request</h1>
function arguments invalid.  (2 missing, 0 additional)

		 * */
		params.setEndPoint(Constants.GET_PLURK_URL);
		params.addParam("user_id", params.getUserUId());
		String time = TimeUtil.now();
		params.addParam("from_date", time);
		params.addParam("date_offset", time);
		params.addParam("fetch_responses", "true");
		return true;
	}
}
