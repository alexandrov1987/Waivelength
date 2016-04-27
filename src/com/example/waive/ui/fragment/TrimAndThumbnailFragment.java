package com.example.waive.ui.fragment;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.interfaces.OnChangeListener;
import com.example.waive.ui.view.VideoThumbnailsView;
import com.example.waive.ui.view.VideoTrimView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.VideoView;

public class TrimAndThumbnailFragment extends Fragment {

	private AddNewWaiveActivity 	mParent = null;
	private VideoView 				mVideoView = null;
	private VideoThumbnailsView		mVideoThumbnailsView = null;
	private VideoTrimView			mVideoTrimView = null;
	
	private int						mTotalDuration = 0;
	private int						mStartTime = 0;
	private int 					mEndTime = 0;
	private int						mThumbnailTime = 0;
	private boolean					mIsPlaying = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_trimvideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();
		
		final String videoPath = DataManager.sharedInstance().mVideoPath;
		
		mVideoView = (VideoView)v.findViewById(R.id.videoView);
		mVideoView.setVideoPath(videoPath);
		
		mTotalDuration = mVideoView.getDuration();
		mStartTime = 0;
		mEndTime = mTotalDuration;
		
		mVideoThumbnailsView = (VideoThumbnailsView)v.findViewById(R.id.videoThumbnails);
		mVideoThumbnailsView.setVideoSource(videoPath);
		mVideoThumbnailsView.loadThumbnails();
		mVideoThumbnailsView.setOnChangeListener(new OnChangeListener(){

			@Override
			public void onChange(long miliseconds) {
				
				mVideoView.seekTo((int)miliseconds);
			}
		});
		
		mVideoTrimView = (VideoTrimView)v.findViewById(R.id.videoTrimView);
		mVideoTrimView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					
					
					
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
		});
		
		ImageButton playButton = (ImageButton)v.findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mIsPlaying){
					mIsPlaying = false;
					mVideoView.pause();
				}else{
					mIsPlaying = true;
					mVideoView.start();
				}
			}
		});
		
		ImageButton backButton = (ImageButton)v.findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mVideoView.pause();
				mIsPlaying = false;
				mParent.goTo(AddNewWaiveActivity.FRAGMENT_RECORD);
			}
		});

		ImageButton nextButton = (ImageButton)v.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVideoView.pause();
				mIsPlaying = false;

				if(mStartTime == 0 && mEndTime == mTotalDuration){
					mParent.goTo(AddNewWaiveActivity.FRAGMENT_FILTER);
				}else{
					takeOutSelectedPortion();
				}
			}
		});

        return v;
	}
	
	void takeOutSelectedPortion(){
		
	}
}
