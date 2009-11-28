package com.googlecode.jplurk.behavior;

import java.util.Map;


import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class AllowOrDenyFriendRequest implements IBehavior {

	@SuppressWarnings("unchecked")
	@Override
	public boolean action(Request params, Object arg) {
		if (arg instanceof Map) {
			Map param = (Map) arg;
			if (param.containsKey("type")) {
				if ("allow".equals(param.get("type"))) {
					params.setEndPoint(Constants.ALLOW_AS_FRIEND);
				}
//				TODO: fan don't work, must check the right way to make a fan.
//				if ("fan".equals(param.get("type"))) {
//					params.setEndPoint(Constants.ALLOW_AS_FAN);
//				}
				if ("deny".equals(param.get("type"))) {
					params.setEndPoint(Constants.DENY_RELATIONSHIP_REQUEST);
				}
			}
			params.addParam("friend_id", "" + param.get("uid"));
			return true;
		}
		return false;
	}

}
