package com.citations;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class CitationsWidgetProvider extends AppWidgetProvider
{
	private CitationsManager citationsData;
	private String[] citation; // I need it here because after the FB sharing I
	// must put back the original sentence

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		citationsData = new CitationsManager(context);
		citationsData.setCategoryInUse("inspiringCategory");

		citation = citationsData.getRandomStringInCategory().split("-");

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);
		views.setTextViewText(R.id.layout_appwidget_TextViewSentence,
				citation[0]);
		views.setTextViewText(R.id.layout_appwidget_TextViewAuthor,
				citation[1]);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		
	}


}//end CitationsWidgetProvider
