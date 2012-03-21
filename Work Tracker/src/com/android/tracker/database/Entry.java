package com.android.tracker.database;

public class Entry {
	
	private Record punchInRecord, punchOutRecord;
	
	public Entry()
	{
		
	}
	
	public Entry(Record punchInRecord, Record punchOutRecord)
	{
		setPunchInRecord(punchInRecord);
		setPunchOutRecord(punchOutRecord);
	}

	public Record getPunchInRecord()
	{
		return punchInRecord;
	}

	public void setPunchInRecord(Record punchInRecord)
	{
		this.punchInRecord = punchInRecord;
	}

	public Record getPunchOutRecord()
	{
		return punchOutRecord;
	}

	public void setPunchOutRecord(Record punchOutRecord)
	{
		this.punchOutRecord = punchOutRecord;
	}

}
