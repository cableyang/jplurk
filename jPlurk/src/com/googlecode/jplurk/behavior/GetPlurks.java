package com.googlecode.jplurk.behavior;

import tw.idv.askeing.jPlurk.Constants;

import com.googlecode.jplurk.net.Request;

public class GetPlurks implements IBehavior {

	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.GET_PLURK_URL);
		params.addParam("user_id", params.getUserUId());
//		String time = TimeUtil.now();
//		params.addParam("from_date", time);
//		params.addParam("date_offset", time);
//		params.addParam("fetch_responses", "true");
		return true;
	}
}
