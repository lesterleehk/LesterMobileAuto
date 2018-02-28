package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import AppiumSupport.Config;


public class DateTimeUtils {
	public static String getCurrentTimeAsStr() {
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat(Config.getInstance().getProperty(
				"time.format")).format(cal.getTime());
	}

	public static Calendar strToCalendarDate(String dateStr) {
		Calendar cal = Calendar.getInstance();
		try {
			String dateFormat = Config.getInstance().getProperty("date.format");
			Date date = new SimpleDateFormat(dateFormat).parse(dateStr);
			cal.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cal;
	}

	public static int getMonthAsInt(String dateString) {
		Calendar cal = DateTimeUtils.strToCalendarDate(dateString);

		return cal.get(Calendar.MONTH) + 1; // the months are numbered
											// from 0 (January) to
											// 11 (December).
	}

	public static int getYearAsInt(String dateString) {
		Calendar cal = DateTimeUtils.strToCalendarDate(dateString);

		return cal.get(Calendar.YEAR);
	}

	public static int getDayAsInt(String dateString) {
		Calendar cal = DateTimeUtils.strToCalendarDate(dateString);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static String calendarDateToString(Calendar date) {
		String dateStr = new SimpleDateFormat(Config.getInstance().getProperty(
				"date.format")).format(date);
		return dateStr;
	}

	public static String dateToString(Date date) {
		String dateStr = new SimpleDateFormat(Config.getInstance().getProperty(
				"date.format")).format(date);
		return dateStr;
	}

	public static String getTimeStamp() {
		Date now = new Date();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
				.format(now);
		return timeStamp;
	}
}
