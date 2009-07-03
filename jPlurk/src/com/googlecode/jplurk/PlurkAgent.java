package com.googlecode.jplurk;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.behavior.ResponsePlurk;
import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.NotLoginException;
import com.googlecode.jplurk.net.Result;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;
import tw.idv.askeing.jPlurk.model.ResponseMessage;
import tw.idv.askeing.jPlurk.util.PatternUtils;

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

	@SuppressWarnings("unchecked")
	public Result addPlurk(Qualifier qualifier, String text){
		checkLogin();
		Message message = new Message();
		message.setQualifier(qualifier);
		message.setContent(text);
		Result result = plurkTemplate.doAction(AddPlurk.class, message);
		/*
		 *
		 * [DEBUG] PlurkTemplate - isOk: true, response: {"plurk":
		 * {"responses_seen": 0, "qualifier": "asks", "plurk_id": 70772936,
		 * "response_count": 0, "limited_to": null, "no_comments": 0,
		 * "is_unread": 0, "lang": "tr_ch", "content_raw":
		 * "\u665a\u4e0a\u9084\u4e0b\u96e8\u55ce?", "user_id": 3146394,
		 * "plurk_type": 0, "id": 70772936, "content":
		 * "\u665a\u4e0a\u9084\u4e0b\u96e8\u55ce?", "posted": new
		 * Date("Fri, 03 Jul 2009 07:11:14 GMT"), "owner_id": 3146394}, "error":
		 * null}
		 */

		result.getAttachement().put("plurkId", PatternUtils.getPropertyWithIntValue(result.getResponseBody(), "plurk_id"));
		result.getAttachement().put("plurkOwnerId", PatternUtils.getPropertyWithIntValue(result.getResponseBody(), "owner_id"));
		return result;
	}

	public Result responsePlurk(Qualifier qualifier, String plurkId, String plurkOwnerId, String text){
		checkLogin();
		ResponseMessage message = new ResponseMessage();
		message.setQualifier(qualifier);
		message.setContent(text);
		message.setPlurkId(plurkId);
		message.setPlurkOwnerId(plurkOwnerId);
		Result result = plurkTemplate.doAction(ResponsePlurk.class, message);
		return result;
	}


	public static void main(String[] args) {
		Account account = new Account();
		PlurkAgent pa = new PlurkAgent(account);
		pa.login();

		/*
		 *
		 * [DEBUG] PlurkTemplate - isOk: true, response: {"plurk":
		 * {"responses_seen": 0, "qualifier": "asks", "plurk_id": 70772936,
		 * "response_count": 0, "limited_to": null, "no_comments": 0,
		 * "is_unread": 0, "lang": "tr_ch", "content_raw":
		 * "\u665a\u4e0a\u9084\u4e0b\u96e8\u55ce?", "user_id": 3146394,
		 * "plurk_type": 0, "id": 70772936, "content":
		 * "\u665a\u4e0a\u9084\u4e0b\u96e8\u55ce?", "posted": new
		 * Date("Fri, 03 Jul 2009 07:11:14 GMT"), "owner_id": 3146394}, "error":
		 * null}
		 */
		Result r = pa.addPlurk(Qualifier.FREESTYLE, "我噗了");
		pa.responsePlurk(Qualifier.FREESTYLE, "" + r.getAttachement().get("plurkId"), "" +r.getAttachement().get("plurkOwnerId"), "我回了");

	}
}
