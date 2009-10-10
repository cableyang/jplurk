package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

public interface IPlurkAgent {

	public Result login() throws LoginFailureException;

	public Result addPlurk(Qualifier qualifier, String text) throws RequestFailureException;

	public Result responsePlurk(Qualifier qualifier, String plurkId,
			String plurkOwnerId, String text) throws RequestFailureException;

	public Result addLongPlurk(Qualifier qualifier, String longText) throws RequestFailureException;

	public Result getUnreadPlurks() throws RequestFailureException;

	/**
	 * @return result with attachment {uids}
	 * @throws RequestFailureException
	 */
	public Result getNotifications() throws RequestFailureException;

}