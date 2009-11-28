package com.googlecode.jplurk.behavior;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.model.ResponseMessage;

import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class ResponsePlurk implements IBehavior{

	static Log logger = LogFactory.getLog(ResponseMessage.class);

	@Override
	public boolean action(final Request param, Object arg) {
		if (!(arg instanceof ResponseMessage)) {
			return false;
		}

		/**
		 * 指定 Request URI
		 * */
		param.setEndPoint(Constants.RESPONSE_PLURK_URL);
		ResponseMessage m = (ResponseMessage) arg;

		if(!m.isValidResponseMessage()){
			logger.info(m + " is not a valid response message");
			return false;
		}

		param.addParam("posted", m.getPosted());
		param.addParam("qualifier", m.getQualifier());
		param.addParam("content", m.getContent());
		param.addParam("lang", m.getLang());
		param.addParam("uid", param.getUserUId());

		/**
		 * attribute for response
		 * */
		param.addParam("plurk_id", m.getPlurkId());
		param.addParam("p_uid", m.getPlurkOwnerId());




		return true;
	}

}
