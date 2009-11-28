package com.googlecode.jplurk.behavior;


import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class GetResponsePlurks implements IBehavior {

	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.GET_RESPONSES_PLURK_URL);
		if (arg != null && arg instanceof Integer) {
			Integer plurkId = (Integer) arg;
			params.addParam("plurk_id", "" + plurkId);
			return true;
		}
		return false;
	}

}
