package com.android.tracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.tracker.R;
import com.android.tracker.ui.records.RecordsActivity;

/**
 * @author vlad
 *
 */
public class WorkTrackerActivity extends Activity implements OnClickListener {
	
	private Button recordsButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        recordsButton = (Button) findViewById(R.id.recordsButton);
        recordsButton.setOnClickListener(this);
    }

	public void onClick(View v)
	{
		if (v == recordsButton)
		{
			startActivity(new Intent(this, RecordsActivity.class));
		}
		
	}
}