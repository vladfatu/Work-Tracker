package com.android.tracker.ui.records;

import java.util.ArrayList;
import java.util.Calendar;

import utils.Constants;
import utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Record;


/**
 * @author vlad
 *
 */
public class RecordsActivity extends Activity implements OnItemClickListener{
	
	private ListView list;
	private RecordsAdapter adapter;
	private ArrayList<Record> records;
	private ProgressDialog m_ProgressDialog = null; 
	private Runnable viewOrders;
	private DatabaseController dbController;
	private Spinner periodSpinner;
	private LinearLayout advancedLayout;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.records_layout);
		
		list = (ListView) findViewById(R.id.list);
		records = new ArrayList<Record>();
		periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
		advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);
		
		adapter = new RecordsAdapter(this, R.layout.record_row, records);
		list.setAdapter(adapter);
		
		dbController = new DatabaseController(this);
		
		// asta e doar de test, adaugarea record-rurilor va trebui facuta din ecranul principal, nu de aici
		Calendar c = Calendar.getInstance();
		Record record = new Record(c.getTime());
		if (dbController.getJobs().get(0) != null) record.setJob(dbController.getJobs().get(0));
		dbController.addRecord(record);
		
		viewOrders = new Runnable(){
            
            public void run() {
                getRecords();
            }
        };
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(this,    
              "Please wait...", "Retrieving data ...", true);
	}
	
	private Runnable returnRes = new Runnable() {

        public void run() {
        	
            if(records != null && records.size() > 0){
                adapter.notifyDataSetChanged();
              
            }
            m_ProgressDialog.dismiss();
        }
    };

	private void getRecords()
	{
		// Aici vor trebui verificate filtrele din activitate pentru a stii ce sa cerem e la baza de date
		records.addAll(dbController.getAllRecords());
		
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		runOnUiThread(returnRes);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// aici ar trebui sa apara un dialog in care sa editam record-ul(probabil data e singura care poate fi schimbata)
		// TODO Auto-generated method stub
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.records_layout_menu, menu);
		return true;
	}
	
	public void onResume()
	{
		super.onResume();
		if(Utils.getBooleanFromPrefs(this, Constants.RECORDS_ADVANCED, false))
		{
			periodSpinner.setVisibility(View.GONE);
			advancedLayout.setVisibility(View.VISIBLE);	
		}
		else
		{
			periodSpinner.setVisibility(View.VISIBLE);
			advancedLayout.setVisibility(View.GONE);	
		}
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

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.addEntry:
			// TODO startActivity(new Intent(this, RecordsActivity.class));
			return true;
		case R.id.deleteVisibleEntries:
			// TODO startActivity(new Intent(this, JobsListActivity.class));
			return true;
		case R.id.sendAsCsv:
			sendEmail();
			return true;
		case R.id.advancedSearch:
			advancedSearchClick();
			return true;
		case R.id.normalSearch:
			 normalSearchClick();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void normalSearchClick() 
	{
		periodSpinner.setVisibility(View.VISIBLE);
		advancedLayout.setVisibility(View.GONE);
		Utils.setBooleanToPrefs(this, Constants.RECORDS_ADVANCED, false);
		
		// TODO Auto-generated method stub
		
	}

	private void advancedSearchClick() 
	{
		periodSpinner.setVisibility(View.GONE);
		advancedLayout.setVisibility(View.VISIBLE);
		Utils.setBooleanToPrefs(this, Constants.RECORDS_ADVANCED, true);
		// TODO Auto-generated method stub
		
	}

	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(Utils.getBooleanFromPrefs(this, Constants.RECORDS_ADVANCED, false))
		{
			menu.findItem(R.id.advancedSearch).setVisible(false);
			menu.findItem(R.id.normalSearch).setVisible(true);
		}
		else
		{
			menu.findItem(R.id.advancedSearch).setVisible(true);
			menu.findItem(R.id.normalSearch).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	
	}
}
