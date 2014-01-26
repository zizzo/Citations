package com.citations;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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

		String[] citation = citationsData.getRandomStringInCategory()
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
				storeImage(bitmap, "pippo");
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

	private boolean storeImage(Bitmap imageData, String filename) {
		//get path to external storage (SD card)
		File iconsStoragePath = Environment.getExternalStorageDirectory();
		// + "/myAppDir/myImages/";
		// File sdIconStorageDir = new File(iconsStoragePath);

		//create storage directories, if they don't exist
		// sdIconStorageDir.mkdirs();

		try {
			String filePath = iconsStoragePath.toString() + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.e("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

}// end MainActivity
