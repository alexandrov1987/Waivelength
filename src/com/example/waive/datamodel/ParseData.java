package com.example.waive.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.example.waive.utils.DialogUtils;
import com.example.waive.utils.NetworkUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;

public class ParseData {
	
	public ParseData(){
		
	}
	
	public List<ParseObject> getNewsFeed(final Context context){

		final List<ParseObject> result = new ArrayList<ParseObject>();
		if(NetworkUtils.isInternetAvailable(context)){
			
			ArrayList<ParseObject> blockedIDs = BlockedList.getBlockedList();
			
			ParseQuery<ParseObject> newsFeedQuery = ParseQuery.getQuery("Waive");
			newsFeedQuery.orderByDescending("createdAt");
			newsFeedQuery.whereNotContainedIn("user", blockedIDs);
			
			try {
				List<ParseObject> objs = newsFeedQuery.find();
				
				for(int i = 0; i < objs.size(); i++){
					ParseObject obj = objs.get(i);

					try {
						obj.fetchIfNeeded();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
				
				result.addAll(objs);

			} catch (ParseException e2) {
				e2.printStackTrace();
			}

//			newsFeedQuery.findInBackground(new FindCallback<ParseObject>() {
//				@Override
//				public void done(List<ParseObject> objs, ParseException e) {
//
//					if(e == null){
//						
//						for(int i = 0; i < objs.size(); i++){
//							ParseObject obj = objs.get(i);
//
//							try {
//								obj.fetchIfNeeded();
//							} catch (ParseException e1) {
//								e1.printStackTrace();
//							}
//						}
//						
//						result.addAll(objs);
//					}else{
//
//						Utility.showErrorAlert(context, "Error", e.getMessage());
//					}
//				}}
//			);
		}else{
			DialogUtils.showErrorAlert(context, "No Internet", "You are not connected to internet. Please connect and try again.");
		}
		
		return result;
	}
}
