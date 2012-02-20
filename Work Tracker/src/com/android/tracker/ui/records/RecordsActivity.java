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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.records_layout);
		
		list = (ListView) findViewById(R.id.list);
		records = new ArrayList<Record>();
		
		adapter = new RecordsAdapter(this, R.layout.record_row, records);
		list.setAdapter(adapter);
		
		dbController = new DatabaseController(this);
		
		// asta e doar de test, adaugarea record-rurilor va trebui facuta din ecranul principal, nu de aici
		Calendar c = Calendar.getInstance();
		dbController.addRecord(new Record(c.getTime()));
		
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

}
