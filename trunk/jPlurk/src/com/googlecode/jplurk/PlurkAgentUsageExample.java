package com.googlecode.jplurk;

import com.googlecode.jplurk.net.Result;

import tw.idv.askeing.jPlurk.model.Account;

public class PlurkAgentUsageExample {
	public static void main(String[] args) {
		IPlurkAgent agent = new PlurkAgent(Account.createWithDynamicProperties());
		agent.login();
		
		Result result = agent.getUnreadPlurks();
		System.out.println(result);
	}
}
