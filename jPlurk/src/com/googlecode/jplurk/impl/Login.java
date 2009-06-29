package com.googlecode.jplurk.impl;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.model.Account;

import com.googlecode.jplurk.Behavior;
import com.googlecode.jplurk.net.Request;

public class Login implements Behavior{

	@Override
	public boolean action(Request params, Object arg) {
		Account account = null;
		if (arg instanceof Account) {
			account = (Account) arg;
			params.setEndPoint(Constants.LOGIN_URL);

			params.addParam("nick_name", account.getName());
			params.addParam("password", account.getPassword());
			return true;

		}

		return false;
	}

}
