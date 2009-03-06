package tw.idv.askeing.jPlurk.net;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public abstract class HttpResultCallback {
	protected Object processResult(PostMethod method){return null;}
	protected Object processResult(GetMethod method){return null;}
}
