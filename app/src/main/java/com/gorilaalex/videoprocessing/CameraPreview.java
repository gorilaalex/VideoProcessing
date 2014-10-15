package com.gorilaalex.videoprocessing;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Alex on 15.10.2014.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    public static String TAG = "VideoProcessing -  CameraPreview";

    public CameraPreview(Context context,Camera camera) {
        super(context);
        mCamera = camera;

        //install a surfaceHolder.Callback so we get notified when the underlying
        //surface is created and destroyed
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        //deprecated setting, but required on Android versions prior to 3.0
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //the surface was created, now tell the camera where to put the preview
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //if your preview change or rotate, take care of those events here
        //make sure to stop the preview before resizing or reformatting it

        if(mSurfaceHolder.getSurface() == null) {
            //preview surface does not exist
            return;
        }

        //stop preview before making changes
        try {
            mCamera.stopPreview();
        }
        catch(Exception ex) {
            //trying to stop a non-existent preview
        }

        //set preview size and make any resize, rotate or reformatting changes here

        //start preview with new settings
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //empty. Take care of releasing Camera preview in your activity
    }
}
