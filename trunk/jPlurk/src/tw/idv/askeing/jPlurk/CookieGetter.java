/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.idv.askeing.jPlurk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.net.HttpMethodUtil;

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
//        boolean forTest = false;
        String cookie = "";

        try {
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setHost(host, 80, "http");

            // extract method is better for testing.
            PostMethod post = HttpMethodUtil.prepareForQueryCookie(user, postUrl, optional_cookie);
            // 委派給新的實作，舊的實作先保留
            // PostMethod post =  createGetCookieRequest(user, postUrl, optional_cookie);

            // 發送請求、返回狀態
            int statusCode = client.executeMethod(post);
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK) {

                // 取得回傳資訊.
                Header[] headers = post.getResponseHeaders();
                for(int i=1 ; i<headers.length ; i++) {
//                    if(forTest) System.out.println( headers[i].getName()+": "+headers[i].getValue() );
                	logger.debug(headers[i].getName()+": "+headers[i].getValue());
                    if(headers[i].getName().equals("Set-Cookie")) {
                    	/* TODO: why re-set cookie ? [2009/03/04 qrtt1]
                         * Re-Set? This line means that, if header has "Set-Cookie", then get this cookie.
                         * Different Users use different cookies.
                         * And one User has more than one cookies.
                         * Viewing Info, sending Message have differnt.
                         * Using viewing info cookie to send message will fail. [2009.03.05 askeing]
                        */
                        cookie = headers[i].getValue();
                        cookie = cookie.substring(0,cookie.indexOf(";"));
                    }
                }
//                if(forTest) {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8")); //編碼需要設定
//                    String line = "";
//                    while ((line = in.readLine()) != null) {
//                        System.out.println(line);
//                    }
//                }
                logger.debug(new String(post.getResponseBody(), "utf-8"));
            } else {
            	logger.warn("Method failed: " + post.getStatusLine());
//                System.err.println("Method failed: " + post.getStatusLine());
            }
        } catch (IOException e) {
//            e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }
        return cookie;
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
