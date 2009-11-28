/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.idv.askeing.jPlurk.util;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.jplurk.Constants;

import tw.idv.askeing.jPlurk.model.Account;

/**
 *
 * @author Askeing
 */
public class AccountUtil {

    static Log logger = LogFactory.getLog(AccountUtil.class);
    final static Pattern MAIL_PATTERN = Pattern.compile("^[_\\.0-9a-zA-Z-]+@([0-9a-zA-Z][_0-9a-zA-Z-]+\\.)+[a-zA-Z]{2,3}$");
    //final static Pattern MAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    final static Pattern LOCATION_PATTERN = Pattern.compile("http:\\/\\/www.plurk.com\\/[^\\s+\\\"\\<\\>]+");

    public static Account getRealName(Account user) {

        Matcher mailMatcher = MAIL_PATTERN.matcher(user.getName());
        if ( !mailMatcher.matches() ) {
            logger.info("User Name not E-Mail, do not get Real Name! - " + user.getName());
            return user;
        } else {
            logger.info("User Name is E-Mail, get Real Name! - " + user.getName());
        }

        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(Constants.PLURK_HOST, 80, "http");
        PostMethod method = new PostMethod(Constants.LOGIN_URL);
        method.addParameter(new NameValuePair("nick_name", user.getName()));
        method.addParameter(new NameValuePair("password", user.getPassword()));

        String redirectLocation = "";
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                /*
                Header[] headers = method.getResponseHeaders();
                for (Header header : headers) {
                logger.debug("Header = " + header.getName() + ": " + header.getValue());
                }
                 * */
                Header locationHeader = method.getResponseHeader("location");
                if (locationHeader != null) {
                    redirectLocation = locationHeader.getValue();
                    logger.debug("Location: " + redirectLocation);

                    Matcher locationMatcher = LOCATION_PATTERN.matcher(redirectLocation);
                    if (locationMatcher.matches()) {
                        logger.debug("matcher.group(): " + locationMatcher.group());
                        String name = locationMatcher.group().replace("http://www.plurk.com/", "");
                        logger.debug("name: " + name);
                        user.setName(name);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AccountUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public static void main(String[] args) {
        Account user = new Account();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your name: ");
        user.setName(scanner.next());
        System.out.println("Name:" + user.getName());
        System.out.print("Please input your password: ");
        user.setPassword(scanner.next());
        System.out.println("Password:" + user.getPassword());

        System.out.println("\n===== Test One =====\n");
        AccountUtil.getRealName(user);
    //System.exit(0);
    }
}
