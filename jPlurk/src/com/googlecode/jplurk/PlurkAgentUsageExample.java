package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;

import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

public class PlurkAgentUsageExample {
	public static void main(String[] args) throws RequestFailureException {
		IPlurkAgent agent = new PlurkAgent(Account.createWithDynamicProperties());
		agent.login();

		Result result = // agent.getUnreadPlurks();
			agent.getNotifications();
		System.out.println(result.getAttachement());

//		agent.responsePlurk(Qualifier.FEELS, "1255125920000", "133932628", "早安 :)" +
//				"");
	}
}
