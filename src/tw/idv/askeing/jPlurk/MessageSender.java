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
import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.model.MessageModel;

/**
 * jPlurk MessageSender: Send Message. It will automatically login, getting uid, get cookie, and sending message.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class MessageSender {
    private static boolean forTest = false;
    private static int uid = 0;
    /**
     * Send Message to Plurk. This method will get UID first.
     * @param user
     * @param message
     * @return
     */
    public static boolean sendMessage(AccountModel user, MessageModel message) {
        int counter = 0;

        // 若無 Uid 就擷取並設定 Uid
        if (message.getUid() == 0) {
            counter = 0;
            do {
                counter++;
                uid = UIDGetter.getUID(user);
                if (forTest) {
                    System.out.println("uid: " + uid);
                }
            } while (uid == 0 && counter <= 3);
            message.setUid(uid);
        }
        return sendMessage(user, message, message.getUid());
    }
    
    /**
     * Send Message to Plurk. This method already has UID.
     * @param user
     * @param message
     * @param uid
     * @return
     */
    public static boolean sendMessage(AccountModel user, MessageModel message, int uid) {
        String cookie = "";
        int counter = 0;

        String host = "www.plurk.com";
        String loginUrl = "/Users/login";
        String postUrl = "/TimeLine/addPlurk";

        // 若無 Uid 就擷取並設定 Uid
        if (uid == 0) {
            counter = 0;
            do {
                counter++;
                uid = UIDGetter.getUID(user);
                if (forTest) {
                    System.out.println("uid: " + uid);
                }
            } while (uid == 0 && counter <= 3);
            message.setUid(uid);
        }
        // 擷取並設定 cookie
        counter = 0;
        do {
            counter++;
            cookie = CookieGetter.getCookie(host, loginUrl, user, null);
            if (forTest) {
                System.out.println(loginUrl + " cookie: " + cookie);
            }
        } while (cookie.equals("") && counter <= 3);

        if ((message.getUid() != 0) && !cookie.equals("")) {
            try {
                HttpClient client = new HttpClient();
                client.getHostConfiguration().setHost(host, 80, "http");

                // 建立 PostMethod，並指派 Post 網址
                PostMethod post = new PostMethod(postUrl);
                post.setRequestHeader("Connection", "Keep-Alive");
                post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                post.setRequestHeader("Referer", "http://www.plurk.com/" + user.getName());
                post.setRequestHeader("Cookie", cookie);

                // 建立 Post 資料 data
                NameValuePair[] data = {
                    new NameValuePair("posted", message.getPosted()),
                    new NameValuePair("qualifier", message.getQualifier()),
                    new NameValuePair("content", message.getContent()),
                    new NameValuePair("lang", message.getLang()),
                    new NameValuePair("no_comments", Integer.toString(message.getNo_comments())),
                    new NameValuePair("uid", Integer.toString(message.getUid()))
                };
                // 設定 Post 請求
                post.setRequestBody(data);
                if (message.hasLimited_to()) {
                    post.addParameter(new NameValuePair("limited_to", message.getLimited_to()));
                }

                // 發送請求、返回狀態
                int statusCode = client.executeMethod(post);
                if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_OK) {
                    // 取得回傳資訊
                    Header[] headers = post.getResponseHeaders();
                    for (int i = 1; i < headers.length; i++) {
                        if (forTest) {
                            System.out.println(headers[i].getName() + ": " + headers[i].getValue());
                        }
                    }
                    if (forTest) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8")); //編碼需要設定
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                    return true;
                } else {
                    System.err.println("Method failed: " + post.getStatusLine());
                    Header[] headers = post.getResponseHeaders();
                    for (int i = 1; i < headers.length; i++) {
                        if (forTest) {
                            System.out.println(headers[i].getName() + ": " + headers[i].getValue());
                        }
                    }
                    if (forTest) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), "UTF-8")); //編碼需要設定
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Test Case
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AccountModel user = new AccountModel();
        MessageModel mesg = new MessageModel();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your name: ");
        user.setName(scanner.next());
        System.out.println("Name:" + user.getName());
        System.out.print("Please input your password: ");
        user.setPassword(scanner.next());
        System.out.println("Password:" + user.getPassword());

        System.out.print("qualifier: ");
        mesg.setQualifier(scanner.next());
        //String content = "Just For Test jPlurk! http://askeing.blogspot.com/ (星塵)";
        String content = "踩線踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩踩 jPlurk!";
        System.out.println("content: " + content);
        mesg.setContent(content);
        System.out.print("no_comments (0 , 1): ");
        mesg.setNo_comments(scanner.nextInt());

        System.out.println("\n===== Test =====\n");
        if( MessageSender.sendMessage(user, mesg) )
            System.out.println("Send Message done!");
        else
            System.out.println("Send Message Error!");
    //System.exit(0);
    }
}
