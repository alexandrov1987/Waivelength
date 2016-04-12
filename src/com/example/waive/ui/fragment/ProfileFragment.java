package com.example.waive.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.FeedDetailActivity;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.waive.R;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileFragment extends Fragment {
	
	private TabBarActivity 	mTab = null;
	private ArrayList<ParseObject>	mWaives = null;
	private ArrayList<ParseObject>	mWaivesWeekly = null;
	private ArrayList<ParseObject>	mWaivesMonthly = null;
	private ParseUser				mUser = null;
	
	private ImageView				mProfileImageView = null;
	private TextView				mUsernameTextView = null;
	private TextView				mFullnameTextView = null;
	private TextView				mFollowersTextView = null;
	private TextView				mFollowingsTextView = null;
	private ImageButton				mZoominButton = null;
	private ImageButton				mZoomoutButton = null;
	private ListView				mListView = null;
	private int						mZoomValue = 0;
	private boolean					mNoWaives = false;
    private WaiveArrayAdapter 		mAdapter = null;
    
    private class WaiveArrayAdapter extends ArrayAdapter<ParseObject>{

    	private ArrayList<ParseObject> items;
    	private int rsrc;
    	private int rsrc1;
    	
		public WaiveArrayAdapter(Context context, int resource, int resource1, ArrayList<ParseObject> objects) {
			super(context, resource, objects);
			
			this.items = objects;
			this.rsrc = resource;
			this.rsrc1 = resource1;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if (v == null) {
				LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				if(position%2==0)
					v = li.inflate(rsrc, null);
				else
					v = li.inflate(rsrc1, null);
			} 
			
			ParseObject waive = items.get(position);
			ParseObject waiver = waive.getParseObject("user");
			
			if(waive != null){
				
				try {
					waiver.fetchIfNeeded();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				TextView fullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
				fullnameTextView.setText(waiver.getString("fullName"));
				fullnameTextView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
						ParseObject user = waive.getParseObject("user");
						
						if(user.equals(ParseUser.getCurrentUser())){
							mTab.mIsOtherUserProfile = false;
						}else{
							mTab.mIsOtherUserProfile = true;
							mTab.mUser = (ParseUser)user;
						}

						mTab.goToTab(TabBarActivity.FRAGMENT_PROFIEL);
					}
				});
				
				final CircularImageView videoThumbnail = (CircularImageView)v.findViewById(R.id.video_thumbnail);
				videoThumbnail.setBorderWidth(0);
				ParseFile thumbnailFile = waive.getParseFile("thumbnail");
				
				thumbnailFile.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        videoThumbnail.setImageBitmap(bitpic);
                    }
                });
				
				TextView timeTextView = (TextView)v.findViewById(R.id.timeTextView);
				timeTextView.setText(String.valueOf(waive.getInt("duration")) + " sec");

				TextView captionTextView = (TextView)v.findViewById(R.id.captionsTextView);
				captionTextView.setText(waive.getString("caption"));
				
				final TextView commentCntTextView = (TextView)v.findViewById(R.id.commentCntTextView);
				ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
				commentsQuery.whereEqualTo("waive", waive);
				commentsQuery.countInBackground(new CountCallback() {
					@Override
					public void done(int count, ParseException e) {
						commentCntTextView.setText(String.valueOf(count));
					}
				 });
				
				final TextView likeCntTextView = (TextView)v.findViewById(R.id.likeCntTextView);
				
				List<ParseObject> likingUsers = waive.getList("likingUsers");
				
				if(likingUsers != null && likingUsers.size() > 0)
					likeCntTextView.setText(String.valueOf(likingUsers.size()));
				else
					likeCntTextView.setText("0");
				
//				likeCntTextView.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//
//						if(NetworkUtils.isInternetAvailable(mTab)){
//							
//							if(!mLikeLock){
//								mLikeLock = true;
//								
//								if(likeCntTextView.getText().equals("Like This")){
//									
//									likeCntTextView.setText("Unlike");
//								
//									final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
//									waive.addUnique("likingUsers", ParseUser.getCurrentUser());
//									waive.saveInBackground(new SaveCallback(){
//
//										@Override
//										public void done(ParseException e) {
//
//											if(e == null){
//												
//												ParseObject notificationObject = ParseObject.create("Notification");
//												notificationObject.add("fromUser", ParseUser.getCurrentUser());
//												notificationObject.add("toUser", waive.getParseUser("user"));
//												notificationObject.add("type", "like");
//												notificationObject.saveInBackground(new SaveCallback(){
//
//													@Override
//													public void done(ParseException e) {
//														
//														mLikeLock = false;
//													}
//												});
//											}
//										}
//									});
//								}else{
//									likeCntTextView.setText("Like This");
//									
//									final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
//									waive.addUnique("likingUsers", ParseUser.getCurrentUser());
//									waive.saveInBackground(new SaveCallback(){
//
//										@Override
//										public void done(ParseException e) {
//
//											if(e == null){
//												
//												ParseObject notificationObject = ParseObject.create("Notification");
//												notificationObject.add("fromUser", ParseUser.getCurrentUser());
//												notificationObject.add("toUser", waive.getParseUser("user"));
//												notificationObject.add("type", "like");
//												notificationObject.saveInBackground(new SaveCallback(){
//
//													@Override
//													public void done(ParseException e) {
//														
//														mLikeLock = false;
//													}
//												});
//											}
//										}
//									});
//								}
//							}
//						}else{
//							
//						}
//					}
//				});
			}
	
			return v;
		}
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mTab = (TabBarActivity)getActivity();
		View v = inflater.inflate(R.layout.fragment_profile, container, false);	

		mProfileImageView = (ImageView)v.findViewById(R.id.profileImageView);
		mUsernameTextView = (TextView)v.findViewById(R.id.usernameTextView);
		mFullnameTextView = (TextView)v.findViewById(R.id.fullnameTextView);
		
		mFollowersTextView = (TextView)v.findViewById(R.id.followersTextView);
		mFollowersTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarActivity.FRAGMENT_FOLLOWERS);
			}
		});
		
		mFollowingsTextView = (TextView)v.findViewById(R.id.followingTextView);
		mFollowingsTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTab.goToTab(TabBarActivity.FRAGMENT_FINDPEOPLE);
			}
		});

		mZoominButton = (ImageButton)v.findViewById(R.id.zoominButton);
		mZoominButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mZoomoutButton = (ImageButton)v.findViewById(R.id.zoomoutButton);
		mZoomoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mWaives = new ArrayList<ParseObject>();
		mWaivesWeekly = new ArrayList<ParseObject>();
		mWaivesMonthly = new ArrayList<ParseObject>();
		
		this.mAdapter = new WaiveArrayAdapter(mTab, R.layout.feed_row, R.layout.feed_row1, mWaives);

		this.mListView = (ListView)v.findViewById(R.id.lv_post);
		this.mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		this.mListView.setDividerHeight(0);
		this.mListView.setAdapter(this.mAdapter);
		this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(mTab, FeedDetailActivity.class);
				intent.putExtra("index", position);
				startActivity(intent);
			}
		});

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
		
		getProfile(forUser);
	}
	
	void getProfile(ParseUser user){
		if(NetworkUtils.isInternetAvailable(mTab)){
			
			ParseQuery<ParseObject> newsFeedQuery = ParseQuery.getQuery("Waive");
			newsFeedQuery.orderByDescending("createdAt");
			newsFeedQuery.whereEqualTo("user", user);
			newsFeedQuery.findInBackground(new FindCallback<ParseObject>() {
			     public void done(List<ParseObject> objects, ParseException e) {
			         if (e == null) {
			        	 
			        	 for(int i = 0; i < objects.size(); i++){
			        		 ParseObject obj = objects.get(i);
			        		 try {
								obj.fetchIfNeeded();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
			        	 }
			        	 
			        	 mWaives.removeAll(mWaives);
			        	 mWaivesMonthly.removeAll(mWaivesMonthly);
			        	 mWaivesWeekly.removeAll(mWaivesWeekly);
			        	 
			        	 if(objects.size() > 0){
				        	 mWaives.addAll(objects);
			        	 }
			        	 
			        	 mAdapter.notifyDataSetChanged();
			        	 
			         } else {
			        	 DialogUtils.showErrorAlert(mTab, "Error", e.getMessage());
			         }
			     }
			 });
		}else{
			DialogUtils.showErrorAlert(mTab, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
	void fillUserDetails(){
		
		ParseUser forUser = null;
		
		if(mUser != null){
			
			forUser = mUser;
			
		}else{
			forUser = ParseUser.getCurrentUser();
		}
		
		ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Followers");
		followersQuery.whereEqualTo("user", forUser);
		followersQuery.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				
				if(e == null){
					
					if(objs.size() > 0){
						
						List<ParseObject> followers = objs.get(0).getList("followers");
						
						if(followers != null && followers.size() > 0)
							mFollowersTextView.setText(String.valueOf(followers.size()));
						else
							mFollowersTextView.setText("0");
					}
				}else{
					
				}
			}
		});
		
		ParseFile userImageFile = forUser.getParseFile("profileImage");
		userImageFile.getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mProfileImageView.setImageBitmap(bitpic);
            }
        });
		
		mUsernameTextView.setText(forUser.getString("userWaivelengthName"));
		mFullnameTextView.setText(forUser.getString("fullName"));
		
		List<ParseObject> followings = forUser.getList("following");
		if(followings != null && followings.size() > 0)
			mFollowingsTextView.setText(String.valueOf(followings.size()));
		else
			mFollowingsTextView.setText("0");
	}
}
