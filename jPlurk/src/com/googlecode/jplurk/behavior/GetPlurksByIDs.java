package com.googlecode.jplurk.behavior;

import org.json.simple.JSONArray;

import tw.idv.askeing.jPlurk.Constants;

import com.googlecode.jplurk.net.Request;

public class GetPlurksByIDs implements IBehavior {

	@SuppressWarnings("unchecked")
	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.GET_PLURK_BY_ID_URL);
		if (arg != null && arg instanceof String[]) {
			String[] userUids = (String[]) arg;
			JSONArray ids = new JSONArray();
			for (String uid : userUids) {
				ids.add(uid);
			}
			params.addParam("ids", ids.toJSONString());
			return true;
		}
		return false;
	}

}
