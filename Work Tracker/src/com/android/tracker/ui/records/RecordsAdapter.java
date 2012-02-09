package com.android.tracker.ui.records;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.tracker.R;
import com.android.tracker.database.Record;

public class RecordsAdapter extends ArrayAdapter<Record> {

    private ArrayList<Record> items;

    public RecordsAdapter(Context context, int textViewResourceId, ArrayList<Record> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) { 
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.record_row, null);
            }
            Record o = items.get(position);
            if (o != null) {
                    TextView dateView = (TextView) v.findViewById(R.id.dateView);
                    
                    if (dateView != null) {
                    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(o.getDate());
                        
                    	dateView.setText(formattedDate);                         
                    }
            }
            return v;
    	}
    
}