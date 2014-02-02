package com.citations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class CitationsWidgetProvider extends AppWidgetProvider
{
	private CitationsManager citationsData;
	private static String[] citation; // I need it here because after the FB
										// sharing I must put back the original
										// sentence

	public static String SHARE_ON_TWITTER = "shareOnTwitter";

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

		Intent intentTwitter = new Intent(context,
				CitationsWidgetProvider.class);
		intentTwitter.setAction(SHARE_ON_TWITTER);
		PendingIntent pendingIntentTwitter = PendingIntent.getBroadcast(
				context, 0, intentTwitter, 0);

		views.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonTwitter,
				pendingIntentTwitter);

		appWidgetManager.updateAppWidget(appWidgetIds, views);
		
	}// end onUpdate

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);

		if (intent.getAction().equals(SHARE_ON_TWITTER))
		{

			String tweetText = citation[0] + "\n" + citation[1];
			String tweetUrl = "https://twitter.com/intent/tweet?text="
					+ tweetText
					+ "&related=LuigiTiburzi,gabrielelanaro,Fra_Pochetti";
			Uri uri = Uri.parse(tweetUrl);
			Intent intentTweet = new Intent(Intent.ACTION_VIEW, uri);
			intentTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentTweet);
		}

	}// end onReceive


}//end CitationsWidgetProvider
