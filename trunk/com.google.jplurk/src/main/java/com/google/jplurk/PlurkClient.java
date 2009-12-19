package com.google.jplurk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.jplurk.action.PlurkActionSheet;
import com.google.jplurk.exception.PlurkException;
import com.google.jplurk.net.JPlurkResponseHandler;
import com.google.jplurk.net.ProxyProvider;

public class PlurkClient {

    // <editor-fold defaultstate="collapsed" desc="Init PlurkClient">
	static Logger logger = LoggerFactory.getLogger(PlurkClient.class);
	HttpClient client = new DefaultHttpClient();

    PlurkSettings config;

    public PlurkClient(PlurkSettings settings) {
        this.config = settings;
        configureHttpClient();
    }

	private void configureHttpClient() {
        // Auth Proxy Setting
		if (StringUtils.isNotBlank(ProxyProvider.getUser())) {
            ((DefaultHttpClient)client).getCredentialsProvider().setCredentials(
                new AuthScope(ProxyProvider.getHost(), ProxyProvider.getPort()),
                new UsernamePasswordCredentials(ProxyProvider.getUser(), ProxyProvider.getPassword()));
		}
        // Proxy Host Setting
		if (StringUtils.isNotBlank(ProxyProvider.getHost())) {
			HttpHost proxy = new HttpHost(ProxyProvider.getHost(), ProxyProvider.getPort());
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				client.getConnectionManager().shutdown();
			}
		});
	}
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Users/login">
    /**
     * /API/Users/login<br/>
     * Login and creat a cookie. This cookie can access other methods.
     * @param user
     * @param password
     * @return The JSONObject of /API/Profile/getOwnProfile
     */
    public JSONObject login(String user, String password) {

        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().login(
                    config.createParamMap()
                    .k("username").v(URLEncoder.encode(user,"utf-8"))
                    .k("password").v(URLEncoder.encode(password,"utf-8"))
                    .getMap());

            JSONObject ret = new JSONObject(execute(method));
            return ret;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
        	logger.error(e.getMessage(), e);
        } catch (JSONException e) {
        	logger.error(e.getMessage(), e);
        }

        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Users/register">
    /**
     * /API/Users/register<br/>
     * Register a new user account.
     * @param nick_name
     * @param full_name
     * @param password
     * @param gender
     * @param date_of_birth
     * @return JSONObject with user info {"id": 42, "nick_name": "frodo_b", ...}
     */
    public JSONObject register(String nick_name, String full_name,
            String password, Gender gender, String date_of_birth) {
        return this.register(nick_name, full_name, password, gender, date_of_birth, "");
    }

    /**
     * /API/Users/register<br/>
     * Register a new user account. (with optional parameters.)
     * @param nick_name
     * @param full_name
     * @param password
     * @param gender
     * @param date_of_birth
     * @param email (optional)
     * @return JSONObject with user info {"id": 42, "nick_name": "frodo_b", ...}
     */
    public JSONObject register(String nick_name, String full_name,
            String password, Gender gender, String date_of_birth, String email) {
        final int FLAG = Pattern.DOTALL | Pattern.MULTILINE;
        Matcher m;
        // validation of nick_name
        m = Pattern.compile("[\\w]{3,}", FLAG).matcher(nick_name);
        m.reset();
        if (!m.find()) {
            return null;
        }
        // validation of full_name
        m = Pattern.compile(".+", FLAG).matcher(full_name);
        m.reset();
        if (!m.find()) {
            return null;
        }
        // validation of password
        m = Pattern.compile(".{3,}", FLAG).matcher(password);
        m.reset();
        if (!m.find()) {
            return null;
        }
        // validation of date_of_birth
        m = Pattern.compile("[0-9]{4}\\-(0[1-9])|(1[0-2])\\-(0[1-9])|(1[0-9])|(2[0-9])|(3[0-1])", FLAG).matcher(date_of_birth);
        m.reset();
        if (!m.find()) {
            return null;
        }
        // TODO: Check password need URLEncode? I try encode and work fine now...
        try {
            MapHelper paramMap = config.createParamMap()
            		.k("nick_name").v(URLEncoder.encode(nick_name,"utf-8"))
            		.k("full_name").v(URLEncoder.encode(full_name,"utf-8"))
            		.k("password").v(URLEncoder.encode(password,"utf-8"))
            		.k("gender").v(gender.toString())
            		.k("date_of_birth").v(date_of_birth);
            if( email != null && !email.equals(("")) ) {
                    paramMap = paramMap.k("email").v(email);
            }
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().register(paramMap.getMap());

            JSONObject ret = new JSONObject(execute(method));
            return ret;
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(PlurkClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PlurkException e) {
        	logger.error(e.getMessage(), e);
        } catch (JSONException e) {
        	logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/getUnreadPlurks">
    /**
     * /API/Timeline/getUnreadPlurks<br/>
     * Get unread plurks.
     * @return JSONObject of unread plurks and their users
     */
    public JSONObject getUnreadPlurks() {
		return this.getUnreadPlurks(null);
	}

    /**
     * /API/Timeline/getUnreadPlurks<br/>
     * Get unread plurks older than offset.
     * @param offset (optional), formatted as 2009-6-20T21:55:34
     * @return JSONObject of unread plurks and their users
     */
    public JSONObject getUnreadPlurks(DateTime offset) {
		return this.getUnreadPlurks(offset, 0);
	}

    /**
     * /API/Timeline/getUnreadPlurks<br/>
     * Get the limited unread plurks.
     * @param limit (optional), Limit the number of plurks
     * @return JSONObject of unread plurks and their users
     */
    public JSONObject getUnreadPlurks(int limit) {
		return this.getUnreadPlurks(null, limit);
	}

    /**
     * /API/Timeline/getUnreadPlurks<br/>
     * Get the limited unread plurks older than offset.
     * @param offset (optional), formatted as 2009-6-20T21:55:34
     * @param limit (optional), limit the number of plurks. 0 as default, which will get 10 plurks
     * @return JSONObject of unread plurks and their users
     */
    public JSONObject getUnreadPlurks(DateTime offset, int limit) {
		try {
            MapHelper paramMap = config.createParamMap();
            if(offset != null) {
                paramMap = paramMap.k("offset").v(offset.timeOffset());
            }
            if(limit > 0) {
                paramMap = paramMap.k("limit").v(Integer.toString(limit));
            }
            else if(limit == 0) {
                paramMap = paramMap.k("limit").v("10");
            }
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getUnreadPlurks(paramMap.getMap());
			return new JSONObject(execute(method));
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/plurkAdd">
    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier) {
    	return this.plurkAdd(content, qualifier, NoComments.False);
    }

    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @param no_comments (optional), true or false
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, NoComments no_comments) {
        return this.plurkAdd(content, qualifier, null, no_comments, null);
    }

    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @param lang (optional)
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, Lang lang) {
        return this.plurkAdd(content, qualifier, null, NoComments.False, lang);
    }

    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @param limited_to (optional), JSON Array contains friends ids
     * @param no_comments (optional), true or false
     * @param lang (optional)
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, String limited_to, NoComments no_comments, Lang lang) {
		try {
            MapHelper paramMap = config.createParamMap()
                    .k("content").v(URLEncoder.encode(content,"utf-8"))
					.k("qualifier").v(qualifier.toString())
                    .k("no_comments").v(no_comments.toString())
					.k("lang").v(lang == null ? config.getLang() : lang.toString());
            if( limited_to != null && !limited_to.equals("") ) {
                paramMap = paramMap.k("limited_to").v(limited_to);
            }
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkAdd(paramMap.getMap());
			return new JSONObject(execute(method));
		} catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/plurkDelete">
    /**
     * /API/Timeline/plurkDelete<br/>
     * Deletes a plurk.
     * @param plurkId
     * @return {"success_text": "ok"}
     */
    public JSONObject plurkDelete(String plurkId){
		try {
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkDelete(
				config.createParamMap().k("plurk_id").v(plurkId).getMap());
			return new JSONObject(execute(method));
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/plurkEdit">
    /**
     * /API/Timeline/plurkEdit<br/>
     * Edits a plurk.
     * @param plurkId
     * @param content
     * @return JSON object of the updated plurk
     */
    public JSONObject plurkEdit(String plurkId, String content){
		try {
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkEdit(
				config.createParamMap()
                .k("plurk_id").v(plurkId)
                .k("content").v(URLEncoder.encode(content,"utf-8"))
                .getMap());
			return new JSONObject(execute(method));
		} catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
    // </editor-fold>

    /**
     * @param plurkId which plurk will be response
     * @param content the responsed content
     * @param qualifier
     * @param lang
     * @return JSON object
     */
    public JSONObject responseAdd(String plurkId, String content, Qualifier qualifier, Lang lang) {
		try {
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().responseAdd(
					config.createParamMap()
					.k("plurk_id").v(plurkId)
					.k("content").v(URLEncoder.encode(content,"utf-8"))
					.k("qualifier").v(qualifier.toString())
					.k("lang").v(lang == null ? config.getLang() : lang.toString())
					.getMap()
			);
			return new JSONObject(execute(method));
		} catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
			logger.error(e.getMessage(), e);
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @param plurkId
	 * @return JSON object
	 */
	public JSONObject responseGet(String plurkId){
		try {
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().responseGet(
				config.createParamMap().k("plurk_id").v(plurkId).k("from_response").v("5").getMap());
			return new JSONObject(execute(method));
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @param ids the plurk ids will be muted
	 * @return JSONObject represent the {"success_text": "ok"}
	 */
	public JSONObject mutePlurks(String... ids) {
		try {
			Set<Integer> idSet = new HashSet<Integer>();
			for (String id : ids) {
				idSet.add(NumberUtils.toInt(id, 0));
			}
			idSet.remove(0);

			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().mutePlurks(
					config.createParamMap().k("ids").v(new JSONArray(idSet).toString()).getMap());

			return new JSONObject(execute(method));
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @param ids the plurk ids will be unmuted.
	 * @return JSONObject represent the {"success_text": "ok"}
	 */
	public JSONObject unmutePlurks(String... ids) {
		try {
			Set<Integer> idSet = new HashSet<Integer>();
			for (String id : ids) {
				idSet.add(NumberUtils.toInt(id, 0));
			}
			idSet.remove(0);

			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().unmutePlurks(
					config.createParamMap().k("ids").v(new JSONArray(idSet).toString()).getMap());

			return new JSONObject(execute(method));
		} catch (PlurkException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

    // <editor-fold defaultstate="collapsed" desc="Execution of HttpRequest">
    private String execute(HttpUriRequest method) throws PlurkException {
        String result = "";
        try {
            result = (String) client.execute(method,  new JPlurkResponseHandler());
        } catch (Exception e) {
            throw new PlurkException(e);
        }
        return result;
    }
    // </editor-fold>

    public static void main(String[] args) throws PlurkException, ClientProtocolException, IOException, InterruptedException {
//        ProxyProvider.setProvider("proxyhost", 8080);

        PlurkClient pc = new PlurkClient(new PlurkSettings());

//                JSONObject oRegister = pc.register(JOptionPane.showInputDialog("nick_name"),
//                        JOptionPane.showInputDialog("full_name"),
//                        JOptionPane.showInputDialog("password"),
//                        JOptionPane.showInputDialog("gender"),
//                        JOptionPane.showInputDialog("date_of_birth"));
//                System.out.println(oRegister);

        JSONObject o = pc.login(JOptionPane.showInputDialog("id"), JOptionPane.showInputDialog("password"));
        System.out.println(o);

//        JSONObject oo = pc.plurkAdd("測試 jPlurk 編輯 plruk 功能！！", Qualifier.IS, NoComments.False);
//        JSONObject oo = pc.plurkAdd("hmmmm", Qualifier.SAYS);
//        System.out.println(oo);

//        JSONObject oe = pc.plurkEdit("183532425", "早上的 http://code.google.com/p/jplurk/ (jPlurk) 先這樣吧～ by jPlurk v2 plurkEdit");
//        System.out.println(oe);

//        JSONObject od = pc.plurkDelete("183525435");
//        System.out.println(od);

        JSONObject js10 = pc.getUnreadPlurks(DateTime.now(), 1);
        System.out.println(js10);

        System.out.println(pc.unmutePlurks("183559649"));;

//        JSONObject js = pc.getUnreadPlurks(DateTime.now());
//        System.out.println(js);
//

//        pc.responseAdd("183178995", "我也不喜歡這樣的人", Qualifier.FEELS, Lang.tr_ch);
//
//        JSONObject ooo = pc.responseGet("183178995");
//        System.out.println(ooo);
    }
}
