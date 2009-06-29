package com.googlecode.jplurk.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public abstract class AbstractParserTest extends TestCase{

	public static String readSample(String path, String enc) throws IOException{
		InputStream in = AbstractParserTest.class.getResourceAsStream(path);
		byte[] buf = new byte[1024];
		int count = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((count = in.read(buf))!=-1) {
			out.write(buf, 0, count);
		}
		in.close();
		String result = new String(out.toByteArray(), (enc == null ? "utf-8" : enc));
		out.reset();
		return result;
	}

}
