package com.android.tracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.tracker.R;
import com.android.tracker.jobs.JobsListActivity;
import com.android.tracker.reports.ReportsActivity;
import com.android.tracker.settings.SettingsActivity;
import com.android.tracker.ui.records.RecordsActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author vlad
 *
 */
public class WorkTrackerActivity extends Activity implements OnClickListener {
	
	private Button punchInButton;

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
        
        punchInButton = (Button) findViewById(R.id.punchInButton);
        punchInButton.setOnClickListener(this);
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
			startActivity(new Intent(this, JobsListActivity.class));
		}
		
	}
}