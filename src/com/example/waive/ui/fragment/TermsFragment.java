package com.example.waive.ui.fragment;

import com.example.waive.ui.activity.TabBarActivity;
import com.joanzapata.pdfview.PDFView;
import com.example.waive.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class TermsFragment extends Fragment {

	private TabBarActivity mParentActivity = null;
	private ImageButton mBackButton = null;
	private PDFView mPdfView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mParentActivity = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.activity_terms, container, false);	

		mBackButton = (ImageButton)v.findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mParentActivity.pop();
				mParentActivity.fragmentReplace(mParentActivity.cur());
			}
		});
		
		PDFView pdfView = (PDFView)v.findViewById(R.id.pdfView);
        pdfView.fromAsset("Terms.pdf")
	    .pages(0, 2, 1, 3, 3, 3)
	    .defaultPage(1)
	    .swipeVertical(true)
	    .showMinimap(false)
	    .enableSwipe(true)
	    .load();
		
        return v;
	}
}
