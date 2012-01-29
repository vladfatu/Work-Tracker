package com.android.tracker.database;

import java.util.ArrayList;
import java.util.Date;

public interface DatabaseControllerInterface {

	/**
	 * Intrarea "record" trebuie adaugata in baza de date. Intrarea trebuie
	 * inserata in tabelul cu jobul ei.(jobul intrarii poate fi aflat prin
	 * "record.getJob()")
	 * 
	 * @param record
	 */
	public void addRecord(Record record);

	/**
	 * Functia asta nu trebuie implementata, decat daca ne hotaram sa facem un
	 * sistem backup-restore.
	 * 
	 * @param records
	 */
	public void addRecords(ArrayList<Record> records);

	/**
	 * Functia trebuie sa returneze toate intrarile care au data inregistrarii
	 * mai mare decat "startDate" si mai mica decat "endDate". Daca parametrul
	 * job este null selectul trebuie facut pentru toate joburile, altfel jobul
	 * intrarilor trebuie sa fie acelasi cu parametrul "job".
	 * 
	 * @param startDate
	 * @param endDate
	 * @param job
	 * @return
	 */
	public ArrayList<Record> getRecords(Date startDate, Date endDate, Job job);

}
