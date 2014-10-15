package com.gorilaalex.videoprocessing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraActivity extends ActionBarActivity {

    FrameLayout mFrameLayout;

    private Camera mCamera;
    private CameraPreview mCameraPreview;

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


        }
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
        //here I will start recording
        // http://developer.android.com/guide/topics/media/camera.html
    }

    private void stopRecording() {

    }
}
