package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.net.Result;

public interface IPlurkAgent {

	public Result login() throws LoginFailureException;

	public Result addPlurk(Qualifier qualifier, String text);

	public Result responsePlurk(Qualifier qualifier, String plurkId,
			String plurkOwnerId, String text);

	public Result addLongPlurk(Qualifier qualifier, String longText);

	public Result getUnreadPlurks();

	public Result getNotifications();

}