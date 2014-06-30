package com.citations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public static final String CATEGORY_TYPE = "categoryType";
	public static final String CATEGORY_NUMBER = "categoryNumber";
	public static final String CITATION_STRING = "citationString";

	public static final String OPEN_APP = "openApp";

	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		Log.d("Widget-onUpdate", "Starting onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("Widget-onUpdate", "Called super");
		
		CitationsManager citationsData = new CitationsManager(context);
		int categoryNumber = 0;
		String[] citation = citationsData.getRandomStringInCategory(
				"inspiringCategory")
				.split("-");

		Log.d("Widget-onUpdate", "Got starting citation: %s");

		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREF_CITATIONS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(CATEGORY_NUMBER, categoryNumber);
		editor.putString(CITATION_STRING, citation[0] + "-" + citation[1]);
		editor.putString(CATEGORY_TYPE, "inspiringCategory");
		editor.commit();
		
		Log.d("Widget-onUpdate", "citation saved in SharedPreferences");

		RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);

		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewSentence,
				citation[0]);
		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewAuthor,
				citation[1]);


		Log.d("Widget-onUpdate", "Correctly set citation");


		Intent intentRandomCitation = new Intent(context,
				CitationsWidgetProvider.class);
		intentRandomCitation.setAction(SET_RANDOM_CITATION);
		PendingIntent pendingIntentRandomCitation = PendingIntent.getBroadcast(
				context, 0, intentRandomCitation, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.widget_refresh_button,
				pendingIntentRandomCitation);


		/* Connect the button that change the category  */
		Intent intentChangeCategory = new Intent(context,
				CitationsWidgetProvider.class);
		intentChangeCategory.setAction(SET_NEXT_CATEGORY);
		PendingIntent pendingIntentChangeCategory = PendingIntent.getBroadcast(
				context, 0, intentChangeCategory, 0);
		
		layoutAppWidget.setOnClickPendingIntent(R.id.widget_category_button,
				pendingIntentChangeCategory);
		
		// Sharing on the social networks
		Intent intentTwitter = new Intent(context,
				CitationsWidgetProvider.class);
		intentTwitter.setAction(SHARE_ON_TWITTER);
		PendingIntent pendingIntentTwitter = PendingIntent.getBroadcast(
				context, 0, intentTwitter, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.widget_twitter_button,
				pendingIntentTwitter);


		Intent intentFacebook = new Intent(context,
				CitationsWidgetProvider.class);
		intentFacebook.setAction(SHARE_ON_FACEBOOK);
		PendingIntent pendingIntentFacebook = PendingIntent.getBroadcast(
				context, 0, intentFacebook, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.widget_facebook_button,
				pendingIntentFacebook);


		Intent intentShare = new Intent(context, CitationsWidgetProvider.class);
		intentShare.setAction(SHARE_GENERIC);
		PendingIntent pendingIntentShare = PendingIntent.getBroadcast(context,
				0, intentShare, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.widget_share_button,
				pendingIntentShare);

		// Open Application
		Intent intentOpenApp = new Intent(context,
				CitationsWidgetProvider.class);
		intentOpenApp.setAction(OPEN_APP);
		PendingIntent pendingIntentOpenApp = PendingIntent.getBroadcast(
				context, 0, intentOpenApp, 0);

		layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_TextViewSentence, pendingIntentOpenApp);
		
		layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_TextViewAuthor, pendingIntentOpenApp);

		// Update any modification
		appWidgetManager.updateAppWidget(appWidgetIds, layoutAppWidget);
		
	}// end onUpdate


	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("Widget-onReceive", "Entering onreceive");
		super.onReceive(context, intent);
		
		Log.d("Widget-onReceive", "Starting action "+intent.getAction());
		
		if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED)) {
			return;
		}
		
		else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_DELETED))
		{
			return;
		}
		
		Log.d("Widget-onReceive", "Create and manage preferences");
		CitationsManager citationsData = new CitationsManager(context);
		Log.d("Widget-onReceive", "Stop1");
		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREF_CITATIONS, 0);
		Log.d("Widget-onReceive", "Stop2");
		int categoryNumber = settings.getInt(CATEGORY_NUMBER, 0);
		String[] citation = settings.getString(CITATION_STRING, "-")
				.split("-");

		String categoryType = settings.getString(CATEGORY_TYPE, "");
		Log.d("Widget-onReceive", "Stop4");
		RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);
		Log.d("Widget-onReceive", "Stop5");
		setColorsOnButtons(context, layoutAppWidget, categoryType);
		Log.d("Widget-onReceive", "Stop6");
//		Log.d("Widget-onReceive",
//				String.format(
//						"Called onReceive, before the action the variables' values are: citation: %s category: %s action: %s",
//						citation[0] + citation[1], categoryType,
//						intent.getAction()));

		
		if (intent.getAction().equals(SET_NEXT_CATEGORY))
		{	
			categoryNumber++;
			// Enter one of these alternatives, set the color of the background,
			// change the one of the active button and restore the previous one
			// to the default
			if (categoryNumber > 4)
			{
				categoryType = "inspiringCategory";
				categoryNumber = 0;
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(context, layoutAppWidget, categoryType);

			} else if (categoryNumber == 4)
			{
				categoryType = "funCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(context, layoutAppWidget, categoryType);

			} else if (categoryNumber == 3)
			{
				categoryType = "loveCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(context, layoutAppWidget, categoryType);

			} else if (categoryNumber == 2)
			{
				categoryType = "politicsCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(context, layoutAppWidget, categoryType);

			} else if (categoryNumber == 1)
			{
				categoryType = "lifeCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(context, layoutAppWidget, categoryType);
			}

		}// end SET_NEXT_CATEGORY

		
		else if (intent.getAction().equals(SET_RANDOM_CITATION))
		{
			citation = citationsData.getRandomStringInCategory(categoryType)
					.split("-");

			layoutAppWidget.setTextViewText(
					R.id.layout_appwidget_TextViewSentence,
					citation[0]);
			layoutAppWidget.setTextViewText(
					R.id.layout_appwidget_TextViewAuthor,
					citation[1]);

		}// end SET_RANDOM_CITATION


		else if (intent.getAction().equals(OPEN_APP))
		{
			Intent appIntent = new Intent(context, MainActivity.class);
			appIntent.putExtra("start_from_widget", true);
			appIntent
					.putExtra(CITATION_STRING, citation[0] + "-" + citation[1]);
			appIntent.putExtra(CATEGORY_TYPE, categoryType);

			appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(appIntent);
		}
		
		else if (intent.getAction().equals(SHARE_ON_TWITTER))
		{
			citationsData.shareOnTwitter(context, citation);
		}// end SHARE_ON_TWITTER

		else if (intent.getAction().equals(SHARE_ON_FACEBOOK))
		{
			citationsData.shareOnFacebook(context, citation, categoryType);
		}// end SHARE_ON_FACEBOOK

		else if (intent.getAction().equals(SHARE_GENERIC))
		{
			citationsData.shareGeneric(context, citation);

		}// end SHARE_GENERIC


		// Update SharedPreferences
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(CITATION_STRING, citation[0] + "-" + citation[1]);
		editor.putString(CATEGORY_TYPE, categoryType);
		editor.putInt(CATEGORY_NUMBER, categoryNumber);
		editor.commit();

		// Update the widget
		ComponentName componentName = new ComponentName(context,
				CitationsWidgetProvider.class);
		AppWidgetManager.getInstance(context).updateAppWidget(componentName,
				layoutAppWidget);
	}// end onReceive

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
	private void setColorsOnButtons(Context context, RemoteViews layoutAppWidget, String categoryType)
	{
		layoutAppWidget.setImageViewBitmap(R.id.widget_category_button, CitationsManager.getCategoryBitmap(categoryType));
	}// end setColorsOnButtons

	
	private void nextCategory() {
		
	}
	
	private void nextCitation() {
		
	}
	/**
	 * @param layoutAppWidget
	 *            set the proper text on the widget
	 */
	private void setText(RemoteViews layoutAppWidget, String[] citation)
	{
		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewSentence,
				citation[0]);
		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewAuthor,
				citation[1]);
	}


}//end CitationsWidgetProvider
