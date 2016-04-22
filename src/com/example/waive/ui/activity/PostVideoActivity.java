package com.example.waive.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.example.waive.R;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import android.provider.MediaStore.Video.Thumbnails;

public class PostVideoActivity extends Activity {

	private boolean 	mIsPlaying = false;
	private double 		mDuration = 0;
	private VideoView 	mVideoView = null;
	private EditText	mCaptionEdit = null;
	private String		mVideoPath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_postvideo);
		
		mVideoPath = getIntent().getExtras().getString("video");
		
		mCaptionEdit = (EditText)findViewById(R.id.captionEdit);
		
		ImageButton backButton = (ImageButton)findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mIsPlaying){
					mVideoView.stopPlayback();
					mIsPlaying = false;
				}
				
				finish();
			}
		});
		
		ImageButton playButton = (ImageButton)findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!mIsPlaying){
					mVideoView.start();
					mIsPlaying = true;
				}else{
					mVideoView.pause();
					mIsPlaying = false;
				}
				
				finish();
			}
		});
		
		
		TextView postButton = (TextView)findViewById(R.id.nextButton);
		postButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				if(mIsPlaying){
					mVideoView.stopPlayback();
					mIsPlaying = false;
				}
				
				if(NetworkUtils.isInternetAvailable(PostVideoActivity.this)){
					
					DialogUtils.displayProgress(PostVideoActivity.this);
					postVideo();
				}else{
					DialogUtils.showErrorAlert(PostVideoActivity.this, "No Internet", "You are not connected to internet. Please connect and try again.");
				}
			}
		});
		
		CircularImageView videoThumbnailView = (CircularImageView)findViewById(R.id.videoThumbnail);
		Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mVideoPath, Thumbnails.MICRO_KIND);
		videoThumbnailView.setImageBitmap(bmThumbnail);
		
		
	}
	
	void postVideo(){
		
		if(NetworkUtils.isInternetAvailable(this)){

			ParseObject waive = ParseObject.create("Waive");
			waive.put("user", ParseUser.getCurrentUser());
			
			ParseFile videoFile = new ParseFile(new File(mVideoPath)); 
			waive.put("video", videoFile);
			
			ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
			Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mVideoPath, Thumbnails.MICRO_KIND);
			bmThumbnail.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
			byte[] imageByte = byteArrayOutputStream.toByteArray();

			ParseFile thumbnail = new ParseFile("thumbnail.png", imageByte); 
			waive.put("thumbnail", thumbnail);

			waive.put("caption", mCaptionEdit.getEditableText().toString());

			waive.saveInBackground(new SaveCallback(){

				@Override
				public void done(ParseException e) {
					
					DialogUtils.closeProgress();

					if(e != null){
						DialogUtils.showErrorAlert(PostVideoActivity.this, "Error!", e.getMessage());
					}
				}
			});
			
		}else{
			DialogUtils.closeProgress();
			DialogUtils.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}

}
