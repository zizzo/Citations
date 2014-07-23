package com.citations;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;




public class DontShowAgain extends DialogFragment
{
	private final String PREFS_DIALOG = "dontShowAgainBool";
	private final int ANIMATION_DURATION = 200;
	LayoutInflater inflater;
	View view;


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{

		inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.alertdialog, null);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);

		TextView title = (TextView) view.findViewById(R.id.checkbox_title);
		title.setText(R.string.dialog_usage_title);

		TextView message = (TextView) view.findViewById(R.id.checkbox_message);
		message.setText(R.string.dialog_usage_message);

		Button button = (Button) view.findViewById(R.id.checkbox_button);

		button.setOnTouchListener(new OnTouchListener()
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

		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Log.d("DontShowAgain-onClick", "Click OK button");

				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_checkbox);

				Log.d("DontShowAgain-onClick", "Checkbox value: " + checkBox.isChecked());
				if (checkBox.isChecked())
				{
					SharedPreferences settings = getActivity().getSharedPreferences(
						PREFS_DIALOG, Context.MODE_MULTI_PROCESS);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("showDialog", false);

					editor.commit();
					Log.d("DontShowAgain-onClick", "Completed preference commit");
				}
				dismiss();
			}
		});


		builder.setCancelable(false);

		// Create the AlertDialog object and return it
		return builder.create();
	}// end onCreateDialog
}
