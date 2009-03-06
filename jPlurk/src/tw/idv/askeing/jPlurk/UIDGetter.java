/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.net.HttpResultCallback;
import tw.idv.askeing.jPlurk.net.HttpTemplate;

/**
 * jPlurk UIDGetter: get UID of User.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class UIDGetter {

	static Log logger = LogFactory.getLog(UIDGetter.class);

    /**
     * Return UID of user.
     * @param user user account
     * @return UID
     */
    public static int getUID(AccountModel user) {
        boolean forTest = false;
        int uid = 0;
//        String cookie = "";

//        String host = "www.plurk.com";
        String postUrl = "/m/login";
        String getUrl = "/m/";


        GetMethod method = new GetMethod(getUrl);
		method.setRequestHeader("Cookie", CookieGetter.getCookie(
				Constants.PLURK_HOST, postUrl, user, null));

        HttpTemplate template = new HttpTemplate(method);
        Object result = template.execute(new int[] { HttpStatus.SC_MOVED_TEMPORARILY,
				HttpStatus.SC_OK }, new HttpResultCallback() {
			@Override
			protected Object processResult(GetMethod method) {
				try {
					BufferedReader in = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));

					String line = "";
					while ((line = in.readLine()) != null) {
						System.out.println(line);
						// Review: need improvement.
						if (line.contains("<input type=\"hidden\" name=\"user_id\" value=\"")) {
							String[] sUID = line.split("\"");
							return Integer.valueOf((sUID[5]));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Integer.valueOf(0);
			}
		});

        if (result != null && result instanceof Integer) {
			return ((Integer) result).intValue();
		}

        return uid;

//        try {
////            cookie = CookieGetter.getCookie(Constants.PLURK_HOST, postUrl, user, null);
//            HttpClient client = HttpUtil.createDefaultHttpClient();
//
//            // 建立 PostMethod，並指派 Post 網址
//            GetMethod get = new GetMethod(getUrl);
//
//            // 設定 Cookie
//            get.setRequestHeader("Cookie",
//            	CookieGetter.getCookie(Constants.PLURK_HOST, postUrl, user, null));
//
//            // 連接不到時自動重新重試三次.
//            //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//
//            // 發送請求、返回狀態
//            int statusCode = client.executeMethod(get);
//            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK) {
//
//                // 取得回傳資訊.
//                Header[] headers = get.getResponseHeaders();
//                if(forTest) {
//                    for(int i=1 ; i<headers.length ; i++) {
//                        System.out.println( headers[i].getName()+": "+headers[i].getValue() );
//                    }
//                }
//                BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8")); //編碼需要設定
//                String line = "";
//                while ((line = in.readLine()) != null) {
//                        if(forTest) System.out.println(line);
//                        if(line.contains("<input type=\"hidden\" name=\"user_id\" value=\"")) {
//                            String[] sUID = line.split("\"");
//                            uid = Integer.parseInt( sUID[5] );
//                            if(forTest) System.out.println("UID = "+uid);
//                        }
//                }
//            } else {
//                System.err.println("Method failed: " + get.getStatusLine());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Test Case
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AccountModel user = new AccountModel();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your name: ");
        user.setName( scanner.next() );
        System.out.println("Name:"+user.getName());
        System.out.print("Please input your password: ");
        user.setPassword( scanner.next() );
        System.out.println("Password:"+user.getPassword());

        System.out.println("\n===== Test =====\n");
        System.out.println("UID: "+ UIDGetter.getUID(user) );
    }
}
