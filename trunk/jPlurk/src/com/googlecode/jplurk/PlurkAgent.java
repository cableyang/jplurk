package com.googlecode.jplurk;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.behavior.ResponsePlurk;
import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.NotLoginException;
import com.googlecode.jplurk.exception.RequestFailureException;
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

	public Result login() throws LoginFailureException {
		Result result = plurkTemplate.doAction(Login.class, account);
		if (!result.isOk()) {
			throw new LoginFailureException(account);
		}
		isLogin = true;
		return result;
	}

	protected void checkLogin() {
		if(!isLogin) {
			throw new NotLoginException();
		}
	}

	protected Result checkResultStatus(final Result result) {
		if(!result.isOk()){
			throw new RequestFailureException();
		}
		return result;
	}

	/**
	 * addPlurk method will attach plurk's plurkId and plurkOwnerId that can use in response.
	 * @param qualifier
	 * @param text
	 * @return
	 */
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

		checkResultStatus(result);

		JSONObject o = (JSONObject) JSONValue.parse(PatternUtils.replaceJsDateToTimestamp(result.getResponseBody()));
		JSONObject plurkObject = (JSONObject) o.get("plurk");
		String pid = "" + plurkObject.get("plurk_id");
		String pownid = "" + plurkObject.get("owner_id");

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
		return checkResultStatus(result);
	}


	public static void main(String[] args) {
		Account account = Account.createWithDynamicProperties();
		PlurkAgent pa = new PlurkAgent(account);
		Result r =pa.login();
		r = pa.addPlurk(Qualifier.SAYS, "R61 在室溫下還是太熱了orz. 開冷氣給他吹(謎：其實是自己想吹吧)");
		System.out.println(r.getResponseBody());
		// {"plurk": {"responses_seen": 0, "qualifier": "feels", "plurk_id": 71633278, "response_count": 0, "limited_to": null, "no_comments": 0, "is_unread": 0, "lang": "tr_ch", "content_raw": "\u623f\u9593\u958b\u59cb\u71b1\u4e86\u8d77\u4f86", "user_id": 3146394, "plurk_type": 0, "id": 71633278, "content": "\u623f\u9593\u958b\u59cb\u71b1\u4e86\u8d77\u4f86", "posted": new Date("Sun, 05 Jul 2009 02:56:30 GMT"), "owner_id": 3146394}, "error": null}
//		String json = "{\"responses_seen\": 0, \"qualifier\": \"feels\", \"plurk_id\": 71633278, \"response_count\": 0, \"limited_to\": null, \"no_comments\": 0, \"is_unread\": 0, \"lang\": \"tr_ch\", \"content_raw\": \"\u623f\u9593\u958b\u59cb\u71b1\u4e86\u8d77\u4f86\", \"user_id\": 3146394, \"plurk_type\": 0, \"id\": 71633278, \"content\": \"\u623f\u9593\u958b\u59cb\u71b1\u4e86\u8d77\u4f86\", \"posted2\": new Date(\"Sun, 08 Jul 2009 02:56:30 GMT\"), \"posted\": new Date(\"Sun, 05 Jul 2009 02:56:30 GMT\"), \"owner_id\": 3146394}";
//		String s = json.replaceAll("(new Date\\([^(]+\\))", "$1");
//		System.out.println(s);

	}
}
