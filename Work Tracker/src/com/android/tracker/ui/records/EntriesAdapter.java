package com.android.tracker.ui.records;

import java.util.ArrayList;

import utils.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.tracker.R;
import com.android.tracker.database.Entry;

/**
 * @author vlad
 * 
 */
public class EntriesAdapter extends ArrayAdapter<Entry> {

	private ArrayList<Entry> items;

	public EntriesAdapter(Context context, int textViewResourceId, ArrayList<Entry> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.entry_row, null);
		}
		Entry o = items.get(position);

		if (o != null)
		{
			TextView startDateView = (TextView) v.findViewById(R.id.startDateView);
			String formattedDate = "";
			if (startDateView != null)
			{
				if (o.getPunchInRecord() != null)
				{
					
					formattedDate = Constants.dateFormatter.format(o.getPunchInRecord().getDate());
					startDateView.setText(formattedDate);
				}
				
			}

			TextView endDateView = (TextView) v.findViewById(R.id.endDateView);

			if (endDateView != null)
			{
				if (o.getPunchOutRecord() != null)
				{
					formattedDate = Constants.dateFormatter.format(o.getPunchOutRecord().getDate());
					endDateView.setText(formattedDate);
					endDateView.setVisibility(View.VISIBLE);
				}
				else
				{
					endDateView.setText(formattedDate);
					endDateView.setVisibility(View.INVISIBLE);

				}
			}
		}
		return v;
	}

}