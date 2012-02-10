package com.android.tracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.tracker.R;
import com.android.tracker.ui.records.RecordsActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author vlad
 *
 */
public class WorkTrackerActivity extends Activity implements OnClickListener {
	
	private Button recordsButton;
	AdView adView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        adView = new AdView(this, AdSize.BANNER, "a14de914472599e"); 
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        // Add the adView to it
        layout.addView(adView);

        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        
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