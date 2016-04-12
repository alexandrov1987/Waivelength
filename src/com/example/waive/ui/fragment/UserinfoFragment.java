package com.example.waive.ui.fragment;

import java.io.File;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.example.waive.R;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class UserinfoFragment extends Fragment {
	
	private static final int 		TAKE_PICTURE = 1;
	private TabBarActivity 	mTab = null;
	private ImageButton				mCloseButton = null;
	private ImageButton				mSaveButton = null;
	private ImageButton				mAddphotoButton = null;
	private CircularImageView		mProfileImageView = null;
	private EditText				mEmailText = null;
	private EditText				mNameText = null;
	private EditText				mUsernameText = null;
	private EditText				mCurpwText = null;
	private EditText				mNewpwText = null;
	private EditText				mConfirmpwText = null;
	private boolean					mIsFbUser = false;
	private boolean 				mUserEnteredPassword = false;
	private int						mWhichButton = 0;
	private Uri 					mImageUri;
	private Bitmap					mBitmap = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_userinfo, container, false);	
		
		mEmailText = (EditText)v.findViewById(R.id.emailText);
		mNameText = (EditText)v.findViewById(R.id.fullnameText);
		mUsernameText = (EditText)v.findViewById(R.id.usernameText);
		mCurpwText = (EditText)v.findViewById(R.id.pwText);
		mNewpwText = (EditText)v.findViewById(R.id.repwText);
		mConfirmpwText = (EditText)v.findViewById(R.id.confirmpwText);

		mProfileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
		mProfileImageView.setVisibility(View.GONE);
		
		mAddphotoButton = (ImageButton)v.findViewById(R.id.addPhotoButton);
		mAddphotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickAddPhoto();
			}
		});

		mCloseButton = (ImageButton)v.findViewById(R.id.closeButton);
		mCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
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
			DialogUtils.showErrorAlert(mTab, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mNameText.getText().toString().isEmpty()){
			DialogUtils.showErrorAlert(mTab, "Name!", "Please enter your full name.");
			return;
		}

		if(this.mUsernameText.getText().toString().isEmpty()){
			
			if(ParseUser.getCurrentUser().getString("facebookId").isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Username!", "Please enter a username.");
				return;
			}
		}

		if(!this.mIsFbUser){
			if(!this.mCurpwText.getText().toString().isEmpty()
			&& !this.mNewpwText.getText().toString().isEmpty()
			&& !this.mConfirmpwText.getText().toString().isEmpty()){
				this.mUserEnteredPassword = true;
				
				if(!this.mNewpwText.getText().toString().equals(this.mConfirmpwText.getText().toString())){
					DialogUtils.showErrorAlert(mTab, "Password Mismatch!", "Please check your password and try again.");
					return;
				}
			}else if(this.mConfirmpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Confirm Password!", "Please confirm your password.");
				return;
			}else if(this.mNewpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Password!", "Please enter your new password.");
				return;
			}else if(this.mCurpwText.getText().toString().isEmpty()){
				DialogUtils.showErrorAlert(mTab, "Password!", "Please enter your current password.");
				return;
			}
		}
		
		DialogUtils.displayProgress(mTab);
		
		ParseUser user = ParseUser.getCurrentUser();
		
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		
		if(!this.mIsFbUser && this.mUserEnteredPassword){
			user.setPassword(this.mNewpwText.getText().toString());
		}
		
		user.add("fullName", this.mNameText.getText().toString());
		user.add("userWaivelengthName", this.mUsernameText.getText().toString());
		
		if(NetworkUtils.isInternetAvailable(this.mTab)){
			
			if(this.mUserEnteredPassword){
				ParseUser.logInInBackground(user.getEmail(), this.mCurpwText.getText().toString(), new LogInCallback() {
					
					public void done(ParseUser user, ParseException e) {
				    
						if (e == null && user != null) {
				    	 
							user.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									
									if(e != null){
										DialogUtils.showErrorAlert(mTab, "Error!", e.getMessage());
									}else{
										mTab.pop();
										mTab.fragmentReplace(mTab.cur());
									}
								}
							});
						} else {
							DialogUtils.showErrorAlert(mTab, "Password!", "The current password you've entered does not match the one on record.");
						}
					}
				});
			}else{
				user.saveInBackground(new SaveCallback(){

					@Override
					public void done(ParseException e) {
						
						if(e != null){
							DialogUtils.showErrorAlert(mTab, "Error!", e.getMessage());
						}else{
							mTab.pop();
							mTab.fragmentReplace(mTab.cur());
						}
					}
				});
			}
		}else{
			
			DialogUtils.showErrorAlert(mTab, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	private void onClickAddPhoto(){
		
		mWhichButton = 0;
		final String items[] = { "Camera", "Photo Library" };
        AlertDialog.Builder ab = new AlertDialog.Builder(mTab);
        ab.setTitle("Add Profile Image");
        ab.setSingleChoiceItems(items, mWhichButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	mWhichButton = whichButton;
                    }
                }).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    	switch(mWhichButton){
                    	case 0:
                    		takePhoto();
                    		break;
                    	case 1:
                    		break;
                    	}
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
	}
	
	public void takePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT,
	            Uri.fromFile(photo));
	    this.mImageUri = Uri.fromFile(photo);
	    startActivityForResult(intent, TAKE_PICTURE);
	}
}
