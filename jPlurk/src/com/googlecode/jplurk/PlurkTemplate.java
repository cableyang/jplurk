package com.googlecode.jplurk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.CookieGetter;
import tw.idv.askeing.jPlurk.UIDManager;
import tw.idv.askeing.jPlurk.model.Account;

import com.googlecode.jplurk.net.Request;
import com.googlecode.jplurk.net.Result;
import com.googlecode.jplurk.net.StatefulAgent;

/**
 * 將 Plurk 操作流程封裝於 Template Method，擴充只要增加新的 Behavior 即可。
 * @author Ching Yi, Chan
 */
final public class PlurkTemplate {

	Account account;
	static Log logger = LogFactory.getLog(PlurkTemplate.class);
	final StatefulAgent agent = new StatefulAgent();

	public PlurkTemplate(String user, String password) {
		account = new Account(user, password);
		logger.info("prefetch uid: " + UIDManager.getUID(account));
	}

	/**
	 * doAction 方法是使用 Plurk 的主要流程，會自動取得使用者 uid、cookie 等資訊。
	 * @param behavior
	 * @param arg
	 * @return
	 */
	final public Result doAction(Behavior behavior, Object arg) {
		if (!validateUid() || !vaildateCookie(account)) {
			return Result.FAILURE_RESULT;
		}

		final Request params = new Request();
		params.setUserUId("" + UIDManager.getUID(account));

		boolean needToExecute = behavior.action(params, arg);
		if (!needToExecute) {
			logger.info("the behavior " + behavior + " do not need to execute.");
			return Result.FAILURE_RESULT;
		}

		Result result = agent.executePost(params.getEndPoint(), params.getParams());
		logger.debug(result);
		return result;
	}

	private boolean vaildateCookie(Account account) {
		String cookie;
		if ("".equals(account.getCookie())) {
			cookie = CookieGetter.getCookie(Constants.PLURK_HOST,
					Constants.LOGIN_URL, account, null);
			account.setCookie(cookie);
		}

		if (account.getCookie() == null
				|| "".equals(account.getCookie().trim())) {
			logger.warn("cookie token is not found.");
			return false;
		}

		return true;
	}

	private boolean validateUid() {
		if (UIDManager.getUID(account) == 0) {
			logger.warn("the uid of user's account is invalid. ");
			return false;
		}
		return true;
	}

}
