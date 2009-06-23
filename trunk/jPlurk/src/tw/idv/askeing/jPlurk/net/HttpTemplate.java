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

			logger.info("start to execute http template");
			int resp = 0;
			if(method instanceof PostMethod){
				logger.info("doPost:" + method);
				resp = httpClient.executeMethod((PostMethod) method);
			}

			if(method instanceof GetMethod){
				logger.info("doGet:" + method);
				resp = httpClient.executeMethod((GetMethod) method);
			}

			if(!checkAcceptStatus(acceptedRespCodes, resp)){
				logger.info("no accepted response code: " + resp);
				return null;
			}

			Object result = null;
			if(method instanceof PostMethod){
				logger.info("call processResult from doPost: " + callback);
				result = callback.processResult((PostMethod) method);
				logger.info("get result: " + result);
			}

			if(method instanceof GetMethod){
				logger.info("call processResult from doGet: " + callback);
				result = callback.processResult((GetMethod) method);
				logger.info("get result: " + result);
			}

			logger.info("finish fetch data from htttp");
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.warn("no more result can return(null)");
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
