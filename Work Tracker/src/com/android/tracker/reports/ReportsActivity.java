package com.android.tracker.reports;

import com.android.tracker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * @author vlad
 *
 */
public class ReportsActivity extends Activity{
	
	private LinearLayout normalLayout;
	private LinearLayout advancedLayout;
	private Menu reports_layout_menu;
	private Boolean isAdvanced = false;
	
	public void onCreate(Bundle savedInstanceState) 
	{

	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.reports_layout); 
	
    normalLayout = (LinearLayout) findViewById(R.id.normalLayout);
    advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);
    
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
		normalLayout.setVisibility(View.VISIBLE);
		advancedLayout.setVisibility(View.GONE);
		isAdvanced = false;
		
		// TODO Auto-generated method stub
		
	}

	private void advancedSearchClick() 
	{
		normalLayout.setVisibility(View.GONE);
		advancedLayout.setVisibility(View.VISIBLE);
		isAdvanced = true;
		
		// TODO Auto-generated method stub
		
	}
	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(isAdvanced)
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
