package com.citations;

import java.io.File;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.RemoteViews;

public class CitationsWidgetProvider extends AppWidgetProvider
{
	// private CitationsManager citationsData;
	// private String[] citation;
	// public int categoryNumber; // 0-4 --> inspiring, life, politics,
										// love, fun

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
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		CitationsManager citationsData = new CitationsManager(context);
		int categoryNumber = 0;
		String[] citation = citationsData.getRandomStringInCategory(
				"inspiringCategory")
				.split("-");

		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREF_CITATIONS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(CATEGORY_NUMBER, categoryNumber);
		editor.putString(CITATION_STRING, citation[0] + "-" + citation[1]);
		editor.putString(CATEGORY_TYPE, "inspiringCategory");
		editor.commit();

		RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);

		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewSentence,
				citation[0]);
		layoutAppWidget.setTextViewText(R.id.layout_appwidget_TextViewAuthor,
				citation[1]);

		layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
				"setBackgroundResource", R.color.inspiringCategoryColor);
		layoutAppWidget.setInt(R.id.layout_appwidget_imageViewInspiringPoint,
				"setBackgroundResource", R.color.inspiringCategoryColorActive);

		// Manage citations and categories
		Intent intentPreviousCategory = new Intent(context,
				CitationsWidgetProvider.class);
		intentPreviousCategory.setAction(SET_PREVIOUS_CATEGORY);
		PendingIntent pendingIntentPreviousCategory = PendingIntent
				.getBroadcast(context, 0, intentPreviousCategory, 0);

		layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_imageButtonPrevious,
				pendingIntentPreviousCategory);

		Intent intentNextCategory = new Intent(context,
				CitationsWidgetProvider.class);
		intentNextCategory.setAction(SET_NEXT_CATEGORY);
		PendingIntent pendingIntentNextCategory = PendingIntent.getBroadcast(
				context, 0, intentNextCategory, 0);

		layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_imageButtonNext,
				pendingIntentNextCategory);


		Intent intentRandomCitation = new Intent(context,
				CitationsWidgetProvider.class);
		intentRandomCitation.setAction(SET_RANDOM_CITATION);
		PendingIntent pendingIntentRandomCitation = PendingIntent.getBroadcast(
				context, 0, intentRandomCitation, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.layout_appwidget_imageButtonRefresh,
				pendingIntentRandomCitation);


		// Sharing on the social networks
		Intent intentTwitter = new Intent(context,
				CitationsWidgetProvider.class);
		intentTwitter.setAction(SHARE_ON_TWITTER);
		PendingIntent pendingIntentTwitter = PendingIntent.getBroadcast(
				context, 0, intentTwitter, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonTwitter,
				pendingIntentTwitter);


		Intent intentFacebook = new Intent(context,
				CitationsWidgetProvider.class);
		intentFacebook.setAction(SHARE_ON_FACEBOOK);
		PendingIntent pendingIntentFacebook = PendingIntent.getBroadcast(
				context, 0, intentFacebook, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonFacebook,
				pendingIntentFacebook);


		Intent intentShare = new Intent(context, CitationsWidgetProvider.class);
		intentShare.setAction(SHARE_GENERIC);
		PendingIntent pendingIntentShare = PendingIntent.getBroadcast(context,
				0, intentShare, 0);

		layoutAppWidget.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonShare,
				pendingIntentShare);

		// Open Application
		Intent intentOpenApp = new Intent(context,
				CitationsWidgetProvider.class);
		intentOpenApp.setAction(OPEN_APP);
		PendingIntent pendingIntentOpenApp = PendingIntent.getBroadcast(
				context, 0, intentOpenApp, 0);

		layoutAppWidget.setOnClickPendingIntent(
				R.id.layout_appwidget_linearLayoutText, pendingIntentOpenApp);


		// Update any modification
		appWidgetManager.updateAppWidget(appWidgetIds, layoutAppWidget);
		
	}// end onUpdate


	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);

		CitationsManager citationsData = new CitationsManager(context);
		SharedPreferences settings = context.getSharedPreferences(
				SHARED_PREF_CITATIONS, 0);
		int categoryNumber = settings.getInt(CATEGORY_NUMBER, 0);
		String[] citation = settings.getString(CITATION_STRING, "-")
				.split("-");
		String categoryType = settings.getString(CATEGORY_TYPE, "");

		RemoteViews layoutAppWidget = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);

		if (intent.getAction().equals(SET_PREVIOUS_CATEGORY))
		{

			categoryNumber--;
			// Enter one of these alternatives, set the color of the background,
			// change the one of the active button and restore the previous one
			// to the default
			if (categoryNumber < 0)
			{
				categoryType = "funCategory";
				categoryNumber = 4;
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 0, 4);

			} else if (categoryNumber == 3)
			{
				categoryType = "loveCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 4, 3);

			} else if (categoryNumber == 2)
			{
				categoryType = "politicsCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 3, 2);

			} else if (categoryNumber == 1)
			{
				categoryType = "lifeCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 2, 1);

			} else if (categoryNumber == 0)
			{
				categoryType = "inspiringCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 1, 0);

			}


		}// end SET_PREVIOUS_CATEGORY

		else if (intent.getAction().equals(SET_NEXT_CATEGORY))
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

				setColorsOnButtons(layoutAppWidget, 4, 0);

			} else if (categoryNumber == 4)
			{
				categoryType = "funCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 3, 4);

			} else if (categoryNumber == 3)
			{
				categoryType = "loveCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 2, 3);

			} else if (categoryNumber == 2)
			{
				categoryType = "politicsCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 1, 2);

			} else if (categoryNumber == 1)
			{
				categoryType = "lifeCategory";
				citation = citationsData
						.getRandomStringInCategory(categoryType).split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 0, 1);
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

		else if (intent.getAction().equals(SHARE_ON_TWITTER))
		{

			String tweetText = citation[0] + "\n" + citation[1];
			String tweetUrl = "https://twitter.com/intent/tweet?text="
					+ tweetText
					+ "&related=LuigiTiburzi,gabrielelanaro,Fra_Pochetti";
			Uri uri = Uri.parse(tweetUrl);
			Intent intentTweet = new Intent(Intent.ACTION_VIEW, uri);
			intentTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentTweet);
		}// end SHARE_ON_TWITTER

		else if (intent.getAction().equals(SHARE_ON_FACEBOOK))
		{
			Bitmap bitmap = citationsData.drawBitmap(context, citation,
					categoryType);
			citationsData.storeImage(bitmap, "imageToShare.png");
			String imagePath = Environment.getExternalStorageDirectory()
					.toString() + File.separator + "imageToShare.png";
			citationsData.shareOnFb(imagePath, context);

		}// end SHARE_ON_FACEBOOK

		else if (intent.getAction().equals(SHARE_GENERIC))
		{
			String shareMessage = citation[0] + "\n" + citation[1];
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
			context.startActivity(Intent.createChooser(shareIntent, "Share...")
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}// end SHARE_GENERIC


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
	private void setColorsOnButtons(RemoteViews layoutAppWidget, int previous,
			int current)
	{

		if (previous == 0)
		{
			layoutAppWidget.setInt(
					R.id.layout_appwidget_imageViewInspiringPoint,
					"setBackgroundResource", R.color.inspiringCategoryColor);
		} else if (previous == 1)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewLifePoint,
					"setBackgroundResource", R.color.lifeCategoryColor);

		} else if (previous == 2)
		{
			layoutAppWidget.setInt(
					R.id.layout_appwidget_imageViewPoliticsPoint,
					"setBackgroundResource", R.color.politicsCategoryColor);
		} else if (previous == 3)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewLovePoint,
					"setBackgroundResource", R.color.loveCategoryColor);
		} else if (previous == 4)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewFunPoint,
					"setBackgroundResource", R.color.funCategoryColor);
		}

		if (current == 0)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
					"setBackgroundResource", R.color.inspiringCategoryColor);

			layoutAppWidget.setInt(
					R.id.layout_appwidget_imageViewInspiringPoint,
					"setBackgroundResource",
					R.color.inspiringCategoryColorActive);

		} else if (current == 1)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
					"setBackgroundResource", R.color.lifeCategoryColor);

			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewLifePoint,
					"setBackgroundResource", R.color.lifeCategoryColorActive);

		} else if (current == 2)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
					"setBackgroundResource", R.color.politicsCategoryColor);

			layoutAppWidget.setInt(
					R.id.layout_appwidget_imageViewPoliticsPoint,
					"setBackgroundResource",
					R.color.politicsCategoryColorActive);

		} else if (current == 3)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
					"setBackgroundResource", R.color.loveCategoryColor);

			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewLovePoint,
					"setBackgroundResource", R.color.loveCategoryColorActive);

		} else if (current == 4)
		{
			layoutAppWidget.setInt(R.id.layout_appwidget_linearLayoutText,
					"setBackgroundResource", R.color.funCategoryColor);

			layoutAppWidget.setInt(R.id.layout_appwidget_imageViewFunPoint,
					"setBackgroundResource", R.color.funCategoryColorActive);

		}

	}// end setColorsOnButtons

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
