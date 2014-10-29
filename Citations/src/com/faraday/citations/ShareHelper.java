package com.faraday.citations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

/**
 * Helper for the online sharing functionality
 * 
 * @author Gabriele Lanaro
 *
 */
public class ShareHelper {
	
	public static void shareOnTwitter(Context context, Citation citation)
	{
		String tweetText = citation.getText() + "\n" + citation.getAuthor();
		String tweetUrl = "https://twitter.com/intent/tweet?text=" + tweetText
			+ "&related=LuigiTiburzi,gabrielelanaro,Fra_Pochetti";
		Uri uri = Uri.parse(tweetUrl);
		Intent intentTweet = new Intent(Intent.ACTION_VIEW, uri);
		intentTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentTweet);
	}
	
	public static void shareOnFacebook(Context context, Citation citation)
	{
		Bitmap bitmap = drawBitmap(context, citation);
		String imagePath = storeImage(context, bitmap);
		shareImageOnFacebook(imagePath, context);
	}


	public static void shareGeneric(Context context, Citation citation)
	{
		String shareMessage = citation.getText() + "\n" + citation.getAuthor();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
		context.startActivity(Intent.createChooser(shareIntent, "Share...").addFlags(
			Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	private static Bitmap drawBitmap(Context context, Citation citation)
	{
		CategoryData categoryData = new CategoryData(context);

		Bitmap bitmap = Bitmap.createBitmap(800, 600, Bitmap.Config.ARGB_8888);
		
		Canvas c = new Canvas(bitmap);
		c.drawColor(categoryData.getColor(citation.getCategory()));
		String bitmapText = citation.getText() + "\n\n" + citation.getAuthor();

		TextPaint tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setTextSize(40);
		tp.setTextAlign(Align.LEFT);
		tp.setAntiAlias(true);
		StaticLayout sl = new StaticLayout(bitmapText, tp, 600,
			Layout.Alignment.ALIGN_CENTER, 1, 0, false);

		c.save();
		c.translate(80, 200);
		sl.draw(c);
		c.restore();

		tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setAlpha(100);
		tp.setTypeface(Typeface.DEFAULT_BOLD);
		tp.setTextSize(20);
		tp.setAntiAlias(true);

		c.drawText(context.getString(R.string.bitmap_watermark), 500, 550, tp);

		c.drawBitmap(bitmap, 0, 0, null);

		return bitmap;
	}
	
	private static String storeImage(Context context, Bitmap bitmap)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);


		File f = null;
		try
		{
			f = File
				.createTempFile("citationsImg", ".png", context.getExternalCacheDir());
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

		return f.getAbsolutePath();
	}// end storeImage


	private static void shareImageOnFacebook(String imagePath, Context context)
	{

		Log.d("CitationsManager-ShareOnFb", "sharing the image " + imagePath);
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		// shareIntent.putExtra(Intent.EXTRA_TEXT, "www.google.com");
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
		for (final ResolveInfo app : activityList)
		{

			Log.d("CitationsManager-ShareOnFb", app.activityInfo.name);
			if ((app.activityInfo.name).contains("com.facebook")
				&& !(app.activityInfo.name).contains("messenger")
				&& !(app.activityInfo.name).contains("pages"))
			{
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
					activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				shareIntent.setComponent(name);
				context.startActivity(shareIntent);
				break;
			}
		}

	}// end shareOnFb

}