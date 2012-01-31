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
	 * Intrarea "record" trebuie updatata.
	 * 
	 * @param record
	 */
	public void updateRecord(Record record);

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
	
	/**
	 * Functia trebuie sa stearga toate intrarile care au data inregistrarii
	 * mai mare decat "startDate" si mai mica decat "endDate". Daca parametrul
	 * job este null stergerea trebuie facuta pentru toate joburile, altfel jobul
	 * intrarilor trebuie sa fie acelasi cu parametrul "job".
	 * 
	 * @param startDate
	 * @param endDate
	 * @param job
	 * @return
	 */
	public void deleteRecords(Date startDate, Date endDate, Job job);
	
	
	/**
	 * Functia trebuie sa adauge un nou job in tabelul joburilor
	 * 
	 * @param job
	 */
	public void addJob(Job job);
	
	/**
	 * Functia trebuie sa jobul primti ca parametru
	 * 
	 * @param job
	 */
	public void updateJob(Job job);

	
	/**
	 * Functia va returna toate joburile din tabelul joburilor.
	 * @return
	 */
	public ArrayList<Job> getJobs();
	
	
	/**
	 * Functia va sterge jobul primit ca parametru
	 * @param job
	 */
	public void deleteJob(Job job);

}
