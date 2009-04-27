package tw.idv.askeing.jPlurk;

import java.util.Iterator;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.idv.askeing.jPlurk.net.HttpResultCallback;
import tw.idv.askeing.jPlurk.net.HttpTemplate;

/**
 *
 * @author Askeing, Yen.
 * @version 1.0
 */
public class PicManager {

    static Log logger = LogFactory.getLog(UIDManager.class);

    public static String getPicByName(String name) {
        GetMethod method = new GetMethod("/"+name);

        HttpTemplate template = new HttpTemplate(method);
        Object result = template.execute(new int[]{HttpStatus.SC_MOVED_TEMPORARILY,
                    HttpStatus.SC_OK}, new HttpResultCallback() {

            @Override
            protected Object processResult(GetMethod method) {
                try {
					Iterator<String> it = getIterator(method.getResponseBodyAsStream(), "utf-8");
					while (it.hasNext()) {
						String line = it.next();
                      // Review: need improvement.
                      // <img src="http://avatars.plurk.com/xxx.jpg" class="profile-pic" id="profile_pic" />
                      if (line.contains("class=\"profile-pic\" id=\"profile_pic\"")) {
                          String[] sPic = line.split("\"");
                          logger.debug("Get: "+sPic[1]);
                          return sPic[1];
                      }
					}
//                    BufferedReader in = new BufferedReader(
//                            new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
//                    String line = "";
//                    while ((line = in.readLine()) != null) {
//                        logger.debug(line);
//                        // Review: need improvement.
//                        // <img src="http://avatars.plurk.com/xxx.jpg" class="profile-pic" id="profile_pic" />
//                        if (line.contains("class=\"profile-pic\" id=\"profile_pic\"")) {
//                            String[] sPic = line.split("\"");
//                            logger.debug("Get: "+sPic[1]);
//                            return sPic[1];
//                        }
//                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                return "";
            }
        });

        if (result != null && result instanceof String) {
            return (String) result;
        }
        return "";
    }
}
