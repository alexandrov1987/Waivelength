package com.waivelength;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {
	
	static boolean isInternetAvailable(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();		
	}
	
	static void showErrorAlert(Context context, String title, String message){
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setNegativeButton("OK", null);
        ab.show();
	}
}
