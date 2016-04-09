package com.waivelength.ui.activity;

import com.parse.ParseUser;
import com.waivelength.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private ImageButton mTermsButton = null;
	private ImageButton mSignupButton = null;
	private ImageButton mSigninButton = null;
	private ImageButton mfbButton = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
			Intent intent = new Intent(this, TabBarFragmentActivity.class);
			startActivity(intent);
		} 
        
        mTermsButton = (ImageButton)findViewById(R.id.termsButton);
        mTermsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TermsActivity.class);
				startActivity(intent);
			}
		});
        
        mSignupButton = (ImageButton)findViewById(R.id.signupButton);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SignupActivity.class);
				startActivity(intent);
			}
		});

        mSigninButton = (ImageButton)findViewById(R.id.signinButton);
        mSigninButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SigninActivity.class);
				startActivity(intent);
			}
		});

        mfbButton = (ImageButton)findViewById(R.id.fbButton);
        mfbButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
    }
}
