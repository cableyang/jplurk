package com.googlecode.jplurk.parser;

public class UidParserTest extends AbstractParserTest {

	public void testParse() throws Exception {
		String text = readSample("UidParser.sample", null);
		assertNotNull(text);
		assertEquals("3146394", ParserExecutor.parseSingleValue(UidParser.class, text));
	}

}
