package com.example.waive.datamodel;

import java.util.ArrayList;

import com.parse.ParseObject;

public class DataManager {

	private static DataManager mInstance = null;
	
	public ArrayList<ParseObject> mWaives = null;
	
	public static DataManager sharedInstance(){
		if(mInstance == null){
			mInstance = new DataManager();
		}
		
		return mInstance;
	}
	
	DataManager(){
		init();
	}

	void init(){
		
		mWaives = new ArrayList<ParseObject>();
	}
	
	public void free(){
		mWaives.removeAll(mWaives);
	}
	
}

