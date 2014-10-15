package com.gorilaalex.videoprocessing;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;

public class MyActivity extends ActionBarActivity {

    SurfaceView mSurfaceView;

    private static final int VIDEO_CAPTURE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        init();
    }

    private void init() {
        mSurfaceView = (SurfaceView)findViewById(R.id.videoView);

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
        if (id == R.id.action_settings) {
            return true;
        }
        switch(id){
            case R.id.action_exit:
            {
                break;
            }
            case R.id.action_recording:
            {
                break;
            }
            case R.id.action_stop: {
                break;
            }
            case R.id.action_settings: {
                break;
            }
            case R.id.action_replay : {
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
}
