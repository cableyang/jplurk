package com.google.jplurk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.jplurk.action.PlurkActionSheet;
import com.google.jplurk.exception.PlurkException;
import com.google.jplurk.net.ThinMultipartEntity;
import com.google.jplurk.org.apache.commons.lang.StringUtils;

/**
 * Main Client for Plurk API.
 * @author Askeing
 */
public class PlurkClient {

    // <editor-fold defaultstate="collapsed" desc="Init PlurkClient">
    private static Log logger = LogFactory.getLog(PlurkClient.class);
    private HttpExecutor executor;
    private ISettings config;

    /**
     * Load Setting from property file.
     * @param settings
     */
    public PlurkClient(ISettings settings) {
        this.config = settings;
        this.executor = new HttpExecutor(settings);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Users/login">
    /**
     * /API/Users/login<br/>
     * Login an already created user. Login creates a session cookie, which can be used to access the other methods. On success it returns the data returned by /API/Profile/getOwnProfile.
     * @param user The user's nick name or email.
     * @param password The user's password.
     * @return The JSONObject of /API/Profile/getOwnProfile
     */
    public JSONObject login(String user, String password) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().login(
                    config.args().name("username").value(user).name("password").value(password).getMap());
            JSONObject ret = new JSONObject(executor.execute(method));
            return ret;
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
     * @param nickName
     * @param fullName
     * @param password
     * @param gender
     * @param dateOfBirth
     * @return JSONObject with user info {"id": 42, "nick_name": "frodo_b", ...}
     */
    public JSONObject register(String nickName, String fullName,
            String password, Gender gender, String dateOfBirth) {
        return this.register(nickName, fullName, password, gender, dateOfBirth, "");
    }

    /**
     * /API/Users/register<br/>
     * Register a new user account. (with optional parameters.)
     * @param nickName
     * @param fullName
     * @param password
     * @param gender
     * @param dateOfBirth
     * @param email (optional)
     * @return JSONObject with user info {"id": 42, "nick_name": "frodo_b", ...}
     */
    public JSONObject register(String nickName, String fullName,
            String password, Gender gender, String dateOfBirth, String email) {
        final int FLAG = Pattern.DOTALL | Pattern.MULTILINE;
        Matcher m;
        // validation of nick_name
        m = Pattern.compile("[\\w]{3,}", FLAG).matcher(nickName);
        m.reset();
        if (!m.find()) {
            return null;
        }
        // validation of full_name
        m = Pattern.compile(".+", FLAG).matcher(fullName);
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
        m = Pattern.compile("[0-9]{4}\\-(0[1-9])|(1[0-2])\\-(0[1-9])|(1[0-9])|(2[0-9])|(3[0-1])", FLAG).matcher(dateOfBirth);
        m.reset();
        if (!m.find()) {
            return null;
        }
        try {
            Args paramMap = config.args().name("nick_name").value(nickName).name("full_name").value(fullName).name("password").value(password).name("gender").value(gender.toString()).name("date_of_birth").value(dateOfBirth);
            if (email != null && !email.equals((""))) {
                paramMap = paramMap.name("email").value(email);
            }
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().register(paramMap.getMap());

            JSONObject ret = new JSONObject(executor.execute(method));
            return ret;
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Users/update">
    /**
     * /API/Users/update
     * @param currentPassword User's current password.
     * @param fullName Change full name.
     * @param newPassword Change password.
     * @param email Change email.
     * @param displayName User's display name, can be empty and full unicode. Must be shorter than 15 characters.
     * @param privacyPolicy User's privacy settings. The option can be world (whole world can view the profile), only_friends (only friends can view the profile) or only_me (only the user can view own plurks).
     * @param birth Should be YYYY-MM-DD, example 1985-05-13.
     * @return
     */
    public JSONObject update(String currentPassword, String fullName,
            String newPassword, String email, String displayName,
            PrivacyPolicy privacyPolicy, DateTime birth) {
        if (StringUtils.isBlank(currentPassword)) {
            logger.warn("current password can not be null.");
            return null;
        }

        Args args = config.args();
        args.name("current_password").value(currentPassword);
        if (StringUtils.isNotBlank(fullName)) {
            args.name("full_name").value(fullName);
        }
        if (StringUtils.isNotBlank(newPassword)) {
            args.name("new_password").value(newPassword);
        }
        if (StringUtils.isNotBlank(displayName)) {
            args.name("display_name").value(displayName);
        }
        if (StringUtils.isNotBlank(email)) {
            args.name("email").value(email);
        }
        if (privacyPolicy != null) {
            args.name("privacy").value(privacyPolicy.toString());
        }
        if (birth != null) {
            args.name("privacy").value(birth.birthday());
        }

        try {
            return new JSONObject(executor.execute(PlurkActionSheet.getInstance().update(args.getMap())));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Users/updatePicture">
    /**
     * /API/Users/updatePicture <br />
     * @param file a image file will be uploaded
     * @return
     */
    public JSONObject updatePicture(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            logger.warn("not a valid file: " + file);
            return null;
        }

        HttpPost method = new HttpPost("http://www.plurk.com/API/Users/updatePicture");
        try {
            ThinMultipartEntity entity = new ThinMultipartEntity();
            entity.addPart("api_key", config.getApiKey());
            entity.addPart("profile_image", file);
            method.setEntity(entity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * /API/Users/updatePicture <br />
     * @param fileName a image file will be uploaded
     * @param inputStream a input stream can read image data
     * @return
     */
    public JSONObject updatePicture(String fileName, InputStream inputStream) {

        HttpPost method = new HttpPost("http://www.plurk.com/API/Users/updatePicture");
        try {
            ThinMultipartEntity entity = new ThinMultipartEntity();
            entity.addPart("api_key", config.getApiKey());
            entity.addPart("profile_image", fileName, inputStream);
            method.setEntity(entity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/getFriendsByOffset">
    /**
     * /API/FriendsFans/getFriendsByOffset <br/>
     * @param userId
     * @param offset
     * @return
     */
    public JSONArray getFriendsByOffset(String userId, int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getFriendsByOffset(config.args().name("user_id").value(userId).name("offset").value("" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONArray(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/getFansByOffset">
    /**
     * /API/FriendsFans/getFansByOffset <br />
     * @param userId
     * @param offset
     * @return
     */
    public JSONArray getFansByOffset(String userId, int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getFansByOffset(config.args().name("user_id").value(userId).name("offset").value("" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONArray(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/getFollowingByOffset">
    /**
     * /API/FriendsFans/getFollowingByOffset
     * @param offset The offset, can be 10, 20, 30 etc.
     * @return Returns a list of JSON objects users, e.g. [{"id": 3, "nick_name": "alvin", ...}, ...]
     */
    public JSONArray getFollowingByOffset(int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getFollowingByOffset(
                    config.args().name("offset").value(
                    "" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONArray(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/becomeFriend">
    /**
     * /API/FriendsFans/becomeFriend
     * @param friendId The ID of the user you want to befriend.
     * @return {"success_text": "ok"} if a friend request has been made.
     */
    public JSONObject becomeFriend(int friendId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().becomeFriend(config.args().name("friend_id").value("" + friendId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/removeAsFriend">
    /**
     * /API/FriendsFans/removeAsFriend
     * @param friendId The ID of the user you want to remove
     * @return {"success_text": "ok"} if friend_id has been removed as friend.
     */
    public JSONObject removeAsFriend(int friendId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().removeAsFriend(config.args().name("friend_id").value("" + friendId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/becomeFan">
    /**
     * /API/FriendsFans/becomeFan
     * @param fanId The ID of the user you want to become fan of
     * @return {"success_text": "ok"} if the current logged in user is a fan of fan_id.
     */
    public JSONObject becomeFan(int fanId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().becomeFan(config.args().name("fan_id").value("" + fanId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/setFollowing">
    /**
     * /API/FriendsFans/setFollowing
     * @param userId
     * @param follow true if the user should be followed, and false if the user should be unfollowed.
     * @return
     */
    public JSONObject setFollowing(int userId, boolean follow) {
        try {
            Boolean isFollow = follow;
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().setFollowing(
                    config.args().name("user_id").value("" + userId).name("follow").value(isFollow.toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/FriendsFans/getCompletion">
    /**
     * /API/FriendsFans/getCompletion <br/>
     * Returns a JSON object of the logged in users friends (nick name and full name).
     * This information can be used to construct auto-completion for private plurking.
     * Notice that a friend list can be big, depending on how many friends a user has, so this list should be lazy-loaded in your application.
     * @return
     */
    public JSONObject getCompletion() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getCompletion(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/getPlurk">
    /**
     * /API/Users/getPlurk
     * @param plurkId
     * @return JSON object of the plurk and owner
     */
    public JSONObject getPlurk(String plurkId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPlurk(
                    config.args().name("plurk_id").value(plurkId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="API/Timeline/getPlurks">
    /**
     * /API/Timeline/getPlurks
     * @param offset (optional) Return plurks older than offset, formatted as 2009-6-20T21:55:34.
     * @param limit (optional) How many plurks should be returned? Default is 20. 
     * @param onlyUser (optional) filter of My Plurks
     * @param onlyResponsed (optional) filter of Private
     * @param onlyPrivate (optional) filter of Responded
     * @return
     */
    public JSONObject getPlurks(DateTime offset, int limit, boolean onlyUser, boolean onlyResponsed, boolean onlyPrivate) {
        try {
            Args args = config.args().name("offset").value((offset == null ? DateTime.now() : offset).toTimeOffset()).name("limit").value("" + (limit <= 0 ? 20 : limit));

            if (onlyUser) {
                args.name("only_user").value("true");
            }
            if (onlyResponsed) {
                args.name("only_responded").value("true");
            }
            if (onlyPrivate) {
                args.name("only_private").value("true");
            }

            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPlurks(
                    args.getMap());

            return new JSONObject(executor.execute(method));
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
            Args args = config.args();
            if (offset != null) {
                args = args.name("offset").value(offset.toTimeOffset());
            }
            if (limit > 0) {
                args = args.name("limit").value(Integer.toString(limit));
            } else if (limit == 0) {
                args = args.name("limit").value("10");
            }
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getUnreadPlurks(args.getMap());
            return new JSONObject(executor.execute(method));
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
        return this.plurkAdd(content, qualifier, CommentBy.All);
    }

    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @param commentBy (optional), true or false
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, CommentBy commentBy) {
        return this.plurkAdd(content, qualifier, null, commentBy, null);
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
        return this.plurkAdd(content, qualifier, null, CommentBy.All, lang);
    }

    /**
     * /API/Timeline/plurkAdd<br/>
     * Add new plurk to timeline.
     * @param content
     * @param qualifier
     * @param limited_to (optional), JSON Array contains friends ids. If limited_to is [0], then the Plurk is privatley posted to the poster's friends.
     * @param commentBy (optional), true or false
     * @param lang (optional)
     * @return JSON object of the new plurk
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, String limited_to, CommentBy commentBy, Lang lang) {
        try {
            Args args = config.args().name("content").value(content).name("qualifier").value(qualifier.toString()).name("no_comments").value(commentBy.toString()).name("lang").value(lang == null ? config.getLang() : lang.toString());
            if (limited_to != null && !limited_to.equals("")) {
                args = args.name("limited_to").value(limited_to);
            }
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkAdd(args.getMap());
            return new JSONObject(executor.execute(method));
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
    public JSONObject plurkDelete(String plurkId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkDelete(
                    config.args().name("plurk_id").value(plurkId).getMap());
            return new JSONObject(executor.execute(method));
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
    public JSONObject plurkEdit(String plurkId, String content) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().plurkEdit(
                    config.args().name("plurk_id").value(plurkId).name("content").value(content).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/mutePlurks">
    /**
     * /API/Timeline/mutePlurks
     * @param ids the plurk ids will be muted
     * @return JSONObject represent the {"success_text": "ok"}
     */
    public JSONObject mutePlurks(String... ids) {
        try {
            HttpUriRequest method = PlurkActionSheet.getInstance().mutePlurks(
                    config.args().name("ids").value(Utils.toIds(ids).toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/unmutePlurks">
    /**
     * /API/Timeline/unmutePlurks
     * @param ids the plurk ids will be unmuted.
     * @return JSONObject represent the {"success_text": "ok"}
     */
    public JSONObject unmutePlurks(String... ids) {
        try {
            HttpUriRequest method = PlurkActionSheet.getInstance().unmutePlurks(
                    config.args().name("ids").value(Utils.toIds(ids).toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;        
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/favoritePlurks">
    /**
     * /API/Timeline/favoritePlurks
     * @param ids The plurk ids, formated as JSON, e.g. [342,23242,2323]
     * @return JSONObject represent the {"success_text": "ok"}
     */
    public JSONObject favoritePlurks(String... ids) {
        try {
            HttpUriRequest method = PlurkActionSheet.getInstance().favoritePlurks(
                    config.args().name("ids").value(Utils.toIds(ids).toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }        
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/unfavoritePlurks">
    /**
     * /API/Timeline/unfavoritePlurks
     * @param ids The plurk ids, formated as JSON, e.g. [342,23242,2323]
     * @return JSONObject represent the {"success_text": "ok"}
     */
    public JSONObject unfavoritePlurks(String... ids) {
        try {
            HttpUriRequest method = PlurkActionSheet.getInstance().unfavoritePlurks(
                    config.args().name("ids").value(Utils.toIds(ids).toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }        
        return null;        
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/markAsRead">
    /**
     * /API/Timeline/markAsRead
     * @param ids the plurk ids will mark as read.
     * @return JSONObject represent the {"success_text": "ok"}
     */
    public JSONObject markAsRead(String... ids) {
        try {
            HttpUriRequest method = PlurkActionSheet.getInstance().markAsRead(
                    config.args().name("ids").value(Utils.toIds(ids).toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }        
        return null;   
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/uploadPicture">
    /**
     * /API/Timeline/uploadPicture
     * @param file a image file will be uploaded
     * @return json with thumbnail url. for example <pre>{"thumbnail":"http://images.plurk.com/tn_3146394_fb04befc28fbca59318f16d83d5c78cc.gif","full":"http://images.plurk.com/3146394_fb04befc28fbca59318f16d83d5c78cc.jpg"}</pre>
     */
    public JSONObject uploadPicture(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            logger.warn("not a valid file: " + file);
            return null;
        }

        HttpPost method = new HttpPost("http://www.plurk.com/API/Timeline/uploadPicture");
        try {
            ThinMultipartEntity entity = new ThinMultipartEntity();
            entity.addPart("api_key", config.getApiKey());
            entity.addPart("image", file);
            method.setEntity(entity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Timeline/uploadPicture">
    /**
     * /API/Timeline/uploadPicture
     * @param fileName a image file will be uploaded
     * @param inputStream
     * @return json with thumbnail url. for example <pre>{"thumbnail":"http://images.plurk.com/tn_3146394_fb04befc28fbca59318f16d83d5c78cc.gif","full":"http://images.plurk.com/3146394_fb04befc28fbca59318f16d83d5c78cc.jpg"}</pre>
     */
    public JSONObject uploadPicture(String fileName, InputStream inputStream) {

        HttpPost method = new HttpPost("http://www.plurk.com/API/Timeline/uploadPicture");
        try {
            ThinMultipartEntity entity = new ThinMultipartEntity();
            entity.addPart("api_key", config.getApiKey());
            entity.addPart("image", fileName, inputStream);
            method.setEntity(entity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Responses/responseAdd">
    /**
     * /API/Responses/responseAdd
     * @param plurkId which plurk will be response
     * @param content the responsed content
     * @param qualifier
     * @return JSON object
     */
    public JSONObject responseAdd(String plurkId, String content, Qualifier qualifier) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().responseAdd(
                    config.args().name("plurk_id").value(plurkId).name("content").value(content).name("qualifier").value(qualifier.toString()).getMap());
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Responses/get">
    /**
     * /API/Responses/get
     * @param plurkId
     * @return JSON object
     */
    public JSONObject responseGet(String plurkId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().responseGet(
                    config.args().name("plurk_id").value(plurkId).name("from_response").value("0").getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Responses/responseDelete">
    /**
     * /API/Responses/responseDelete
     * @param plurkId the plurk id contains the response
     * @param responseId the id of the response will be deleted.
     * @return {"success_text": "ok"} when deletion is success, otherwise null.
     */
    public JSONObject responseDelete(String plurkId, String responseId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().responseDelete(
                    config.args().name("plurk_id").value(plurkId).name("response_id").value(responseId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/getActive">
    /**
     * /API/Alerts/getActive <br />
     * Return a JSON list of current active alerts.
     * @return
     */
    public JSONObject getActive() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getActive(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/getHistory">
    /**
     * /API/Alerts/getHistory <br />
     * Return a JSON list of past 30 alerts.
     * @return
     */
    public JSONObject getHistory() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getHistory(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/addAsFan">
    /**
     * /API/Alerts/addAsFan <br />
     * Accept user_id as fan.
     * @param userId The user_id that has asked for friendship.
     * @return
     */
    public JSONObject addAsFan(int userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().addAsFan(config.args().name("").value("user_id" + userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/addAsFriend">
    /**
     * /API/Alerts/addAsFriend <br />
     * Accept user_id as friend.
     * @param userId The user_id that has asked for friendship.
     * @return
     */
    public JSONObject addAsFriend(int userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().addAsFriend(config.args().name("").value("user_id" + userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/addAllAsFan">
    /**
     * /API/Alerts/addAllAsFan <br />
     * Accept all friendship requests as fans
     * @return {"success_text": "ok"} when request success, otherwise null
     */
    public JSONObject addAllAsFan() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().addAllAsFan(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/addAllAsFriends">
    /**
     * /API/Alerts/addAllAsFriends <br />
     * Accept all friendship requests as friends.
     * @return {"success_text": "ok"} when request success, otherwise null
     */
    public JSONObject addAllAsFriends() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().addAllAsFriends(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/denyFriendship">
    /**
     * /API/Alerts/denyFriendship <br />
     * Deny friendship to user_id.
     * @param userId The user_id that has asked for friendship.
     * @return
     */
    public JSONObject denyFriendship(int userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().denyFriendship(config.args().name("").value("user_id" + userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Alerts/removeNotification">
    /**
     * /API/Alerts/removeNotification <br />
     * Remove notification to user with id user_id.
     * @param userId The user_id that the current user has requested friendship for.
     * @return
     */
    public JSONObject removeNotification(int userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().removeNotification(config.args().name("").value("user_id" + userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Profile/getOwnProfile">
    /**
     * /API/Profile/getOwnProfile
     * @return
     */
    public JSONObject getOwnProfile() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getOwnProfile(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Profile/getPublicProfile">
    /**
     * /API/Profile/getPublicProfile
     * @param userId
     * @return
     */
    public JSONObject getPublicProfile(String userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPublicProfile(config.args().name("user_id").value(userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Realtime/getUserChannel">
    /**
     * Get instant notifications when there are new plurks and responses on a
     * user's timeline. This is much more efficient and faster than polling so
     * please use it!
     *
     * This API works like this:
     *
     * <li /> A request is sent to /API/Realtime/getUserChannel and in it you get
     * an unique channel to currnetly logged in user's timeline
     * <li />You do requests to this unqiue channel in order to get notifications
     *
     * @return
     */
    public PlurkNotifier getUserChannel() {
        JSONObject obj = new JSONObject();
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getUserChannel(config.args().getMap());
            obj = new JSONObject(executor.execute(method));
            return new PlurkNotifier(executor.getHttpClient(), obj);
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Polling/getPlurks">
    /**
     * /API/Polling/getPlurks <br/>
     * You should use this call to find out if there any new plurks posted to the user's timeline.
     * It's much more efficient than doing it with /API/Timeline/getPlurks, so please use it :)
     * @param offset
     * @param limit
     * @return
     */
    public JSONObject getPollingPlurks(DateTime offset, int limit) {
        try {
            String _offset = (offset == null ? DateTime.now().toTimeOffset() : offset.toTimeOffset());
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPollingPlurks(config.args().name("offset").value(_offset).name("limit").value("" + (limit <= 0 ? 20 : limit)).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Polling/getUnreadCount">
    /**
     * /API/Polling/getUnreadCount <br/>
     * Use this call to find out if there are unread plurks on a user's timeline.
     * @return
     */
    public JSONObject getPollingUnreadCount() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPollingUnreadCount(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/PlurkSearch/search">
    /**
     * /API/PlurkSearch/search <br/>
     * Returns the latest 20 plurks on a search term.
     * @param query
     * @return
     */
    public JSONObject searchPlurk(String query) {
        return searchPlurk(query, 0);
    }

    /**
     * /API/PlurkSearch/search <br/>
     * Returns the latest 20 plurks on a search term.
     * @param query
     * @param offset A plurk_id of the oldest Plurk in the last search result.
     * @return
     */
    public JSONObject searchPlurk(String query, int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().searchPlurk(
                    config.args().name("query").value(query).name("offset").value("" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/UserSearch/search">
    /**
     * /API/UserSearch/search <br/>
     * Returns 10 users that match query, users are sorted by karma. <br/>
     * (Hint: This API seems like do nothing...)
     * @param query
     * @return
     */
    public JSONObject searchUser(String query) {
        return searchUser(query, 10);
    }

    /**
     * /API/UserSearch/search <br/>
     * Returns 10 users that match query, users are sorted by karma.
     * @param query
     * @param offset Page offset, like 10, 20, 30 etc. .
     * @return
     */
    public JSONObject searchUser(String query, int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().searchUser(
                    config.args().name("query").value(query).name("offset").value("" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Emoticons/get">
    /**
     * /API/Emoticons/get <br/>
     * This call returns a JSON object that looks like: <br/>
     * {"karma": {"0": [[":-))", "http:\/\/statics.plurk.com\/XXX.gif"], ...], ...},
     * "recuited": {"10": [["(bigeyes)", "http:\/\/statics.plurk.com\/XXX.gif"], ...], ...} }  <br/>
     * emoticons["karma"][25] denotes that the user has to have karma over 25 to use these emoticons.  <br/>
     * emoticons["recuited"][10] means that the user has to have user.recuited >= 10 to use these emoticons. <br/>
     * It's important to check for these things on the client as well, since the emoticon levels are checked in the models.
     * @return
     */
    public JSONObject getEmoticons() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getEmoticons(config.args().getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Blocks/get">
    /**
     * /API/Blocks/get <br/>
     * default offset 0
     * @return
     */
    public JSONObject getBlocks() {
        return getBlocks(0);
    }

    /**
     * /API/Blocks/get
     * @param offset
     * @return
     */
    public JSONObject getBlocks(int offset) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getBlocks(config.args().name("offset").value("" + (offset < 0 ? 0 : offset)).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Blocks/block">
    /**
     * /API/Blocks/block
     * @param userId
     * @return
     */
    public JSONObject block(String userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().block(config.args().name("user_id").value(userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Blocks/unblock">
    /**
     * /API/Blocks/unblock
     * @param userId
     * @return
     */
    public JSONObject unblock(String userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().unblock(config.args().name("user_id").value(userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/get_cliques">
    /**
     * /API/Cliques/get_cliques
     * @return a JSON list of users current cliques
     */
    public JSONArray getCliques() {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getCliques(config.args().getMap());
            return new JSONArray(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/create_clique">
    /**
     * /API/Cliques/create_clique <br/>
     * Create the new clique.
     * @param cliqueName The name of the new clique
     * @return
     */
    public JSONObject createClique(String cliqueName) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().createClique(config.args().name("clique_name").value(cliqueName).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/get_clique">
    /**
     * /API/Cliques/get_clique <br/>
     * Get the user in the clique.
     * @param cliqueName
     * @return Returns the users in the clique, e.g. [{"display_name": "amix3", "gender": 0, "nick_name": "amix", "has_profile_image": 1, "id": 1, "avatar": null}]
     */
    public JSONArray getClique(String cliqueName) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getClique(config.args().name("clique_name").value(cliqueName).getMap());
            return new JSONArray(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/renameClique">
    /**
     * /API/Cliques/renameClique <br/>
     * Rename the clique.
     * @param cliqueName
     * @param newName
     * @return
     */
    public JSONObject renameClique(String cliqueName, String newName) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().renameClique(config.args().name("clique_name").value(cliqueName).name("new_name").value(newName).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/delete_clique">
    /**
     * /API/Cliques/delete_clique <br/>
     * Delete the clique.
     * @param cliqueName
     * @return
     */
    public JSONObject deleteClique(String cliqueName) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().deleteClique(config.args().name("clique_name").value(cliqueName).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/add">
    /**
     * /API/Cliques/add <br/>
     * Add the user into the clique.
     * @param cliqueName
     * @param userId
     * @return
     */
    public JSONObject addToClique(String cliqueName, String userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().addToClique(config.args().name("clique_name").value(cliqueName).name("user_id").value(userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="/API/Cliques/remove">
    /**
     * /API/Cliques/remove <br/>
     * Remove user from the clique
     * @param cliqueName
     * @param userId
     * @return
     */
    public JSONObject removeFromClique(String cliqueName, String userId) {
        try {
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().removeFromClique(config.args().name("clique_name").value(cliqueName).name("user_id").value(userId).getMap());
            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    // </editor-fold>


    /**
     * [Non-Offical API]
     * /API/Timeline/getPlurks
     * Add new function: filter of Liked.
     * Function getFavoritePlurks() also use getPlurks API, because onlyFavorite can NOT using with onlyUser, onlyResponsed, and onlyPrivate, so this function is split out.
     * @param offset (optional) Return plurks older than offset, formatted as 2009-6-20T21:55:34.
     * @param limit (optional) How many plurks should be returned? Default is 20.
     * @param onlyFavorite (optional) filter of Liked.
     * @return
     */
    public JSONObject getFavoritePlurks(DateTime offset, int limit, boolean onlyFavorite) {
        try {
            Args args = config.args().name("offset").value((offset == null ? DateTime.now() : offset).toTimeOffset()).name("limit").value("" + (limit <= 0 ? 20 : limit));

            if (onlyFavorite) {
                args.name("only_favorite").value("true");
            }
            // Function getFavoritePlurks() also use getPlurks API,
            // because onlyFavorite can NOT using with onlyUser, onlyResponsed, and onlyPrivate, so this function is split out.
            HttpGet method = (HttpGet) PlurkActionSheet.getInstance().getPlurks(
                    args.getMap());

            return new JSONObject(executor.execute(method));
        } catch (PlurkException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     *
     * @param args
     * @throws com.google.jplurk.exception.PlurkException
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws PlurkException, ClientProtocolException, IOException, InterruptedException {
//        ProxyProvider.setProvider("proxyhost", 8080);

//        ISettings config = new PlurkSettings();
//        PlurkClient pc = new PlurkClient(config);
//        JSONObject o = null;
//        System.out.println( pc.login(JOptionPane.showInputDialog("id"), JOptionPane.showInputDialog("password")) );
//        pc.plurkAdd("把忘了加的 multithread connection manager 加上去了", Qualifier.SAYS);
//        Timer timer = new Timer();
//        timer.schedule(pc.getUserChannel(), new Date(), 15000);

//        System.out.println(pc.plurkAdd("用 https 似乎真的慢很多啊 orz", Qualifier.SAYS, CommentBy.All));
//        System.out.println(pc.plurkAdd("今天睡到下午一點，真爽耶.", Qualifier.SAYS, CommentBy.All));
//        System.out.println(pc.plurkAdd("不知不覺就到下午四點多了", Qualifier.SAYS, CommentBy.None));
//        System.out.println(pc.plurkAdd("測試只有朋友能回應", Qualifier.SAYS, CommentBy.Friends));

//        JSONObject oRegister = pc.register(JOptionPane.showInputDialog("nick_name"),
//                JOptionPane.showInputDialog("full_name"),
//                JOptionPane.showInputDialog("password"),
//                JOptionPane.showInputDialog("gender"),
//                JOptionPane.showInputDialog("date_of_birth"));
//        System.out.println(oRegister);

//        System.out.println(pc.getPlurk("186562865"));
//        System.out.println(pc.responseGet("186562865"));

//        System.out.println(pc.getFansByOffset("3146394", 0));
//        System.out.println(pc.getFansByOffset("3146394", 10));
//        System.out.println(pc.getFansByOffset("3146394", 6666));
//        System.out.println(pc.getOwnProfile());
//        System.out.println(pc.getPublicProfile("3146394"));
//        System.out.println(pc.setFollowing(4932792, false));

//        System.out.println(pc.getPollingPlurks(null, 10));
//        System.out.println(pc.getPollingUnreadCount());
//        186562865 , 186616350  : fail
//        186567616 : ok
//        System.out.println(pc.uploadPicture(new File("C:/images/image.jpg")));
//        System.out.println(pc.uploadPicture("fooo", new FileInputStream(new File("C:/images/image.jpg"))));

//        System.out.println(pc.updatePicture(new File("C:/Users/qrtt1/Desktop/4932792-big2.jpg")));
//        System.out.println(pc.plurkAdd("hmmmm", Qualifier.SAYS));
//        System.out.println(pc.plurkAdd("測試 jPlurk 編輯 plruk 功能！！", Qualifier.IS, NoComments.False));

//        System.out.println(pc.getPlurk("183532425"));
//        System.out.println(pc.plurkEdit("183532425", "早上的 http://code.google.com/p/jplurk/ (jPlurk) 先這樣吧～ by jPlurk v2 plurkEdit"));
//        System.out.println(pc.plurkDelete("183525435"));
//        System.out.println(pc.getUnreadPlurks(DateTime.now(), 1));
//        System.out.println(pc.getUnreadPlurks(DateTime.now()));

//        System.out.println(pc.mutePlurks("183559649"));
//        System.out.println(pc.unmutePlurks("183559649"));

//        System.out.println(pc.getFavoritePlurks(null, 1, true));
//        System.out.println(pc.favoritePlurks("408253903"));
//        System.out.println(pc.getFavoritePlurks(null, 1, true));
//        System.out.println(pc.unfavoritePlurks("408253903"));

//        System.out.println(pc.responseAdd("183532425", "測試刪除回應", Qualifier.FEELS));
//{"posted":"Sat, 19 Dec 2009 07:57:07 GMT","user_id":3290989,"content_raw":"測試刪除回應","lang":"en","content":"測試刪除回應","qualifier":"feels","id":825994340,"plurk_id":183532425}
//        System.out.println(pc.responseDelete("183532425", "825994340"));

//        System.out.println(pc.searchPlurk("jPlurk"));
        /* TODO
         * askeing: I don't know how to use this API (searchUser)?
         * */
//        System.out.println(pc.searchUser("qrtt1"));

//        System.out.println(pc.getEmoticons());
//        System.out.println(pc.getBlocks());
//        System.out.println(pc.getBlocks(10));

//        System.out.println(pc.createClique("jPlurkTest"));
//        System.out.println(pc.getCliques());
//        System.out.println(pc.addToClique("jPlurkTest", "3290989"));
//        System.out.println(pc.getClique("jPlurkTest"));
//        System.out.println(pc.removeFromClique("jPlurkTest", "3290989"));
//        System.out.println(pc.getClique("jPlurkTest"));
//        System.out.println(pc.renameClique("jPlurkTest", "TestJavaPlurkAPI"));
//        System.out.println(pc.getCliques());
//        System.out.println(pc.deleteClique("TestJavaPlurkAPI"));
//        System.out.println(pc.getCliques());

//        Scanner scanner = new Scanner(System.in);
//        scanner.next();
    }
}
