package com.google.jplurk;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.jplurk.action.PlurkActionSheet;
import com.google.jplurk.exception.PlurkException;
import com.google.jplurk.net.ProxyProvider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;

public class PlurkClient {

    PlurkSettings config;
    private ResponseHandler<String> responseHandler = new BasicResponseHandler();

    public PlurkClient(PlurkSettings settings) {
        this.config = settings;
    }

    public JSONObject login(String user, String password) {

        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().login(
                    config.createParamMap().k("username").v(user).k("password").v(password).getMap());

            JSONObject ret = new JSONObject(execute(method));
            return ret;
        } catch (PlurkException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject register(String nick_name, String full_name,
            String password, String gender, String date_of_birth) {
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
        // validation of gender
        m = Pattern.compile("male|female", FLAG).matcher(gender);
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

        /* TODO
         * Need add opt param: email. (waiting for PlurkActionSheet finished)
         */
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().register(
                    config.createParamMap().k("nick_name").v(nick_name).k("full_name").v(full_name).k("password").v(password).k("gender").v(gender).k("date_of_birth").v(date_of_birth).getMap());
            JSONObject ret = new JSONObject(execute(method));
            return ret;
        } catch (PlurkException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String execute(HttpRequestBase method) throws PlurkException {
        HttpClient client = new DefaultHttpClient();

        if (StringUtils.isNotBlank(ProxyProvider.getHost())) {
            HttpHost proxy = new HttpHost(ProxyProvider.getHost(), ProxyProvider.getPort());
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        /* TODO
         * Add Proxy of Auth
         */
//		if (StringUtils.isNotBlank(ProxyProvider.getUser())) {
//			HttpState state = new HttpState();
//			state.setProxyCredentials(
//					new AuthScope(ProxyProvider.getHost(),	ProxyProvider.getPort()),
//					new UsernamePasswordCredentials(ProxyProvider.getUser(), ProxyProvider.getPassword()));
//			client.setState(state);
//		}

        String result = null;
        try {
            result = (String) client.execute(method, responseHandler);
        } catch (Exception e) {
            throw new PlurkException(e);
        }
        client.getConnectionManager().shutdown();
        return result;
    }

    public static void main(String[] args) throws PlurkException, ClientProtocolException, IOException {
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

    }
}
