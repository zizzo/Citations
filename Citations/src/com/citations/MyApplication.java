package com.citations;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "",
 resToastText = R.string.crash_toast_text, mailTo = "luigi.tiburzi@outlook.com, gabriele.lanaro@gmail.com", mode = ReportingInteractionMode.TOAST, logcatArguments = {
		"-t", "100", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" })

public class MyApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		// The following line triggers the initialization of ACRA
		ACRA.init(this);
	}
}