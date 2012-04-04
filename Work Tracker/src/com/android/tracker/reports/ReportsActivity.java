package com.android.tracker.reports;

import java.util.ArrayList;

import utils.Constants;
import utils.Utils;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Job;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author vlad
 *
 */
public class ReportsActivity  extends Activity implements OnItemSelectedListener {
	
	private Spinner typeSpinner;
	private LinearLayout advancedLayout;
	private DatabaseController dbController;
	private Job currentJobReports;
	private Spinner jobSpinner;
	ArrayList<Job> jobs;
	
	AdView adView;
	
	public void onCreate(Bundle savedInstanceState) 
	{

	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.reports_layout);
	
    adView = new AdView(this, AdSize.BANNER, "a14de914472599e"); 
    
    LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

    // Add the adView to it
    layout.addView(adView);

    // Initiate a generic request to load it with an ad
    adView.loadAd(new AdRequest());
	
    dbController = new DatabaseController(this);
    
    jobSpinner = (Spinner) findViewById(R.id.jobSpinner);
    jobSpinner.setOnItemSelectedListener(this);
    typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
    advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);
    
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
	
	public void onResume()
	{
		super.onResume();
		updateSpinner();
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
		currentJobReports = jobs.get(position);
		Utils.setLongToPrefs(this, Constants.JOB_ID_PREF_REPORTS, currentJobReports.getId());
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}



}
