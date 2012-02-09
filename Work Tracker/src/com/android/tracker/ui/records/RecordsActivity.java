package com.android.tracker.ui.records;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.tracker.R;
import com.android.tracker.database.Record;

public class RecordsActivity extends Activity implements OnItemClickListener{
	
	private ListView list;
	private RecordsAdapter adapter;
	private ArrayList<Record> records;
	private ProgressDialog m_ProgressDialog = null; 
	private Runnable viewOrders;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.records_layout);
		
		list = (ListView) findViewById(R.id.list);
		records = new ArrayList<Record>();
		
		adapter = new RecordsAdapter(this, R.layout.record_row, records);
		list.setAdapter(adapter);
		
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

	protected void getRecords()
	{
		// In loc de exemplu asta va trebui adaugat cod pentru extragerea din baza de date
		Calendar c = Calendar.getInstance();
        
		records.add(new Record(c.getTime()));
		records.add(new Record(c.getTime()));
		//
		//TODO
		
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

}
