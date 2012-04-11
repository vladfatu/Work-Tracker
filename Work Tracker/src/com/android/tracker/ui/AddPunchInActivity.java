package com.android.tracker.ui;

import com.android.tracker.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class AddPunchInActivity extends Activity implements OnClickListener{
	
	private Button addButton;
	private Button cancelButton;
   
	@Override
    public void onCreate(Bundle savedInstanceState) {          

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    
        setContentView(R.layout.add_punch_in_layout);
        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);


    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == addButton)
		{
			//TODO
			this.finish();
		}
		if(v == cancelButton)
		{
			this.finish();
		}
		
	}
	
	


}
