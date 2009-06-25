package com.googlecode.jplurk.impl;

import com.googlecode.jplurk.Behavior;
import com.googlecode.jplurk.RequestParams;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.Message;

public class NewMessage implements Behavior {

	@Override
	public boolean action(final RequestParams param, Object arg) {
		if (!(arg instanceof Message)) {
			return false;
		}

		/**
		 * 指定 Request URI
		 * */
		param.setEndpoint(Constants.ADDPLURK_URL);

		Message m = (Message) arg;
		param.setPosted(m.getPosted());
		param.setQualifier(m.getQualifier());
		param.setContent(m.getContent());
		param.setLang(m.getLang());
		param.setNo_comments("" + m.getNoComments());

		/**
		 * 這裡不再需要指定 uid，將由 Plurk Template 指定
		 * */
//		param.setUid("" + m.getUid());

		if (m.hasLimited_to()) {
			param.setLimited_to(m.getLimitedTo());
		}

		return true;
	}

}
