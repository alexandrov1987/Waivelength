package com.waivelength;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class NewsfeedFragment extends Fragment {
	
	private TabBarFragmentActivity 	mParentActivity = null;
	private ImageButton				mSettingsButton = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_newsfeed, container, false);	

		mSettingsButton = (ImageButton)v.findViewById(R.id.settingsButton);
		mSettingsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParentActivity.goToTab(TabBarFragmentActivity.FRAGMENT_SETTINGS);
			}
		});
		
        return v;
	}
}
