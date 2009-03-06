/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk.model;

import java.util.Calendar;
import java.util.TimeZone;

import tw.idv.askeing.jPlurk.Qualifier;

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

    private TimeZone zone = TimeZone.getTimeZone("GMT 0:00");
    private Calendar now = Calendar.getInstance( zone );
    private String posted = "";
    private String qualifier = "";
    private String content = "";
    private String lang = "tr_ch";
    private int noComments = 0;
    private int uid = 0;
    private String limitedTo = "";

    public MessageModel() {
        generatePosted();
    }

    public MessageModel(Qualifier qualifier, String content, int uid) {
    	super();
        this.setQualifier(qualifier);
        this.setContent(content);
        this.setUid(uid);
    }


    public void generatePosted () {
        posted = "\"" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH)
                    + "T" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + "\"";
    }

    public void setQualifier (Qualifier qualifier) {
		if (qualifier == null) {
			this.qualifier = Qualifier.NULL.toString();
		} else {
			this.qualifier = qualifier.toString();
		}
    }

    public void setContent(String content) {
		if (content.length() > 140) {
			this.content = content.substring(0, 136) + "...";
		} else {
			this.content = content;
		}
	}

    public void setLang (String lang) {
    	if("".equals(lang)){
    		this.lang = "tr_ch";
    	}else{
    		this.lang = lang;
    	}
    }

    public void setNoComments(int no_comments) {
		if (no_comments > 1 || no_comments < 0) {
			this.noComments = 0;
		} else {
			this.noComments = no_comments;
		}
	}

    public void setUid(int uid) {
		this.uid = uid;
	}

	public void setLimitedTo(String limited_to) {
		this.limitedTo = limited_to;
	}

	public String getPosted() {
		return this.posted;
	}

	public String getQualifier() {
		return qualifier;
	}

	public String getContent() {
		return this.content;
	}

	public String getLang() {
		return this.lang;
	}

	public int getNoComments() {
		return this.noComments;
	}

	public int getUid() {
		return this.uid;
	}

	public String getLimitedTo() {
		return this.limitedTo;
	}

	public boolean hasLimited_to() {
		if("".equals(limitedTo)){
			return false;
		}else{
			return true;
		}
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
