package com.waivelength.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.waivelength.R;
import com.waivelength.datamodel.BlockedList;
import com.waivelength.datamodel.DataManager;
import com.waivelength.datamodel.ParseData;
import com.waivelength.ui.activity.FeedDetailActivity;
import com.waivelength.ui.activity.TabBarFragmentActivity;
import com.waivelength.ui.activity.Utility;
import com.waivelength.ui.view.CircularImageView;
import com.waivelength.utils.DialogUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class NewsfeedFragment extends Fragment {
	
	private TabBarFragmentActivity 	mParentActivity = null;
	private ImageButton				mSettingsButton = null;
	private ListView 				mListView = null;
    private WaiveArrayAdapter 		mArrayAdapter = null;
    
    private class WaiveArrayAdapter extends ArrayAdapter<ParseObject>{

    	private ArrayList<ParseObject> items;
    	private int rsrc;
    	private int rsrc1;
    	
		public WaiveArrayAdapter(Context context, int resource, int resource1, ArrayList<ParseObject> objects) {
			super(context, resource, objects);
			
			this.items = objects;
			this.rsrc = resource;
			this.rsrc1 = resource1;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if (v == null) {
				LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				if(position%2==0)
					v = li.inflate(rsrc, null);
				else
					v = li.inflate(rsrc1, null);
			} 
			
			ParseObject waive = items.get(position);
			ParseObject waiver = waive.getParseObject("user");
			
			if(waive != null){
				
				try {
					waiver.fetchIfNeeded();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				TextView fullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
				fullnameTextView.setText(waiver.getString("fullName"));
				
				final CircularImageView videoThumbnail = (CircularImageView)v.findViewById(R.id.video_thumbnail);
				videoThumbnail.setBorderWidth(0);
				ParseFile thumbnailFile = waive.getParseFile("thumbnail");
				
				thumbnailFile.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        videoThumbnail.setImageBitmap(bitpic);
                    }
                });
				
				TextView timeTextView = (TextView)v.findViewById(R.id.timeTextView);
				timeTextView.setText(String.valueOf(waive.getInt("duration")) + " sec");

				TextView captionTextView = (TextView)v.findViewById(R.id.captionsTextView);
				captionTextView.setText(waive.getString("caption"));
				
				final TextView commentCntTextView = (TextView)v.findViewById(R.id.commentCntTextView);
				ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
				commentsQuery.whereEqualTo("waive", waive);
				commentsQuery.countInBackground(new CountCallback() {
					@Override
					public void done(int count, ParseException e) {
						commentCntTextView.setText(String.valueOf(count));
					}
				 });
				
				//TextView likeCntTextView = (TextView)v.findViewById(R.id.likeCntTextView);
				//likeCntTextView.setText(String.valueOf(waive.getList("likingUsers").size()));
			}
	
			return v;
		}
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_newsfeed, container, false);	

		this.mArrayAdapter = new WaiveArrayAdapter(mParentActivity, R.layout.feed_row, R.layout.feed_row1, DataManager.sharedInstance().mWaives);

		this.mListView = (ListView)v.findViewById(R.id.lv_post);
		this.mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		this.mListView.setDividerHeight(0);
		this.mListView.setAdapter(this.mArrayAdapter);
		this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mParentActivity, FeedDetailActivity.class);
				intent.putExtra("index", position);
				startActivity(intent);
			}
		});
		
		this.mSettingsButton = (ImageButton)v.findViewById(R.id.settingsButton);
		this.mSettingsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParentActivity.goToTab(TabBarFragmentActivity.FRAGMENT_SETTINGS);
			}
		});
		
		refreshNewsFeed();
		
        return v;
	}
	
	void refreshNewsFeed(){

		if(Utility.isInternetAvailable(mParentActivity)){
			
			DialogUtils.displayProgress(mParentActivity);
			ArrayList<ParseObject> blockedIDs = BlockedList.getBlockedList();
			
			ParseQuery<ParseObject> newsFeedQuery = ParseQuery.getQuery("Waive");
			newsFeedQuery.orderByDescending("createdAt");
			newsFeedQuery.whereNotContainedIn("user", blockedIDs);
			
			newsFeedQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objs, ParseException e) {

					if(e == null){
						
						for(int i = 0; i < objs.size(); i++){
							ParseObject obj = objs.get(i);

							try {
								obj.fetchIfNeeded();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}
						
						if(objs.size() > 0){
							
							DataManager.sharedInstance().mWaives.removeAll(DataManager.sharedInstance().mWaives);
							DataManager.sharedInstance().mWaives.addAll(objs);
						}
						
						mArrayAdapter.notifyDataSetChanged();

					}else{

						Utility.showErrorAlert(mParentActivity, "Error", e.getMessage());
					}
					
					DialogUtils.closeProgress();
				}}
			);
		}else{
			Utility.showErrorAlert(mParentActivity, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
