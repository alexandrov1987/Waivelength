package com.example.waive.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.example.waive.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
	
	private ImageButton 		mAddphotoButton = null;
	private EditText 			mEmailText = null;
	private EditText			mFullnameText = null;
	private EditText			mUsernameText = null;
	private EditText			mPwText = null;
	private EditText			mRepwText = null;
	private CircularImageView 	mProfileImageView = null;
	private int					mWhichButton = 0;
	private Uri 				mImageUri;
	private Bitmap				mBitmap = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        mEmailText = (EditText)findViewById(R.id.emailText);
        mFullnameText = (EditText)findViewById(R.id.fullnameText);
        mUsernameText = (EditText)findViewById(R.id.usernameText);
        mPwText = (EditText)findViewById(R.id.pwText);
        mRepwText = (EditText)findViewById(R.id.repwText);
        mProfileImageView = (CircularImageView)findViewById(R.id.profileImageView);
        
        mEmailText.setText("alexandrov1987@outlook.com");
        mFullnameText.setText("Nazar Alexandrov");
        mUsernameText.setText("alexandrov1987");
        mPwText.setText("alexandrov1987");
        mRepwText.setText("alexandrov1987");
        
        mAddphotoButton = (ImageButton)findViewById(R.id.addPhotoButton);
        mAddphotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickAddPhoto();
			}
		});
        
        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SignupActivity.this.finish();
			}
		});
        
        ImageButton doneButton = (ImageButton)findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
			
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
	                this.mProfileImageView.setImageBitmap(this.mBitmap);

	            } catch (Exception e) {
	                Log.e("Camera", e.toString());
	            }
	        }
	    }
	}



	private void onClicDone(){
		
		if(this.mEmailText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Email!", "Please enter your email.");
			return;
		}
		
		if(this.mFullnameText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Full Name!", "Please enter your full name.");
			return;
		}
		
		if(this.mUsernameText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Username!", "Please enter a username.");
			return;
		}

		if(this.mPwText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Password!", "Please enter a password.");
			return;
		}
		
		if(this.mRepwText.getText().toString().isEmpty()){

			DialogUtils.showErrorAlert(this, "Confirm Password!", "Please confirm your password.");
			return;
		}

		if(!this.mPwText.getText().toString().equals(this.mRepwText.getText().toString())){

			DialogUtils.showErrorAlert(this, "Password Mismatch", "Please check your password and try again.");
			return;
		}
		
		final ParseUser user = new ParseUser();
		
		user.setUsername(this.mEmailText.getText().toString());
		user.setEmail(this.mEmailText.getText().toString());
		user.setPassword(this.mPwText.getText().toString());
		user.add("fullName", this.mFullnameText.getText().toString());
		user.add("userWaivelengthName", this.mUsernameText.getText().toString());
		user.add("profileImage", conversionBitmapParseFile(this.mBitmap));
		
		if(NetworkUtils.isInternetAvailable(this)){
			
			DialogUtils.displayProgress(this);
			
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					
					DialogUtils.closeProgress();
					
					if (e == null) {
						ParseObject followersObject = new ParseObject("Followers");
						followersObject.add("user", user);
						followersObject.saveInBackground();
						
						Intent intent = new Intent(SignupActivity.this, TabBarActivity.class);
						startActivity(intent);
						finish();
					} else {
						DialogUtils.showErrorAlert(SignupActivity.this, "Error!", e.getMessage());
					}
				}
			});
		}else{
			DialogUtils.showErrorAlert(SignupActivity.this, "No Network!", "You are not connected to internet. Please connect and try again.");
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
		imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
		byte[] imageByte = byteArrayOutputStream.toByteArray();
		ParseFile parseFile = new ParseFile("profileImage",imageByte);
		parseFile.saveInBackground();
		return parseFile;
	}
}
