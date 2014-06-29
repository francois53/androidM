package com.example.webview;

import com.example.webview.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.ParseUser;
import android.app.Application;

public class webview extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Add your initialization code here
		Parse.initialize(this, getString(R.string.parse_appid), getString(R.string.parse_clientkey));


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
	
}

