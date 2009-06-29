package com.googlecode.jplurk.behavior;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.Message;

import com.googlecode.jplurk.net.Request;

public class AddPlurk implements IBehavior {

	@Override
	public boolean action(final Request param, Object arg) {
		if (!(arg instanceof Message)) {
			return false;
		}

		/**
		 * 指定 Request URI
		 * */
		param.setEndPoint(Constants.ADD_PLURK_URL);

		Message m = (Message) arg;
		param.addParam("posted", m.getPosted());
		param.addParam("qualifier", m.getQualifier());
		param.addParam("content", m.getContent());
		param.addParam("lang", m.getLang());
		param.addParam("no_comments", "" + m.getNoComments());
		param.addParam("uid", param.getUserUId());
		if (m.hasLimited_to()) {
			param.addParam("limited_to", m.getLimitedTo());
		}

		return true;
	}

}
