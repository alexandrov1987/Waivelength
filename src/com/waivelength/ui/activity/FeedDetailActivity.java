package com.waivelength.ui.activity;

import java.io.ByteArrayOutputStream;

import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.waivelength.R;
import com.waivelength.datamodel.DataManager;
import com.waivelength.ui.view.CircularImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedDetailActivity extends Activity {
	
	private ImageButton 		mCommentButton = null;
	private ImageButton 		mLikeButton = null;
	private ImageButton			mBackButton = null;
	private CircularImageView 	mProfileImageView = null;
	private ImageView			mThumbnailImageview = null;
	private TextView			mFullnameTextView = null;
	private TextView			mViewCntTextView = null;
	private TextView			mCommentCntTextView = null;
	private TextView			mLikeCntTextView = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);

        final int index = getIntent().getIntExtra("index", 0);

        mProfileImageView = (CircularImageView)findViewById(R.id.profileImageView);
        mThumbnailImageview = (ImageView)findViewById(R.id.thumbnailImageView);
        mFullnameTextView = (TextView)findViewById(R.id.detailfullnameTextview);
        mViewCntTextView = (TextView)findViewById(R.id.viewsCntTextView);
        mCommentCntTextView = (TextView)findViewById(R.id.commentCntTextView);
        mLikeCntTextView = (TextView)findViewById(R.id.likeCntTextView);
        
        mCommentButton = (ImageButton)findViewById(R.id.commentButton);
        mCommentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedDetailActivity.this, CommentsActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});
        
        mLikeButton = (ImageButton)findViewById(R.id.likeButton);
        mLikeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedDetailActivity.this, LikesActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		});

        mBackButton = (ImageButton)findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

        ParseObject waive = DataManager.sharedInstance().mWaives.get(index);
        
        try {
        	waive.fetchIfNeeded();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        ParseObject waiver =  waive.getParseObject("user");
        
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
        
        ParseFile videoThumbnailFile = waive.getParseFile("thumbnail");
        
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
//        mViewCntTextView.setText(waive.getJSONArray("numberOfViews").length() + " views");
//        mLikeCntTextView.setText(waive.getList("likingUsers").size() + " views");
        
        ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
		commentsQuery.whereEqualTo("waive", waive);
		commentsQuery.countInBackground(new CountCallback() {
			@Override
			public void done(int count, ParseException e) {
				mCommentCntTextView.setText(String.valueOf(count));
			}
		 });
    }
}
