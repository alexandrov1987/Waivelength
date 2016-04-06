package com.waivelength;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SigninActivity extends Activity {

	private ImageButton mBackButton = null;
	private ImageButton mDoneButton = null;
	private EditText 	mEmailText = null;
	private EditText	mPwText = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        
        mEmailText = (EditText)findViewById(R.id.emailText);
        mPwText = (EditText)findViewById(R.id.pwText);
        
        mBackButton = (ImageButton)findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SigninActivity.this.finish();
			}
		});
        
        mDoneButton = (ImageButton)findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClicDone();
			}
		});
    }
	
	private void onClicDone(){

		String email = this.mEmailText.getText().toString();
		String password = this.mPwText.getText().toString();
		
		if(email.isEmpty()){

			Utility.showErrorAlert(this, "Email!", "Please enter your email.");
			return;
		}
		
		if(password.isEmpty()){

			Utility.showErrorAlert(this, "Password!", "Please enter a password.");
			return;
		}
		
		if(Utility.isInternetAvailable(this)){
			
			ParseUser.logInInBackground(email, 
					password, 
					new LogInCallback() {
				public void done(ParseUser user, ParseException e) {
			     if (e == null && user != null) {
			    	 
						Intent intent = new Intent(SigninActivity.this, TabBarFragmentActivity.class);
						startActivity(intent);
						finish();

			     } else {
			    	 Utility.showErrorAlert(SigninActivity.this, "Error!", e.getMessage());
			     }
			   }
			 });
			
		}else{
			
			Utility.showErrorAlert(SigninActivity.this, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
	

}
