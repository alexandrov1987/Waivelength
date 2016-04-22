package com.example.waive.ui.activity;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.waive.R;
import com.example.waive.common.media.CameraHelper;
import com.example.waive.utils.DialogUtils;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AddVideoActivity extends Activity implements SurfaceHolder.Callback, ImageChooserListener{

	private static final String TAG = "Recorder";
	private static final int TIME_INTERVAL = 10;
	
    private Camera 				mCamera;
    private SurfaceView 		mPreview;
    private SurfaceHolder 		mSurfaceHolder;
    private MediaRecorder 		mMediaRecorder;
    
    private int 				mTotalRecordingSecond = 0;
    private List<String> 		mVideoList = null;
    private ImageChooserManager mImageChooserManager = null;
    private boolean 			isRecording = false;
    private boolean 			mIsFacingCamera = false;
    private Handler 			mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
	
			if(mTotalRecordingSecond < 30000){
				mTotalRecordingSecond += TIME_INTERVAL;
				mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
			}else{
				DialogUtils.showErrorAlert(AddVideoActivity.this, "", "Waivelength videos are 30 seconds. Please choose a smaller video.");
				mVideoList.removeAll(mVideoList);
			}
			
			super.handleMessage(msg);
		}
    };
    
    /**
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                isRecording = true;
                mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL);
                
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                //AddVideoActivity.this.finish();
            	Toast.makeText(AddVideoActivity.this, "Camera Recording Error", Toast.LENGTH_LONG).show();
            }
        }
    }

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

        ImageView recordButton = (ImageView)findViewById(R.id.recordButton);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					
					if(!isRecording){
						new MediaPrepareTask().execute(null, null, null);
					}
					
				}else if(event.getAction() == MotionEvent.ACTION_UP){

					if (isRecording) {
			        	try{
				            mMediaRecorder.stop();  // stop the recording
				            releaseMediaRecorder(); // release the MediaRecorder object
				            mCamera.lock();         // take camera access back from MediaRecorder
				            isRecording = false;
			        	}catch(RuntimeException e){
			        		
			        	}
			        }				
		        }

				return true;
			}
		});

        ImageButton rotateButton = (ImageButton)findViewById(R.id.rotateCameraButton);
        rotateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mIsFacingCamera = !mIsFacingCamera;
				
				releaseCamera();
				prepareCamera();
				refreshCamera();
			}
		});
        
        ImageButton galleryButton = (ImageButton)findViewById(R.id.uploadButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseVideo();
			}
		});
        
        ImageButton nextButton = (ImageButton)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddVideoActivity.this, TrimVideoActivity.class);
				intent.putExtra("video", mVideoList.get(0));
				startActivity(intent);
			}
		});
        
        mVideoList = new ArrayList<String>();
        mPreview = (SurfaceView) findViewById(R.id.texture);
        mSurfaceHolder = mPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
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

        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
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
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void prepareCamera(){
        // BEGIN_INCLUDE (configure_preview)
        mCamera = mIsFacingCamera ? CameraHelper.getDefaultFrontFacingCameraInstance() : 
        	CameraHelper.getDefaultBackFacingCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,
                mPreview.getWidth(), mPreview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        // END_INCLUDE (configure_preview)
        mCamera.setDisplayOrientation(90);
    }
    
    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
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


}
