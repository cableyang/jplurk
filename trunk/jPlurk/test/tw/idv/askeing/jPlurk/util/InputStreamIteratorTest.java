package tw.idv.askeing.jPlurk.util;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputStreamIteratorTest {

	InputStream inputStream;

	@Before
	public void setUp() throws Exception {
		inputStream = InputStreamIteratorTest.class
				.getResourceAsStream("testInput");
	}

	@After
	public void tearDown() throws Exception {
		if (inputStream != null) {
			inputStream.close();
		}
	}

	@Test
	public void testIterator() throws Exception {
		Iterator<String> it = new IterableInputStreamWrapper(inputStream).iterator();
		String[] expected = new String[]{"aaa", "bbb", "ccc"};

		for (String s : expected) {
			assertEquals(s, it.next());
		}

		assertNull(it.next());
	}
}
