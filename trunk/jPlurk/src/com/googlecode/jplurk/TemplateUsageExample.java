package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.GetPlurks;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.net.Result;

public class TemplateUsageExample {
	public static void main(String[] args) {
		Result result = null;
		Account account = new Account();
		PlurkTemplate template = new PlurkTemplate(account);
		result = template.doAction(new Login(), account);
		System.out.println(result.getResponseBody());
		if(result.isOk()){
			System.exit(0);
		}


		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("該思考 jplurk 下一步怎麼改了");

		result = template.doAction(new GetPlurks(), null);
		result = template.doAction(new AddPlurk(), mesg);

		System.out.println("post successful !? :: " + result.isOk());
		System.out.println(result.getResponseBody());
	}
}
