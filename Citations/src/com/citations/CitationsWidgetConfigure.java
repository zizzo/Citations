package com.citations;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class CitationsWidgetConfigure extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		int mAppWidgetId = 0;
		if (extras != null)
		{
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// widget conf (optional)

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getApplicationContext());

		RemoteViews views = new RemoteViews(getPackageName(),
				R.layout.layout_appwidget);
		appWidgetManager.updateAppWidget(mAppWidgetId, views);

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();

	}// end onCreate

}// end CitationsWidgetConfigure
