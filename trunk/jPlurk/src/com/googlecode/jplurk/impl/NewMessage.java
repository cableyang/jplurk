package com.googlecode.jplurk.impl;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.Message;

import com.googlecode.jplurk.Behavior;
import com.googlecode.jplurk.Request;

public class NewMessage implements Behavior {

	@Override
	public boolean action(final Request param, Object arg) {
		if (!(arg instanceof Message)) {
			return false;
		}

		/**
		 * 指定 Request URI
		 * */
		param.setEndPoint(Constants.ADDPLURK_URL);

		Message m = (Message) arg;
		// param.setPosted(m.getPosted());
		param.addParam("posted", m.getPosted());

		// param.setQualifier(m.getQualifier());
		param.addParam("qualifier", m.getQualifier());

		// param.setContent(m.getContent());
		param.addParam("content", m.getContent());

		// param.setLang(m.getLang());
		param.addParam("lang", m.getLang());

		// param.setNo_comments("" + m.getNoComments());
		param.addParam("no_comments", "" + m.getNoComments());

		// param.setUid("" + m.getUid());
		param.addParam("uid", param.getUserUId());

		if (m.hasLimited_to()) {
			// param.setLimited_to(m.getLimitedTo());
			param.addParam("limited_to", m.getLimitedTo());
		}

		return true;
	}

}
