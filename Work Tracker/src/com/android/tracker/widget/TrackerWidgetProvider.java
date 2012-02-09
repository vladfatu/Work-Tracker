package com.android.tracker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.android.tracker.R;
import com.android.tracker.ui.WorkTrackerActivity;

public class TrackerWidgetProvider extends AppWidgetProvider {

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
		// TODO Auto-generated method stub
		super.onEnabled(context);
	} 

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{

		for (int appWidgetId : appWidgetIds)
		{

			Intent intent = new Intent(context, WorkTrackerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
			remoteView.setChronometer(R.id.widgetChronometer, SystemClock.elapsedRealtime(), null, true);
			remoteView.setOnClickPendingIntent(R.id.widgetChronometer, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, remoteView);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
