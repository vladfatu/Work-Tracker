package com.android.tracker.database;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author vlad
 *
 */
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
	 * Functia trebuie sa returneze toate intrarile 
	 * 
	 * @return
	 */
	public ArrayList<Record> getAllRecords();

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
	 * Functia trebuie sa streaga din baza de date toate intrarile din lista "records"
	 * 
	 * @param records
	 */
	public void deleteRecords(ArrayList<Record> records);
	
	/**
	 * Functia trebuie sa stearga din baza de date intrarea "record"
	 * 
	 * @param record
	 */
	public void deleteRecord(Record record);
	
	/**
	 * 
	 * Functia trebuie sa stearga din baza de date toate intrarile
	 * 
	 */
	public void deleteAllRecords();
	
	
	/**
	 * Functia trebuie sa adauge un nou job in tabelul joburilor
	 * 
	 * @param job
	 */
	public void addJob(Job job);
	
	/**
	 * Functia trebuie sa faca update la jobul primit ca parametru
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
	
	/**
	 * Functia trebuie sa stearga din baza de date toate joburile din lista "jobs"
	 * 
	 * @param jobs
	 */
	public void deleteJobs(ArrayList<Job> jobs);
	
	/**
	 * Functia trebuie sa stearga din baza de date toate joburile.
	 */
	public void deleteAllJobs();
	
	/**
	 * Functia trebuie sa adauge in tabelul VACATION_DAYS_TABLE ziua "day"
	 * 
	 * @param day
	 */
	public void addVacationDay(VacationDay day);
	
	/**
	 * functia trebuie sa faca update la ziua primita ca parametru 
	 * 
	 * @param day
	 */
	public void updateVacationDay(VacationDay day);
	
	/**
	 * Functia trebuie sa returneze toate zilele de vacanta care au ca JOB_ID parametrul "job"
	 * 
	 * @param job
	 * @return
	 */
	public ArrayList<VacationDay> getVacationDaysForJob(Job job);
	
	/**
	 * Functia trebuie sa returneze toate zilele de vacanta care sunt intre datele "startDate" si "endDate"
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<VacationDay> getVacationDaysForDates(Date startDate, Date endDate);
	
	/**
	 * Functia trebuie sa returneze toate zilele de vacanta care sunt intre datele "startDate" si "endDate" si 
	 * au ca JOB_ID parametrul "job"
	 * 
	 * @param job
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<VacationDay> getVacationDaysForJobAndDates(Job job, Date startDate, Date endDate);
	
	/**
	 * Functia trebuie sa stearga ziua de vacanta "day"
	 * 
	 * @param day
	 */
	public void deleteVacationDay(VacationDay day);
	
	/**
	 * Functia trebuie sa stearga toate zilele de concediu din lista "days"
	 * 
	 * @param days
	 */
	public void deleteVacationDays(ArrayList<VacationDay> days);
	
	/**
	 * Functia trebuie sa stearga toate zilele de concediu.
	 */
	public void deleteAllVacationDays();

}
