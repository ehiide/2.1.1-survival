package mc.server.survival.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class TimeUtil 
{
	public static String hour() 
	{
		final Date now = new Date();
		final SimpleDateFormat format = new SimpleDateFormat("HH");
		
		return format.format(now) + "";
	}
	
	public static String minute() 
	{
		final Date now = new Date();
		final SimpleDateFormat format = new SimpleDateFormat("mm");
		
		return format.format(now) + "";
	}

	public static int getDifferenceInMinutes(String lastDate)
	{
		final String minutes = lastDate.substring(lastDate.length() - 5, lastDate.length() - 3);
		final int minute = Integer.parseInt(minutes);
		final String hours = lastDate.substring(lastDate.length() - 8, lastDate.length() - 6);
		final int hour = Integer.parseInt(hours);
		final String days = lastDate.substring(lastDate.length() - 11, lastDate.length() - 9);
		final int day = Integer.parseInt(days);
		final String months = lastDate.substring(lastDate.length() - 14, lastDate.length() - 12);
		final int month = Integer.parseInt(months);
		final String years = lastDate.substring(lastDate.length() - 19, lastDate.length() - 15);
		final int year = Integer.parseInt(years);
		final long time = Duration.between(LocalDateTime.of(year, month, day, hour, minute, 0), LocalDateTime.now()).toMinutes();
		final String str = Long.toString(time);

		return Integer.parseInt(str);
	}

	public static int getDifferenceInSeconds(String lastDate)
	{
		final String seconds = lastDate.substring(lastDate.length() - 2);
		final int second = Integer.parseInt(seconds);
		final String minutes = lastDate.substring(lastDate.length() - 5, lastDate.length() - 3);
		final int minute = Integer.parseInt(minutes);
		final String hours = lastDate.substring(lastDate.length() - 8, lastDate.length() - 6);
		final int hour = Integer.parseInt(hours);
		final String days = lastDate.substring(lastDate.length() - 11, lastDate.length() - 9);
		final int day = Integer.parseInt(days);
		final String months = lastDate.substring(lastDate.length() - 14, lastDate.length() - 12);
		final int month = Integer.parseInt(months);
		final String years = lastDate.substring(lastDate.length() - 19, lastDate.length() - 15);
		final int year = Integer.parseInt(years);
		final long time = Duration.between(LocalDateTime.of(year, month, day, hour, minute, second), LocalDateTime.now()).toMillis() / 1000;
		final String str = Long.toString(time);

		return Integer.parseInt(str);
	}
}