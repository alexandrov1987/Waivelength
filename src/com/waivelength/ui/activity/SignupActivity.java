package com.waivelength.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.waivelength.R;
import com.waivelength.utils.DialogUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SignupActivity extends Activity {

	private static final int TAKE_PICTURE = 1;  
	
	private ImageButton mBackButton = null;
	private ImageButton mDoneButton = null;
	private ImageView 	mAddphotoButton = null;
	private EditText 	mEmailText = null;
	private EditText	mFullnameText = null;
	private EditText	mUsernameText = null;
	private EditText	mPwText = null;
	private EditText	mRepwText = null;
	
	private int			mWhichButton = 0;
	private Uri 		mImageUri;
	private Bitmap		mBitmap = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        mEmailText = (EditText)findViewById(R.id.emailText);
        mFullnameText = (EditText)findViewById(R.id.fullnameText);
        mUsernameText = (EditText)findViewById(R.id.usernameText);
        mPwText = (EditText)findViewById(R.id.pwText);
        mRepwText = (EditText)findViewById(R.id.repwText);
        
        mAddphotoButton = (ImageView)findViewById(R.id.addPhotoButton);
        mAddphotoButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					mAddphotoButton.setBackgroundResource(R.drawable.add_photo_pressed);
					break;
				case MotionEvent.ACTION_UP:
					mAddphotoButton.setBackgroundResource(R.drawable.add_photo);
					onClickAddPhoto();
					break;
				}
				
				return true;
			}
		});
        
        mBackButton = (ImageButton)findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SignupActivity.this.finish();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            
	        	Uri selectedImage = this.mImageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	            ContentResolver cr = getContentResolver();
	            try {
	            	this.mBitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
	                this.mAddphotoButton.setImageBitmap(this.mBitmap);
	                Toast.makeText(this, selectedImage.toString(),
	                        Toast.LENGTH_LONG).show();
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	            }
	        }
	    }
	}



	private void onClicDone(){
		
		if(this.mEmailText.getText().toString().isEmpty()){

			Utility.showErrorAlert(this, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mFullnameText.getText().toString().isEmpty()){

			Utility.showErrorAlert(this, "Full Name!", "Please enter your full name.");
			return;
		}
		
		if(this.mUsernameText.getText().toString().isEmpty()){

			Utility.showErrorAlert(this, "Username!", "Please enter a username.");
			return;
		}

		if(this.mPwText.getText().toString().isEmpty()){

			Utility.showErrorAlert(this, "Password!", "Please enter a password.");
			return;
		}
		
		if(this.mRepwText.getText().toString().isEmpty()){

			Utility.showErrorAlert(this, "Confirm Password!", "Please confirm your password.");
			return;
		}

		if(!this.mPwText.getText().toString().equals(this.mRepwText.getText().toString())){

			Utility.showErrorAlert(this, "Password Mismatch", "Please check your password and try again.");
			return;
		}
		
		final ParseUser user = new ParseUser();
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		user.setPassword(this.mPwText.getText().toString());
		user.add("fullName", this.mFullnameText.getText().toString());
		user.add("userWaivelengthName", this.mUsernameText.getText().toString());
		user.add("profileImage", conversionBitmapParseFile(this.mBitmap));
		
		if(Utility.isInternetAvailable(this)){
			
			DialogUtils.displayProgress(this);
			
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					
					DialogUtils.closeProgress();
					
					if (e == null) {
						ParseObject followersObject = new ParseObject("Followers");
						followersObject.add("user", user);
						followersObject.saveInBackground();
						
						Intent intent = new Intent(SignupActivity.this, TabBarFragmentActivity.class);
						startActivity(intent);
						finish();
						
					} else {

						Utility.showErrorAlert(SignupActivity.this, "Error!", e.getMessage());
					}
					
					
				}
			});
		}else{
			
			Utility.showErrorAlert(SignupActivity.this, "No Network!", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	private void onClickAddPhoto(){
		
		mWhichButton = 0;
		final String items[] = { "Camera", "Photo Library" };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
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

	public ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
		byte[] imageByte = byteArrayOutputStream.toByteArray();
		ParseFile parseFile = new ParseFile("profileImage.png",imageByte);
		return parseFile;
	}
}
