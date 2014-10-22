package com.gorilaalex.videoprocessing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

public class ReplayActivity extends Activity {

    //http://mrbool.com/how-to-play-video-formats-in-android-using-videoview/28299
    //http://docs.gstreamer.com/display/GstSDK/Android+tutorial+4%3A+A+basic+media+player

    VideoView mVideoView;
    DisplayMetrics mDisplayMetrics;
    SurfaceView mSurfaceView;
    MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
    init();
    }

    public void init() {

        mMediaController = new MediaController(this);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mDisplayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int height = mDisplayMetrics.heightPixels;
        int width = mDisplayMetrics.widthPixels;
        mVideoView.setMinimumHeight(height);
        mVideoView.setMinimumWidth(width);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/DCIM/video.mp4");
        mVideoView.start();
    }




}
