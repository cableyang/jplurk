package com.googlecode.jplurk;

import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

public interface IPlurkAgent {

	public Result addLongPlurk(Qualifier qualifier, String longText)
			throws RequestFailureException;

	public Result addPlurk(Qualifier qualifier, String text)
			throws RequestFailureException;

	public Result allowFriendRequest(int uid) throws RequestFailureException;

	public Result denyFriendRequest(int uid) throws RequestFailureException;

	public Result editPlurk(String plurkId, String message) throws RequestFailureException;
	
	/**
	 * @return result with attachment {uids}
	 * @throws RequestFailureException
	 */
	public Result getNotifications() throws RequestFailureException;

	public Result getPlurks() throws RequestFailureException;

	public Result getResponsePlurks(Integer plurkId) throws RequestFailureException;

	public Result getUnreadPlurks() throws RequestFailureException;

	public Result login() throws LoginFailureException;

	public Result responsePlurk(Qualifier qualifier, String plurkId,
			String plurkOwnerId, String text) throws RequestFailureException;

//	public Result makeFan(int uid) throws RequestFailureException;
}