package com.citations;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;



public class MainActivity extends Activity
{
	private CitationsManager citationsData;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		citationsData = new CitationsManager(getApplicationContext());

		drawLayout();
	}


	/**
	 * Draw the layout for the main activity
	 */
	private void drawLayout()
	{
		citationsData.setCategoryInUse("inspiringCategory");

		String[] citation = citationsData.getRandomStringInCategory().split("-");

		TextView textView = (TextView) findViewById(R.id.activity_main_TextViewSentence);
		textView.setText(citation[0]);

		textView = (TextView) findViewById(R.id.activity_main_TextViewAuthor);
		textView.setText(citation[1]);

	}// end drawLayout




	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
