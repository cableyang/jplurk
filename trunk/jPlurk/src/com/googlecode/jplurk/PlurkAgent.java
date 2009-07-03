package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Account;

public class PlurkAgent {

	Account account;
	PlurkTemplate plurkTemplate;
	
	public PlurkAgent(Account account) {
		this.account = account;
		this.plurkTemplate = new PlurkTemplate(account);
	}

	public void login() {
	}

}
