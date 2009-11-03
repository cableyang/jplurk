package com.googlecode.jplurk;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import tw.idv.askeing.jPlurk.model.Account;

import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

public class PlurkAgentUsageExample {
	public static void main(String[] args) throws RequestFailureException, InterruptedException {
		IPlurkAgent agent = new PlurkAgent(Account.createWithDynamicProperties());
		agent.login();


		Date offset = new Date(System.currentTimeMillis() - 2L * 24L * 60 * 60 * 1000);
//		offset = new Date(1257174268000L);
		Result result = agent.getPlurks(offset);

		JSONArray a = (JSONArray) result.getAttachement().get("json");
		for (Object j : a) {
			JSONObject js  = (JSONObject) j;
			System.out.println(new Date((Long) js.get("posted")) + " :: "+  js.get("posted") + js.get("content"));
		}
		System.out.println(a.size());

		System.out.println(offset);
		
//		agent.addPlurk(Qualifier.HAS, "那個 Cookie 欄位似乎沒有在用了?");
		
		System.out.println(agent.getAvatar("npui").getAttachement());
	}
}
