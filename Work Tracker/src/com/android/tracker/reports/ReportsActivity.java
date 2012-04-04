package com.android.tracker.reports;

import utils.Constants;
import utils.Utils;

import com.android.tracker.R;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * @author vlad
 *
 */
public class ReportsActivity extends Activity{
	
	private Spinner typeSpinner;
	private LinearLayout advancedLayout;
	
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
	
    typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
    advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);
    
	}
	
	public void onResume()
	{
		super.onResume();
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


}
