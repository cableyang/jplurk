/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * jPlurk MessageModel: The model to record  Message data structure, and Qualifier constant field.
 * @author Askeing, Yen.
 * @version 1.0
 */
public class MessageModel {
    /*
     * posted			( 時間 URLEncode ) "2009-3-1T11:04:09"
     * qualifier		( 語氣 URLEncode ) 
     * 		:
     * 		wants		想要
     * 		needs		需要
     * 		says		說
     * 		asks		問
     * 		wonders		好奇
     * 		freestyle	自由發揮
     * 		is		正在
     * 		shares		推
     * 		will		打算
     * 		feels		覺得
     * 		wishes		希望
     * 		likes		喜歡
     * 		loves		愛
     * 		hopes		希望
     * 		has		已經
     * 		was		曾經
     * 		thinks		想
     * 		hates		討厭
     * 		gives		給
     * content			( 內容 URLEncode )
     * lang			( tr_ch )
     * no_comments		( 回應 ) 0 可回應 1 禁止回應
     * uid			( UID )
     * limited_to		( 限制 URLEncode ) 無 , "only-friends" , [3290989,"301630","645543"]
     * 
     * java.net.URLEncode.encode( String );
     * */
    
    /**
     * ":" 無. 同 "QUALIFIER_FREESTYLE".
     */
    public static final String QUALIFIER_NULL = ":";
    /**
     * "wants" 想要
     */
    public static final String QUALIFIER_WANTS = "wants";
    /**
     * "needs" 需要
     */
    public static final String QUALIFIER_NEEDS = "needs";
    /**
     * "says" 說
     */
    public static final String QUALIFIER_SAYS = "says";
    /**
     * "asks" 問
     */
    public static final String QUALIFIER_ASKS = "asks";
    /** 
     * "wonders" 好奇
     */
    public static final String QUALIFIER_WONDERS = "wonders";
    /**
     * "freestyle" 自由發揮
     */
    public static final String QUALIFIER_FREESTYLE = "freestyle";
    /**
     * "is" 正在
     */
    public static final String QUALIFIER_IS = "is";
    /**
     * "shares" 推
     */
    public static final String QUALIFIER_SHARES = "shares";
    /**
     * "will" 打算
     */
    public static final String QUALIFIER_WILL = "will";
    /**
     * "feels" 覺得
     */
    public static final String QUALIFIER_FEELS = "feels";
    /**
     * "wishes" 希望
     */
    public static final String QUALIFIER_WISHES = "wishes";
    /**
     * "likes" 喜歡
     */
    public static final String QUALIFIER_LIKES = "likes";
    /**
     * "loves" 愛
     */
    public static final String QUALIFIER_LOVES = "loves";
    /**
     * "hopes" 希望
     */
    public static final String QUALIFIER_HOPES = "hopes";
    /**
     * "has" 已經
     */
    public static final String QUALIFIER_HAS = "has";
    /**
     * "was" 曾經
     */
    public static final String QUALIFIER_WAS = "was";
    /**
     * "thinks" 想
     */
    public static final String QUALIFIER_THINKS = "thinks";
    /**
     * "hates" 討厭
     */
    public static final String QUALIFIER_HATES = "hates";
    /**
     * "gives" 給
     */
    public static final String QUALIFIER_GIVES = "gives";
    
    private TimeZone zone = TimeZone.getTimeZone("GMT 0:00");
    private Calendar now = Calendar.getInstance( zone );
    //private Date postedDate = new Date();
    
    private String posted = "";
    private String qualifier = "";
    private String content = "";
    private String lang = "tr_ch";
    private int no_comments = 0;
    private int uid = 0;
    private String limited_to = "";
    
    public MessageModel() {
        this.setPosted();
    }
    public MessageModel(String qualifier, String content, int uid) {
        this.setPosted();
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setUid(uid);
    }
    public MessageModel(String qualifier, String content, int no_comments, int uid) {
        this.setPosted();
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setNo_comments(no_comments);
        this.setUid(uid);
    }
    public MessageModel(String qualifier, String content, String lang, int no_comments, int uid) {
        this.setPosted();
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setLang(lang);
        this.setNo_comments(no_comments);
        this.setUid(uid);
    }
    public MessageModel(Date postedDate, String qualifier, String content, String lang, int no_comments, int uid) {
        this.setPosted(postedDate);
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setLang(lang);
        this.setNo_comments(no_comments);
        this.setUid(uid);
    }
    public MessageModel(Date postedDate, String qualifier, String content, String lang, int no_comments, int uid, String limited_to) {
        this.setPosted(postedDate);
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setLang(lang);
        this.setNo_comments(no_comments);
        this.setUid(uid);
        this.setLimited_to(limited_to);
    }
    
    public void setPosted () {
        posted = "\"" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH) 
                    + "T" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "\"";
        this.setPosted(posted);
    }
    public void setPosted (Date postedDate) {
        if(postedDate != null)
            this.now.setTime(postedDate);
        posted = "\"" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH) 
                    + "T" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "\"";
        this.setPosted(posted);
    }
    public void setPosted (String posted) {
        this.posted = posted;
    }
    public void setQualifier (String qualifier) {
        if( qualifier.equals(":") || qualifier.equals("wants") || qualifier.equals("needs") || qualifier.equals("says") 
                || qualifier.equals("asks") || qualifier.equals("wonders") || qualifier.equals("freestyle") || qualifier.equals("is") 
                || qualifier.equals("shares") || qualifier.equals("will") || qualifier.equals("feels") || qualifier.equals("wishes") 
                || qualifier.equals("likes") || qualifier.equals("loves") || qualifier.equals("hopes") || qualifier.equals("has") 
                || qualifier.equals("was") || qualifier.equals("thinks") || qualifier.equals("hates") || qualifier.equals("gives") )
            this.qualifier = qualifier;
        else
            this.qualifier = ":";
    }
    public void setContent (String content) {
        if(content.length() > 140)
            this.content = content.substring(0, 136)+"...";
        else
            this.content = content;
    }
    public void setLang (String lang) {
        if( lang.equals("") )
            this.lang = "tr_ch";
        else
            this.lang = lang;
    }
    public void setNo_comments (int no_comments) {
        if( no_comments > 1 || no_comments < 0 )
            this.no_comments = 0;
        else
            this.no_comments = no_comments;
    }
    public void setUid (int uid) {
        this.uid = uid;
    }
    public void setLimited_to (String limited_to) {
        this.limited_to = limited_to;
    }
    
    public String getPosted () {
        return this.posted;
    }
    public String getQualifier () {
        return this.qualifier;
    }
    public String getContent () {
        return this.content;
    }
    public String getLang () {
        return this.lang;
    }
    public int getNo_comments () {
        return this.no_comments;
    }
    public int getUid () {
        return this.uid;
    }
    public String getLimited_to () {
        return this.limited_to;
    }
    public boolean hasLimited_to () {
        if(this.limited_to.equals(""))
            return false;
        else
            return true;
    }
    /**
     * Test Case
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MessageModel msg = new MessageModel();
        
        System.out.println("\n===== Test =====\n");
        System.out.println("posted: "+ msg.getPosted() );
    }
}
