package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.impl.AddPlurk;
import com.googlecode.jplurk.impl.Login;
import com.googlecode.jplurk.net.Result;

public class TemplateUsageExample {
	public static void main(String[] args) {

		Account account = new Account();
		PlurkTemplate template = new PlurkTemplate(account.getName(), account.getPassword());

		template.doAction(new Login(), account);

		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("思考一下");
//
		Result result = template.doAction(new AddPlurk(), mesg);
//		Result result = template.doAction(new GetPlurks(), null);
		System.out.println("post successful !? :: " + result.isOk());
		System.out.println(result.getResponseBody());
	}
}
