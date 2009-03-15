package tw.idv.askeing.jPlurk.net;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpTemplate {

	static Log logger = LogFactory.getLog(HttpTemplate.class);
	Object method;

	public HttpTemplate(PostMethod method) {
		this.method = method;
	}

	public HttpTemplate(GetMethod method) {
		this.method = method;
	}

	/**
	 * 如果需要不同的 HttpClient 可以覆寫這個方法
	 * @return
	 */
	protected HttpClient createHttpClient(){
		return HttpUtil.createDefaultHttpClient();
	}

	public Object execute(int[] acceptedRespCodes, HttpResultCallback callback){
		HttpClient httpClient = createHttpClient();
		try {

			int resp = 0;
			if(method instanceof PostMethod){
				resp = httpClient.executeMethod((PostMethod) method);
			}

			if(method instanceof GetMethod){
				resp = httpClient.executeMethod((GetMethod) method);
			}

			if(!checkAcceptStatus(acceptedRespCodes, resp)){
				// no accepted response code
				return null;
			}

			Object result = null;
			if(method instanceof PostMethod){
				result = callback.processResult((PostMethod) method);
			}

			if(method instanceof GetMethod){
				result = callback.processResult((GetMethod) method);
			}

			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	boolean checkAcceptStatus(int[] acceptedRespCodes, int resp) {
		for (int accepted : acceptedRespCodes) {
			if(resp == accepted){
				return true;
			}
		}
		return false;
	}

}
