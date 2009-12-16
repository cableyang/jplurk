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

public class PlurkClient {
	PlurkSettings config;

	private ResponseHandler<String> responseHandler = new BasicResponseHandler();

	public PlurkClient(PlurkSettings settings) {
		this.config = settings;
	}

	public JSONObject login(String user, String password) throws ClientProtocolException, IOException {

		try {
			HttpGet method = (HttpGet) PlurkActionSheet.getInstance().login(
				config.createParamMap()
					.k("username").v(user)
					.k("password").v(password)
					.getMap()
			);

			JSONObject ret = new JSONObject(execute(method));
			return ret;
		} catch (PlurkException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String execute(HttpRequestBase method) throws PlurkException{
		HttpClient client = new DefaultHttpClient();
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
		PlurkClient pc = new PlurkClient(new PlurkSettings());
		JSONObject o = pc.login(JOptionPane.showInputDialog("id"), JOptionPane.showInputDialog("password"));
		System.out.println(o);
	}


}
