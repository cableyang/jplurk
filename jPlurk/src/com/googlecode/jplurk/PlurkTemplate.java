package com.googlecode.jplurk;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.googlecode.jplurk.behavior.IBehavior;
import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.model.Account;
import com.googlecode.jplurk.net.Request;
import com.googlecode.jplurk.net.Result;
import com.googlecode.jplurk.net.StatefulAgent;
import com.googlecode.jplurk.utils.PatternUtils;

/**
 * 將 Plurk 操作流程封裝於 Template Method，擴充只要增加新的 Behavior 即可。
 * @author Ching Yi, Chan
 */
final public class PlurkTemplate {

	private Account account;
	private static Log logger = LogFactory.getLog(PlurkTemplate.class);
	private final StatefulAgent agent = new StatefulAgent();
	private Long uid;
	private boolean isGuestMode = false;

	public PlurkTemplate(Account account) throws RequestFailureException {
		this.account = account;
		getUid(this.account);
		logger.info("prefetch uid: " + uid);
	}
	
	private PlurkTemplate() {
		logger.warn("in guest mode (no account logged in)");
		isGuestMode = true;
	}
	
	public static PlurkTemplate guest() {
		return new PlurkTemplate();
	}
	
	private void getUid(Account account) throws RequestFailureException{
		Result result = agent.executePost("http://www.plurk.com/" + account.getName(), new HashMap<String, String>());
		if(result.isOk()){
			uid = PatternUtils.parseUserUidFromUserpage(result.getResponseBody());
		}
		
		if(!result.isOk()){
			throw new RequestFailureException(result);
		}
		
		if(uid == null){
			throw new RequestFailureException("cannot parse uid.");
		}
	}

	private IBehavior createBehavior(Class<? extends IBehavior> clazz){
		IBehavior o = null;
		try {
			o = clazz.newInstance();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return o;
	}

	/**
	 * doAction 方法是使用 Plurk 的主要流程，會自動取得使用者 uid、cookie 等資訊。
	 * @param behavior
	 * @param arg
	 * @return
	 */
	final public Result doAction(Class<? extends IBehavior> behaviorClazz, Object arg) {
		if (!validateUid() /*|| !vaildateCookie(account)*/) {
			return Result.FAILURE_RESULT;
		}

		final IBehavior behavior = createBehavior(behaviorClazz);
		if(behavior == null){
			logger.warn("cannot create the behavior: " + behaviorClazz);
			return Result.FAILURE_RESULT;
		}

		final Request params = new Request();
		if (!isGuestMode) {
			params.setUserUId("" + uid);
		} else {
			logger.warn("In guest mode, uid will not be set with query params");
		}
		

		boolean needToExecute = behavior.action(params, arg);
		if (!needToExecute) {
			logger.info("the behavior " + behavior + " do not need to execute.");
			return Result.FAILURE_RESULT;
		}

		Result result = agent.executePost(params.getEndPoint(), params.getParams());
		return result;
	}

//	private boolean vaildateCookie(Account account) {
//		
//		String cookie;
//		if ("".equals(account.getCookie())) {
//			cookie = CookieGetter.getCookie(Constants.PLURK_HOST,
//					Constants.LOGIN_URL, account, null);
//			account.setCookie(cookie);
//		}
//
//		if (account.getCookie() == null
//				|| "".equals(account.getCookie().trim())) {
//			logger.warn("cookie token is not found.");
//			return false;
//		}
//
//		return true;
//	}

	private boolean validateUid() {
		if(isGuestMode){
			logger.warn("In guest mode, validate uid will never be checked.");
			return true;
		}
		
		if (uid == 0) {
			logger.warn("the uid of user's account is invalid. ");
			return false;
		}
		return true;
	}

}
