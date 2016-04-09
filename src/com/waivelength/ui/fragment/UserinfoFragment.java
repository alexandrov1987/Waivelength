package com.waivelength.ui.fragment;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.waivelength.R;
import com.waivelength.ui.activity.TabBarFragmentActivity;
import com.waivelength.ui.activity.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class UserinfoFragment extends Fragment {
	
	private TabBarFragmentActivity 	mParentActivity = null;
	private ImageButton				mCloseButton = null;
	private ImageButton				mSaveButton = null;
	private EditText				mEmailText = null;
	private EditText				mNameText = null;
	private EditText				mUsernameText = null;
	private EditText				mCurpwText = null;
	private EditText				mNewpwText = null;
	private EditText				mConfirmpwText = null;
	private boolean					mIsFbUser = false;
	private boolean 				mUserEnteredPassword = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_userinfo, container, false);	
		
		mEmailText = (EditText)v.findViewById(R.id.emailText);
		mNameText = (EditText)v.findViewById(R.id.fullnameText);
		mUsernameText = (EditText)v.findViewById(R.id.usernameText);
		mCurpwText = (EditText)v.findViewById(R.id.pwText);
		mNewpwText = (EditText)v.findViewById(R.id.repwText);
		mConfirmpwText = (EditText)v.findViewById(R.id.confirmpwText);
		
		mCloseButton = (ImageButton)v.findViewById(R.id.closeButton);
		mCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParentActivity.pop();
				mParentActivity.fragmentReplace(mParentActivity.cur());
			}
		});
		
		mSaveButton = (ImageButton)v.findViewById(R.id.saveButton);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickSave();
			}
		});
		
		getUserDetails();
		
        return v;
	}
	
	private void getUserDetails(){
		ParseUser user = ParseUser.getCurrentUser();
		String email = user.getEmail();
		
		this.mEmailText.setText(user.getEmail());
		this.mNameText.setText(user.getString("fullName"));
		this.mUsernameText.setText(user.getString("userWaivelengthName"));
		
		if(user.getString("facebookId") == null){
			
			this.mIsFbUser = true;
			
			this.mNewpwText.setEnabled(false);
			this.mNewpwText.setText(R.string.newpw_hint);
			
			this.mConfirmpwText.setEnabled(false);
			this.mConfirmpwText.setText(R.string.confirmpw_hint);
			
		}else{
			this.mIsFbUser = false;
		}
	}
	
	private void onClickSave(){
		
		if(this.mEmailText.getText().toString().isEmpty()){
			Utility.showErrorAlert(mParentActivity, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mNameText.getText().toString().isEmpty()){
			Utility.showErrorAlert(mParentActivity, "Name!", "Please enter your full name.");
			return;
		}

		if(this.mUsernameText.getText().toString().isEmpty()){
			
			if(ParseUser.getCurrentUser().getString("facebookId").isEmpty()){
				Utility.showErrorAlert(mParentActivity, "Username!", "Please enter a username.");
				return;
			}
		}

		if(!this.mIsFbUser){
			if(!this.mCurpwText.getText().toString().isEmpty()
			&& !this.mNewpwText.getText().toString().isEmpty()
			&& !this.mConfirmpwText.getText().toString().isEmpty()){
				this.mUserEnteredPassword = true;
				
				if(!this.mNewpwText.getText().toString().equals(this.mConfirmpwText.getText().toString())){
					Utility.showErrorAlert(mParentActivity, "Password Mismatch!", "Please check your password and try again.");
					return;
				}
			}else if(this.mConfirmpwText.getText().toString().isEmpty()){
				Utility.showErrorAlert(mParentActivity, "Confirm Password!", "Please confirm your password.");
				return;
			}else if(this.mNewpwText.getText().toString().isEmpty()){
				Utility.showErrorAlert(mParentActivity, "Password!", "Please enter your new password.");
				return;
			}else if(this.mCurpwText.getText().toString().isEmpty()){
				Utility.showErrorAlert(mParentActivity, "Password!", "Please enter your current password.");
				return;
			}
		}
		
		ParseUser user = ParseUser.getCurrentUser();
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		
		if(!this.mIsFbUser && this.mUserEnteredPassword){
			user.setPassword(this.mNewpwText.getText().toString());
		}
		
		user.add("fullName", this.mNameText.getText().toString());
		user.add("userWaivelengthName", this.mUsernameText.getText().toString());
		
		if(Utility.isInternetAvailable(this.mParentActivity)){
			
			if(this.mUserEnteredPassword){
				ParseUser.logInInBackground(user.getEmail(), this.mCurpwText.getText().toString(), new LogInCallback() {
					
					public void done(ParseUser user, ParseException e) {
				    
						if (e == null && user != null) {
				    	 
							user.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									
									if(e != null){
										Utility.showErrorAlert(mParentActivity, "Error!", e.getMessage());
									}else{
										mParentActivity.pop();
										mParentActivity.fragmentReplace(mParentActivity.cur());
									}
								}
							});
						} else {
							Utility.showErrorAlert(mParentActivity, "Password!", "The current password you've entered does not match the one on record.");
						}
					}
				});
			}else{
				user.saveInBackground(new SaveCallback(){

					@Override
					public void done(ParseException e) {
						
						if(e != null){
							Utility.showErrorAlert(mParentActivity, "Error!", e.getMessage());
						}else{
							mParentActivity.pop();
							mParentActivity.fragmentReplace(mParentActivity.cur());
						}
					}
				});
			}
		}else{
			
			Utility.showErrorAlert(mParentActivity, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
}
