package tw.idv.askeing.jPlurk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IterableInputStreamWrapper implements Iterable<String> {

	static Log logger = LogFactory.getLog(IterableInputStreamWrapper.class);

	BufferedReader reader;

	public IterableInputStreamWrapper(InputStream inputStream)
			throws UnsupportedEncodingException {
		this(inputStream, "utf-8");
	}

	public IterableInputStreamWrapper(InputStream inputStream, String encoding)
			throws UnsupportedEncodingException {
		reader = new BufferedReader(
				new InputStreamReader(inputStream, encoding));
	}

	//@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {

			Queue<String> queue = new LinkedList<String>();

			//@Override
			public boolean hasNext() {
				try {
					String s = reader.readLine();

					if (s != null) {
						queue.add(s);
					}

				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					return false;
				}

				return !queue.isEmpty();
			}

			//@Override
			public String next() {
				if(hasNext()){
					return queue.remove();
				}
				return null;
			}

			//@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"Cannot remove element because of no-support.");
			}
		};
	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
				reader = null;
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clone();
	}

}
