package com.example.waive.ui.view;

import com.example.waive.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class VideoTrimView extends View {

	Paint 	mPaint = null;
	Paint 	mPaint1 = null;
	int 	mWidth = 0;
	int 	mHeight = 0;
	int		mStart = 0;
	int 	mEnd = 0;
	Path	mSelectPortionPath = null;
	Bitmap	mLeftSing = null;
	Bitmap	mRightSing = null;
	float 	mDensity = 0;
	
	public VideoTrimView(Context context,AttributeSet attrs) {
		super(context, attrs);
		
		mLeftSing = BitmapFactory.decodeResource(getResources(), R.drawable.sing_selected_left);
		mRightSing = BitmapFactory.decodeResource(getResources(), R.drawable.sing_selected_right);
		
		mDensity = context.getResources().getDisplayMetrics().density;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mSelectPortionPath.reset();
		mSelectPortionPath.moveTo(mStart, mHeight/2);
		mSelectPortionPath.lineTo(mEnd, mHeight/2);
		
		canvas.drawColor(Color.rgb(186, 242, 224));
		canvas.drawPath(mSelectPortionPath, mPaint);
		canvas.drawBitmap(mLeftSing, new Rect(0, 0, 18, 81), new Rect(mStart, 0, (int) (10 * mDensity + 0.5f), mHeight), mPaint1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = w;
		mHeight = h;
		
		mStart = 0;
		mEnd = mWidth;
		
		if(mPaint == null){
			mPaint = new Paint();
		}

		mPaint.setStrokeWidth(mHeight);
		mPaint.setColor(Color.rgb(90, 199, 170));
		mPaint.setStyle(Paint.Style.STROKE);
		
		if(mPaint1 == null){
			mPaint1 = new Paint();
		}
		
		if(mSelectPortionPath == null){
			mSelectPortionPath = new Path();
		}
	}
	
	public void setStartEnd(int start, int end){
		mStart = start;
		mEnd = end;
	}
}
