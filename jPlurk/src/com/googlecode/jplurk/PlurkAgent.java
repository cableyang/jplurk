package com.googlecode.jplurk;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import tw.idv.askeing.jPlurk.model.Account;
import tw.idv.askeing.jPlurk.model.Message;
import tw.idv.askeing.jPlurk.model.Qualifier;
import tw.idv.askeing.jPlurk.model.ResponseMessage;
import tw.idv.askeing.jPlurk.util.JsonUtil;

import com.googlecode.jplurk.behavior.AddPlurk;
import com.googlecode.jplurk.behavior.GetNotifications;
import com.googlecode.jplurk.behavior.GetUnreadPlurks;
import com.googlecode.jplurk.behavior.IBehavior;
import com.googlecode.jplurk.behavior.Login;
import com.googlecode.jplurk.behavior.ResponsePlurk;
import com.googlecode.jplurk.exception.LoginFailureException;
import com.googlecode.jplurk.exception.NotLoginException;
import com.googlecode.jplurk.exception.RequestFailureException;
import com.googlecode.jplurk.net.Result;

/**
 * PlurkAgent is a facade that assemble many plurk's behavior in one class.
 * @author Ching Yi, Chan
 */
public class PlurkAgent implements IPlurkAgent {

	Account account;
	PlurkTemplate plurkTemplate;
	boolean isLogin;

	public PlurkAgent(Account account) {
		this.account = account;
		this.plurkTemplate = new PlurkTemplate(account);
	}

	/**
	 * @see com.googlecode.jplurk.IPlurkAgent#login()
	 */
	public Result login() throws LoginFailureException {
		Result result = plurkTemplate.doAction(Login.class, account);
		if (!result.isOk()) {
			throw new LoginFailureException(account);
		}
		isLogin = true;
		return result;
	}

	/**
	 * execute is a standard process to check login status and execute behavior.
	 * if the agent get a login failure in previous login method call, it raise NotLoginException.
	 * if the agent get the result is not ok, it raise the RequestFailureException.
	 * @param clazz
	 * @param args
	 * @return
	 */
	protected Result execute(Class<? extends IBehavior> clazz, Object args){
		if(!isLogin) {
			throw new NotLoginException();
		}

		Result result = plurkTemplate.doAction(clazz, args);

		if(!result.isOk()){
			throw new RequestFailureException(result);
		}
		return result;
	}

	/**
	 * @see com.googlecode.jplurk.IPlurkAgent#addPlurk(tw.idv.askeing.jPlurk.model.Qualifier, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Result addPlurk(Qualifier qualifier, String text){
		Message message = new Message();
		message.setQualifier(qualifier);
		message.setContent(text);
		Result result = execute(AddPlurk.class, message);

		// parse json response and attach the plurk_id and owner_id
		JSONObject plurkObject = (JSONObject) JsonUtil.parse(result.getResponseBody()).get("plurk");
		result.getAttachement().putAll(JsonUtil.get(plurkObject, "plurk_id", "owner_id"));
		return result;
	}

	public Result getNotifications(){
		// FIXME not completed implementation, need to parse the result and add to attachement.
		return execute(GetNotifications.class, null);
	}

	/**
	 * @see com.googlecode.jplurk.IPlurkAgent#responsePlurk(tw.idv.askeing.jPlurk.model.Qualifier, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Result responsePlurk(Qualifier qualifier, String plurkId, String plurkOwnerId, String text){
		ResponseMessage message = new ResponseMessage();
		message.setQualifier(qualifier);
		message.setContent(text);
		message.setPlurkId(plurkId);
		message.setPlurkOwnerId(plurkOwnerId);
		return execute(ResponsePlurk.class, message);
	}

	/**
	 * @see com.googlecode.jplurk.IPlurkAgent#addLongPlurk(tw.idv.askeing.jPlurk.model.Qualifier, java.lang.String)
	 */
	public Result addLongPlurk(Qualifier qualifier, String longText){
		List<String> texts = new ArrayList<String>();
		StringBuffer buf = new StringBuffer(longText);
		while(buf.length()>0){
			if(buf.length() <= 135){
				texts.add(buf.toString());
				buf.setLength(0);
				continue;
			}

			texts.add(buf.substring(0, 135));
			buf.delete(0, 135);
		}

		String first = texts.remove(0);
		Result result = addPlurk(qualifier, first);
		String plurkId =  (String) result.getAttachement().get("plurk_id");
		String plurkOwnerId = (String) result.getAttachement().get("owner_id");
		for (String t : texts) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ignored) {
			}
			responsePlurk(qualifier, plurkId, plurkOwnerId, t);
		}

		return result;
	}

	/**
	 * @see com.googlecode.jplurk.IPlurkAgent#getUnreadPlurks()
	 */
	@SuppressWarnings("unchecked")
	public Result getUnreadPlurks(){
		Result result = execute(GetUnreadPlurks.class, null);
		for (Object each : JsonUtil.parseArray(result.getResponseBody())) {
			if (each instanceof JSONObject) {
				JSONObject o = (JSONObject) each;
				result.getAttachement().put("" + o.get("plurk_id"), o);
			}
		}
		return result;
	}


	public static void main(String[] args) {
		Account account = Account.createWithDynamicProperties();
		IPlurkAgent pa = new PlurkAgent(account);
		Result r =pa.login();
		r = pa.getUnreadPlurks();
		System.out.println(r.getAttachement());
		System.out.println(r.getAttachement().size());

		pa.addLongPlurk(Qualifier.FREESTYLE, "漢皇重色思傾國，御宇多年求不得。楊家有女初長成，養在深閏人未識。天生麗質難自棄，一朝選在君王側。回眸一笑百媚生，六宮粉黛無顏色。春寒賜浴華清池，溫泉水滑洗凝脂；待兒扶起嬌無力，始是新承恩澤時。雲鬢花顏金步搖，芙蓉帳暖度春宵；春宵苦短日高起，從此君王不早朝。承歡侍宴無閑暇，春從春遊夜專夜。後宮佳麗三千人，三千寵愛在一身。金屋妝成嬌侍夜，玉樓宴罷醉和春。姊妹弟兄皆列士，可憐光彩生門戶。遂令天下父母心，不重生男重生女。驪宮高處入青雲，仙樂風飄處處聞。緩歌慢舞凝絲竹，盡日君王看不足。漁陽鼙鼓動地來，驚破霓裳羽衣曲。九重城闕煙塵生，千乘萬騎西南行。翠華搖搖行復止，西出都門百餘里；六軍不發無奈何？宛轉蛾眉馬前死。花鈿委地無人收，翠翹金雀玉搔頭。君王掩面救不得，回看血淚相和流。黃埃散漫風蕭素，雲棧縈紆登劍閣。峨嵋山下少人行，旌旗無光日色薄。蜀江水碧蜀山青，聖主朝朝暮暮情。行宮見月傷心色，夜雨聞鈴腸斷聲。天旋地轉迥龍馭，到此躊躇不能去。馬嵬坡下泥土中，不見玉顏空死處。君臣相顧盡霑衣，東望都門信馬歸。歸來池苑皆依舊，太液芙蓉未央柳；芙蓉如面柳如眉，對此如何不淚垂？春風桃李花開日，秋雨梧桐葉落時。西宮南內多秋草，落葉滿階紅不掃。梨園子弟白髮新，椒房阿監青娥老。夕殿螢飛思悄然，孤燈挑盡未成眠。遲遲鐘鼓初長夜，耿耿星河欲曙天。鴛鴦瓦冷霜華重，翡翠衾寒誰與共？悠悠生死別經年，魂魄不曾來入夢。臨邛道士鴻都客，能以精誠致魂魄；為感君王輾轉思，遂教方士殷勤覓。排空馭氣奔如電，升天入地求之遍；上窮碧落下黃泉，兩處茫茫皆不見。忽聞海上有仙山，山在虛無縹緲間。樓閣玲瓏五雲起，其中綽約多仙子。中有一人字太真，雪膚花貌參差是。金闕西廂叩玉扃，轉教小玉報雙成。聞道漢家天子使，九華帳裡夢魂驚；攬衣推枕起徘徊，珠箔銀屏迤邐開。雲鬢半偏新睡覺，花冠不整下堂來。風吹仙袂飄飄舉，猶似霓裳羽衣舞。玉容寂寞淚闌干，梨花一枝春帶雨。含情凝睇謝君王，一別音容兩渺茫。昭陽殿裡恩愛絕，蓬萊宮中日月長。回頭下望人寰處，不見長安見塵霧。唯將舊物表深情，鈿合金釵寄將去。釵留一股合一扇，釵擘黃金合分鈿。但教心似金鈿堅，天上人間會相見。臨別殷勤重寄詞，詞中有誓兩心知，七月七日長生殿，夜半無人私語時：「在天願作比翼鳥，在地願為連理枝。」天長地久有時盡，此恨綿綿無絕期。");

		// pid 71669284, own 3131562
//		pa.responsePlurk(Qualifier.FEELS, "71669284", "3131562", "是朋友就會有分寸啊!!!");
	}
}
