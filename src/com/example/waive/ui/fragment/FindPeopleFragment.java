package com.example.waive.ui.fragment;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.R;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FindPeopleFragment extends Fragment {
	
	private TabBarActivity mTab = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_findpeople, container, false);	

		ImageButton backButton = (ImageButton)v.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
			}
		});
		
        return v;
	}
}
