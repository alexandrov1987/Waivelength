package com.waivelength.ui.fragment;

import com.waivelength.R;
import com.waivelength.ui.activity.TabBarFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragment extends Fragment {
	
	private TabBarFragmentActivity mParentActivity = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_notification, container, false);	

        return v;
	}
}
