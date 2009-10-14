package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

public class PlurkAgentUsageExample {
	public static void main(String[] args) throws RequestFailureException, InterruptedException {
		IPlurkAgent agent = new PlurkAgent(Account.createWithDynamicProperties());
		agent.login();

		Result result = // agent.getUnreadPlurks();
//			agent.getNotifications();
			//agent.getPlurks();
			agent.addPlurk(Qualifier.WANTS, "不不不不不想要");
		System.out.println(result.getAttachement().get("plurk_id"));
		
		Thread.sleep(1000 * 30);
		
		agent.editPlurk(""+result.getAttachement().get("plurk_id"), "想要");
//		System.out.println(result.getAttachement());

//		List<Integer> uids = (List<Integer>) result.getAttachement().get("uids");
//		result = agent.allowFriendRequest(uids.get(0));
//		System.out.println(result);
//		agent.responsePlurk(Qualifier.FEELS, "1255125920000", "133932628", "早安 :)" +
//				"");
	}
}
