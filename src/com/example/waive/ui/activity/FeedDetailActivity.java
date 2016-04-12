package com.example.waive.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.view.CircularImageView;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.example.waive.R;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class FeedDetailActivity extends Activity {
	
	private ImageButton 		mPlayButton = null;
	private CircularImageView 	mProfileImageView = null;
	private ImageView			mThumbnailImageview = null;
	private TextView			mFullnameTextView = null;
	private TextView			mViewCntTextView = null;
	private TextView			mCommentCntTextView = null;
	private TextView			mLikeCntTextView = null;
	private TextView			mProgressTextView = null;
	private VideoView			mVideoView = null;
	private boolean				mIsDownloadingVideo = false;
	private ParseObject			mWaive = null;
	private Context				mContext = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);

        final int index = getIntent().getIntExtra("index", 0);
        mContext = this;
        mProfileImageView = (CircularImageView)findViewById(R.id.profileImageView);
        mThumbnailImageview = (ImageView)findViewById(R.id.thumbnailImageView);
        mFullnameTextView = (TextView)findViewById(R.id.detailfullnameTextview);
        mViewCntTextView = (TextView)findViewById(R.id.viewsCntTextView);
        mCommentCntTextView = (TextView)findViewById(R.id.commentCntTextView);
        mLikeCntTextView = (TextView)findViewById(R.id.likeCntTextView);
        mProgressTextView = (TextView)findViewById(R.id.progressTextView);
        mProgressTextView.setVisibility(View.GONE);
        mVideoView = (VideoView)findViewById(R.id.videoView);
        
        mVideoView.setVisibility(View.GONE);
        mVideoView.setOnPreparedListener(new OnPreparedListener(){

			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();  
			}
        });
        
        mVideoView.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				
				mVideoView.setVisibility(View.GONE);
				mPlayButton.setVisibility(View.VISIBLE);
				mThumbnailImageview.setVisibility(View.VISIBLE);
			}
        });
        
        mPlayButton = (ImageButton)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mPlayButton.setVisibility(View.GONE);
				mProgressTextView.setVisibility(View.VISIBLE);
				downloadVideo();
			}
		});
        
        ImageButton commentButton = (ImageButton)findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedDetailActivity.this, CommentsActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});
        
        ImageButton likeButton = (ImageButton)findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedDetailActivity.this, LikesActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});

        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

        ImageButton flagButton = (ImageButton)findViewById(R.id.flagButton);
        flagButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});

        mWaive = DataManager.sharedInstance().mWaives.get(index);
        
        try {
        	mWaive.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        ParseObject waiver =  mWaive.getParseObject("user");
        
        try {
			waiver.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        ParseFile userImageFile = waiver.getParseFile("profileImage");
        
        userImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mProfileImageView.setImageBitmap(bitpic);
            }
        });
        
        ParseFile videoThumbnailFile = mWaive.getParseFile("thumbnail");
        
        videoThumbnailFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mThumbnailImageview.setImageBitmap(bitpic);
            }
        });

        mFullnameTextView.setText(waiver.getString("fullName"));
        
        List<ParseObject> numberOfViews = mWaive.getList("numberOfViews");
        if(numberOfViews != null && numberOfViews.size() > 0)
        	mViewCntTextView.setText(numberOfViews.size() + " views");
        else
        	mViewCntTextView.setText("0 views");
        
        List<ParseObject> likingUsers = mWaive.getList("likingUsers");
        if(likingUsers != null && likingUsers.size() > 0)
        	mLikeCntTextView.setText(String.valueOf(likingUsers.size()));
        else
        	mLikeCntTextView.setText("0");
        
        ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
		commentsQuery.whereEqualTo("waive", mWaive);
		commentsQuery.countInBackground(new CountCallback() {
			@Override
			public void done(int count, ParseException e) {
				mCommentCntTextView.setText(String.valueOf(count));
			}
		 });
    }
	
	void downloadVideo(){
		
		mIsDownloadingVideo = true;
		ParseFile videoFile = mWaive.getParseFile("video");
		videoFile.getDataInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] data, ParseException e) {
				
				File path = mContext.getFilesDir();
				File file = new File(path, "current_video.mp4");
				FileOutputStream stream;
				
				try {
					stream = new FileOutputStream(file);
				    stream.write(data);
				    stream.close();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				mProgressTextView.setVisibility(View.GONE);
				mThumbnailImageview.setVisibility(View.GONE);
				mVideoView.setVisibility(View.VISIBLE);
				mVideoView.setVideoPath(file.getAbsolutePath());
				
			}
		}, new ProgressCallback(){
			@Override
			public void done(Integer progress) {
				
				mProgressTextView.setText(progress + " %");
				
				if(progress == 100){
					mWaive.addUnique("numberOfViews", ParseUser.getCurrentUser());
					mWaive.saveInBackground();
				}
			}
		});
	}
}
