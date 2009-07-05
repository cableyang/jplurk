package com.googlecode.jplurk;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.GetUnreadPlurks;
import com.googlecode.jplurk.behavior.IBehavior;
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
import tw.idv.askeing.jPlurk.util.JsonUtil;
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

	/**
	 * execute is a standard process to check login status and execute behavior.
	 * if the agent get a login failure in previous login method call, it raise NotLoginException.
	 * if the agent get the result is not ok, it raise the RequestFailureException.
	 * @param clazz
	 * @param args
	 * @return
	 */
	protected Result execute(Class<? extends IBehavior> clazz, Object args){
		if(!isLogin) {
			throw new NotLoginException();
		}

		Result result = plurkTemplate.doAction(clazz, args);

		if(!result.isOk()){
			throw new RequestFailureException();
		}
		return result;
	}

	/**
	 * addPlurk method will attach plurk's plurk_id and owner_id that can use in response.
	 * @param qualifier
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Result addPlurk(Qualifier qualifier, String text){
		Message message = new Message();
		message.setQualifier(qualifier);
		message.setContent(text);
		Result result = execute(AddPlurk.class, message);

		// parse json response and attach the plurk_id and owner_id
		JSONObject plurkObject = (JSONObject) JsonUtil.parse(result.getResponseBody()).get("plurk");
		result.getAttachement().putAll(JsonUtil.get(plurkObject, "plurk_id", "owner_id"));
		return result;
	}

	public Result responsePlurk(Qualifier qualifier, String plurkId, String plurkOwnerId, String text){
		ResponseMessage message = new ResponseMessage();
		message.setQualifier(qualifier);
		message.setContent(text);
		message.setPlurkId(plurkId);
		message.setPlurkOwnerId(plurkOwnerId);
		return execute(ResponsePlurk.class, message);
	}

	@SuppressWarnings("unchecked")
	public Result getUnreadPlurks(){
		Result result = execute(GetUnreadPlurks.class, null);
		for (Object each : JsonUtil.parseArray(result.getResponseBody())) {
			if (each instanceof JSONObject) {
				JSONObject o = (JSONObject) each;
				result.getAttachement().put("" + o.get("plurk_id"), o);
			}
		}
		return result;
	}


	public static void main(String[] args) {
		Account account = Account.createWithDynamicProperties();
		PlurkAgent pa = new PlurkAgent(account);
		Result r =pa.login();
		r = pa.getUnreadPlurks();
		System.out.println(r.getAttachement());
		System.out.println(r.getAttachement().size());

		// pid 71669284, own 3131562
//		pa.responsePlurk(Qualifier.FEELS, "71669284", "3131562", "是朋友就會有分寸啊!!!");
	}
}
