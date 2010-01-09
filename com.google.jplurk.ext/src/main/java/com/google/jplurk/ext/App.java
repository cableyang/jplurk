package com.google.jplurk.ext;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.google.jplurk.ISettings;
import com.google.jplurk.PlurkClient;
import com.google.jplurk.PlurkSettings;
import com.google.jplurk.exception.PlurkException;

public class App {
	public static void main(String[] args) throws PlurkException {
		ISettings settings = new PlurkSettings();
		PlurkClient client = new PlurkClient(settings);
		JSONObject o = client.login(JOptionPane.showInputDialog("id"),
				JOptionPane.showInputDialog("password"));
		System.out.println(o);

	}
}
