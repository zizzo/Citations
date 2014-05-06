package com.citations;

import java.util.ArrayList;
import java.util.List;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private CitationsManager citationsData;
	private GestureDetectorCompat mDetector;
	private TextView textViewSentence;
	private TextView textViewAuthor;
	private String[] citation;
	private String categoryType;
	private final String CATEGORY_TYPE = "categoryType";
	private final String CITATION_STRING = "citationString";
	private DrawerLayout mDrawerLayout;
	
	private final int ANIMATION_DURATION = 200;
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
				citation = citationsData
						.getRandomStringInCategory(
						categoryType)
						.split("-");
				setCitation(CitationChangeType.SWIPE_LEFT);
			}
			// swipe up/down
			else if (dyAbs / dxAbs > SWIPE_RATIO)
			{
				String[] citAndCat = citationsData.getRandomString();
				citation = citAndCat[0].split("-");
				categoryType = citAndCat[1];
				setCitation(CitationChangeType.INIT);
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
		View mainLayout = findViewById(R.id.main_layout);
		
		mainLayout.setOnTouchListener(new OnTouchListener () {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
			
		});
		
		textViewSentence = (TextView) findViewById(R.id.activity_main_TextViewSentence);
		textViewAuthor = (TextView) findViewById(R.id.activity_main_TextViewAuthor);


		Intent widgetIntent = getIntent();
		boolean startFromWidget = widgetIntent.getBooleanExtra(
				"start_from_widget", false);
		if (startFromWidget)
		{
			categoryType = widgetIntent.getStringExtra(CATEGORY_TYPE);
			citation = widgetIntent.getStringExtra(CITATION_STRING).split("-");
		} else
		{
			// The first String is going to come from the Inspiring category
			categoryType = "inspiringCategory";
			citation = citationsData.getRandomStringInCategory(categoryType)
					.split("-");
		}
		currentColor = citationsData.getCategoryInUseColor(categoryType);
		setCitation(CitationChangeType.INIT);


		ImageButton buttonShare = (ImageButton) findViewById(R.id.activity_mainImageButtonShare);
		buttonShare.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.3F);
					alpha.setDuration(ANIMATION_DURATION); // Make animation
															// instant
					alpha.setFillAfter(false); // Tell it not to persist after
												// the animation ends
					v.startAnimation(alpha);
				}
				return false;
			}
		});

		buttonShare.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				citationsData.shareGeneric(getApplicationContext(), citation);
			}
		});
		
		ImageButton buttonTwitter = (ImageButton) findViewById(R.id.activity_mainImageButtonTwitter);

		buttonTwitter.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.3F);
					alpha.setDuration(ANIMATION_DURATION); // Make animation
															// instant
					alpha.setFillAfter(false); // Tell it not to persist after
												// the animation ends
					v.startAnimation(alpha);
				}
				return false;
			}
		});

		buttonTwitter.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				citationsData.shareOnTwitter(getApplicationContext(), citation);

			}
		});// end onClick

		ImageButton buttonFacebook = (ImageButton) findViewById(R.id.activity_mainImageButtonFacebook);

		buttonFacebook.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.3F);
					alpha.setDuration(ANIMATION_DURATION); // Make animation
															// instant
					alpha.setFillAfter(false); // Tell it not to persist after
												// the animation ends
					v.startAnimation(alpha);
				}
				return false;
			}
		});

		buttonFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				citationsData.shareOnFacebook(getApplicationContext(),
						citation, categoryType);
			}
		});// end onClick
		
		/* Setup the navigation drawer with all the categories */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        List<String> categoryList = CitationsManager.getCategories();
        
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, categoryList));
		
        
//        // Set the touch listener to handle touches
//        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
//			private boolean isOnDrawer = false;
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// Don't pass the event down
//				return false;
//			}
//		});

	}// end drawLayout

	/**
	 * @param citation
	 *            to be set on the TextViews
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setCitation(CitationChangeType mode)
	{

        if (mode == CitationChangeType.INIT) {
            textViewSentence.setText(citation[0]);
            textViewAuthor.setText(citation[1]);
        }


        if (mode == CitationChangeType.SWIPE_LEFT) {
            final Animation slideout = AnimationUtils.loadAnimation(this, R.anim.slide_text);
            final Animation slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in);


            slidein.setDuration(100);
            slideout.setDuration(100);
            final String[] cittext = citation;

            slideout.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    textViewSentence.setText(cittext[0]);
                    textViewSentence.startAnimation(slidein);
                    textViewAuthor.setText(cittext[1]);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            textViewSentence.startAnimation(slideout);

        }


        // Set the proper background

        Integer startColor = currentColor;
		Integer endColor = citationsData.getCategoryInUseColor(categoryType);
        ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.main_layout), "backgroundColor",
                startColor, endColor);
        anim.setDuration(500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.start();

        currentColor = endColor;

	}// end setCitation

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}// end MainActivity
