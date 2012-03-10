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
		Boolean opened = true;
		if(!db.isOpen()) 
		{
			open(); 
			opened = false;
		}
		
 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(RECORD_DATE, dateFormat.format(record.getDate()));
		tempValues.put(RECORD_TYPE, record.getType());
		record.setId(db.insert(RECORDS_TABLE, null, tempValues));
		
		if(!opened) close();

	}

	public void updateRecord(Record record)
	{
		open();
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(RECORD_DATE, dateFormat.format(record.getDate()));
		tempValues.put(RECORD_TYPE, record.getType());
		db.update(RECORDS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(record.getId())});
		
		close();
	}

	public void addRecords(ArrayList<Record> records)
	{
		open();
		
		for(int i=0; i<records.size(); i++)
			addRecord(records.get(i));
		close();
		
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
					e.printStackTrace();
				}

	        } while (cursor.moveToNext());
	    }
		
		close();
		
		return records;
	}

	public ArrayList<Record> getRecords(Date startDate, Date endDate, Job job)
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
	        	Date recordDate;
				try
				{
					recordDate = dateFormat.parse(cursor.getString(2));
					if (recordDate.after(startDate) && recordDate.before(endDate))
					{
						if (job == null || (job != null && cursor.getInt(1) == job.getId()))
						{
							Record record = new Record();
							try
							{
								record.setId(cursor.getLong(0));
								record.setJob(getJobForId(cursor.getLong(1)));
								record.setDate(dateFormat.parse(cursor.getString(2)));
								record.setType(cursor.getInt(3));
								records.add(record);
							} catch (ParseException e)
							{

								e.printStackTrace();
							}
						}
					}
				} catch (ParseException e1)
				{
					e1.printStackTrace();
				}
				

	        } while (cursor.moveToNext());
	    }
		
		close();
		
		return records;
		
	}

	public void deleteRecords(Date startDate, Date endDate, Job job)
	{
		open();
		deleteRecords(getRecords(startDate, endDate, job));
		close();
	}

	public void deleteRecords(ArrayList<Record> records)
	{
		open();
		
		for(int i=0; i<records.size(); i++)
			deleteRecord(records.get(i));
		close();
		
	}

	public void deleteRecord(Record record)
	{
		Boolean opened = true;
		if(!db.isOpen()) 
		{
			open(); 
			opened = false;
		}
		
		db.delete(RECORDS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(record.getId())});
		if(!opened) close();
	}

	public void deleteAllRecords()
	{
		open();
		deleteRecords(getAllRecords());
		close();
	}

	public void addJob(Job job)
	{
		Boolean opened = true;
		if(db == null || !db.isOpen()) 
		{
			open(); 
			opened = false;
		}
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOB_NAME, job.getName());
		tempValues.put(JOB_PRICE_PER_HOUR, job.getPricePerHour());
		job.setId(db.insert(JOBS_TABLE, null, tempValues));
		if(!opened) close();
		
	}

	public void updateJob(Job job)
	{
		open();
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOB_NAME, job.getName());
		tempValues.put(JOB_PRICE_PER_HOUR, job.getPricePerHour());
		db.update(JOBS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(job.getId())});
		
		close();
		
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
		Job job = new Job();
		open();
		Cursor cursor = db.query(JOBS_TABLE, new String[] {
		        KEY_ROWID, 
		        JOB_NAME,
		        JOB_PRICE_PER_HOUR}, 
		        KEY_ROWID+"=?", 
		        new String[] {
		Long.toString(id)},
		                null, 
		                null, 
		                null);

		if (cursor.moveToFirst())
		    {      
		        job.setId(cursor.getLong(0));
		        job.setName(cursor.getString(1));
		        job.setPricePerHour(cursor.getInt(2));
		    }

		close();
		return job;
	}

	public void deleteJob(Job job)
	{
		Boolean opened = true;
		if(!db.isOpen()) 
		{
			open(); 
			opened = false;
		}
		
		db.delete(JOBS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(job.getId())});
		if(!opened) close();
	}

	public void deleteJobs(ArrayList<Job> jobs)
 {
		open();

		for (int i = 0; i < jobs.size(); i++)
			deleteJob(jobs.get(i));
		close();

	}

	public void deleteAllJobs()
	{
		open();
		deleteJobs(getJobs());
		close();
	}

	public void addVacationDay(VacationDay day)
	{
		open();
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(VACATION_DAY__DATE, dateFormat.format(day.getDate()));
		day.setId(db.insert(VACATION_DAYS_TABLE, null, tempValues));
		close();
		
	}
	
	public void updateVacationDay(VacationDay day)
	{
		open();
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(VACATION_DAY__DATE, dateFormat.format(day.getDate()));
		tempValues.put(JOBS_ID, 0);
		db.update(VACATION_DAYS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(day.getId())});
		
		close();
		
		
	}

	public ArrayList<VacationDay> getVacationDaysForJob(Job job)
	{
		open();
		Cursor cursor = db.query(VACATION_DAYS_TABLE, new String[] { KEY_ROWID,
				JOBS_ID, VACATION_DAY__DATE }, null, null, null, null, null);

		ArrayList<VacationDay> vdays = new ArrayList<VacationDay>();

		if (cursor.moveToFirst()) {
			do {
				Date vdate = new Date();
				try {
					vdate = dateFormat.parse(cursor.getString(2));
				} catch (ParseException e) {

					e.printStackTrace();
				}
				if (cursor.getInt(1) == job.getId()) {
					VacationDay vday = new VacationDay(vdate);

					vday.setId(cursor.getLong(0));
					vday.setJob(getJobForId(cursor.getLong(1)));
					vday.setDate(vdate);
					vdays.add(vday);
				}
			} while (cursor.moveToNext());
		}

		close();
		return vdays;
	}

	public ArrayList<VacationDay> getVacationDaysForDates(Date startDate, Date endDate)
	{
		open();
		Cursor cursor = db.query(VACATION_DAYS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		VACATION_DAY__DATE}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		ArrayList<VacationDay> vdays = new ArrayList<VacationDay>();
		
		if (cursor.moveToFirst())
	    {
	        do {   
	        	Date vacDate;
				try
				{
					vacDate = dateFormat.parse(cursor.getString(2));
					if (vacDate.after(startDate) && vacDate.before(endDate))
					{
							VacationDay day = new VacationDay(vacDate);
							try
							{
								day.setId(cursor.getLong(0));
								day.setJob(getJobForId(cursor.getLong(1)));
								day.setDate(dateFormat.parse(cursor.getString(2)));
								vdays.add(day);
							} catch (ParseException e)
							{

								e.printStackTrace();
							}
						
					}
				} catch (ParseException e1)
				{
					e1.printStackTrace();
				}
				

	        } while (cursor.moveToNext());
	    }
		
		close();
		
		return vdays;
		
	}

	public ArrayList<VacationDay> getVacationDaysForJobAndDates(Job job, Date startDate, Date endDate)
	{
		open();
		Cursor cursor = db.query(VACATION_DAYS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		VACATION_DAY__DATE}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		ArrayList<VacationDay> vdays = new ArrayList<VacationDay>();
		
		if (cursor.moveToFirst())
	    {
	        do {   
	        	Date vacDate;
				try
				{
					vacDate = dateFormat.parse(cursor.getString(2));
					if (vacDate.after(startDate) && vacDate.before(endDate) && cursor.getInt(1) == job.getId())
					{
							VacationDay day = new VacationDay(vacDate);
							try
							{
								day.setId(cursor.getLong(0));
								day.setJob(getJobForId(cursor.getLong(1)));
								day.setDate(dateFormat.parse(cursor.getString(2)));
								vdays.add(day);
							} catch (ParseException e)
							{

								e.printStackTrace();
							}
						
					}
				} catch (ParseException e1)
				{
					e1.printStackTrace();
				}
				

	        } while (cursor.moveToNext());
	    }
		
		close();
		
		return vdays;
		
	}
	public void deleteVacationDay(VacationDay day)
	{
		open();
		db.delete(VACATION_DAYS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(day.getId())});
		close();
		
	}

	public void deleteVacationDays(ArrayList<VacationDay> days)
 {
		open();
		for (int i = 0; i < days.size(); i++)
			deleteVacationDay(days.get(i));
		close();

	}

	public void deleteAllVacationDays()
	{
		
		open();
		Cursor cursor = db.query(VACATION_DAYS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		VACATION_DAY__DATE}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		
		if (cursor.moveToFirst())
	    {
	        do {   
	        	db.delete(VACATION_DAYS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(cursor.getInt(0))});
	        } while (cursor.moveToNext());
	    }
		
		close();
	}

}
