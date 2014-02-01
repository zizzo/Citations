package com.citations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private CitationsManager citationsData;
	private GestureDetectorCompat mDetector;
	private TextView textViewSentence;
	private TextView textViewAuthor;
	private String[] citation; // I need it here because after the FB sharing I
								// must put back the original sentence
	private final double SWIPE_RATIO = 4.5;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());

		citationsData = new CitationsManager(getApplicationContext());

		drawLayout();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * @author luigi Customized GestureListener for the Activity
	 * 
	 */
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener
	{

		@Override
		public boolean onDown(MotionEvent event)
		{
			Log.d("MainActivityOnDown", "onDown: " + event.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY)
		{

			float dxAbs = Math.abs(event2.getX() - event1.getX());
			float dyAbs = Math.abs(event2.getY() - event1.getY());

			Log.d("MainActivityOnFling", "onFling: dx = " + dxAbs);
			Log.d("MainActivityOnFling", "onFling: dy = " + dyAbs);

			// Instead of using the absolute differences (dx && dy) we use
			// proportions as to be portable on screens of different dimensions.
			// swipe left/right
			if (dxAbs / dyAbs > SWIPE_RATIO)
			{
				String[] citation = citationsData.getRandomStringInCategory()
						.split("-");
				setCitation(citation);
			}
			// swipe up/down
			else if (dyAbs / dxAbs > SWIPE_RATIO)
			{
				String[] citation = citationsData.getRandomString().split("-");
				setCitation(citation);
			}
			// swipe not valid
			else
			{

			}

			return true;
		}
	}

	/**
	 * Draw the layout for the main activity
	 */
	private void drawLayout()
	{
		textViewSentence = (TextView) findViewById(R.id.activity_main_TextViewSentence);
		textViewAuthor = (TextView) findViewById(R.id.activity_main_TextViewAuthor);

		// The first String is going to come from the Inspiring category
		citationsData.setCategoryInUse("inspiringCategory");

		citation = citationsData.getRandomStringInCategory()
				.split("-");
		setCitation(citation);

		ImageButton buttonShare = (ImageButton) findViewById(R.id.activity_mainImageButtonShare);
		buttonShare.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String shareMessage = textViewSentence.getText() + "\n"
						+ textViewAuthor.getText();
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain"); 
				shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
				startActivity(Intent.createChooser(shareIntent, "Share..."));
			}
		});
		
		ImageButton buttonTwitter = (ImageButton) findViewById(R.id.activity_mainImageButtonTwitter);
		buttonTwitter.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String tweetText = textViewSentence.getText() + "\n"
						+ textViewAuthor.getText();
				String tweetUrl = "https://twitter.com/intent/tweet?text="
						+ tweetText
						+ "&related=LuigiTiburzi,gabrielelanaro,Fra_Pochetti";
				Uri uri = Uri.parse(tweetUrl);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));

			}
		});// end onClick

		ImageButton buttonFacebook = (ImageButton) findViewById(R.id.activity_mainImageButtonFacebook);
		buttonFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_mainLinearLayout);
				int width = linearLayout.getWidth();
				int height = linearLayout.getHeight();
				Bitmap bitmap = loadBitmapFromView(linearLayout, width, height);
				storeImage(bitmap, "imageToShare.png");
				String imagePath =
 Environment.getExternalStorageDirectory()
						.toString() + File.separator + "imageToShare.png";
				shareOnFb(imagePath);
				setCitation(citation);
			}
		});// end onClick


	}// end drawLayout

	/**
	 * @param citation
	 *            to be set on the TextViews
	 */
	private void setCitation(String[] citation)
	{
		textViewSentence.setText(citation[0]);
		textViewAuthor.setText(citation[1]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Bitmap loadBitmapFromView(View v, int width, int height)
	{
		Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
		v.draw(c);
		return b;
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
	private void shareOnFb(String imagePath)
	{
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(imagePath)));
		PackageManager pm = getPackageManager();
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
				startActivity(shareIntent);
				break;
			}
		}

	}// end shareOnFb


}// end MainActivity
