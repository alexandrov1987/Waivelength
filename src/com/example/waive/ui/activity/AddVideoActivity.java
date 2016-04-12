package com.example.waive.ui.activity;


import com.example.waive.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AddVideoActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvideo);
        
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
    }
}
