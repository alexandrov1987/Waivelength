package com.example.waive.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.waive.R;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class FollowersFragment extends Fragment {

	private TabBarActivity 	mTab = null;
	private ImageButton				mBackButton = null;
	
	private ListView				mListView = null;
	private ParseObject				mWaive = null;
	private ParseUser				mUserToCheck = null;
    private LikesArrayAdapter 		mAdapter = null;
    private List<ParseObject> 		mLikes = null;
    
    private class LikesArrayAdapter extends ArrayAdapter<ParseObject>{
    	
    	private Context context;
    	private List<ParseObject> items;
    	private int rsrc;
    	
		public LikesArrayAdapter(Context context, int resource, List<ParseObject> objects) {
			super(context, resource, objects);
			
			this.context = context;
			this.items = objects;
			this.rsrc = resource;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if (v == null) {
				LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(rsrc, null);
			} 
			
			ParseObject likingUser = items.get(position);
			
			if(likingUser != null){
			
				try {
					likingUser.fetchIfNeeded();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
				profileImageView.setBorderWidth(0);
				ParseFile thumbnailFile = likingUser.getParseFile("profileImage");
				
				thumbnailFile.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        profileImageView.setImageBitmap(bitpic);
                    }
                });
				
				TextView fullnameTextView = (TextView)v.findViewById(R.id.like_fullnameTextview);
				fullnameTextView.setText(likingUser.getString("fullName"));
			}
	
			return v;
		}
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_followers, container, false);	

		mBackButton = (ImageButton)v.findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.pop();
				mTab.fragmentReplace(mTab.cur());
			}
		});
		
		mLikes = new ArrayList<ParseObject>();
		mAdapter = new LikesArrayAdapter(mTab, R.layout.row_like, mLikes);
		mListView = (ListView)v.findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		refreshLikes();
		
        return v;
	}
	
	void refreshLikes(){
		
		if(NetworkUtils.isInternetAvailable(mTab)){
			
			DialogUtils.displayProgress(mTab);
			
			mLikes.removeAll(mLikes);

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
			query.whereEqualTo("user", mUserToCheck);
			query.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objs, ParseException e) {
					
					if(e == null && objs != null && objs.size() > 0){
						ParseObject firstObject = objs.get(0);
						List<ParseObject> followers = firstObject.getList("followers");
						mLikes.addAll(followers);
						mAdapter.notifyDataSetChanged();
					}
					
					DialogUtils.closeProgress();
				}
			});
			
			mAdapter.notifyDataSetChanged();
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
}
