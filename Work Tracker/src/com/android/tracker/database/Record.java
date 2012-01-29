package com.android.tracker.database;

import java.util.Date;

public class Record {

	public static final int PUNCH_IN = 0;
	public static final int PUNCH_OUT = 1;

	private int type;
	private Date date;
	private Job job;

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
}
