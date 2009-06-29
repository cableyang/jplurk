package tw.idv.askeing.jPlurk;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;

import com.googlecode.jplurk.PlurkTemplate;
import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.net.Result;

/**
 * jPlurk MessageSender: Send Message. It will automatically login, getting uid, get cookie, and sending message.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class MessageSender {

	static Log logger = LogFactory.getLog(MessageSender.class);

    /**
     * Send Message to Plurk. This method will get UID first.
     * @param user
     * @param message
     * @return
     */
    public static boolean sendMessage(Account account, Message message) {
		PlurkTemplate template = new PlurkTemplate(account);
		template.doAction(new Login(), account);

		Result result = template.doAction(new AddPlurk(), message);
		return result.isOk();

//
//        if (UIDManager.getUID(user) == 0) {
//        	logger.warn("the uid of sent message is invalid. ");
//        	return false;
//		}
//
//        message.setUid(UIDManager.getUID(user));
//
//        /* TODO: Reuse Cookie
//         * 測試後發現， 3/1 與 3/5、3/7 的 cookie 相同
//         * 因此初步推測 Cookie 跟人跑，一人有多組 Cookie
//         * 如有不妥，再拿掉
//         * */
//        String cookie = user.getCookie();
//        if( "".equals(user.getCookie()) ) {
//            cookie = CookieGetter.getCookie(Constants.PLURK_HOST, Constants.LOGIN_URL, user, null);
//            user.setCookie(cookie);
//        }
//
//        if(cookie == null || "".equals(cookie.trim())){
//        	logger.warn("cookie token is not found.");
//        	return false;
//        }
//
//        /****
//         * 以上為 pre-condition testing.
//         * ***/
//
//        // 建立 PostMethod，並指派 Post 網址
//        PostMethod post = new PostMethod(Constants.ADD_PLURK_URL);
//        post.setRequestHeader("Connection", "Keep-Alive");
//        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setRequestHeader("Referer", "http://www.plurk.com/" + user.getName());
//        post.setRequestHeader("Cookie", cookie);
//
//        // 設定 Post 請求
//        post.setRequestBody(createRequestBodyFromMessage(message));
//        applyLimitedToIfExists(message, post);
//
//        Object result = new HttpTemplate(post).execute(
//            	new int[] {HttpStatus.SC_MOVED_TEMPORARILY, HttpStatus.SC_OK },
//				new HttpResultCallback() {
//            		@Override
//            		protected Object processResult(PostMethod method) {
//            			return Boolean.TRUE;
//            		}
//				});
//
//        if(result != null && result instanceof Boolean){
//        	return ((Boolean)result).booleanValue();
//        }
//
//        return false;
    }

//	private static void applyLimitedToIfExists(
//		Message message, PostMethod post) {
//		if (!message.hasLimited_to()) {
//			return;
//		}
//		post.addParameter(
//			new NameValuePair("limited_to", message.getLimitedTo()));
//	}

//	private static NameValuePair[] createRequestBodyFromMessage(Message message) {
//		// 建立 Post 資料 data
//		NameValuePair[] data = {
//		    new NameValuePair("posted", message.getPosted()),
//		    new NameValuePair("qualifier", message.getQualifier()),
//		    new NameValuePair("content", message.getContent()),
//		    new NameValuePair("lang", message.getLang()),
//		    new NameValuePair("no_comments", Integer.toString(message.getNoComments())),
//		    new NameValuePair("uid", Integer.toString(message.getUid()))
//		};
//		return data;
//	}

    /**
     * Test Case
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Account user = new Account();
        Message mesg = new Message();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your name: ");
        user.setName(scanner.next());
        System.out.println("Name:" + user.getName());
        System.out.print("Please input your password: ");
        user.setPassword(scanner.next());
        System.out.println("Password:" + user.getPassword());

        System.out.print("qualifier: ");
        mesg.setQualifier(Qualifier.fromString(scanner.next()));
        System.out.print("no_comments (0 , 1): ");
        mesg.setNoComments(scanner.nextInt());
        String content = "Test http://askeing.twbbs.org/jPlurk/1.0/doc/api/ (jPlurk)! Time: " + new java.util.Date();
        mesg.setContent(content);
        System.out.println("content: " + content);

        System.out.println("\n===== Test =====\n");
        if (MessageSender.sendMessage(user, mesg)) {
            System.out.println("Send Message done!");
        } else {
            System.out.println("Send Message Error!");
        }
    //System.exit(0);
    }
}
