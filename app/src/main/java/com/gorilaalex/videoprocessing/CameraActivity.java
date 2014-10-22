package com.gorilaalex.videoprocessing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends ActionBarActivity {

    FrameLayout mFrameLayout;

    public static String TAG = "VideoProcessing -  CameraActivity : ";
    public static int MEDIA_TYPE_VIDEO = 2;
    private boolean isRecording = false;


    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private MediaRecorder mMediaRecorder;

    private static final String NO_CAMERA = "This device doesn't have a camera, in order to use this application. ";

    private static final int VIDEO_CAPTURE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        init();
    }

    private void init() {
        if(!hasCamera())
        {
            Toast.makeText(this,NO_CAMERA,Toast.LENGTH_LONG);
            finish();
        }
        else {

            //create an instance of camera
            mCamera = getCameraInstance();

            //create our preview view and set it as the content of our activity
            mCameraPreview = new CameraPreview(this,mCamera);

            mFrameLayout = (FrameLayout) findViewById(R.id.frame);
            mFrameLayout.addView(mCameraPreview);
            prepareVideoRecorder();
        }
    }

    private boolean  prepareVideoRecorder() {

        mCamera = getCameraInstance();
        mMediaRecorder = new MediaRecorder();

        //step 1 : Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        //step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        //step 3: set a CamcorderProfile (API level 8 or >)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        }
        else {
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        }

        //step 4: set output file and check to see if the sd card is mounted
       // if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
      //  }

        //step 5: set the preview output
        mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        //step 6: prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            Toast.makeText(this,"IllegalStateException preparing MediaRecorder: " + e.getMessage(),Toast.LENGTH_LONG);
            finish();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            Toast.makeText(this, "IOException preparing MediaRecorder: " + e.getMessage(), Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if(mMediaRecorder!=null) {
            mMediaRecorder.reset(); //clear recorder configuration
            mMediaRecorder.release(); //release the recorder object
            mMediaRecorder = null;
            mCamera.lock(); //lock camera for later use
        }
    }

    private void releaseCamera() {
        if(mCamera!=null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder(); //if you are using MediaRecorder, release it first
        releaseCamera(); //release the camera in the pause event
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_exit:
            {
                finish();
                break;
            }
            case R.id.action_recording:
            {
                startRecording();
                break;
            }
            case R.id.action_stop: {
                stopRecording();
                break;
            }
            case R.id.action_settings: {
                break;
            }
            case R.id.action_replay : {
                //opens a new activity and replay what was recorded
                Intent intent = new Intent(this,ReplayActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasCamera() {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            return true;
        else return false;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch(Exception ex){

        }
        return c;
    }

    private void startRecording() {
        //initialize video camera
       if(prepareVideoRecorder()) {
       //camera is available and unlocked, MediaRecorder is prepared
       //start recording
           mMediaRecorder.start();

           isRecording = true;
       }
       else{
            //prepare didn't work, release the camera
           releaseMediaRecorder();
           //inform user
       }
    }

    private void stopRecording() {
        if(isRecording) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();
            isRecording = false;
        }
    }

    //create a file uri for saving a video
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //create file for saving video
    private static File getOutputMediaFile(int type) {
        //check to see if the sdCard is mounted


            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "VideoProcessing");

            //create the storage directory if it does not exist
            if(!mediaStorageDir.exists()){
                if(!mediaStorageDir.mkdirs()) {
                    Log.d(TAG,"Failed to create the directory.");
                    return null;
                }
            }

            //create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
            if(type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                 "VID_" + timeStamp +".mp4");
            }
            else return null;

            return mediaFile;
    }

}
