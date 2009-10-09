package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.net.Result;

public interface IPlurkAgent {

	public abstract Result login() throws LoginFailureException;

	public abstract Result addPlurk(Qualifier qualifier, String text);

	public abstract Result responsePlurk(Qualifier qualifier, String plurkId,
			String plurkOwnerId, String text);

	public abstract Result addLongPlurk(Qualifier qualifier, String longText);

	public abstract Result getUnreadPlurks();

}