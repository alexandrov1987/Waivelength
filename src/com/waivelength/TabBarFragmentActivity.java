
package com.waivelength;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TabBarFragmentActivity extends FragmentActivity {

	final String TAG = "TabBarFragmentActivity";
	
    public final static int FRAGMENT_NEWSFEED = 0;
    public final static int FRAGMENT_NOTIFICATION = 1;
    public final static int FRAGMENT_ADDVIDEO = 2;
    public final static int FRAGMENT_PROFIEL = 3;
    public final static int FRAGMENT_SETTINGS = 4;
    public final static int FRAGMENT_USERINFO = 5;
    
    private static final int REQUEST_SHARE = 0;
    
	int mCurIndex;
    int oldFragmentIndex;

    public ImageView mNewsfeedButton = null;
    public ImageView mNotificationButton = null;
    public ImageView mAddvideoButton = null;
    public ImageView mProfileButton = null;
    public ImageView mSettingsButton = null;

    public TextView mNewsfeedTextView = null;
    public TextView mNotificationTextView = null;
    public TextView mProfileTextView = null;
    public TextView mSettingsTextView = null;

    NewsfeedFragment mNewsfeedFragment = null;
    NotificationFragment mNotificationFragment = null;
    ProfileFragment mProfileFragment = null;
    SettingsFragment mSettingsFragment = null;
    UserinfoFragment mUserinfoFragment = null;
    
    public ArrayList<Integer> mArrayPath = null;
    
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_tabbar_fragment);

		mArrayPath = new ArrayList<Integer>();
		
		mNewsfeedFragment = new NewsfeedFragment();
		mNotificationFragment = new NotificationFragment();
		mProfileFragment = new ProfileFragment();
		mSettingsFragment = new SettingsFragment();
		mUserinfoFragment = new UserinfoFragment();
		
		mNewsfeedTextView = (TextView)findViewById(R.id.tab_newsfeed_text);
		mNotificationTextView = (TextView)findViewById(R.id.tab_notification_text);
		mProfileTextView = (TextView)findViewById(R.id.tab_profile_text);
		mSettingsTextView = (TextView)findViewById(R.id.tab_settings_text);
		
		mNewsfeedButton = (ImageView)findViewById(R.id.tab_newsfeed);
		mNewsfeedButton.setImageResource(R.drawable.newsfeed_pressed);
		mNewsfeedButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					goToTab(FRAGMENT_NEWSFEED);
				}
				
				return false;
			}
		});
        
		mNotificationButton = (ImageView)findViewById(R.id.tab_notification);
		mNotificationButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					goToTab(FRAGMENT_NOTIFICATION);
				}

				return false;
			}
		});

		mAddvideoButton = (ImageView)findViewById(R.id.tab_addvideo);
		mAddvideoButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					showShareActivity();
				}

				return false;
			}
		});
        
		mProfileButton = (ImageView)findViewById(R.id.tab_profile);
		mProfileButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					goToTab(FRAGMENT_PROFIEL);
				}

				return false;
			}
		});

		mSettingsButton = (ImageView)findViewById(R.id.tab_settings);
		mSettingsButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					goToTab(FRAGMENT_SETTINGS);
				}

				return false;
			}
		});
        
        push(FRAGMENT_NEWSFEED);
        fragmentReplace(cur());
        
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		
	}
	
    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        free();
        
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == Activity.RESULT_OK){
        	
        	if(requestCode == REQUEST_SHARE){
        		
        		
        	}else{
        	}
        	
        }else if(resultCode == Activity.RESULT_CANCELED){
        	
        }
    }
    
    public void goToTab(int whichtab){
    	switch(whichtab){
    	case FRAGMENT_NEWSFEED:
			mNewsfeedTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_pressed_text_color));
			mNotificationTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			
			mNewsfeedButton.setImageResource(R.drawable.newsfeed_pressed);
			mNotificationButton.setImageResource(R.drawable.notification);
			mProfileButton.setImageResource(R.drawable.profile);
			mSettingsButton.setImageResource(R.drawable.settings_disable);	

			free();
			push(FRAGMENT_NEWSFEED);
	        fragmentReplace(cur());

    		break;
    	case FRAGMENT_NOTIFICATION:
			mNewsfeedTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_pressed_text_color));
			mProfileTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));

			mNewsfeedButton.setImageResource(R.drawable.newsfeed);
			mNotificationButton.setImageResource(R.drawable.notification_presed);
			mProfileButton.setImageResource(R.drawable.profile);
			mSettingsButton.setImageResource(R.drawable.settings_disable);
	        
			free();
			push(FRAGMENT_NOTIFICATION);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_PROFIEL:
			mNewsfeedTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_pressed_text_color));
			mSettingsTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));

			mNewsfeedButton.setImageResource(R.drawable.newsfeed);
			mNotificationButton.setImageResource(R.drawable.notification);
			mProfileButton.setImageResource(R.drawable.profile_pressed);
			mSettingsButton.setImageResource(R.drawable.settings_disable);
	        
			free();
			push(FRAGMENT_PROFIEL);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_SETTINGS:
			mNewsfeedTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mNotificationTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mProfileTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_disable_text_color));
			mSettingsTextView.setTextColor(ContextCompat.getColor(TabBarFragmentActivity.this, R.color.tab_button_pressed_text_color));
			
			mNewsfeedButton.setImageResource(R.drawable.newsfeed);
			mNotificationButton.setImageResource(R.drawable.notification);
			mProfileButton.setImageResource(R.drawable.profile);
			mSettingsButton.setImageResource(R.drawable.settings_pressed);
	        
			free();
			push(FRAGMENT_SETTINGS);
	        fragmentReplace(cur());
    		break;
    	case FRAGMENT_USERINFO:
			push(FRAGMENT_USERINFO);
	        fragmentReplace(cur());
    		break;
    	}
    }

	public void push(int index){
		mArrayPath.add(Integer.valueOf(index));
		mCurIndex = index;
	}
	
	public void pop(){
		mArrayPath.remove(mArrayPath.size() - 1);
		mCurIndex = mArrayPath.get(mArrayPath.size() - 1);
	}
	
	public int cur(){
		return mCurIndex;
	}
	
	public void free(){
		mArrayPath.removeAll(mArrayPath);
	}
	
	public void fragmentReplace(int reqNewFragmentIndex) {
		 
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        
        switch (reqNewFragmentIndex) {
        case FRAGMENT_NEWSFEED:
        	transaction.replace(R.id.layout_fragment_content, this.mNewsfeedFragment);        	
        	break;
        case FRAGMENT_NOTIFICATION:
        	transaction.replace(R.id.layout_fragment_content, this.mNotificationFragment);
        	break;
        case FRAGMENT_PROFIEL:
        	transaction.replace(R.id.layout_fragment_content, this.mProfileFragment);
        	break;
        case FRAGMENT_SETTINGS:
        	transaction.replace(R.id.layout_fragment_content, this.mSettingsFragment);
            break;
        case FRAGMENT_USERINFO:
        	transaction.replace(R.id.layout_fragment_content, this.mUserinfoFragment);
        	break;
        }

        transaction.commit(); 
    }
    
    private void showShareActivity(){
    	
		free();
		push(FRAGMENT_ADDVIDEO);
        fragmentReplace(cur());

    	Intent intent = new Intent(this, AddVideoActivity.class);
    	startActivityForResult(intent, REQUEST_SHARE);
    }
}
