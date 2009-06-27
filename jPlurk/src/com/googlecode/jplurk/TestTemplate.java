package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.impl.GetPlurks;

public class TestTemplate {
	public static void main(String[] args) {

		PlurkTemplate template = new PlurkTemplate("user", "password");
		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("為什麼不對呢?");
//
//		Result result = template.doAction(new NewMessage(), mesg);
		Result result = template.doAction(new GetPlurks(), null);
		System.out.println("post successful !? :: " + result.isOk());
		System.out.println(result.getResponseBody());
	}
}
