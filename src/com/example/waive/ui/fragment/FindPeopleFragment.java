package com.example.waive.ui.fragment;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.R;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FindPeopleFragment extends Fragment {
	
	private static final int NORM_SEARCH_SELECTOR = 0;
	private static final int FB_SEARCH_SELECTOR = 1;
	private static final int TW_SEARCH_SELECTOR = 2;
	
	private TabBarActivity 	mTab = null;
	private ImageView 		mNormSearchSelector = null;
	private ImageView 		mFbSearchSelector = null;
	private ImageView 		mTwSearchSelector = null;
	private int				mSearchSelector = NORM_SEARCH_SELECTOR;
	
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
		
        return v;
	}
}
