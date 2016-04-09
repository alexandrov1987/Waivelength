package com.waivelength.ui.fragment;

import com.waivelength.R;
import com.waivelength.ui.activity.TabBarFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TermsFragment extends Fragment {

	private TabBarFragmentActivity mParentActivity = null;
	private ImageButton mBackButton = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.activity_terms, container, false);	

		mBackButton = (ImageButton)v.findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParentActivity.pop();
				mParentActivity.fragmentReplace(mParentActivity.cur());
			}
		});
		
        return v;
	}
}
