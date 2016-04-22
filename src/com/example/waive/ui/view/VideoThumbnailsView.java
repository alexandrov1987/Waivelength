package com.example.waive.ui.view;

import com.example.waive.ui.interfaces.OnChangeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VideoThumbnailsView extends RelativeLayout {

	private static final int THUMBNAILS = 10;
	private MediaMetadataRetriever	mMediaRetriever = null;
	private Context	 				mContext = null;
	private RelativeLayout			mTickFrame = null;
	private ImageView 				mIvTick = null;
	private boolean 				mIsTouchTick = false;
	private float					mDeltaX = 0;
	private float					mWidth = 0;
	private long 					mTotalDurationMs = 0;
	private long 					mDurationMs = 0;
	private int 					mOldThumbIndex = 0;
	private OnChangeListener 		mOnChangeListener;
	
	public VideoThumbnailsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mMediaRetriever = new MediaMetadataRetriever();
		mContext = context;
		
	}
	
	public void setVideoSource(String filePath){
		mMediaRetriever.setDataSource(filePath);
	}
	
	public void setOnChangeListener(OnChangeListener listener) {
		mOnChangeListener = listener;
    }
	
	public void loadThumbnails(){
		
		float scale = getContext().getResources().getDisplayMetrics().density;
		mWidth = (int) (336 * scale + 0.5f);
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setWeightSum(10);
		layout.setBackgroundColor(Color.WHITE);
		
		
		int w = (int) (331 * scale + 0.5f);
		int h = (int) (50.4 * scale + 0.5f);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.setLayoutParams(layoutParams);
		
		mTotalDurationMs = Long.parseLong(mMediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)); 
		mDurationMs = mTotalDurationMs / 10;

		for(int i = 0; i < THUMBNAILS; i++){
			
			ImageView ivThumb = new ImageView(mContext);
			ivThumb.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
			Bitmap frame = mMediaRetriever.getFrameAtTime(mDurationMs*i * 1000);
			ivThumb.setBackground(new BitmapDrawable(frame));
			layout.addView(ivThumb);
		}
		
		this.addView(layout);
		
		mTickFrame = new RelativeLayout(mContext);
		mTickFrame.setPadding(6, 6, 6, 6);
		mTickFrame.setLayoutParams(new RelativeLayout.LayoutParams((int) (55.2 * scale + 0.5f), (int) (55.2 * scale + 0.5f)));
		mTickFrame.setBackgroundColor(Color.rgb(90, 190, 170));
		
		mIvTick = new ImageView(mContext);
		mIvTick.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mTickFrame.addView(mIvTick);

		this.addView(mTickFrame);
		
		this.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				float x1 = event.getX();
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:{
						if(x1 > mTickFrame.getX() && x1 < mTickFrame.getX() + mTickFrame.getWidth()){
							mIsTouchTick = true;
							mDeltaX = x1 - mTickFrame.getX();
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:{
					
						if(mIsTouchTick){
							
							if(mTickFrame.getX() < 0){
								mTickFrame.setX(0);
								mIsTouchTick = false;
								mDeltaX = 0;
							}
							else if(mTickFrame.getX() + mTickFrame.getWidth() > mWidth){
								mTickFrame.setX(mWidth - mTickFrame.getWidth());
								mIsTouchTick = false;
								mDeltaX = 0;
							}
							else{
								mTickFrame.setX(x1 - mDeltaX);
							}
							
							float xxx = mTickFrame.getX()/mWidth;
							
							if(mOnChangeListener != null)
								mOnChangeListener.onChange((long)xxx * mTotalDurationMs * 1000);
							
							int i = 0;
							for(; i < 10; i++){
								
								if(xxx > i * 0.1 && xxx < (i+1) * 0.1 ){
									
									break;
								}
							}
							
							if(mOldThumbIndex != i){
								mOldThumbIndex = i;
								
								Bitmap frame = mMediaRetriever.getFrameAtTime(mDurationMs*mOldThumbIndex*1000);
								mIvTick.setBackground(new BitmapDrawable(frame));
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:{
						mIsTouchTick = false;
						mDeltaX = 0;
					}
					break;
				}
				
				Log.i("x", String.valueOf(x1));
				return true;
			}
		});
	}
}
