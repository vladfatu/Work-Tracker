package com.android.tracker.database;

/**
 * @author vlad
 *
 */
public class Job {

	private long id;
	private String name;
	private int pricePerHour;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPricePerHour()
	{
		return pricePerHour;
	}

	public void setPricePerHour(int pricePerHour)
	{
		this.pricePerHour = pricePerHour;
	}

}
