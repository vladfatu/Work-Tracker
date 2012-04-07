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
	private static final String RECORD_DESCRIPTION = "recorddescription";
	private static final String RECORDS_TABLE_CREATE = "create table " + RECORDS_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ JOBS_ID + " integer not null, "
			+ RECORD_TYPE + " integer not null, "
			+ RECORD_DATE + " text not null, "
			+ RECORD_DESCRIPTION + " text not null);";
	
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
	public DatabaseController open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this; 
	}

	// ---closes the database---
	public void close()
	{
		DBHelper.close();
	}

	public void addRecord(Record record)
	{
		
 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, record.getJob().getId());
		tempValues.put(RECORD_DATE, dateFormat.format(record.getDate()));
		tempValues.put(RECORD_TYPE, record.getType());
		tempValues.put(RECORD_DESCRIPTION, record.getDescription());
		record.setId(db.insert(RECORDS_TABLE, null, tempValues));
		
	

	}

	public void updateRecord(Record record)
	{
		
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(RECORD_DATE, dateFormat.format(record.getDate()));
		tempValues.put(RECORD_TYPE, record.getType());
		tempValues.put(RECORD_DESCRIPTION, record.getDescription());
		db.update(RECORDS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(record.getId())});
		
		
	}

	public void addRecords(ArrayList<Record> records)
	{
		
		for(int i=0; i<records.size(); i++)
			addRecord(records.get(i));
		
	}
	
	public ArrayList<Record> getAllRecords()
	{
		Cursor cursor = db.query(RECORDS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		RECORD_DATE,
        		RECORD_TYPE,
        		RECORD_DESCRIPTION}, 
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
					record.setDescription(cursor.getString(4));
					records.add(record);
				} catch (ParseException e)
				{
					e.printStackTrace();
				}

	        } while (cursor.moveToNext());
	    }
		
		
		return records;
	}

	public ArrayList<Record> getRecords(Date startDate, Date endDate, Job job)
	{
		Cursor cursor = db.query(RECORDS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		RECORD_DATE,
        		RECORD_TYPE,
        		RECORD_DESCRIPTION}, 
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
								record.setDescription(cursor.getString(4));
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
		
		
		return records;
		
	}

	public ArrayList<Record> getRecords(Job job)
	{
		Cursor cursor = db.query(RECORDS_TABLE, new String[] {
        		KEY_ROWID, 
        		JOBS_ID,
        		RECORD_DATE,
        		RECORD_TYPE,
        		RECORD_DESCRIPTION}, 
                null, 
                null, 
                null, 
                null, 
                null);
		
		ArrayList<Record> records = new ArrayList<Record>();
		
		if (cursor.moveToFirst()) {
			do {
				if (cursor.getInt(1) == job.getId()) {
					Record record = new Record();
					try {
						record.setId(cursor.getLong(0));
						record.setJob(getJobForId(cursor.getLong(1)));
						record.setDate(dateFormat.parse(cursor.getString(2)));
						record.setType(cursor.getInt(3));
						record.setDescription(cursor.getString(4));
						records.add(record);
					} catch (ParseException e) {

						e.printStackTrace();
					}
				}
				{
				}

			} while (cursor.moveToNext());
		}
		
		
		return records;
		
	}


	public void deleteRecords(Date startDate, Date endDate, Job job)
	{
		deleteRecords(getRecords(startDate, endDate, job));
	}

	public void deleteRecords(ArrayList<Record> records)
	{
		
		for(int i=0; i<records.size(); i++)
			deleteRecord(records.get(i));
		
	}

	public void deleteRecord(Record record)
	{
		
		db.delete(RECORDS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(record.getId())});
	}

	public void deleteAllRecords()
	{
		deleteRecords(getAllRecords());
	}

	public void addJob(Job job)
	{
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOB_NAME, job.getName());
		tempValues.put(JOB_PRICE_PER_HOUR, job.getPricePerHour());
		job.setId(db.insert(JOBS_TABLE, null, tempValues));
		
	}

	public void updateJob(Job job)
	{
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOB_NAME, job.getName());
		tempValues.put(JOB_PRICE_PER_HOUR, job.getPricePerHour());
		db.update(JOBS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(job.getId())});
		
		
	}

	public ArrayList<Job> getJobs()
	{
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
		
		return jobs;
	}
	
	private Job getJobForId(long id)
	{
		Job job = new Job();
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

		return job;
	}

	public void deleteJob(Job job)
	{
		
		db.delete(JOBS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(job.getId())});
	}

	public void deleteJobs(ArrayList<Job> jobs)
 {

		for (int i = 0; i < jobs.size(); i++)
			deleteJob(jobs.get(i));

	}

	public void deleteAllJobs()
	{
		deleteJobs(getJobs());
	}

	public void addVacationDay(VacationDay day)
	{
		ContentValues tempValues = new ContentValues();
		tempValues.put(JOBS_ID, 0);
		tempValues.put(VACATION_DAY__DATE, dateFormat.format(day.getDate()));
		day.setId(db.insert(VACATION_DAYS_TABLE, null, tempValues));
		
	}
	
	public void updateVacationDay(VacationDay day)
	{
		 
		ContentValues tempValues = new ContentValues();
		tempValues.put(VACATION_DAY__DATE, dateFormat.format(day.getDate()));
		tempValues.put(JOBS_ID, 0);
		db.update(VACATION_DAYS_TABLE, tempValues, KEY_ROWID+"=?", new String[] {
				Long.toString(day.getId())});
		
		
		
	}

	public ArrayList<VacationDay> getVacationDaysForJob(Job job)
	{
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

		return vdays;
	}

	public ArrayList<VacationDay> getVacationDaysForDates(Date startDate, Date endDate)
	{
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
		
		
		return vdays;
		
	}

	public ArrayList<VacationDay> getVacationDaysForJobAndDates(Job job, Date startDate, Date endDate)
	{
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
		
		
		return vdays;
		
	}
	public void deleteVacationDay(VacationDay day)
	{
		db.delete(VACATION_DAYS_TABLE, KEY_ROWID+"=?", new String [] {String.valueOf(day.getId())});
		
	}

	public void deleteVacationDays(ArrayList<VacationDay> days)
 {
		for (int i = 0; i < days.size(); i++)
			deleteVacationDay(days.get(i));

	}

	public void deleteAllVacationDays()
	{
		
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
		
	}

}
