package com.android.tracker.database;

import java.util.Date;

/**
 * @author vlad
 *
 */
public class Record {

	public static final int PUNCH_IN = 0;
	public static final int PUNCH_OUT = 1;

	private long id;
	private int type;
	private Date date;
	private Job job;
	private String description; 

	
	public Record()
	{
		setDescription("");
	};
	
	public Record(Date date)
	{
		this.date = date;
		setDescription("");
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
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
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
