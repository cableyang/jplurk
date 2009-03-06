package tw.idv.askeing.jPlurk.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

public class MessageModelTest extends TestCase{

	MessageModel mesg;
	@Override
	protected void setUp() throws Exception {
		mesg = new MessageModel();
	}

	public void test_() throws Exception {
		TimeZone zone = TimeZone.getTimeZone("GMT 0:00");
	    Calendar now = Calendar.getInstance(zone);

		mesg.generatePosted();
		System.out.println(mesg.getPosted());

		SimpleDateFormat year = new SimpleDateFormat("yyyy-M-d");
		SimpleDateFormat time = new SimpleDateFormat("H:k:m");

		Date current = now.getTime();
		System.out.println(now.getTimeZone());
		System.out.println(year.format(current) + "T" + time.format(current));
	}

}
