package com.example.waive.ui.activity;

import com.example.waive.R;
import com.example.waive.ui.interfaces.OnChangeListener;
import com.example.waive.ui.view.VideoThumbnailsView;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

public class TrimVideoActivity extends Activity {

	private VideoView 				mVideoView = null;
	private VideoThumbnailsView		mVideoThumbnailsView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_trimvideo);
		
		final String videoPath = getIntent().getExtras().getString("video");
		mVideoView = (VideoView)findViewById(R.id.videoView);
		mVideoView.setVideoPath(videoPath);
		
		mVideoThumbnailsView = (VideoThumbnailsView)findViewById(R.id.videoThumbnails);
		mVideoThumbnailsView.setVideoSource(videoPath);
		mVideoThumbnailsView.loadThumbnails();
		mVideoThumbnailsView.setOnChangeListener(new OnChangeListener(){

			@Override
			public void onChange(long miliseconds) {
				
				mVideoView.seekTo((int)miliseconds);
			}
		});
		
		ImageButton playButton = (ImageButton)findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVideoView.start();
			}
		});
		
		ImageButton backButton = (ImageButton)findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ImageButton nextButton = (ImageButton)findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TrimVideoActivity.this, FilterVideoActivity.class);
				intent.putExtra("video", videoPath);
				startActivity(intent);
			}
		});
		
	}
			
}
