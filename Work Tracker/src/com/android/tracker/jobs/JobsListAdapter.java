package com.android.tracker.jobs;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.tracker.R;
import com.android.tracker.database.Job;

/**
 * @author vlad
 *
 */
public class JobsListAdapter extends ArrayAdapter<Job>{
	
	private ArrayList<Job> items;
	private int resourceId;

    public JobsListAdapter(Context context, int textViewResourceId, ArrayList<Job> items) {
            super(context, textViewResourceId, items);
            resourceId = textViewResourceId;
            this.items = items;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) { 
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(resourceId, null);
            }
            Job jobItem = items.get(position);
            if (jobItem != null) {
                    TextView nameView = (TextView) v.findViewById(R.id.nameView);
                    if (nameView != null)
                    {
                    	nameView.setText(jobItem.getName());
                    }
                    TextView clientView = (TextView) v.findViewById(R.id.clientView);
                    if (clientView != null)
                    {
                    	clientView.setText(jobItem.getClient());
                    }
                    TextView pricePerHourView = (TextView) v.findViewById(R.id.pricePerHourView);
                    if (pricePerHourView != null)
                    {
                    	pricePerHourView.setText(Integer.toString(jobItem.getPricePerHour()));
                    }
            }
            return v;
    	}

}
