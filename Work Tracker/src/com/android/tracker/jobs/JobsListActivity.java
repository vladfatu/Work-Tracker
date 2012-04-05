package com.android.tracker.jobs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Job;
import com.android.tracker.widget.TrackerWidgetProvider;

/**
 * @author vlad
 * 
 */
public class JobsListActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ListView list;
	private JobsListAdapter adapter;
	private ArrayList<Job> jobs;
	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private DatabaseController dbController;
	private Button addNewJobButton;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.jobs_layout);

		list = (ListView) findViewById(R.id.list);
		jobs = new ArrayList<Job>();

		dbController = new DatabaseController(this);

		adapter = new JobsListAdapter(this, R.layout.jobs_row, jobs);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);

		addNewJobButton = (Button) findViewById(R.id.addNewJobButton);
		addNewJobButton.setOnClickListener(this);

	}

	private Runnable returnRes = new Runnable() {

		public void run()
		{

			if (jobs != null && jobs.size() > 0)
			{
				adapter.notifyDataSetChanged();

			}
			m_ProgressDialog.dismiss();
		}
	};

	private void getJobs()
	{
		jobs.removeAll(jobs);
		jobs.addAll(dbController.getJobs());

		runOnUiThread(returnRes);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		intent.putExtra(TrackerWidgetProvider.JOB, jobs.get(position).getName());
		sendBroadcast(intent);
		finish();
		Log.d("JobsListActivity", "sent");
		// aici ar trebui sa se deschida activitatea "JobSettingsActivity"
		// pentru jobul de la pozitia "position"
		// TODO Auto-generated method stub

	}

	public void onClick(View v)
	{
		if (v == addNewJobButton)
		{			
			startActivity(new Intent(this, JobSettingsActivity.class));
		}
		

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		dbController.close();
	}
	
	private void updateJobs(){
		
		viewOrders = new Runnable() {

			public void run()
			{
				getJobs();
			}
		};
		Thread thread = new Thread(null, viewOrders, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving data ...", true);
		
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		dbController.open();
		updateJobs();

	}

}
