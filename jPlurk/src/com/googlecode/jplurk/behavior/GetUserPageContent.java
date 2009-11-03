package com.googlecode.jplurk.behavior;

import com.googlecode.jplurk.net.Request;

public class GetUserPageContent implements IBehavior {

	@Override
	public boolean action(Request params, Object arg) {
		if (arg != null && arg instanceof String) {
			String userId = (String) arg;
			params.setEndPoint(userId.startsWith("/") ? userId : "/" + userId);
			return true;
		}
		return false;
	}

}
