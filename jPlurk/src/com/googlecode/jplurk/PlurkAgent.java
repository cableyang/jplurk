package com.googlecode.jplurk;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.NotLoginException;
import com.googlecode.jplurk.net.Result;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

/**
 * PlurkAgent is a facade that assemble many plurk's behavior in one class.
 * @author Ching Yi, Chan
 */
public class PlurkAgent {

	Account account;
	PlurkTemplate plurkTemplate;
	boolean isLogin;

	public PlurkAgent(Account account) {
		this.account = account;
		this.plurkTemplate = new PlurkTemplate(account);
	}

	public boolean login() throws LoginFailureException {
		Result result = plurkTemplate.doAction(Login.class, account);
		if (!result.isOk()) {
			throw new LoginFailureException(account);
		}
		isLogin = true;
		return result.isOk();
	}

	protected void checkLogin() {
		if(!isLogin) {
			throw new NotLoginException();
		}
	}

	public Result addPlurk(Qualifier qualifier, String text){
		checkLogin();
		Message message = new Message();
		message.setQualifier(qualifier);
		message.setContent(text);
		Result result = plurkTemplate.doAction(AddPlurk.class, message);

		return result;
	}

}
