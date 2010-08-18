package com.google.jplurk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.jplurk.exception.PlurkException;

/**
 * JPlurkSessionTestCase is used for the testing some plurk operations which are
 * needed to login. The class provides exportCookieStore() method to help you
 * create the CookieStore data.
 * 
 * @author qrtt1
 */
public abstract class AbstractJPlurkSessionTestCase extends TestCase {

    protected static Log logger = LogFactory.getLog(PlurkClientTestCase.class);
    protected PlurkClient client;
    protected PlurkSettings settings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            settings = new PlurkSettings();
            BufferedReader reader = new BufferedReader(new FileReader(
                    getCookieStore()));
            JSONArray cookies = new JSONArray(reader.readLine());
            settings.importCookies(cookies);
            logger.info("import cookies: " + cookies);
            client = new PlurkClient(settings);
        } catch (PlurkException e) {
            throw new Exception(e);
        }
    }

    private static File getCookieStore() {
        File dir = new File(System.getProperty("user.home", "."));
        dir.mkdirs();
        return new File(dir, ".jplurkCookies");
    }

    public static void main(String[] args) {
        exportCookieStore();
    }

    private static void exportCookieStore() {
        PlurkSettings settings;
        try {
            settings = new PlurkSettings();
        } catch (PlurkException e) {
            JOptionPane.showConfirmDialog(null,
                    "cannot create jplurk settings.");
            return;
        }

        JSONObject result = new PlurkClient(settings).login(JOptionPane
                .showInputDialog("Please input your username"), JOptionPane
                .showInputDialog("Please input your password"));

        if (result == null) {
            JOptionPane.showMessageDialog(null,
                    "export cookie store is failure. Stop to export cookie");
            return;
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(getCookieStore());
            fw.write(settings.exportCookies().toString());
            fw.close();
            JOptionPane.showMessageDialog(null,
                    "export cookie store to ~/.jplurkCookies");
        } catch (IOException e) {
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignored) {
                } finally {
                    fw = null;
                }
            }
        }
    }

}
