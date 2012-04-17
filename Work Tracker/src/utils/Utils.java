package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.tracker.R;
import com.android.tracker.database.Entry;
import com.android.tracker.database.Record;

public class Utils {
	
	public static AlertDialog getAlertDialog(Context context, int titleId, int iconId, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
			}
		});
		if (iconId > 0)
		{
			builder.setIcon(iconId);
		}
		return builder.create();
	}
	
	

	public static AlertDialog getAlertDialog(Context context, int titleId, int iconId, int messageId)
	{
		return getAlertDialog(context, titleId, iconId, context.getString(messageId));
	}
	
	public static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static SharedPreferences getSharedPreferences(Context context, String preferenceFileName)
	{
		return context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
	}

	public static String getStringFromPrefs(Context context, String key, String defaultValue)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		return localPrefs.getString(key, defaultValue);
	}
	
	public static Integer getIntFromPrefs(Context context, String key, int defaultValue)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		return localPrefs.getInt(key, defaultValue);
	}
	
	public static long getLongFromPrefs(Context context, String key, long defaultValue)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		return localPrefs.getLong(key, defaultValue);
	}

	public static boolean getBooleanFromPrefs(Context context, String key, boolean defaultValue)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		return localPrefs.getBoolean(key, defaultValue);
	}

	public static void setStringToPrefs(Context context, String key, String value)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		localPrefs.edit().putString(key, value).commit();
	}

	public static void setLongToPrefs(Context context, String key, long value)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		localPrefs.edit().putLong(key, value).commit();
	}
	
	public static void setIntToPrefs(Context context, String key, int value)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		localPrefs.edit().putInt(key, value).commit();
	}

	public static void setBooleanToPrefs(Context context, String key, boolean value)
	{
		SharedPreferences localPrefs = getSharedPreferences(context);
		localPrefs.edit().putBoolean(key, value).commit();
	}
	
	public static Date todayFirstHour()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.clear();
		calendar.set(Calendar.DAY_OF_YEAR, day);
		calendar.set(Calendar.YEAR, year);

		// Now get the first day of week.
		Date date = calendar.getTime();
		return date;
	}
	
	public static Date firstDayOfThisWeek()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.clear();
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.YEAR, year);

		// Now get the first day of week.
		Date date = calendar.getTime();
		return date;
	}
	
	public static Date lastDayOfThisWeek()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.clear();
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.YEAR, year);
		calendar.add(Calendar.DAY_OF_YEAR, 7);

		// Now get the first day of week.
		Date date = calendar.getTime();
		return date;
	}
	
	public static Date firstDayOfThisMonth()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

		// Now get the first day of week.
		Date date = calendar.getTime();
		return date;
	}
	
	public static Date lastDayOfThisMonth()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, 1);

		// Now get the first day of week.
		Date date = calendar.getTime();
		return date;
	}
	
	public static ArrayList<Entry> getEntriesFromRecords(ArrayList<Record> records)
	{
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry;
		for (int i=0;i<records.size();i++)
		{
			if (i+1 < records.size())
			{
				entry = new Entry(records.get(i), records.get(i+1));
				i++;
			}
			else
			{
				entry = new Entry();
				entry.setPunchInRecord(records.get(i));
			}
			entries.add(entry);
		}
		return entries;
	}
	
	public static Calendar getHoursWorkedFromEntries(ArrayList<Entry> entries, Boolean useNowAsLastPunchOut)
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		for(Entry entry : entries)
		{
			if (entry.getPunchOutRecord() != null) c.add(Calendar.MILLISECOND, (int) (entry.getPunchOutRecord().getDate().getTime() - entry.getPunchInRecord().getDate().getTime()));
			else if (useNowAsLastPunchOut)
			{
				c.add(Calendar.MILLISECOND, (int) (Calendar.getInstance().getTime().getTime() - entry.getPunchInRecord().getDate().getTime()));
			}
		}
		//return Constants.dateFormatterHHMM.format(c.getTime());
		return c;
	}
	
	public static Boolean samePeriod(Date date1, Date date2, int period)
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
			return false;
		if (period == 0 && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR))
			return true;
		else if (period == 1 && c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR))
			return true;
		else if (period == 2 && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
			return true;
		return false;
	}

}
