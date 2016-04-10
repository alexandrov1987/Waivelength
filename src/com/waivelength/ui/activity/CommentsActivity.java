package com.waivelength.ui.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.waivelength.R;
import com.waivelength.datamodel.DataManager;
import com.waivelength.ui.view.CircularImageView;
import com.waivelength.utils.DialogUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsActivity extends Activity {

	private List<ParseObject> 		mComments = null;
	private ImageButton 			mBackButton = null;
	private ListView				mListView = null;
	private ParseObject				mWaive = null;
    private CommentsArrayAdapter 	mAdapter = null;
    
    private class CommentsArrayAdapter extends ArrayAdapter<ParseObject>{
    	
    	private Context context;
    	private List<ParseObject> items;
    	private int rsrc;
    	
		public CommentsArrayAdapter(Context context, int resource, List<ParseObject> objects) {
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
			
			ParseObject comment = items.get(position);
			ParseObject commentor = comment.getParseObject("user");
			
			if(commentor != null){
			
				try {
					commentor.fetchIfNeeded();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				final CircularImageView profileImageView = (CircularImageView)v.findViewById(R.id.profileImageView);
				profileImageView.setBorderWidth(0);
				ParseFile profileImageFile = commentor.getParseFile("profileImage");
				
				profileImageFile.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, ParseException e) {
                        Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        profileImageView.setImageBitmap(bitpic);
                    }
                });
				
				TextView fullnameTextView = (TextView)v.findViewById(R.id.comment_fullnameTextview);
				fullnameTextView.setText(commentor.getString("fullName"));
				
				TextView commentTextView = (TextView)v.findViewById(R.id.comment_commentTextview);
				commentTextView.setText(comment.getString("comment"));
				
				Date d = comment.getDate("createdAt");
				
			}
	
			return v;
		}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comments);
		
		int index = getIntent().getIntExtra("index", 0);

		mComments = new ArrayList<ParseObject>();
		mWaive = DataManager.sharedInstance().mWaives.get(index);

		mBackButton = (ImageButton)findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mAdapter = new CommentsArrayAdapter(this, R.layout.row_comment, mComments);
		mListView = (ListView)findViewById(R.id.listView1);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(new ColorDrawable(android.R.color.transparent));
		mListView.setDividerHeight(0);

		refreshComments();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	void refreshComments(){
		
		if(Utility.isInternetAvailable(this)){
			
			DialogUtils.displayProgress(this);
			ParseQuery<ParseObject> commentsQuery = ParseQuery.getQuery("Comment");
			commentsQuery.orderByDescending("createdAt");
			commentsQuery.whereEqualTo("waive", mWaive);
			
			commentsQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objs, ParseException e) {

					if(e == null){
						
						for(int i = 0; i < objs.size(); i++){
							ParseObject obj = objs.get(i);

							try {
								obj.fetchIfNeeded();
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}
						
						if(objs.size() > 0){
							
							mComments.removeAll(mComments);
							mComments.addAll(objs);
							mAdapter.notifyDataSetChanged();
							
						}

					}else{
						Utility.showErrorAlert(CommentsActivity.this, "Error", e.getMessage());
					}
					
					DialogUtils.closeProgress();
				}}
			);
		}else{
			Utility.showErrorAlert(this, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
	}
	
}
