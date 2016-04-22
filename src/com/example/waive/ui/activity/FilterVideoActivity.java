package com.example.waive.ui.activity;

import com.example.waive.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class FilterVideoActivity extends Activity {
	
	private Context mContext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_filtervideo);
		
		mContext = this;
		
		final String videoPath = getIntent().getExtras().getString("video");
		VideoView videoView = (VideoView)findViewById(R.id.videoView);
		videoView.setVideoPath(videoPath);
		videoView.start();
		
		ImageButton backButton = (ImageButton)findViewById(R.id.closeButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ImageButton nextButton = (ImageButton)findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FilterVideoActivity.this, PostVideoActivity.class);
				intent.putExtra("video", videoPath);
				startActivity(intent);
			}
		});
		
		final RelativeLayout noneButton = (RelativeLayout)findViewById(R.id.noneButton);
		final RelativeLayout sepiaButton = (RelativeLayout)findViewById(R.id.sepiaButton);
		final RelativeLayout monoButton = (RelativeLayout)findViewById(R.id.monoButton);
		final RelativeLayout saturationButton = (RelativeLayout)findViewById(R.id.saturationButton);
		final RelativeLayout gammaButton = (RelativeLayout)findViewById(R.id.gammaButton);
		final RelativeLayout dimButton = (RelativeLayout)findViewById(R.id.dimButton);
		final RelativeLayout amatrokaButton = (RelativeLayout)findViewById(R.id.amatrokaButton);
		final RelativeLayout softButton = (RelativeLayout)findViewById(R.id.softButton);
		final RelativeLayout hueButton = (RelativeLayout)findViewById(R.id.hueButton);

		final TextView noneTextView = (TextView)findViewById(R.id.noneTextView);
		final TextView sepiaTextView = (TextView)findViewById(R.id.sepiaTextView);
		final TextView monoTextView = (TextView)findViewById(R.id.monoTextView);
		final TextView saturationTextView = (TextView)findViewById(R.id.saturationTextView);
		final TextView gammaTextView = (TextView)findViewById(R.id.gammaTextView);
		final TextView dimTextView = (TextView)findViewById(R.id.dimTextView);
		final TextView amatrokaTextView = (TextView)findViewById(R.id.amatrokaTextView);
		final TextView softTextView = (TextView)findViewById(R.id.softTextView);
		final TextView hueTextView = (TextView)findViewById(R.id.hueTextView);
		
		
		noneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
			}
		});

		
		sepiaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});

		
		monoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		saturationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		gammaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});

		
		dimButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));

				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		amatrokaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		softButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));

			}
		});
		
		
		hueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				noneButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				sepiaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				monoButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				saturationButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				gammaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				dimButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				amatrokaButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				softButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_bg_color));
				hueButton.setBackgroundColor(mContext.getResources().getColor(R.color.filter_select_bg_color));
				
				noneTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				sepiaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				monoTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				saturationTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				gammaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				dimTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				amatrokaTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				softTextView.setTextColor(mContext.getResources().getColor(R.color.filter_text_color));
				hueTextView.setTextColor(mContext.getResources().getColor(R.color.filter_select_text_color));

			}
		});
	}

}
