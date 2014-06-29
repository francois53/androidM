package com.example.webview;

import com.example.webview.R;
import com.parse.ParseAnalytics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
 
//Activity for Splash screen
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class splashscreen extends Activity {
final static int DURATION = 2000;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().hide();
   	ParseAnalytics.trackAppOpened(getIntent());
   	
   	if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
    { 
        setContentView(R.layout.splash_screen_landscape);
    }
    else
    {
        setContentView(R.layout.splash_screen);
    }       
}

@Override
protected void onResume() {
    super.onResume();
    splashWelcome(DURATION);
}

//Run the splash screen for given time limit
protected void splashWelcome(final int limit) {
    Thread splashThread = new Thread() {
        @Override
        public void run() {
            try {
                int waited = 0;
                while (waited < limit) {
                    sleep(100);
                    waited += 100;
                }
            } catch (InterruptedException e) {
                Log.d("SplashScreen Error:", e.getMessage().toString());
            } finally {
            	Intent i = new Intent(splashscreen.this, MainActivity.class);
                startActivity(i);
                finish();   
            }

        } 
    };
    splashThread.start();
}
}