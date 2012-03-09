package com.android.tracker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.tracker.R;
import com.android.tracker.jobs.JobsListActivity;
import com.android.tracker.ui.WorkTrackerActivity;

public class TrackerWidgetProvider extends AppWidgetProvider {

	public static final String JOB = "job";
	public static final String ID = "id";
	private static final String Tag = "TrackerWidgetProvider";
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context)
	{
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context)
	{
		super.onEnabled(context);
	} 

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d(Tag, "received");
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
		Bundle extras = intent.getExtras();
		if (extras != null) 
		{
			if (extras.getString(JOB) != null) 
			{
				Log.d(Tag, extras.getString(JOB));
				remoteView.setTextViewText(R.id.jobSpinner, extras.getString(JOB));
				ComponentName cn = new ComponentName(context, TrackerWidgetProvider.class); 
				AppWidgetManager.getInstance(context).updateAppWidget(cn, remoteView);
			}
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{

		for (int appWidgetId : appWidgetIds)
		{

			Intent intent = new Intent(context, WorkTrackerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            Intent jobIntent = new Intent(context, JobsListActivity.class);
            PendingIntent jobPendingIntent = PendingIntent.getActivity(context, 0, jobIntent, 0);
			
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
			remoteView.setOnClickPendingIntent(R.id.jobSpinner, jobPendingIntent);
			remoteView.setChronometer(R.id.widgetChronometer, SystemClock.elapsedRealtime(), null, true);
			remoteView.setOnClickPendingIntent(R.id.widgetChronometer, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
