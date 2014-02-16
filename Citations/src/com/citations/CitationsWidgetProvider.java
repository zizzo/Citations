package com.citations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		CitationsManager citationsData = new CitationsManager(context);
		citationsData.setCategoryInUse("inspiringCategory");
		int categoryNumber = 0;

		String[] citation = citationsData.getRandomStringInCategory()
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
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 0, 4);

			} else if (categoryNumber == 3)
			{
				categoryType = "loveCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 4, 3);

			} else if (categoryNumber == 2)
			{
				categoryType = "politicsCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 3, 2);

			} else if (categoryNumber == 1)
			{
				categoryType = "lifeCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 2, 1);

			} else if (categoryNumber == 0)
			{
				categoryType = "inspiringCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

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
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 4, 0);

			} else if (categoryNumber == 4)
			{
				categoryType = "funCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 3, 4);

			} else if (categoryNumber == 3)
			{
				categoryType = "loveCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 2, 3);

			} else if (categoryNumber == 2)
			{
				categoryType = "politicsCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 1, 2);

			} else if (categoryNumber == 1)
			{
				categoryType = "lifeCategory";
				citationsData.setCategoryInUse(categoryType);
				citation = citationsData.getRandomStringInCategory().split("-");

				setText(layoutAppWidget, citation);

				setColorsOnButtons(layoutAppWidget, 0, 1);
			}

		}// end SET_NEXT_CATEGORY

		else if (intent.getAction().equals(SET_RANDOM_CITATION))
		{
			citationsData.setCategoryInUse(categoryType);
			citation = citationsData.getRandomStringInCategory().split("-");

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
			Bitmap bitmap = drawBitmap(context, citation, categoryType);
			storeImage(bitmap, "imageToShare.png");
			String imagePath = Environment.getExternalStorageDirectory()
					.toString() + File.separator + "imageToShare.png";
			shareOnFb(imagePath, context);

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

	/**
	 * @param context
	 * @return the drawn bitmap
	 */
	private Bitmap drawBitmap(Context context, String[] citation,
			String categoryInUse)
	{

		Bitmap bitmap = Bitmap.createBitmap(500, 300, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		// Paint paint = new Paint();
		// paint.setTextAlign(Align.CENTER);
		// paint.setTextSize(18);

		if (categoryInUse.equals("inspiringCategory"))
			c.drawColor(context.getResources().getColor(
					R.color.inspiringCategoryColor));
		else if (categoryInUse.equals("lifeCategory"))
			c.drawColor(context.getResources().getColor(
					R.color.lifeCategoryColor));
		else if (categoryInUse.equals("politicsCategory"))
			c.drawColor(context.getResources().getColor(
					R.color.politicsCategoryColor));
		else if (categoryInUse.equals("funCategory"))
			c.drawColor(context.getResources().getColor(
					R.color.funCategoryColor));
		else if (categoryInUse.equals("loveCategory"))
			c.drawColor(context.getResources().getColor(
					R.color.loveCategoryColor));

		String bitmapText = citation[0] + "\n" + citation[1];

		TextPaint tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setTextSize(20);
		tp.setTextAlign(Align.CENTER);
		tp.setAntiAlias(true);
		StaticLayout sl = new StaticLayout(bitmapText, tp, 500,
				Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

		// c.translate(250, 150);
		sl.draw(c);
		
		// c.drawText(bitmapText, 250, 150, paint);

		c.drawBitmap(bitmap, 0, 0, null);

		return bitmap;
	}

	/**
	 * @param bitmap
	 * @param filename
	 *            Store the image on the SD card
	 */
	private void storeImage(Bitmap bitmap, String filename)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + filename);
		try
		{
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}// end storeImage

	/**
	 * To share photo facebook
	 * 
	 * @param imagePath
	 */
	private void shareOnFb(String imagePath, Context context)
	{
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(imagePath)));
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		for (final ResolveInfo app : activityList)
		{
			if ((app.activityInfo.name).contains("facebook.katana"))
			{
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				shareIntent.setComponent(name);
				context.startActivity(shareIntent);
				break;
			}
		}

	}// end shareOnFb

}//end CitationsWidgetProvider
