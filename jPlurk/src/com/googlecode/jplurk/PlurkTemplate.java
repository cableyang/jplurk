package com.googlecode.jplurk;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.Constants;
import tw.idv.askeing.jPlurk.CookieGetter;
import tw.idv.askeing.jPlurk.UIDManager;
import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.net.HttpResultCallback;
import tw.idv.askeing.jPlurk.net.HttpTemplate;

/**
 * 將 Plurk 操作流程封裝於 Template Method，擴充只要增加新的 Behavior 即可。
 * @author Ching Yi, Chan
 */
final public class PlurkTemplate {

	Account account;
	static Log logger = LogFactory.getLog(PlurkTemplate.class);

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
	final public boolean doAction(Behavior behavior, Object arg) {
		if (!validateUid() || !vaildateCookie(account)) {
			return false;
		}

		final RequestParams params = new RequestParams();
		params.setUid("" + UIDManager.getUID(account));

		boolean needToExecute = behavior.action(params, arg);
		if (!needToExecute) {
			logger.info("the behavior " + behavior + " do not need to execute.");
			return false;
		}

		PostMethod post = new PostMethod(params.getEndpoint());
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		post.setRequestHeader("Referer", "http://www.plurk.com/" + account.getName());
		post.setRequestHeader("Cookie", account.getCookie());

		/**
		 * 利用 Reflection trick 建立 Request Body 需要的參數。
		 * 請適當地擴充 RequestParams.validParams 名單
		 * */
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		for (String eachField : RequestParams.validParams) {
			try {
				Field field = params.getClass().getDeclaredField(eachField);
				field.setAccessible(true);
				Object v = field.get(params);
				if (v == null) {
					continue;
				}
				NameValuePair nvp = new NameValuePair(eachField, "" + v);
				nvps.add(nvp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		post.setRequestBody(nvps.toArray(new NameValuePair[0]));

		Object result = new HttpTemplate(post).execute(new int[] {
				HttpStatus.SC_MOVED_TEMPORARILY, HttpStatus.SC_OK },
				new HttpResultCallback() {
					@Override
					protected Object processResult(PostMethod method) {
						return Boolean.TRUE;
					}
				});

		if (result != null && result instanceof Boolean) {
			return ((Boolean) result).booleanValue();
		}

		return false;
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
