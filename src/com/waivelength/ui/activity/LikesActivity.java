package com.waivelength.ui.activity;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.waivelength.R;
import com.waivelength.datamodel.DataManager;
import com.waivelength.ui.view.CircularImageView;
import com.waivelength.utils.DialogUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class LikesActivity extends Activity {

	private List<ParseObject> 		mLikes = null;
	private ImageButton 			mBackButton = null;
	private ListView				mListView = null;
	private ParseObject				mWaive = null;
    private LikesArrayAdapter 		mAdapter = null;
    
    private class LikesArrayAdapter extends ArrayAdapter<ParseObject>{
    	
    	private Context context;
    	private List<ParseObject> items;
    	private int rsrc;
    	
		public LikesArrayAdapter(Context context, int resource, List<ParseObject> objects) {
			super(context, resource, objects);
			
			this.context = context;
			this.items = objects;
			this.rsrc = resource;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if (v == null) {
				LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(rsrc, null);
			} 
			
			ParseObject likingUser = items.get(position);
			
			if(likingUser != null){
			
				try {
					likingUser.fetchIfNeeded();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
				profileImageView.setBorderWidth(0);
				ParseFile thumbnailFile = likingUser.getParseFile("profileImage");
				
				thumbnailFile.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        profileImageView.setImageBitmap(bitpic);
                    }
                });
				
				TextView fullnameTextView = (TextView)v.findViewById(R.id.like_fullnameTextview);
				fullnameTextView.setText(likingUser.getString("fullName"));
			}
	
			return v;
		}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_likes);
		
		int index = getIntent().getIntExtra("index", 0);

		mLikes = new ArrayList<ParseObject>();
		mWaive = DataManager.sharedInstance().mWaives.get(index);

		mBackButton = (ImageButton)findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mAdapter = new LikesArrayAdapter(this, R.layout.row_like, mLikes);
		mListView = (ListView)findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		
		refreshLikes();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	void refreshLikes(){
		
		if(Utility.isInternetAvailable(this)){
			
			DialogUtils.displayProgress(this);
			
			mLikes.removeAll(mLikes);

			try {
				mWaive.fetchIfNeeded();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			List<ParseObject> objs = mWaive.getList("likingUsers");
			
			if(objs != null && objs.size() > 0)
				mLikes.addAll(objs);

			mAdapter.notifyDataSetChanged();
			
			DialogUtils.closeProgress();
		}else{
			Utility.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
