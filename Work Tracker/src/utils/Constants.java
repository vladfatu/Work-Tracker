package utils;

import java.text.SimpleDateFormat;

public class Constants {
	
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public static SimpleDateFormat dateFormatterDMY = new SimpleDateFormat("dd.MM.yyyy");
	public static SimpleDateFormat dateFormatterHHMM = new SimpleDateFormat("HH:mm");
	
	public static String JOB_ID_PREF = "com.android.tracker.JOB_ID_PREF";
	public static String PUNCH_IN_PREF = "com.android.tracker.PUNCH_IN_PREF";
	public static String DESCRIPTION = "com.android.traker.DESCRIPTION";
	public static String REPORTS_ADVANCED = "com.android.tracker.REPORTS_ADVANCED";
	public static String RECORDS_ADVANCED = "com.android.tracker.RECORDS_ADVANCED";
	public static String CURRENT_PERIOD_PREF = "com.android.tracker.CURRENT_PERIOD_PREF";
	public static String JOB_ID_PREF_ENTRIES = "com.android.tracker.JOB_ID_PREF_ENTRIES";
	public static String JOB_ID_PREF_REPORTS = "com.android.tracker.JOB_ID_PREF_REPORTS";
	public static String TYPE_REPORTS = "com.android.tracker.TYPE_REPORTS";
	public static String WORK_STARTED = "com.android.tracker.WORK_STARTED";
}
