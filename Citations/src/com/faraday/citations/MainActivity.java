package com.faraday.citations;



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

	// This is the model
	private CitationState state;
	
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private TextView textViewSentence;
	private TextView textViewAuthor;
	private GestureDetectorCompat mDetector;
	
	private final int ANIMATION_DURATION = 200;

	private final double SWIPE_RATIO = 0.5;
	private final String PREFS_DIALOG = "dontShowAgainBool";
	
	private CitationsDB database;
	private CategoryData categoryData;
	
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
		database = new CitationsDB(getApplicationContext());
		categoryData = new CategoryData(getApplicationContext());
		state = new CitationState(getApplicationContext());
		
		drawLayout();
	}


	/**
	 * Customized GestureListener for the Activity
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

				if (dx > 0) {
					changeCitationText(state.nextCitation(),
									   CitationChangeType.SWIPE_RIGHT);
				}
				else {
					changeCitationText(state.previousCitation(),
								       CitationChangeType.SWIPE_LEFT);
				}
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
			// We get the citation Id
			Integer citationId = Integer.parseInt(widgetIntent.getStringExtra("CITATION_ID"));
			Citation cit = database.getCitation(citationId);
			state.setCurrentCitation(cit);
		}
		
		changeCitationText(state.getCurrentCitation(), CitationChangeType.INIT);		
		changeBackgroundColor(categoryData.getColor(state.getCurrentCategory()),
				              categoryData.getColor(state.getCurrentCategory()));
		
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
				ShareHelper.shareGeneric(getApplicationContext(), state.getCurrentCitation());
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
				ShareHelper.shareOnTwitter(getApplicationContext(), state.getCurrentCitation());

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
				ShareHelper.shareOnFacebook(getApplicationContext(), state.getCurrentCitation());
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
				MainActivity.this.changeCategory(Category.fromOrdinal(position));
				mDrawerLayout.closeDrawers();

			}

		});


		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /*
										 * android.R.color.transparent nav
										 * drawer icon. Set it to transparent to
										 * use the ActionBar icon. Set it to
										 * nav_drawer to have the menu indicator
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
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);


		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Set icon according to category
		setIconForCategory(state.getCurrentCategory());

	}// end drawLayout


	private void setIconForCategory(Category category)
	{
		// TODO: this code should be moved in CategoryData
		if (category.equals(Category.INSPIRING))
			getActionBar().setIcon(R.drawable.citations_inspiring);
		else if (category.equals(Category.LOVE))
			getActionBar().setIcon(R.drawable.citations_love);
		else if (category.equals(Category.LIFE))
			getActionBar().setIcon(R.drawable.citations_life);
		else if (category.equals(Category.POLITICS))
			getActionBar().setIcon(R.drawable.citations_politics);
		else if (category.equals(Category.FUN))
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

		if (id == R.id.action_tellfriend)
		{
			tellFriend();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Change the displayed citation text
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void changeCitationText(final Citation citation, CitationChangeType mode)
	{		
		if (mode == CitationChangeType.INIT)
		{
			textViewSentence.setText(citation.getText());
			textViewAuthor.setText(citation.getAuthor());
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

			slideout.setAnimationListener(new Animation.AnimationListener()
			{
				@Override
				public void onAnimationStart(Animation animation)
				{

				}

				@Override
				public void onAnimationEnd(Animation animation)
				{
					textViewSentence.setText(citation.getText());
					textViewSentence.startAnimation(slidein);
					textViewAuthor.setText(citation.getAuthor());
				}


				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}
			});

			textViewSentence.startAnimation(slideout);

		}
	}
	
	/**
	 * Change the background color for the app using a morphing animation
	 * 
	 */
	private void changeBackgroundColor(Integer startColor, Integer endColor) {
		ObjectAnimator anim = ObjectAnimator.ofInt(findViewById(R.id.main_layout),
				"backgroundColor", startColor, endColor);
			anim.setDuration(500);
			anim.setEvaluator(new ArgbEvaluator());
			anim.start();		
	}
	public void changeCategory(Category category)
	{
		// Set the proper background1
		Integer startColor = categoryData.getColor(state.getCurrentCategory());
		state.setCurrentCategory(category);
		this.changeCitationText(state.getCurrentCitation(),
						 		CitationChangeType.INIT);
		
		Integer endColor = categoryData.getColor(category);
		changeBackgroundColor(startColor, endColor);
		
		setIconForCategory(category);
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
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}


	public void tellFriend()
	{
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT,
			getString(R.string.app_link).replace("%26", "&"));
		startActivity(Intent.createChooser(shareIntent, "Share...").addFlags(
			Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}// end MainActivity
