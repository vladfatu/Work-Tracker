package com.android.tracker.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import utils.Constants;
import utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Job;
import com.android.tracker.database.Record;
import com.android.tracker.jobs.JobSettingsActivity;
import com.android.tracker.jobs.JobsListActivity;
import com.android.tracker.reports.ReportsActivity;
import com.android.tracker.settings.SettingsActivity;
import com.android.tracker.ui.records.RecordsActivity;
import com.android.tracker.widget.TrackerWidgetProvider;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author vlad
 *
 */
public class WorkTrackerActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private Button punchInButton;
	private Button addPunchInButton;
	private TextView todayHoursTextView;
	private TextView todayIncomeTextView;
	private TextView thisWeekHoursTextView;
	private TextView thisWeekIncomeTextView;
	private TextView thisMonthHoursTextView;
	private TextView thisMonthIncomeTextView;
	private DatabaseController dbController;
	private Job currentJob;
	private Spinner jobSpinner;
	private LinearLayout punchInInfoLayout;
	private Button jobSettingsButton;
	private EditText descriptionEditText;
	private TextView workStartedAtTextView;
	private TextView hoursWorkedTextView;
	private TextView hoursWorkedIncomeTextView;
	ArrayList<Job> jobs;
	//private Date startDate;
	//private static final int DATE_DIALOG_ID = 0;
	
	
	/*private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		        	startDate.setYear(year - 1900);
		        	startDate.setMonth(monthOfYear);
		        	startDate.setDate(dayOfMonth);
		            updateDisplay();
		        }
		    };*/

	AdView adView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);
        
        adView = new AdView(this, AdSize.BANNER, "a14de914472599e"); 
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        // Add the adView to it
        layout.addView(adView);

        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        
        dbController = new DatabaseController(this);
        
        punchInButton = (Button) findViewById(R.id.punchInButton);
        punchInButton.setOnClickListener(this);
    	todayHoursTextView = (TextView) findViewById(R.id.todayHoursTextView);
    	todayIncomeTextView = (TextView) findViewById(R.id.todayIncomeTextView);
    	thisWeekHoursTextView = (TextView) findViewById(R.id.thisWeekHoursTextView);
    	thisWeekIncomeTextView = (TextView) findViewById(R.id.thisWeekIncomeTextView);
    	thisMonthHoursTextView = (TextView) findViewById(R.id.thisMonthHourTextView);
    	thisMonthIncomeTextView = (TextView) findViewById(R.id.thisMonthIncomeTextView);
    	jobSpinner = (Spinner) findViewById(R.id.jobSpinner);
    	jobSpinner.setOnItemSelectedListener(this);
    	punchInInfoLayout = (LinearLayout) findViewById(R.id.punchInInfoLayout);
    	jobSettingsButton = (Button) findViewById(R.id.jobSettingsButton);
    	jobSettingsButton.setOnClickListener(this);
    	descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    	workStartedAtTextView = (TextView) findViewById(R.id.workStartedAtTextView);
    	hoursWorkedTextView = (TextView) findViewById(R.id.hoursWorkedTextView);
    	hoursWorkedIncomeTextView = (TextView) findViewById(R.id.hoursWorkedIncomeTextView);
    	addPunchInButton = (Button) findViewById(R.id.addPunchInButton);
    	addPunchInButton.setOnClickListener(this);
    	
		//Calendar c = Calendar.getInstance();
		//startDate = c.getTime();
    	
    }
    
    /*protected void updateDisplay() {
		// TODO Auto-generated method stub
		
	}*/

	private void updateUI()
    {
    	if (currentJob != null)
    	{
	    	Calendar c = Calendar.getInstance();
	    	Date nowDate = c.getTime();
	    	
	    	
	    	ArrayList<Record> todayRecords = dbController.getRecords(Utils.todayFirstHour(), nowDate, currentJob);
	    	Calendar t = Utils.getHoursWorkedFromEntries(Utils.getEntriesFromRecords(todayRecords), true);
	    	todayHoursTextView.setText(Constants.dateFormatterHHMM.format(t.getTime()));
	       	todayIncomeTextView.setText(Integer.toString((int)(t.getTimeInMillis()/3600000)*currentJob.getPricePerHour()));

	    	ArrayList<Record> thisWeekRecords = dbController.getRecords(Utils.firstDayOfThisWeek(), nowDate, currentJob);
	    	Calendar w = Utils.getHoursWorkedFromEntries(Utils.getEntriesFromRecords(thisWeekRecords), true);
	    	thisWeekHoursTextView.setText(Constants.dateFormatterHHMM.format(w.getTime()));
	       	thisWeekIncomeTextView.setText(Integer.toString((int)(w.getTimeInMillis()/3600000)*currentJob.getPricePerHour()));

	    	ArrayList<Record> thisMonthRecords = dbController.getRecords(Utils.firstDayOfThisMonth(), nowDate, currentJob);
	    	Calendar m = Utils.getHoursWorkedFromEntries(Utils.getEntriesFromRecords(thisMonthRecords), true);	
	    	thisMonthHoursTextView.setText(Constants.dateFormatterHHMM.format(m.getTime()));
	       	thisMonthIncomeTextView.setText(Integer.toString((int)((m.getTimeInMillis()/3600000)*currentJob.getPricePerHour())));
	       	
	       	Boolean punchedIn = Utils.getBooleanFromPrefs(this, Constants.PUNCH_IN_PREF, false);
			if(punchedIn==true)
			{
				punchInButton.setText(R.string.punch_out);
				punchInInfoLayout.setVisibility(View.VISIBLE);
				jobSpinner.setEnabled(false);
				descriptionEditText.setText(Utils.getStringFromPrefs(this, Constants.DESCRIPTION, ""));
				descriptionEditText.setSelection(descriptionEditText.length());
			}
			else 
			{
				punchInButton.setText(R.string.punch_in);
				punchInInfoLayout.setVisibility(View.INVISIBLE);
				jobSpinner.setEnabled(true);
			}
    	}
    }
    
    private void updatePunchInInfoLayout()
    {
		Boolean punchedIn = Utils.getBooleanFromPrefs(this, Constants.PUNCH_IN_PREF, false);
		if(punchedIn == true)
		{
			long l = Utils.getLongFromPrefs(this, Constants.WORK_STARTED, new Date().getTime());
			Calendar c = Calendar.getInstance();
			Date nowDate = c.getTime();
			Calendar h = Calendar.getInstance();
			h.clear();
			h.add(Calendar.MILLISECOND, (int)(nowDate.getTime()-l));
			workStartedAtTextView.setText(Constants.dateFormatterHHMM.format(l));
			hoursWorkedTextView.setText(Constants.dateFormatterHHMM.format(h.getTime()));
			hoursWorkedIncomeTextView.setText(Integer.toString((int)(h.getTimeInMillis()/3600000)*currentJob.getPricePerHour()));	
		}
		else
		{
			workStartedAtTextView.setText("");
			hoursWorkedTextView.setText("");
			hoursWorkedIncomeTextView.setText("");	
		}

    	
    }
    
    private void updateSpinner()
    {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        jobs = dbController.getJobs();
        for(Job job:jobs)
        {
        	adapter.add(job.getName());
        }
        jobSpinner.setAdapter(adapter);
        long currentJobId = Utils.getLongFromPrefs(this, Constants.JOB_ID_PREF, -1);
        if (currentJobId != -1)
        {
        	for (int i = 0; i < jobs.size(); i++)
        	{
        		if (jobs.get(i).getId() == currentJobId)
        		{
        			currentJob = jobs.get(i);
        			jobSpinner.setSelection(i, true);
        		}
        	}
        }
    }
    
	/*protected Dialog onCreateDialog(int id) {
		   switch (id) {
		   case DATE_DIALOG_ID:
			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
			   builder.setTitle(R.string.select_date_and_time);
			   builder.setNegativeButton("Date",new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //this.finish();
		           }
		       });
		       builder.setPositiveButton("Date",new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //this.finish();
		           }
		       });
		       builder.setNeutralButton("Date",new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //this.finish();
		           }
		       })
;
			   AlertDialog addDialog = builder.create();
			   return addDialog;
		      return new DatePickerDialog(this,
		                mDateSetListener,
		                startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate());
		   }
		   return null;
		}*/
	
    @Override
	protected void onResume() {
		super.onResume();
		dbController.open();
		updateSpinner();
		updateUI();
		updatePunchInInfoLayout();
	}
    
    protected void onPause()
    {
    	super.onPause();
    	dbController.close();
    	Utils.setStringToPrefs(this, Constants.DESCRIPTION, descriptionEditText.getText().toString());
    }
    
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.first_screen_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.manageEntries:
			startActivity(new Intent(this, RecordsActivity.class));
			return true;
		case R.id.manageJobs:
			startActivity(new Intent(this, JobsListActivity.class));
			return true;
		case R.id.reports:
			startActivity(new Intent(this, ReportsActivity.class));
			return true;
		case R.id.backupRestore:
			//TODO startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.help:
			//TODO startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View v)
	{
		if (v == punchInButton)
		{
			Record record = new Record();
			Calendar c = Calendar.getInstance();
			Boolean punchedIn = Utils.getBooleanFromPrefs(this, Constants.PUNCH_IN_PREF, false);
			if(punchedIn==false)
			{
				record.setDate(c.getTime());
				record.setType(Record.PUNCH_IN);
				TrackerWidgetProvider.lastStartDate = c.getTime();
				record.setDescription(descriptionEditText.getText().toString());
				record.setJob(currentJob);
				punchInButton.setText(R.string.punch_out);
				punchInInfoLayout.setVisibility(View.VISIBLE);
				jobSpinner.setEnabled(false);
				Utils.setBooleanToPrefs(this, Constants.PUNCH_IN_PREF, true);
		    	Utils.setLongToPrefs(this, Constants.WORK_STARTED, record.getDate().getTime());
				updatePunchInInfoLayout();
				
			}
			else 
			{
				record.setDate(c.getTime());
				record.setType(Record.PUNCH_OUT);
				record.setDescription(descriptionEditText.getText().toString());
				record.setJob(currentJob);
				punchInButton.setText(R.string.punch_in);
				punchInInfoLayout.setVisibility(View.INVISIBLE);
				jobSpinner.setEnabled(true);
				descriptionEditText.setText("");
				Utils.setBooleanToPrefs(this, Constants.PUNCH_IN_PREF, false);
				
			}
			dbController.addRecord(record);
		}
		
		if(v == addPunchInButton)
		{
			startActivity(new Intent(this, AddPunchInActivity.class));
			//showDialog(DATE_DIALOG_ID);
		}
		
		if(v == jobSettingsButton)
		{
			Intent i = new Intent(this, JobSettingsActivity.class);
			i.putExtra("CURRENT_JOB", currentJob.getId());
			startActivity(i);
		}
		
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		currentJob = jobs.get(position);
		Utils.setLongToPrefs(this, Constants.JOB_ID_PREF, currentJob.getId());
		updateUI();
		
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
		
	}
}