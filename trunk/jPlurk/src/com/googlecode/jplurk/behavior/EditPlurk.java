package com.googlecode.jplurk.behavior;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.Constants;

import com.googlecode.jplurk.net.Request;

public class EditPlurk implements IBehavior {

	static Log logger = LogFactory.getLog(EditPlurk.class);

	@Override
	public boolean action(Request params, Object arg) {
		params.setEndPoint(Constants.EDIT_PLURK);
		if (arg != null && arg instanceof String[]) {
			String[] param = (String[]) arg;
			try {
				if (param.length == 2) {
					String message = (String) param[0];
					
					// a trick to validate plurk id
					String plurkId = "" + NumberUtils.toInt(param[1], 0);
					if (!"0".equals(plurkId)) {
						params.addParam("content_raw", message);
						params.addParam("plurk_id", plurkId);
						return true;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return false;
	}

}
