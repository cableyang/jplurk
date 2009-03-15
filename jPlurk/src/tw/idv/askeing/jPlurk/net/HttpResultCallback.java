package tw.idv.askeing.jPlurk.net;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import tw.idv.askeing.jPlurk.util.IterableInputStreamWrapper;

public abstract class HttpResultCallback {

	IterableInputStreamWrapper iterable;

	protected Object processResult(PostMethod method) {
		return null;
	}

	protected Object processResult(GetMethod method) {
		return null;
	}

	final protected Iterator<String> getIterator(InputStream in, String encoding)
			throws UnsupportedEncodingException {
		iterable = new IterableInputStreamWrapper(in, encoding);
		return iterable.iterator();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(iterable != null){
			iterable.close();
			iterable = null;
		}
	}
}
