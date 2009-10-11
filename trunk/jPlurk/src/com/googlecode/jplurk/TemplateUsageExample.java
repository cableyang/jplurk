package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.behavior.GetPlurks;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.net.Result;

public class TemplateUsageExample {
	public static void main(String[] args) {
		Result result = null;
		Account account = Account.createWithDynamicProperties();
		PlurkTemplate template = new PlurkTemplate(account);
		result = template.doAction(Login.class, account);
		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("Behavior 應該要是無狀態的，把 Template Method 的參數改成使用 Class 好了.");

		result = template.doAction(GetPlurks.class, null);
//		result = template.doAction(AddPlurk.class, mesg);
//		result = template.doAction(GetUnreadPlurks.class, null);
		System.out.println("post successful !? :: " + result.isOk());
//		System.out.println(result.getResponseBody());
//		JSONArray array = JsonUtil.parseArray(result.getResponseBody());
//		System.out.println(JsonUtil.parseArray(result.getResponseBody()));
//		System.out.println(result.getResponseBody());

	}
}
