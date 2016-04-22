package com.example.waive.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.adapter.FindPeopleAdapter;
import com.example.waive.ui.adapter.LikeAdapter;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.example.waive.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FindPeopleFragment extends Fragment {
	
	private static final int NORM_SEARCH_SELECTOR = 0;
	private static final int FB_SEARCH_SELECTOR = 1;
	private static final int TW_SEARCH_SELECTOR = 2;
	
	private TabBarActivity 		mTab = null;
	private ImageView 			mNormSearchSelector = null;
	private ImageView 			mFbSearchSelector = null;
	private ImageView 			mTwSearchSelector = null;
	private int					mSearchSelector = NORM_SEARCH_SELECTOR;
	private List<ParseObject>	mAllUsers = null;
	private List<ParseObject>	mFbUsers = null;
	private List<ParseObject>	mTwUsers = null;
	private List<ParseObject>	mFilteredUsers = null;
    private FindPeopleAdapter 	mAdapter = null;
	private ListView			mListView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_findpeople, container, false);	

		mNormSearchSelector = (ImageView)v.findViewById(R.id.normsearchSelector);
		mFbSearchSelector = (ImageView)v.findViewById(R.id.fbsearchSelector);
		mTwSearchSelector = (ImageView)v.findViewById(R.id.twsearchSelector);
		
		mFbSearchSelector.setVisibility(View.GONE);
		mTwSearchSelector.setVisibility(View.GONE);
		
		ImageButton backButton = (ImageButton)v.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
			}
		});
	
		RelativeLayout normSearchButton = (RelativeLayout)v.findViewById(R.id.normsearchSelectorLayout);
		normSearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchSelector = FindPeopleFragment.NORM_SEARCH_SELECTOR;
				mNormSearchSelector.setVisibility(View.VISIBLE);
				mFbSearchSelector.setVisibility(View.GONE);
				mTwSearchSelector.setVisibility(View.GONE);
			}
		});
		
		RelativeLayout fbSearchButton = (RelativeLayout)v.findViewById(R.id.fbsearchSelectorLayout);
		fbSearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchSelector = FindPeopleFragment.FB_SEARCH_SELECTOR;
				mNormSearchSelector.setVisibility(View.GONE);
				mFbSearchSelector.setVisibility(View.VISIBLE);
				mTwSearchSelector.setVisibility(View.GONE);
			}
		});
		
		RelativeLayout twSearchButton = (RelativeLayout)v.findViewById(R.id.twsearchSelectorLayout);
		twSearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchSelector = FindPeopleFragment.TW_SEARCH_SELECTOR;
				mNormSearchSelector.setVisibility(View.GONE);
				mFbSearchSelector.setVisibility(View.GONE);
				mTwSearchSelector.setVisibility(View.VISIBLE);
			}
		});
		
		mAdapter = new FindPeopleAdapter(mTab, R.layout.row_findpeople, mAllUsers);
		mListView = (ListView)v.findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		fetchAllUsers();
		
        return v;
	}
	
	void fetchAllUsers(){
		
		if(NetworkUtils.isInternetAvailable(mTab)){
			DialogUtils.displayProgress(mTab);
			
			mTab.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					ParseQuery<ParseObject> allUsersQuery = null;
					
					if(mTab.mUser == null){
						allUsersQuery = ParseQuery.getQuery("_User");
						allUsersQuery.orderByDescending("createdAt");
						allUsersQuery.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
					}else{
						List<ParseUser> following = mTab.mUser.getList("following");
						
						if(following == null)
							following = new ArrayList<ParseUser>();
						
						List<String> idArray = new ArrayList<String>();
						
						for(int i = 0; i < following.size(); i++){
							
							ParseUser user = following.get(i);
							String objectId = user.getObjectId();
							idArray.add(objectId);
						}
						
						allUsersQuery = ParseQuery.getQuery("_User");
						allUsersQuery.orderByDescending("createdAt");
						allUsersQuery.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
						allUsersQuery.whereNotContainedIn("objectId", idArray);
					}
					
					allUsersQuery.findInBackground(new FindCallback<ParseObject>(){

						@Override
						public void done(List<ParseObject> objs, ParseException e) {
							
							DialogUtils.closeProgress();
							
							if(e == null){
								mAllUsers.removeAll(mAllUsers);
								mAllUsers.addAll(objs);
								mAdapter.notifyDataSetChanged();
							}
						}
					});
				}
			});
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	void fetchFbFriends(){
		
	}
	
}
