package com.example.waive.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import com.example.waive.datamodel.BlockedList;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.FeedDetailActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.FeedAdapter;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.waive.R;
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
	
	private TabBarActivity 			mTab = null;
	private ListView 				mListView = null;
    private FeedAdapter 			mAdapter = null;
    private boolean					mLikeLock = false;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_newsfeed, container, false);	

		this.mAdapter = new FeedAdapter(mTab, R.layout.feed_row, R.layout.feed_row1, R.layout.feed_row2, R.layout.feed_row2,
				1, DataManager.sharedInstance().mWaives, null, null, mTab);

		this.mListView = (ListView)v.findViewById(R.id.lv_post);
		this.mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		this.mListView.setDividerHeight(0);
		this.mListView.setAdapter(this.mAdapter);
		this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mTab, FeedDetailActivity.class);
				intent.putExtra("index", position);
				intent.putExtra("ownerController", "N");
				startActivity(intent);
			}
		});
		
		ImageButton settingsButton = (ImageButton)v.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarActivity.FRAGMENT_SETTINGS);
			}
		});
		
		refreshNewsFeed();
		
        return v;
	}
	
	void refreshNewsFeed(){

		if(NetworkUtils.isInternetAvailable(mTab)){
			
			DialogUtils.displayProgress(mTab);
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
						
						mAdapter.notifyDataSetChanged();

					}else{

						DialogUtils.showErrorAlert(mTab, "Error", e.getMessage());
					}
					
					DialogUtils.closeProgress();
				}}
			);
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
