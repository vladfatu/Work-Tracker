package com.android.tracker.database;

import java.util.Date;

public class VacationDay {
	
	private long id;
	private Date date;
	private Job job;
	
	public VacationDay(Date date)
	{
		setDate(date);
	}
	
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public Job getJob()
	{
		return job;
	}
	public void setJob(Job job)
	{
		this.job = job;
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

}
