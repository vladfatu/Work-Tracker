package com.android.tracker.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author vlad
 *
 */
public class DatabaseController implements DatabaseControllerInterface {

	private static final String DATABASE_NAME = "worktracker";
	private static final int DATABASE_VERSION = 1;
	public static final String KEY_ROWID = "_id";
	
	private static final String JOBS_TABLE = "jobs";
	private static final String JOB_NAME = "jobname";
	private static final String JOB_PRICE_PER_HOUR = "jobpph";
	private static final String JOBS_TABLE_CREATE = "create table " + JOBS_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ JOB_NAME + " text not null, "
			+ JOB_PRICE_PER_HOUR + " integer not null);";

	private static final String RECORDS_TABLE = "records";
	private static final String JOBS_ID = "jobsid";
	private static final String RECORD_DATE = "recorddate";
	private static final String RECORD_TYPE = "recordtype";
	private static final String RECORDS_TABLE_CREATE = "create table " + RECORDS_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ JOBS_ID + " integer not null, "
			+ RECORD_TYPE + " integer not null, "
			+ RECORD_DATE + " text not null);";
	
	private static final String VACATION_DAYS_TABLE = "vacationdays";
	private static final String VACATION_DAY__DATE = "vacationdaydate";
	private static final String VACATION_DAYS_TABLE_CREATE = "create table " + VACATION_DAYS_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ JOBS_ID + " integer not null, "
			+ VACATION_DAY__DATE + " text not null);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	public DatabaseController(Context ctx) {
		this.context = ctx;
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(JOBS_TABLE_CREATE);
		tables.add(RECORDS_TABLE_CREATE);
		tables.add(VACATION_DAYS_TABLE_CREATE);
		DBHelper = new DatabaseHelper(context, DATABASE_NAME, DATABASE_VERSION, tables);
	}

	// ---opens the database---
	private DatabaseController open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this; 
	}

	// ---closes the database---
	private void close()
	{
		DBHelper.close();
	}

	public void addRecord(Record record)
	{
		open();
 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(RECORD_DATE, dateFormat.format(record.getDate()));
		tempValues.put(RECORD_TYPE, record.getType());
		record.setId(db.insert(RECORDS_TABLE, null, tempValues));
		close();

	}

	public void updateRecord(Record record)
	{
		// TODO Auto-generated method stub
		
	}

	public void addRecords(ArrayList<Record> records)
	{
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Record> getAllRecords()
	{
		open();
		Cursor cursor = db.query(RECORDS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		RECORD_DATE,
        		RECORD_TYPE}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		ArrayList<Record> records = new ArrayList<Record>();
		
		if (cursor.moveToFirst())
	    {
	        do {       
	        	Record record = new Record();
	        	try
				{
					record.setId(cursor.getLong(0));
					// tabelul contine doar id-urile job-urilor; pentru a crea un job am adaugat functia privata
					// "getJobForId"
					record.setJob(getJobForId(cursor.getLong(1)));
					record.setDate(dateFormat.parse(cursor.getString(2)));
					record.setType(cursor.getInt(3));
					records.add(record);
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        } while (cursor.moveToNext());
	    }
		
		close();
		
		return records;
	}

	public ArrayList<Record> getRecords(Date startDate, Date endDate, Job job)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteRecords(Date startDate, Date endDate, Job job)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteRecords(ArrayList<Record> records)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteRecord(Record record)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteAllRecords()
	{
		// TODO Auto-generated method stub
		
	}

	public void addJob(Job job)
	{
		open();
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOB_NAME, job.getName());
		tempValues.put(JOB_PRICE_PER_HOUR, job.getPricePerHour());
		job.setId(db.insert(JOBS_TABLE, null, tempValues));
		close();
		
	}

	public void updateJob(Job job)
	{
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Job> getJobs()
	{
		open();
		Cursor cursor = db.query(JOBS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOB_NAME,
        		JOB_PRICE_PER_HOUR}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		ArrayList<Job> jobs = new ArrayList<Job>();
		
		if (cursor.moveToFirst())
	    {
	        do {       
	        	Job job = new Job();
				job.setId(cursor.getLong(0));
				job.setName(cursor.getString(1));
		        job.setPricePerHour(cursor.getInt(2));		
		        jobs.add(job);
	        } while (cursor.moveToNext());
	    }
		
		close();
		return jobs;
	}
	
	private Job getJobForId(long id)
	{
		return null;
	}

	public void deleteJob(Job job)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteJobs(ArrayList<Job> jobs)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteAllJobs()
	{
		// TODO Auto-generated method stub
		
	}

	public void addVacationDay(VacationDay day)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void updateVacationDay(VacationDay day)
	{
		// TODO Auto-generated method stub
		
	}

	public ArrayList<VacationDay> getVacationDaysForJob(Job job)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<VacationDay> getVacationDaysForDates(Date startDate, Date endDate)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<VacationDay> getVacationDaysForJobAndDates(Job job, Date startDate, Date endDate)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteVacationDay(VacationDay day)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteVacationDays(ArrayList<VacationDay> days)
	{
		// TODO Auto-generated method stub
		
	}

	public void deleteAllVacationDays()
	{
		// TODO Auto-generated method stub
		
	}

}
