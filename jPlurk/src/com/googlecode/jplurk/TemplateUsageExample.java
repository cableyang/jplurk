package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.behavior.GetPlurks;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.net.Result;

public class TemplateUsageExample {
	public static void main(String[] args) {

		Account account = new Account();
		PlurkTemplate template = new PlurkTemplate(account);
		template.doAction(new Login(), account);
//		System.out.println(template.doAction(new Login(), account));

		if(true){
			System.exit(0);
		}

		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("該思考 jplurk 下一步怎麼改了");

//		Result result = template.doAction(new AddPlurk(), mesg);
		Result result = template.doAction(new GetPlurks(), null);
		System.out.println("post successful !? :: " + result.isOk());
		System.out.println(result.getResponseBody());
	}
}
