package com.waivelength;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SettingsFragment extends Fragment {
	
	private TabBarFragmentActivity 	mParentActivity = null;
	private ImageButton				mSignoutButton = null;
	private ImageButton				mUserinfoButton = null;
	private ImageButton				mTermsButton = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_settings, container, false);	
		
		mSignoutButton = (ImageButton)v.findViewById(R.id.signoutButton);
		mSignoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ParseUser.logOutInBackground(new LogOutCallback() {

					public void done(ParseException e) {
					
						if (e == null) {
							mParentActivity.finish();
							
						} else {
							Utility.showErrorAlert(mParentActivity, "Error!", e.getMessage());
						}
					}
				});
			}
		});
		
		mUserinfoButton = (ImageButton)v.findViewById(R.id.userinfoButton);
		mUserinfoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mParentActivity.goToTab(TabBarFragmentActivity.FRAGMENT_USERINFO);
			}
		});
		
		mTermsButton = (ImageButton)v.findViewById(R.id.termsButton);
		mTermsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
        return v;
	}
}
