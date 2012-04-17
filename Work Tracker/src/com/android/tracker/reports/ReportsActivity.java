package com.android.tracker.reports;

import java.util.ArrayList;
import java.util.Calendar;

import utils.Constants;
import utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Entry;
import com.android.tracker.database.Job;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author vlad
 *
 */
public class ReportsActivity  extends Activity implements OnItemSelectedListener {
	
	private Spinner typeSpinner;
	private LinearLayout advancedLayout;
	private DatabaseController dbController;
	private Job currentJobReports;
	private int currentType;
	private Spinner jobSpinner;
	private ArrayList<Job> jobs;
	private TableLayout tl;
	
	AdView adView;
	
	public void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.reports_layout);

		adView = new AdView(this, AdSize.BANNER, "a14de914472599e");

		LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);

		// Add the adView to it
		layout.addView(adView);

		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());

		dbController = new DatabaseController(this);

		jobSpinner = (Spinner) findViewById(R.id.jobSpinner);
		jobSpinner.setOnItemSelectedListener(this);
		typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
		typeSpinner.setOnItemSelectedListener(this);
		advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);

		tl = (TableLayout) findViewById(R.id.reportLayout);
		
	}
	
	private void updateReport()
	{
		tl.removeAllViewsInLayout();
		ArrayList<Entry> entries = Utils.getEntriesFromRecords(dbController.getAllRecords());
		Calendar tempHours = Calendar.getInstance();
		tempHours.clear();
		for (int i=0 ; i<entries.size() ; i++)
		{
			if (i < entries.size()-1 && 
				Utils.samePeriod(entries.get(i).getPunchInRecord().getDate(), 
					entries.get(i+1).getPunchInRecord().getDate(), currentType))
			{
				if (entries.get(i).getPunchOutRecord() != null) 
				{
					tempHours.add(Calendar.MILLISECOND, 
						(int) (entries.get(i).getPunchOutRecord().getDate().getTime() - 
							   entries.get(i).getPunchInRecord().getDate().getTime()));
				}
			}
			else
			{
				if (entries.get(i).getPunchOutRecord() != null) 
				{
					tempHours.add(Calendar.MILLISECOND, 
							(int) (entries.get(i).getPunchOutRecord().getDate().getTime() - 
								   entries.get(i).getPunchInRecord().getDate().getTime()));
				}
				tl.addView(getNewReportRow(Constants.dateFormatterDMY.format(entries.get(i).getPunchInRecord().getDate()), 
										   Constants.dateFormatterDMY.format(entries.get(i).getPunchInRecord().getDate()), 
										   Constants.dateFormatterHHMM.format(tempHours.getTime()), 
										   (int)(tempHours.getTimeInMillis()/3600000)*currentJobReports.getPricePerHour(), 
										   ((i % 2) == 0)), 
								new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				tempHours.clear();
			}
		}
	}
	
	private TableRow getNewReportRow(String startDate, String endDate, String hours, int income, boolean white)
	{
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tr.setPadding(0, 10, 0, 10);
		if (white) tr.setBackgroundColor(Color.WHITE);
		else tr.setBackgroundColor(Color.LTGRAY);
		
		TextView startView = new TextView(this);
		startView.setText(startDate);
		startView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		startView.setGravity(Gravity.CENTER);
		startView.setTextColor(Color.BLACK);
		tr.addView(startView);
		
		TextView endView = new TextView(this);
		endView.setText(endDate);
		endView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		endView.setGravity(Gravity.CENTER);
		endView.setTextColor(Color.BLACK);
		tr.addView(endView);
		
		TextView hoursView = new TextView(this);
		hoursView.setText(hours);
		hoursView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		hoursView.setGravity(Gravity.CENTER);
		hoursView.setTextColor(Color.BLACK);
		tr.addView(hoursView);
		
		TextView incomeView = new TextView(this);
		incomeView.setText(Integer.toString(income));
		incomeView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		incomeView.setGravity(Gravity.CENTER);
		incomeView.setTextColor(Color.BLACK);
		tr.addView(incomeView);
		
		return tr;
	}
	
    private void updateJobSpinner()
    {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        jobs = dbController.getJobs();
        for(Job job:jobs)
        {
        	adapter.add(job.getName());
        }
        jobSpinner.setAdapter(adapter);
        long currentJobId = Utils.getLongFromPrefs(this, Constants.JOB_ID_PREF_REPORTS, -1);
        if (currentJobId != -1)
        {
        	for (int i = 0; i < jobs.size(); i++)
        	{
        		if (jobs.get(i).getId() == currentJobId)
        		{
        			currentJobReports = jobs.get(i);
        			jobSpinner.setSelection(i, true);
        		}
        	}
        }
    }
	
	private void updateTypeSpinner()
	{
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_reports, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        typeSpinner.setAdapter(adapter);
        currentType = Utils.getIntFromPrefs(this, Constants.TYPE_REPORTS, 0);
        typeSpinner.setSelection(currentType, true);
        
	}
	
	public void onResume()
	{
		super.onResume();
		dbController.open();
		updateJobSpinner();
		updateTypeSpinner();
		if(Utils.getBooleanFromPrefs(this, Constants.REPORTS_ADVANCED, false))
		{
			typeSpinner.setVisibility(View.GONE);
			advancedLayout.setVisibility(View.VISIBLE);
		}
		else 
		{
			typeSpinner.setVisibility(View.VISIBLE);
			advancedLayout.setVisibility(View.GONE);
		}
		
	}

	public void onPause()
	{
		super.onPause();
		dbController.close();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.reports_layout_menu, menu);
		return true;
	}
	
	private void sendEmail()
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("plain/text");
		// emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new
		// String[]{ address.getText().toString()});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Work Tracker");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is an email from Work Tracker!");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	private void normalSearchClick() 
	{
		typeSpinner.setVisibility(View.VISIBLE);
		advancedLayout.setVisibility(View.GONE);
		Utils.setBooleanToPrefs(this, Constants.REPORTS_ADVANCED, false);
		
		// TODO Auto-generated method stub
		
	}

	private void advancedSearchClick() 
	{
		typeSpinner.setVisibility(View.GONE);
		advancedLayout.setVisibility(View.VISIBLE);
		Utils.setBooleanToPrefs(this, Constants.REPORTS_ADVANCED, true);
		
		// TODO Auto-generated method stub
		
	}
	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(Utils.getBooleanFromPrefs(this, Constants.REPORTS_ADVANCED, false))
		{
			menu.findItem(R.id.normalSearch).setVisible(true);
			menu.findItem(R.id.advancedSearch).setVisible(false);
		}
		else 
		{
			menu.findItem(R.id.normalSearch).setVisible(false);
			menu.findItem(R.id.advancedSearch).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.sendAsCsv:
			sendEmail();
			return true;
		case R.id.advancedSearch:
			advancedSearchClick();
			 //TODO
			return true;
		case R.id.normalSearch:
			normalSearchClick();
			 //TODO
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		switch (arg0.getId()) 
		{
		case R.id.jobSpinner:
			currentJobReports = jobs.get(position);
			Utils.setLongToPrefs(this, Constants.JOB_ID_PREF_REPORTS, currentJobReports.getId());
			break;
		case R.id.typeSpinner:
			currentType = position;
			updateReport();
			Utils.setIntToPrefs(this, Constants.TYPE_REPORTS, position);
			break;
		}
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}



}
