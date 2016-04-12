package com.example.waive;

import com.parse.Parse;
import com.parse.ParseACL;
import android.app.Application;

public class WaivelengthApplication extends Application {
	@Override
    public void onCreate() {
        super.onCreate();
 
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, Define.YOUR_APPLICATION_ID, Define.YOUR_CLIENT_KEY);
        ParseACL.setDefaultACL(new ParseACL(), true);
    }
}
