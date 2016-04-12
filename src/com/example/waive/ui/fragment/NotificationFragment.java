package com.example.waive.ui.fragment;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.R;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragment extends Fragment {
	
	private TabBarActivity mParentActivity = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_notification, container, false);	

        return v;
	}
}
