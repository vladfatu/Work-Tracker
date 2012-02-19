package com.android.tracker.database;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author vlad
 *
 */
public class DatabaseController implements DatabaseControllerInterface {

	private static final String DATABASE_NAME = "worktracker";
	private static final int DATABASE_VERSION = 1;

	private static final String JOBS_TABLE = "jobs";
	private static final String JOB_NAME = "jobname";
	private static final String JOB_PRICE_PER_HOUR = "jobpph";
	private static final String JOBS_TABLE_CREATE = "create table " + JOBS_TABLE 
			+ " (_id integer primary key autoincrement, "
			+ JOB_NAME + " text not null, "
			+ JOB_PRICE_PER_HOUR + " integer not null);";

	private static final String RECORDS_TABLE = "records";
	private static final String JOBS_ID = "jobsid";
	private static final String RECORD_DATE = "recorddate";
	private static final String RECORDS_TABLE_CREATE = "create table " + RECORDS_TABLE 
			+ " (_id integer primary key autoincrement, " 
			+ JOBS_ID + " integer not null, "
			+ RECORD_DATE + " text not null);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DatabaseController(Context ctx) {
		this.context = ctx;
		ArrayList<String> tables = new ArrayList<String>();
		tables.add(JOBS_TABLE_CREATE);
		tables.add(RECORDS_TABLE_CREATE);
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
 
		// TODO Auto-generated method stub

		close();

	}

	public void addRecords(ArrayList<Record> records)
	{
		// TODO Auto-generated method stub

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

	public void addJob(Job job)
	{
		// TODO Auto-generated method stub

	}

	public ArrayList<Job> getJobs()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteJob(Job job)
	{
		// TODO Auto-generated method stub

	}

	public void updateRecord(Record record)
	{
		// TODO Auto-generated method stub

	}

	public void updateJob(Job job)
	{
		// TODO Auto-generated method stub

	}

}
