package com.citations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private CitationsManager citationsData;
	private GestureDetectorCompat mDetector;
	private TextView textViewSentence;
	private TextView textViewAuthor;
	private String[] citation; // I need it here because after the FB sharing I
								// must put back the original sentence

    private String[] currentCitation;
    private Integer currentColor;
	private final double SWIPE_RATIO = 4.5;

    public enum CitationChangeType {INIT, SWIPE_LEFT, SWIPE_RIGHT};


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
				setCitation(citation, CitationChangeType.SWIPE_LEFT);
			}
			// swipe up/down
			else if (dyAbs / dxAbs > SWIPE_RATIO)
			{
				String[] citation = citationsData.getRandomString().split("-");
				setCitation(citation, CitationChangeType.INIT);
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
        currentColor = citationsData.getCategoryInUseColor();

		citation = citationsData.getRandomStringInCategory()
				.split("-");
        currentCitation = citation;

		setCitation(citation, CitationChangeType.INIT);

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

				Bitmap bitmap = drawBitmap();
				storeImage(bitmap, "imageToShare.png");
				String imagePath = Environment.getExternalStorageDirectory()
						.toString() + File.separator + "imageToShare.png";
				shareOnFb(imagePath);

			}
		});// end onClick


	}// end drawLayout

	/**
	 * @param citation
	 *            to be set on the TextViews
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setCitation(String[] citation, CitationChangeType mode)
	{

        if (mode == CitationChangeType.INIT) {
            textViewSentence.setText(citation[0]);
            textViewAuthor.setText(citation[1]);
        }


        if (mode == CitationChangeType.SWIPE_LEFT) {
            Animation slideout = AnimationUtils.loadAnimation(this, R.anim.slide_text);
            Animation slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in);


            final String cittext = citation[0];

            slidein.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    textViewSentence.setText(cittext);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //textViewSentence.setText(cittext);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            slideout.setDuration(500);
            slidein.setDuration(500);
            AnimationSet as = new AnimationSet(true);

            // as.setStartOffset(250);
            as.addAnimation(slideout);
            //
            as.setStartOffset(1000);
            as.addAnimation(slidein);
            //

            textViewSentence.startAnimation(as);

        }


        Log.d("MAIN", citationsData.getCategoryInUse());
        // Set the proper background

        Integer startColor = currentColor;
        Integer endColor = citationsData.getCategoryInUseColor();
        ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.main_layout), "backgroundColor",
                startColor, endColor);
        anim.setDuration(500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.start();

        currentColor = endColor;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Bitmap drawBitmap()
	{
		Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		Paint paint = new Paint();
		paint.setTextSize(20);

		String categoryInUse = citationsData.getCategoryInUse();

        // TODO: change this to use citationsData.getCategoryInUseColor()
		if (categoryInUse.equals("inspiringCategory"))
			c.drawColor(getResources().getColor(R.color.inspiringCategoryColor));
		else if (categoryInUse.equals("lifeCategory"))
			c.drawColor(getResources().getColor(R.color.lifeCategoryColor));
		else if (categoryInUse.equals("politicsCategory"))
			c.drawColor(getResources().getColor(R.color.politicsCategoryColor));
		else if (categoryInUse.equals("funCategory"))
			c.drawColor(getResources().getColor(R.color.funCategoryColor));
		else if (categoryInUse.equals("loveCategory"))
			c.drawColor(getResources().getColor(R.color.loveCategoryColor));

		String bitmapText = textViewSentence.getText() + "\n"
				+ textViewAuthor.getText();

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
