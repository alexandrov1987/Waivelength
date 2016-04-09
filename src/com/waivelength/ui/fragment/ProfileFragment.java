package com.waivelength.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.waivelength.R;
import com.waivelength.ui.activity.TabBarFragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	
	private TabBarFragmentActivity 	mTab = null;
	private List<ParseObject>		mWaives = null;
	private List<ParseObject>		mWaivesWeekly = null;
	private List<ParseObject>		mWaivesMonthly = null;
	private ParseUser				mUser = null;
	
	private ImageView				mProfileImageView = null;
	private TextView				mUsernameTextView = null;
	private TextView				mFullnameTextView = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarFragmentActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_profile, container, false);	

		mProfileImageView = (ImageView)v.findViewById(R.id.profileImageView);
		mUsernameTextView = (TextView)v.findViewById(R.id.usernameTextView);
		mFullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
		
		mWaives = new ArrayList<ParseObject>();
		mWaivesWeekly = new ArrayList<ParseObject>();
		mWaivesMonthly = new ArrayList<ParseObject>();
		
		refreshProfile();
		
        return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mWaives.removeAll(mWaives);
		mWaivesWeekly.removeAll(mWaivesWeekly);
		mWaivesMonthly.removeAll(mWaivesMonthly);
	}
	
	void refreshProfile(){
		
		ParseUser forUser = null;
		
		if(mTab.mIsOtherUserProfile){
			
			forUser = mTab.mUser;
			mUser = mTab.mUser;
		}else{
			mUser = null;
			forUser = ParseUser.getCurrentUser();
		}
		
		forUser.fetchIfNeededInBackground(new GetCallback<ParseObject>(){

			@Override
			public void done(ParseObject obj, ParseException e) {
				if(e == null){
					fillUserDetails();
					
				}else{
					
				}
			}
			
		});
	}
	
	void fillUserDetails(){
		
		ParseUser forUser = null;
		
		if(mUser != null){
			
			forUser = mUser;
			
		}else{
			forUser = ParseUser.getCurrentUser();
			
			ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
			followersQuery.whereEqualTo("user", ParseUser.getCurrentUser());
			followersQuery.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {
					
					if(e == null){
						
						
					}else{
						
					}
				}
			});
			
			try {
				forUser.fetchIfNeeded();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			ParseFile userImageFile = forUser.getParseFile("profileImage");
//			userImageFile.getDataInBackground(new GetDataCallback() {
//
//	            @Override
//	            public void done(byte[] data, ParseException e) {
//	                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
//	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
//	                mProfileImageView.setImageBitmap(bitpic);
//	            }
//	        });
			
			mUsernameTextView.setText(forUser.getUsername());
		}
	}
}
