package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.impl.NewMessage;

public class TestTemplate {
	public static void main(String[] args) {
		Message mesg = new Message();
		mesg.setQualifier(Qualifier.ASKS);
		mesg.setContent("haha");

		boolean result = new PlurkTemplate("userid", "password").doAction(
				new NewMessage(), mesg);
		System.out.println("post successful !? :: " + result);
	}
}
