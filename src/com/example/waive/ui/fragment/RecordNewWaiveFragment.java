package com.example.waive.ui.fragment;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.waive.R;
import com.example.waive.common.media.CameraHelper;
import com.example.waive.datamodel.DataManager;
import com.example.waive.ui.activity.AddNewWaiveActivity;
import com.example.waive.ui.interfaces.OnChangeListener;
import com.example.waive.ui.view.VideoThumbnailsView;
import com.example.waive.ui.view.WaiveView;
import com.example.waive.utils.DialogUtils;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class RecordNewWaiveFragment extends Fragment implements SurfaceHolder.Callback, ImageChooserListener{

	private static final String TAG = "Recorder";
	private static final int TIME_INTERVAL = 100;
	
    private Camera 				mCamera = null;
    private SurfaceView 		mPreview = null;
    private SurfaceHolder 		mSurfaceHolder = null;
    private MediaRecorder 		mMediaRecorder = null;
    private WaiveView			mWaiveView = null;
    private RelativeLayout		mTimeLayout = null;
    private TextView			mTimeTextView = null;
    
    private int 				mTimeSinceTimeViewPause = 0;
    private List<String> 		mVideoList = null;
    private ImageChooserManager mImageChooserManager = null;
    private boolean 			mIsFacingCamera = false;
    private boolean 			mPresentedVideoLibraryUI = false;
    private boolean 			mIsRecording = false;
    private boolean				mIsPlaying = false;
    private boolean				mRecordedFull30Secs = false;
    private boolean				mStopButtonHeld = false;
    private AddNewWaiveActivity mParent = null;
    
    private Handler 			mTimerHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
	
			if(!mStopButtonHeld){
				mStopButtonHeld = true;
				mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);

				mTimeLayout.setVisibility(View.VISIBLE);
				new MediaPrepareTask().execute(null, null, null);

			}else{

				if(mTimeSinceTimeViewPause >= 30000){
					
					mTimerHandler.removeMessages(0);
					mRecordedFull30Secs = true;
					mStopButtonHeld = false;
					
				}else{
					
					mTimeSinceTimeViewPause += TIME_INTERVAL;
					mWaiveView.setWaiveScale((float)mTimeSinceTimeViewPause/30000);
					mWaiveView.invalidate();
					
					mTimeTextView.setText(String.format("%05.2f", (float)mTimeSinceTimeViewPause/1000));
					mTimeLayout.setX((float)mTimeSinceTimeViewPause/30000*mWaiveView.getWidth() - mTimeLayout.getWidth()/2);
					mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
				}
			}
			
			super.handleMessage(msg);
		}
    };
    
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (prepareVideoRecorder()) {
                mMediaRecorder.start();
                
            } else {
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
            	Toast.makeText(mParent, "Camera Recording Error", Toast.LENGTH_LONG).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_addvideo, container, false);	
		mParent = (AddNewWaiveActivity)getActivity();
		
        ImageButton closeButton = (ImageButton)v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mVideoList.size() > 0){

			        AlertDialog.Builder ab = new AlertDialog.Builder(mParent);
			        ab.setTitle("Discard Video");
			        ab.setMessage("Would you like to discard the recorded video?");
			        ab.setNegativeButton("No", null);
			        ab.setPositiveButton("Yes", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
			        ab.show();
				}else{
					mParent.finish();
				}
			}
		});

        ImageView recordButton = (ImageView)v.findViewById(R.id.recordButton);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					if(!mRecordedFull30Secs){
						
		                mTimerHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
					}else{
						DialogUtils.showErrorAlert(mParent, "30 Seconds!", "Waivelength only supports posting videos that are leas than 30 seconds.");
					}
					
				}else if(event.getAction() == MotionEvent.ACTION_UP){

					if (mStopButtonHeld) {
			        	try{
				            mMediaRecorder.stop();  // stop the recording
				            releaseMediaRecorder(); // release the MediaRecorder object
				            mCamera.lock();         // take camera access back from MediaRecorder
				            mStopButtonHeld = false;
			        	}catch(RuntimeException e){
			        		
			        	}
			        }
					
					mTimerHandler.removeMessages(0);
		        }

				return true;
			}
		});

        ImageButton rotateButton = (ImageButton)v.findViewById(R.id.rotateCameraButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mIsFacingCamera = !mIsFacingCamera;
				
				releaseCamera();
				prepareCamera();
				refreshCamera();
			}
		});
        
        ImageButton galleryButton = (ImageButton)v.findViewById(R.id.uploadButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseVideo();
			}
		});
        
        ImageButton nextButton = (ImageButton)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mVideoList.size() > 0){
//					Intent intent = new Intent(mParent, TrimAndThumbnailFragment.class);
//					intent.putExtra("video", mVideoList.get(0));
//					startActivity(intent);
					
					DataManager.sharedInstance().mVideoPath = mVideoList.get(0);
					mParent.goTo(AddNewWaiveActivity.FRAGMENT_TRIM);
				}else{
					DialogUtils.showErrorAlert(mParent, "No Waive", "Please record a waive");
				}
			}
		});
        
        mTimeLayout = (RelativeLayout)v.findViewById(R.id.timeLayout);
        mTimeLayout.setVisibility(View.GONE);
        mTimeTextView = (TextView)v.findViewById(R.id.timeTextView);
        mWaiveView = (WaiveView)v.findViewById(R.id.waiveView);
        
        mVideoList = new ArrayList<String>();
        mPreview = (SurfaceView)v.findViewById(R.id.texture);
        mSurfaceHolder = mPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return v;
	}
	
    @Override
	public void onStop() {
		super.onStop();
		
        releaseMediaRecorder();
        releaseCamera();
	}

	@Override
    public void surfaceCreated(SurfaceHolder holder) {
       
    	prepareCamera();
    	refreshCamera();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	
        refreshCamera();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    	try{
        	if(mCamera != null){
            	mCamera.stopPreview();
            	mCamera.release();
            	mCamera = null;
        	}
    		
    	}catch(RuntimeException e){
    		
    	}
    }

	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImageChosen(ChosenImage arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void chooseVideo() {
		mImageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_VIDEO, "", true);
		mImageChooserManager.setImageChooserListener(this);
		try {
			
			mVideoList.add(mImageChooserManager.choose());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private boolean prepareVideoRecorder(){

        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        //mMediaRecorder.setProfile(profile);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        
        String videoPath = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO).toString();
        mVideoList.add(videoPath);
        mMediaRecorder.setOutputFile(videoPath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setOrientationHint(90);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder(){

    	if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private void prepareCamera(){

        mCamera = mIsFacingCamera ? CameraHelper.getDefaultFrontFacingCameraInstance() : 
        	CameraHelper.getDefaultBackFacingCameraInstance();

        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,
                mPreview.getWidth(), mPreview.getHeight());

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }
    
    private void releaseCamera(){
        if (mCamera != null){

        	try {
        		mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
        	}        
        	catch (Exception e) {
        	}
        }
    }
    
    public void refreshCamera() {
        
    	if (mSurfaceHolder.getSurface() == null) {
           return;
        }
        
    	try {
    		mCamera.stopPreview();
    	}        
    	catch (Exception e) {
    	}
        
    	try {
    		mCamera.setPreviewDisplay(mSurfaceHolder);
    		mCamera.startPreview();
    	}
    	catch (Exception e) {
        
    	}
    }
    
    void mergeVideos(){
    	
    }
}
