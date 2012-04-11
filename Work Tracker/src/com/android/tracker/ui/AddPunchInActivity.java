package com.android.tracker.ui;

import com.android.tracker.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class AddPunchInActivity extends Activity{
   
	@Override
    public void onCreate(Bundle savedInstanceState) {          

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    
        setContentView(R.layout.add_punch_in_layout);
        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);


    }


}
