package com.example.waive.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.waive.R;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.TabBarActivity;
import com.example.waive.ui.view.CircularImageView;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedAdapter extends ArrayAdapter<ParseObject> {
	
	private ArrayList<ParseObject> 	items;
	private int 					rsrc;
	private int 					rsrc1;
	private Context 				context;
	private TabBarActivity 			tab;
	
	public FeedAdapter(Context context, int resource, int resource1, ArrayList<ParseObject> objects, TabBarActivity tab) {
		super(context, resource, objects);
		
		this.items = objects;
		this.rsrc = resource;
		this.rsrc1 = resource1;
		this.context = context;
		this.tab = tab;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater li = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
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
						tab.mIsOtherUserProfile = false;
					}else{
						tab.mIsOtherUserProfile = true;
						tab.mUser = (ParseUser)user;
					}

					tab.goToTab(TabBarActivity.FRAGMENT_PROFIEL);
				}
			});
			
			final CircularImageView videoThumbnail = (CircularImageView)v.findViewById(R.id.video_thumbnail);
			ParseFile thumbnailFile = waive.getParseFile("thumbnail");
			thumbnailFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
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
			
//			likeCntTextView.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//
//					if(NetworkUtils.isInternetAvailable(mTab)){
//						
//						if(!mLikeLock){
//							mLikeLock = true;
//							
//							if(likeCntTextView.getText().equals("Like This")){
//								
//								likeCntTextView.setText("Unlike");
//							
//								final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
//								waive.addUnique("likingUsers", ParseUser.getCurrentUser());
//								waive.saveInBackground(new SaveCallback(){
//
//									@Override
//									public void done(ParseException e) {
//
//										if(e == null){
//											
//											ParseObject notificationObject = ParseObject.create("Notification");
//											notificationObject.add("fromUser", ParseUser.getCurrentUser());
//											notificationObject.add("toUser", waive.getParseUser("user"));
//											notificationObject.add("type", "like");
//											notificationObject.saveInBackground(new SaveCallback(){
//
//												@Override
//												public void done(ParseException e) {
//													
//													mLikeLock = false;
//												}
//											});
//										}
//									}
//								});
//							}else{
//								likeCntTextView.setText("Like This");
//								
//								final ParseObject waive = DataManager.sharedInstance().mWaives.get(position);
//								waive.addUnique("likingUsers", ParseUser.getCurrentUser());
//								waive.saveInBackground(new SaveCallback(){
//
//									@Override
//									public void done(ParseException e) {
//
//										if(e == null){
//											
//											ParseObject notificationObject = ParseObject.create("Notification");
//											notificationObject.add("fromUser", ParseUser.getCurrentUser());
//											notificationObject.add("toUser", waive.getParseUser("user"));
//											notificationObject.add("type", "like");
//											notificationObject.saveInBackground(new SaveCallback(){
//
//												@Override
//												public void done(ParseException e) {
//													
//													mLikeLock = false;
//												}
//											});
//										}
//									}
//								});
//							}
//						}
//					}else{
//						
//					}
//				}
//			});
		}

		return v;
	}
}
