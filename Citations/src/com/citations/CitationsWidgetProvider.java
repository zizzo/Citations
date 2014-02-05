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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.widget.RemoteViews;

public class CitationsWidgetProvider extends AppWidgetProvider
{
	private static CitationsManager citationsData;
	private static String[] citation; // I need it here because after the FB
										// sharing I must put back the original
										// sentence

	public static String SHARE_ON_TWITTER = "shareOnTwitter";
	public static String SHARE_ON_FACEBOOK = "shareOnFacebook";
	public static String SHARE_GENERIC = "shareGeneric";

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


		Intent intentFacebook = new Intent(context,
				CitationsWidgetProvider.class);
		intentFacebook.setAction(SHARE_ON_FACEBOOK);
		PendingIntent pendingIntentFacebook = PendingIntent.getBroadcast(
				context, 0, intentFacebook, 0);

		views.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonFacebook,
				pendingIntentFacebook);


		Intent intentShare = new Intent(context, CitationsWidgetProvider.class);
		intentShare.setAction(SHARE_GENERIC);
		PendingIntent pendingIntentShare = PendingIntent.getBroadcast(context,
				0, intentShare, 0);

		views.setOnClickPendingIntent(R.id.layout_appwidgetImageButtonShare,
				pendingIntentShare);

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

		if (intent.getAction().equals(SHARE_ON_FACEBOOK))
		{
			Bitmap bitmap = drawBitmap(context);
			storeImage(bitmap, "imageToShare.png");
			String imagePath = Environment.getExternalStorageDirectory()
					.toString() + File.separator + "imageToShare.png";
			shareOnFb(imagePath, context);

		}

		if (intent.getAction().equals(SHARE_GENERIC))
		{
			String shareMessage = citation[0] + "\n" + citation[1];
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
			context.startActivity(Intent.createChooser(shareIntent, "Share...")
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}

	}// end onReceive


	private Bitmap drawBitmap(Context context)
	{
		Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		Paint paint = new Paint();
		paint.setTextSize(20);

		String categoryInUse = citationsData.getCategoryInUse();
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

		c.drawText(bitmapText, 30, 30, paint);

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
