package com.android.tracker.jobs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.tracker.R;
import com.android.tracker.database.DatabaseController;
import com.android.tracker.database.Job;

/**
 * @author vlad
 *
 */
public class JobSettingsActivity extends Activity implements OnClickListener{
	
	private EditText nameEditText;
	private Button saveButton;
	private DatabaseController dbController;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_jobs_layout);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(this);
		dbController = new DatabaseController(this);
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void onClick(View v)
	{
		if ( v == saveButton){
			Job job = new Job();
			job.setName(nameEditText.getText().toString());
			dbController.addJob(job);
			finish();
		}
		
	}

}
