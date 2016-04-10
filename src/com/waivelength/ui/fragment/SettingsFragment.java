package com.waivelength.ui.fragment;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.waivelength.Define;
import com.waivelength.R;
import com.waivelength.ui.activity.TabBarFragmentActivity;
import com.waivelength.ui.activity.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SettingsFragment extends Fragment {
	
	private TabBarFragmentActivity 	mTab = null;
	private ImageButton				mSignoutButton = null;
	private ImageButton				mUserinfoButton = null;
	private ImageButton				mTermsButton = null;
	private ImageButton				mFbConnectButton = null;
	private ImageButton				mTwConnectButton = null;
	private ImageButton				mInstaConnectButton = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_settings, container, false);	
		
		mSignoutButton = (ImageButton)v.findViewById(R.id.signoutButton);
		mSignoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ParseUser.logOutInBackground(new LogOutCallback() {

					public void done(ParseException e) {
					
						if (e == null) {
							mTab.finish();
							
						} else {
							Utility.showErrorAlert(mTab, "Error!", e.getMessage());
						}
					}
				});
			}
		});
		
		mUserinfoButton = (ImageButton)v.findViewById(R.id.userinfoButton);
		mUserinfoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mTab.goToTab(TabBarFragmentActivity.FRAGMENT_USERINFO);
			}
		});
		
		mTermsButton = (ImageButton)v.findViewById(R.id.termsButton);
		mTermsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarFragmentActivity.FRAGMENT_TERMS);
			}
		});

		mFbConnectButton = (ImageButton)v.findViewById(R.id.fbconnectButton);
		mFbConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("FB", false)){
					mFbConnectButton.setBackground(mTab.getDrawable(R.drawable.fb_checked));
					editor.putBoolean("FB", true);
				}else{
					mFbConnectButton.setBackground(mTab.getDrawable(R.drawable.fb_norm));
					editor.putBoolean("FB", false);
				}

				editor.commit();
			}
		});
		
		mTwConnectButton = (ImageButton)v.findViewById(R.id.twconnectButton);
		mTwConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("TW", false)){
					mTwConnectButton.setBackground(mTab.getDrawable(R.drawable.tw_checked));
					editor.putBoolean("TW", true);
				}else{
					mTwConnectButton.setBackground(mTab.getDrawable(R.drawable.tw_norm));
					editor.putBoolean("TW", false);
				}

				editor.commit();
			}
		});

		mInstaConnectButton = (ImageButton)v.findViewById(R.id.instaconnectButton);
		mInstaConnectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
				SharedPreferences.Editor editor = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE).edit();
				
				if(!pref.getBoolean("INST", false)){
					mInstaConnectButton.setBackground(mTab.getDrawable(R.drawable.insta_checked));
					editor.putBoolean("INST", true);
				}else{
					mInstaConnectButton.setBackground(mTab.getDrawable(R.drawable.insta_norms));
					editor.putBoolean("INST", false);
				}

				editor.commit();
			}
		});

		SharedPreferences pref = mTab.getSharedPreferences(Define.APPNAME, Context.MODE_PRIVATE); 
		
		if(!pref.getBoolean("FB", false)){
			mFbConnectButton.setBackground(mTab.getDrawable(R.drawable.fb_norm));
		}else{
			mFbConnectButton.setBackground(mTab.getDrawable(R.drawable.fb_checked));
		}
		
		if(!pref.getBoolean("TW", false)){
			mTwConnectButton.setBackground(mTab.getDrawable(R.drawable.tw_norm));
		}else{
			mTwConnectButton.setBackground(mTab.getDrawable(R.drawable.tw_checked));
		}
		
		if(!pref.getBoolean("INST", false)){
			mInstaConnectButton.setBackground(mTab.getDrawable(R.drawable.insta_norms));
		}else{
			mInstaConnectButton.setBackground(mTab.getDrawable(R.drawable.insta_checked));
		}
		
        return v;
	}
}
