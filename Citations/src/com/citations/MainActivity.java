package com.citations;



import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;



public class MainActivity extends FragmentActivity
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
	private ActionBarDrawerToggle mDrawerToggle;

	private final int ANIMATION_DURATION = 200;
	private Integer currentColor;
	private final double SWIPE_RATIO = 0.5;

	private final String PREFS_DIALOG = "dontShowAgainBool";


	public enum CitationChangeType
	{
		INIT, SWIPE_LEFT, SWIPE_RIGHT
	};


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
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY)
		{

			float dx = event2.getX() - event1.getX();
			float dy = event2.getY() - event1.getY();
			float dxAbs = Math.abs(dx);
			float dyAbs = Math.abs(dy);

			Log.d("MainActivityOnFling", "onFling: dx = " + dxAbs);
			Log.d("MainActivityOnFling", "onFling: dy = " + dyAbs);

			// Instead of using the absolute differences (dx && dy) we use
			// proportions as to be portable on screens of different dimensions.
			// swipe left/right
			if (dxAbs / dyAbs > SWIPE_RATIO)
			{
				citation = citationsData.getRandomStringInCategory(categoryType).split(
					"-");

				if (dx > 0)
					setCitation(CitationChangeType.SWIPE_RIGHT);
				else
					setCitation(CitationChangeType.SWIPE_LEFT);

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


		SharedPreferences showDialogPrefs = getSharedPreferences(PREFS_DIALOG,
			Context.MODE_MULTI_PROCESS);
		boolean showDialog = showDialogPrefs.getBoolean("showDialog", true);

		// Explain how to get the actions
		if (showDialog)
		{
			Log.d("MainActivity-drawLayout", "Show dialog to explain usage");
			DialogFragment dontShowAgain = new DontShowAgain();
			dontShowAgain.show(getSupportFragmentManager(), "DontShowAgain Instructions");
		}


		mainLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (mDetector.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}

		});

		textViewSentence = (TextView) findViewById(R.id.activity_main_TextViewSentence);
		textViewAuthor = (TextView) findViewById(R.id.activity_main_TextViewAuthor);


		Intent widgetIntent = getIntent();
		boolean startFromWidget = widgetIntent
			.getBooleanExtra("start_from_widget", false);
		if (startFromWidget)
		{
			categoryType = widgetIntent.getStringExtra(CATEGORY_TYPE);
			citation = widgetIntent.getStringExtra(CITATION_STRING).split("-");
		} else
		{
			// The first String is going to come from the Inspiring category
			categoryType = "inspiringCategory";
			citation = citationsData.getRandomStringInCategory(categoryType).split("-");
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
				citationsData.shareOnFacebook(getApplicationContext(), citation,
					categoryType);
			}
		});// end onClick

		/* Setup the navigation drawer with all the categories */
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		final ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// List<String> categoryList = CitationsManager.getCategories();
		Integer[] catList = { 0, 1, 2, 3, 4 };
		// Set the adapter for the list view
		mDrawerList.setAdapter(new MenuAdapter(this, R.layout.drawer_list_item, catList));


		mDrawerList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				String cat = CitationsManager.getCategories().get(position);
				MainActivity.this.changeCategory(cat);
				mDrawerLayout.closeDrawers();

			}

		});


		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		android.R.color.transparent, /*
									 * nav drawer icon. Set it to transparent to
									 * use the ActionBar icon
									 */
		R.string.app_name, /* "open drawer" description */
		R.string.app_name /* "close drawer" description */
		)
		{


			/** Called when a drawer has settled in a completely closed state. */
			@Override
			public void onDrawerClosed(View view)
			{
				super.onDrawerClosed(view);
				getActionBar().setTitle(getString(R.string.app_name));
			}


			/** Called when a drawer has settled in a completely open state. */
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(getString(R.string.app_name));
				// drawerImageRes = R.drawable.citations_love;
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);


		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Set icon according to category
		setIconForCategory(categoryType);

	}// end drawLayout


	private void setIconForCategory(String categoryType)
	{
		if (categoryType.equals("inspiringCategory"))
			getActionBar().setIcon(R.drawable.citations_inspiring);
		else if (categoryType.equals("loveCategory"))
			getActionBar().setIcon(R.drawable.citations_love);
		else if (categoryType.equals("lifeCategory"))
			getActionBar().setIcon(R.drawable.citations_life);
		else if (categoryType.equals("politicsCategory"))
			getActionBar().setIcon(R.drawable.citations_politics);
		else if (categoryType.equals("funCategory"))
			getActionBar().setIcon(R.drawable.citations_fun);

	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle your other action bar items...

		int id = item.getItemId();
		if (id == R.id.action_about)
		{
			showAbout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	/**
	 * @param citation
	 *            to be set on the TextViews
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setCitation(CitationChangeType mode)
	{

		if (mode == CitationChangeType.INIT)
		{
			textViewSentence.setText(citation[0]);
			textViewAuthor.setText(citation[1]);
		}


		if (mode == CitationChangeType.SWIPE_LEFT
			|| mode == CitationChangeType.SWIPE_RIGHT)
		{

			final Animation slidein, slideout;

			if (mode == CitationChangeType.SWIPE_LEFT)
			{
				slideout = AnimationUtils.loadAnimation(this, R.anim.slide_text_left);
				slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
			} else
			{
				slideout = AnimationUtils.loadAnimation(this, R.anim.slide_text_right);
				slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
			}

			slidein.setDuration(100);
			slideout.setDuration(100);
			final String[] cittext = citation;

			slideout.setAnimationListener(new Animation.AnimationListener()
			{
				@Override
				public void onAnimationStart(Animation animation)
				{

				}


				@Override
				public void onAnimationEnd(Animation animation)
				{
					textViewSentence.setText(cittext[0]);
					textViewSentence.startAnimation(slidein);
					textViewAuthor.setText(cittext[1]);
				}


				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}
			});

			textViewSentence.startAnimation(slideout);

		}


		// Set the proper background

		Integer startColor = currentColor;
		Integer endColor = citationsData.getCategoryInUseColor(categoryType);
		ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.main_layout),
			"backgroundColor", startColor, endColor);
		anim.setDuration(500);
		anim.setEvaluator(new ArgbEvaluator());
		anim.start();

		currentColor = endColor;

	}// end setCitation


	public void changeCategory(String catId)
	{

		categoryType = catId;
		citation = citationsData.getRandomStringInCategory(categoryType).split("-");
		setCitation(CitationChangeType.INIT);

		// Set the proper background1
		Integer startColor = currentColor;
		Integer endColor = citationsData.getCategoryInUseColor(categoryType);
		ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.main_layout),
			"backgroundColor", startColor, endColor);
		anim.setDuration(500);
		anim.setEvaluator(new ArgbEvaluator());
		anim.start();

		setIconForCategory(categoryType);

		currentColor = endColor;

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void showAbout()
	{
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

}// end MainActivity
