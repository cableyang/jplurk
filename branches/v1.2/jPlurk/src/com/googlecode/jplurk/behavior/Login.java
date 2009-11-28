package com.googlecode.jplurk.behavior;

import org.apache.commons.lang.StringUtils;

import tw.idv.askeing.jPlurk.model.Account;

import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;

public class Login implements IBehavior {

	@Override
	public boolean action(Request params, Object arg) {
		Account account = null;
		if (arg instanceof Account) {
			account = (Account) arg;
			params.setEndPoint(Constants.LOGIN_URL);

			if (StringUtils.isBlank(account.getName())) {
				throw new IllegalArgumentException("user id cannot be empty.");
			}
			params.addParam("nick_name", account.getName());

			if (StringUtils.isBlank(account.getPassword())) {
				throw new IllegalArgumentException(
						"user password cannot be empty.");
			}
			params.addParam("password", account.getPassword());
			return true;
		}

		return false;
	}

}
