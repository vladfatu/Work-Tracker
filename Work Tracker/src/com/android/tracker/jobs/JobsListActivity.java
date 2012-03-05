package com.android.tracker.jobs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Job;

/**
 * @author vlad
 *
 */
public class JobsListActivity extends Activity{
	
	private ListView list;
	private JobsListAdapter adapter;
	private ArrayList<Job> jobs;
	private ProgressDialog m_ProgressDialog = null; 
	private Runnable viewOrders;
	private DatabaseController dbController;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.records_layout);
		
		list = (ListView) findViewById(R.id.list);
		jobs = new ArrayList<Job>();
		
		dbController = new DatabaseController(this);
		Job job = new Job();
		job.setName("Job1");
		dbController.addJob(job);
		
		adapter = new JobsListAdapter(this, R.layout.jobs_row, jobs);
		list.setAdapter(adapter);
		
		viewOrders = new Runnable(){
            
            public void run() {
                getJobs();
            }
        };
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(this,    
              "Please wait...", "Retrieving data ...", true);
	}
	
	private Runnable returnRes = new Runnable() {

        public void run() {
        	
            if(jobs != null && jobs.size() > 0){
                adapter.notifyDataSetChanged();
              
            }
            m_ProgressDialog.dismiss();
        }
    };
    
    private void getJobs()
	{
		jobs.addAll(dbController.getJobs());
		
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
		// aici ar trebui sa se deschida activitatea "JobSettingsActivity" pentru jobul de la pozitia "position" 
		// TODO Auto-generated method stub
		
	}

}