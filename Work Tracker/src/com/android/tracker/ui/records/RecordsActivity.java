package com.android.tracker.ui.records;

import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Entry;
import com.android.tracker.database.Job;

/**
 * @author vlad
 * 
 */
public class RecordsActivity extends Activity implements OnItemClickListener, OnItemSelectedListener {

	private ListView list;
	private EntriesAdapter adapter;
	private ArrayList<Entry> entries;
	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private DatabaseController dbController;
	private Spinner periodSpinner;
	private LinearLayout advancedLayout;
	private Job currentJob;
	private Spinner jobSpinner;
	private int currentPeriod;
	ArrayList<Job> jobs;
	ArrayList<String> periods;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.records_layout);

		list = (ListView) findViewById(R.id.list);
		entries = new ArrayList<Entry>();
		periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
		periodSpinner.setOnItemSelectedListener(this);
		advancedLayout = (LinearLayout) findViewById(R.id.advancedLayout);
		jobSpinner = (Spinner) findViewById(R.id.jobSpinner);
		jobSpinner.setOnItemSelectedListener(this);

		adapter = new EntriesAdapter(this, R.layout.entry_row, entries);
		list.setAdapter(adapter);

		dbController = new DatabaseController(this);
		
		viewOrders = new Runnable() {

			public void run()
			{
				getRecords();
			}
		};

	}

	private void updateRecords()
	{
		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving data ...", true);
	}

	private Runnable returnRes = new Runnable() {

		public void run()
		{

			if (entries != null && entries.size() > 0)
			{
				adapter.notifyDataSetChanged();

			}
			m_ProgressDialog.dismiss();
		}
	};

	private void getRecords()
	{
		// Aici vor trebui verificate filtrele din activitate pentru a stii ce
		// sa cerem e la baza de date
		if (currentJob != null)
			entries.addAll(Utils.getEntriesFromRecords(dbController.getRecords(currentJob)));

		runOnUiThread(returnRes);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// aici ar trebui sa apara un dialog in care sa editam
		// record-ul(probabil data e singura care poate fi schimbata)
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
		dbController.open();
		updateSpinner();
		updateRecords();
		updateSpinner2();

		if (Utils.getBooleanFromPrefs(this, Constants.RECORDS_ADVANCED, false))
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

	public void onPause()
	{
		super.onPause();
		dbController.close();
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
		if (Utils.getBooleanFromPrefs(this, Constants.RECORDS_ADVANCED, false))
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

	//

	private void updateSpinner()
	{
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		jobs = dbController.getJobs();
		for (Job job : jobs)
		{
			spinnerAdapter.add(job.getName());
		}
		jobSpinner.setAdapter(spinnerAdapter);
		long currentJobId = Utils.getLongFromPrefs(this, Constants.JOB_ID_PREF_ENTRIES, -1);
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

	private void updateSpinner2()
	{

		ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.current_period, android.R.layout.simple_spinner_item);
		spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		periodSpinner.setAdapter(spinnerAdapter2);
		currentPeriod = Utils.getIntFromPrefs(this, Constants.CURRENT_PERIOD_PREF, 0);
		periodSpinner.setSelection(currentPeriod, true);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		switch (arg0.getId())
		{
		case R.id.jobSpinner:
			currentJob = jobs.get(position);
			Utils.setLongToPrefs(this, Constants.JOB_ID_PREF_ENTRIES, currentJob.getId());
			break;
		case R.id.periodSpinner:
			currentPeriod = position;
			Utils.setIntToPrefs(this, Constants.CURRENT_PERIOD_PREF, position);
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub

	}
}