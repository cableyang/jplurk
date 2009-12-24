package com.google.jplurk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.google.jplurk.exception.PlurkException;

/**
 * PlurkSettings class will read a property file which contains plurk api key at least.
 *
 * @author qty
 */
public class PlurkSettings {

    public final static String settings = "jplurk.properties";
    private Properties prop;

    /**
     * Create settings object from ~/jplurk.properties file
     *
     * @throws PlurkException
     */
    public PlurkSettings() throws PlurkException {
        prop = getSettings();
    }

    /**
     * Create settings object from given file object.
     *
     * @param settingFile
     * @throws PlurkException
     */
    public PlurkSettings(File settingFile) throws PlurkException {
        prop = getSettings(settingFile);
    }

    /**
     * @return api key read from settings file
     */
    public String getApiKey() {
        return prop.getProperty("api_key");
    }

    /**
     * @return default user read from settings file
     */
    public String getDefaultUser() {
        return prop.getProperty("default_user");
    }

    /**
     * @return default password read from settings file
     */
    public String getDefaultPassword() {
        return prop.getProperty("default_password");
    }

    /**
     * @return
     */
    public String getDefaultProxyHost() {
        return prop.getProperty("default_proxy_host");
    }

    public int getDefaultProxyPort() {
        int port = 80;
        try {
            port = Integer.valueOf(prop.getProperty("default_proxy_port"));
        } catch (NumberFormatException e) {
        }
        return port;
    }

    public String getDefaultProxyUser() {
        return prop.getProperty("default_proxy_user");
    }

    public String getDefaultProxyPassword() {
        return prop.getProperty("default_proxy_password");
    }

    public MapHelper createParamMap() {
        return new MapHelper(new HashMap<String, String>() {

            private static final long serialVersionUID = -5306686629368927936L;

            {
                put("api_key", getApiKey());
            }
        });
    }

    public String getLang() {
        return prop.getProperty("lang", "en");
    }

    public MapHelper createParamMapWithDefaultUserInfo() {
        return createParamMap().k("username").v(getDefaultUser()).k("password").v(getDefaultPassword());
    }

    private Properties getSettings() throws PlurkException {
        File dir = new File(System.getProperty("user.home", "."));
        dir.mkdirs();
        File setting = new File(dir, settings);
        return getSettings(setting);
    }

    private Properties getSettings(File settingFile) throws PlurkException {
        Properties prop = new Properties();
        if (!settingFile.exists()) {
            throw new PlurkException("settings file is not found: " + settingFile.getAbsolutePath());
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(settingFile);
            prop.load(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                }
            }
        }

        if (StringUtils.isBlank(prop.getProperty("api_key"))) {
            throw new PlurkException("settings has no api_key.");
        }

        return prop;
    }

    public static void main(String[] args) throws PlurkException {
        PlurkSettings p = new PlurkSettings();
        System.out.println(p);
    }
}
