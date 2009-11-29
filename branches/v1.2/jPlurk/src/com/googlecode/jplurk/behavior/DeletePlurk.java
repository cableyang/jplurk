package com.googlecode.jplurk.behavior;

import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class DeletePlurk implements IBehavior{

	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.DELETE_PLURK);
		if (arg instanceof String) {
			String pid = (String) arg;
			params.addParam("plurk_id", pid);
			return true;
		}

		return false;
	}

}
