package com.faraday.citations;



import com.faraday.citations.R;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;



public class CitationsWidgetProvider extends AppWidgetProvider
{
	public static final String SHARE_ON_TWITTER = "shareOnTwitter";
	public static final String SHARE_ON_FACEBOOK = "shareOnFacebook";
	public static final String SHARE_GENERIC = "shareGeneric";

	public static final String SET_RANDOM_CITATION = "setRandomCitation";
	public static final String SET_NEXT_CATEGORY = "setNextCategory";
	public static final String SET_PREVIOUS_CATEGORY = "setPreviousCategory";

	public static final String SHARED_PREF_CITATIONS = "sharedPrefsCitations";
	public static final String CATEGORY_NUMBER = "categoryNumber";
	public static final String CITATION_ID = "citationId";

	public static final String OPEN_APP = "openApp";



	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		int[] appWidgetIds)
	{
		Log.d("Widget-onUpdate", "Starting onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("Widget-onUpdate", "Called super");

		context.startService(new Intent(context, MyUpdateService.class));



	}// end onUpdate


	public static class MyUpdateService extends Service
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.app.Service#onBind(android.content.Intent)
		 */
		@Override
		public IBinder onBind(Intent intent)
		{
			return null;
		}


		@SuppressWarnings("deprecation")
		@Override
		public void onStart(Intent intent, int startId)
		{
			Log.d("Widget-onStart Service", "Start Service");
			super.onStart(intent, startId);

			Context context = this;

			RemoteViews layoutAppWidget = buildRemoteView(context);

			// Update any modification
			pushUpdate(layoutAppWidget);
		}


		private void pushUpdate(RemoteViews remoteView)
		{
			ComponentName myWidget = new ComponentName(this,
				CitationsWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(myWidget, remoteView);
		}


		@Override
		public void onConfigurationChanged(Configuration newConfig)
		{
			Log.d("Widget-onConfigurationChanged Service", "Configuration changed");
			int oldOrientation = getResources().getConfiguration().orientation;

			// Update the widget
			RemoteViews remoteView = buildRemoteView(this);

			// Push update to homescreen
			pushUpdate(remoteView);

		}


		private RemoteViews buildRemoteView(Context context)
		{
			CitationsDB database = new CitationsDB(context);
			
			// MODE_MULTI_PROCESS fundamental flag to have SharedPreferences in
			// common for services and activities
			SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREF_CITATIONS, Context.MODE_MULTI_PROCESS);
			RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);
			
			refreshWidget(context, database, settings, layoutAppWidget);
			
			
			/* Linking the actions to the buttons */
			Intent intentRandomCitation = new Intent(context,
				CitationsWidgetProvider.class);
			intentRandomCitation.setAction(SET_RANDOM_CITATION);
			PendingIntent pendingIntentRandomCitation = PendingIntent.getBroadcast(
				context, 0, intentRandomCitation, 0);

			layoutAppWidget.setOnClickPendingIntent(R.id.widget_refresh_button,
				pendingIntentRandomCitation);


			/* Connect the button that change the category */
			Intent intentChangeCategory = new Intent(context,
				CitationsWidgetProvider.class);
			intentChangeCategory.setAction(SET_NEXT_CATEGORY);
			PendingIntent pendingIntentChangeCategory = PendingIntent.getBroadcast(
				context, 0, intentChangeCategory, 0);

			layoutAppWidget.setOnClickPendingIntent(R.id.widget_category_button,
				pendingIntentChangeCategory);

			// Sharing on the social networks
			Intent intentTwitter = new Intent(context, CitationsWidgetProvider.class);
			intentTwitter.setAction(SHARE_ON_TWITTER);
			PendingIntent pendingIntentTwitter = PendingIntent.getBroadcast(context, 0,
				intentTwitter, 0);

			layoutAppWidget.setOnClickPendingIntent(R.id.widget_twitter_button,
				pendingIntentTwitter);


			Intent intentFacebook = new Intent(context, CitationsWidgetProvider.class);
			intentFacebook.setAction(SHARE_ON_FACEBOOK);
			PendingIntent pendingIntentFacebook = PendingIntent.getBroadcast(context, 0,
				intentFacebook, 0);

			layoutAppWidget.setOnClickPendingIntent(R.id.widget_facebook_button,
				pendingIntentFacebook);


			Intent intentShare = new Intent(context, CitationsWidgetProvider.class);
			intentShare.setAction(SHARE_GENERIC);
			PendingIntent pendingIntentShare = PendingIntent.getBroadcast(context, 0,
				intentShare, 0);

			layoutAppWidget.setOnClickPendingIntent(R.id.widget_share_button,
				pendingIntentShare);

			// Open Application
			Intent intentOpenApp = new Intent(context, CitationsWidgetProvider.class);
			intentOpenApp.setAction(OPEN_APP);
			PendingIntent pendingIntentOpenApp = PendingIntent.getBroadcast(context, 0,
				intentOpenApp, 0);

			layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_TextViewSentence, pendingIntentOpenApp);

			layoutAppWidget.setOnClickPendingIntent(R.id.layout_appwidget_TextViewAuthor,
				pendingIntentOpenApp);

			return layoutAppWidget;
		}
	}// end Service


	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("Widget-onReceive", "Starting action " + intent.getAction());
		super.onReceive(context, intent);

		CitationsDB database = new CitationsDB(context);
		SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_CITATIONS,
																  Context.MODE_MULTI_PROCESS);
		RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
													  R.layout.layout_appwidget);
		
		if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED))
		{
			return;
		}

		else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DELETED))
		{
			return;
		}

		else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			refreshWidget(context, database, settings, layoutAppWidget);
		}
		else if (intent.getAction().equals(OPEN_APP))
		{
			// Get the current displayed citation

			Integer citationId = settings.getInt(CITATION_ID, -1);
			Citation citation = database.getCitation(citationId);
			
			// Creating and starting the intent
			Intent appIntent = new Intent(context, MainActivity.class);
			appIntent.putExtra("start_from_widget", true);
			appIntent.putExtra("CITATION_ID", String.valueOf(citation.getId()));
			
			appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(appIntent);
		}
		
		else if (intent.getAction().equals(SET_NEXT_CATEGORY))
		{
			// Cycle categories
			int categoryNumber = database.getCitation(settings.getInt(CITATION_ID, -1)).getCategory().ordinal();
			categoryNumber = (categoryNumber + 1) % 5;
			Citation citation = database.getRandomCitation(Category.fromOrdinal(categoryNumber));
			setText(layoutAppWidget, citation.getText(), citation.getAuthor());
			setColorsOnButtons(context, layoutAppWidget, citation.getCategory());
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(CITATION_ID, citation.getId());
			editor.commit();
		}
		else if (intent.getAction().equals(SET_RANDOM_CITATION))
		{
			Category category = database.getCitation(settings.getInt(CITATION_ID, -1)).getCategory();
			Citation citation = database.getRandomCitation(category);
			setText(layoutAppWidget, citation.getText(), citation.getAuthor());
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(CITATION_ID, citation.getId());
			editor.commit();
		}

		else if (intent.getAction().equals(SHARE_ON_TWITTER))
		{
			Citation citation = database.getCitation(settings.getInt(CITATION_ID, -1));
			ShareHelper.shareOnTwitter(context, citation);
		}

		else if (intent.getAction().equals(SHARE_ON_FACEBOOK))
		{
			Citation citation = database.getCitation(settings.getInt(CITATION_ID, -1));
			ShareHelper.shareOnFacebook(context, citation);
		}

		else if (intent.getAction().equals(SHARE_GENERIC))
		{
			Citation citation = database.getCitation(settings.getInt(CITATION_ID, -1));
			ShareHelper.shareGeneric(context, citation);

		}

		// Update the widget
		ComponentName componentName = new ComponentName(context,
			CitationsWidgetProvider.class);
		AppWidgetManager.getInstance(context).updateAppWidget(componentName,
			layoutAppWidget);
	}


	private static void refreshWidget(Context context, CitationsDB database,
			SharedPreferences settings, RemoteViews layoutAppWidget) {
		// This is called at intervals (also when it starts)
		
		int citationId = settings.getInt(CITATION_ID, -1);
		Citation citation;
		// The widget was just opened because citationId was not defined
		if (citationId == -1) 
		{
			// Get a random string from a category
			citation = database.getRandomCitation(Category.INSPIRING);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(CITATION_ID, citation.getId());
			editor.commit();
		}
		else // The widget is just refreshing
		{
			citation = database.getCitation(citationId);
		}
		
		// Make Interface Changes
		
		setColorsOnButtons(context, layoutAppWidget, citation.getCategory());	
		setText(layoutAppWidget, citation.getText(), citation.getAuthor());
	}


	/**
	 * @param layoutAppWidget
	 * @param previous
	 *            the number of the previous category 0-4 --> inspiring, life,
	 *            politics, love, fun
	 * @param current
	 *            the number of the current category 0-4 --> inspiring, life,
	 *            politics, love, fun
	 * 
	 *            set the proper colors on the small point in the widget
	 */
	private static void setColorsOnButtons(Context context, 
									RemoteViews layoutAppWidget,
									Category category)
	{
		CategoryData categoryData = new CategoryData(context);
		
		layoutAppWidget.setImageViewBitmap(R.id.widget_category_button,
			categoryData.getIcon(category));

		int c = categoryData.getColor(category);
		layoutAppWidget.setInt(R.id.widget_textwrapper, "setBackgroundColor",
			Color.argb(204, Color.red(c), Color.green(c), Color.blue(c)));
	}// end setColorsOnButtons


	/**
	 * @param layoutAppWidget
	 *            set the proper text on the widget
	 */
	private static void setText(RemoteViews layoutAppWidget, String text, String author)
	{
		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewSentence,
			text);
		layoutAppWidget
			.setTextViewText(R.id.layout_appwidget_TextViewAuthor, author);
	}


}// end CitationsWidgetProvider
