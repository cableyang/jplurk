/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.idv.askeing.jPlurk;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.net.HttpUtil;

/**
 * jPlurk CookieGetter: Get Cookie of User.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class CookieGetter {

	static Log logger = LogFactory.getLog(CookieGetter.class);

    /**
     * Return Cookie of user.
     * @param host host name
     * @param postUrl post url
     * @param user user account
     * @return Cookie
     */
    public static String getCookie(String host, String postUrl, AccountModel user, String optional_cookie) {
        try {
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setHost(host, 80, "http");

            // 委派給新的實作，舊的實作先保留
            PostMethod post = HttpUtil.prepareForQueryCookie(user, postUrl, optional_cookie);
            // PostMethod post =  createGetCookieRequest(user, postUrl, optional_cookie);

            // 發送請求、返回狀態
//            int statusCode = client.executeMethod(post);
            int httpResponseCode = client.executeMethod(post);
            boolean isValidState = (httpResponseCode == HttpStatus.SC_MOVED_TEMPORARILY || httpResponseCode == HttpStatus.SC_OK);

            if(!isValidState){
            	logger.warn("Method failed: " + post.getStatusLine());
            	return "";
            }

            return HttpUtil.parseSetCookieHeader(post.getResponseHeaders());

//            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK) {
//            	logger.debug(new String(post.getResponseBody(), "utf-8"));
//               cookie = parseSetCookieHeader(post.getResponseHeaders());
//            	 return HttpMethodUtil.parseSetCookieHeader(post.getResponseHeaders());
//            } else {
//            	logger.warn("Method failed: " + post.getStatusLine());
//            }
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }
        return "";
    }

	/**
	 * @deprecated use tw.idv.askeing.jPlurk.net.HttpMethodUtil.parseSetCookieHeader(Header[]) instead.
	 * @param headers
	 * @return
	 */
	static String parseSetCookieHeader(Header[] headers) {
		for(int i=1 ; i<headers.length ; i++) {
			logger.debug(headers[i].getName()+": "+headers[i].getValue());
		    if(headers[i].getName().equals("Set-Cookie")) {
		    	/* Review: why re-set cookie ? [2009/03/04 qrtt1] */

		        /* Re-Set? This line means that, if header has "Set-Cookie", then get this cookie.
		         * Different Users use different cookies.
		         * And one User has more than one cookies.
		         * Viewing Info, sending Message have different.
		         * Using viewing info cookie to send message will fail. [2009.03.05 askeing]
		        */

		    	/*
		    	 * 	http://www.ietf.org/rfc/rfc2109.txt
		    	 *
					4.2.2  Set-Cookie Syntax

					   The syntax for the Set-Cookie response header is

					   set-cookie      =       "Set-Cookie:" cookies
					   cookies         =       1#cookie
					   cookie          =       NAME "=" VALUE *(";" cookie-av)
					   NAME            =       attr
					   VALUE           =       value
					   cookie-av       =       "Comment" "=" value
					                   |       "Domain" "=" value
					                   |       "Max-Age" "=" value
					                   |       "Path" "=" value
					                   |       "Secure"
					                   |       "Version" "=" 1*DIGIT

					Example:
					Set-Cookie: plurkcookie=hbuu4hs1X4CtOZa2bnPUDAIg9+A=?chk=STg1MDA2NTY1KAou&user_id=TDNxNDYpOTRMCi4=; Domain=.plurk.com; expires=Thu, 19-Mar-2009 15:47:06 GMT; Max-Age=1209600; Path=/
		    	 * */

		        String cookie = headers[i].getValue();
		        return cookie.substring(0,cookie.indexOf(";"));
		    }
		}
		return "";
	}

	/**
	 * @deprecated use HttpMethodUtil.prepareForQueryCookie instead
	 * @param user
	 * @param postUrl
	 * @param optional_cookie
	 * @return
	 */
	static PostMethod createGetCookieRequest(AccountModel user,
			String postUrl, String optional_cookie) {
		// 建立 PostMethod，並指派 Post 網址
		PostMethod post = new PostMethod(postUrl);

		// 建立 Post 資料 data
		String nameKey = "nick_name";
		if (postUrl.equals("/m/login")) {
		    nameKey = "username";
		    post.addParameter(new NameValuePair(nameKey, user.getName()));
		    post.addParameter(new NameValuePair("password", user.getPassword()));
		} else if (postUrl.equals("/Users/login")) {
		    nameKey = "nick_name";
		    post.addParameter(new NameValuePair(nameKey, user.getName()));
		    post.addParameter(new NameValuePair("password", user.getPassword()));
		}else {
		    post.addParameter(new NameValuePair("Cookie", optional_cookie));
		}

		return post;
	}

    /**
     * Test Case
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = "www.plurk.com";
        String postUrl_1 = "/Users/login";
        String postUrl_2 = "/m/login";

        AccountModel user = new AccountModel();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your name: ");
        user.setName( scanner.next() );
        System.out.println("Name:"+user.getName());
        System.out.print("Please input your password: ");
        user.setPassword( scanner.next() );
        System.out.println("Password:"+user.getPassword());

        System.out.println("\n===== Test One =====\n");
        System.out.println(postUrl_1+" Cookie: "+ CookieGetter.getCookie(host, postUrl_1, user, null) );
        System.out.println("\n===== Test Two =====\n");
        System.out.println(postUrl_2+" Cookie: "+ CookieGetter.getCookie(host, postUrl_2, user, null) );
        //System.exit(0);
    }
}
