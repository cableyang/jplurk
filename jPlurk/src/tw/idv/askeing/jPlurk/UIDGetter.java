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
import org.apache.commons.httpclient.methods.GetMethod;
import tw.idv.askeing.jPlurk.model.AccountModel;

/**
 * jPlurk UIDGetter: get UID of User.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class UIDGetter {
    
    /**
     * Return UID of user.
     * @param user user account
     * @return UID
     */
    public static int getUID(AccountModel user) {
        boolean forTest = false;
        int uid = 0;
        String cookie = "";
        
        String host = "www.plurk.com";
        String postUrl = "/m/login";
        String getUrl = "/m/";
        
        try {
            cookie = CookieGetter.getCookie(host, postUrl, user, null);
            
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setHost(host, 80, "http");
            // 建立 PostMethod，並指派 Post 網址
            GetMethod get = new GetMethod(getUrl);
            // 設定 Cookie
            get.setRequestHeader("Cookie", cookie);
            
            // 連接不到時自動重新重試三次.
            //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

            // 發送請求、返回狀態
            int statusCode = client.executeMethod(get);
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK) {
                
                // 取得回傳資訊.
                Header[] headers = get.getResponseHeaders();
                if(forTest) {
                    for(int i=1 ; i<headers.length ; i++) {
                        System.out.println( headers[i].getName()+": "+headers[i].getValue() );
                    }
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8")); //編碼需要設定
                String line = "";
                while ((line = in.readLine()) != null) {
                        if(forTest) System.out.println(line);
                        if(line.contains("<input type=\"hidden\" name=\"user_id\" value=\"")) {
                            String[] sUID = line.split("\"");
                            uid = Integer.parseInt( sUID[5] );
                            if(forTest) System.out.println("UID = "+uid);
                        }
                }
            } else {
                System.err.println("Method failed: " + get.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uid;
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
