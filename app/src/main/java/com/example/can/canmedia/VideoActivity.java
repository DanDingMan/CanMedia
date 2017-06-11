package com.example.can.canmedia;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import tv.danmaku.ijk.media.example.application.Settings;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by can on 17-5-30.
 */

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";
    public static final String VIDEOTITLE = "vedioTitle";
    public static final String VIDEOPATH = "vedioPath";
    private String vedioTitle;
    private String vedioPath;
    private AndroidMediaController mMediaController;
    //    private Uri mVideoUri;
    private IjkVideoView mVideoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            vedioTitle = intent.getStringExtra(VIDEOTITLE);
            vedioPath = intent.getStringExtra(VIDEOPATH);
            if (TextUtils.isEmpty(vedioTitle) || TextUtils.isEmpty(vedioPath)) {
                Log.e(TAG, "title and videopath can not be null");
                finish();
                return;
            }
            setTitle(vedioTitle);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"screen onResume");
        super.onResume();
        initVideo();
    }

    private void initVideo() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        // prefer mVideoPath
        if (vedioPath != null)
            mVideoView.setVideoPath(vedioPath);
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mMediaController.show();
        mVideoView.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent.putExtra(VIDEOTITLE, vedioTitle);
        intent.putExtra(vedioPath, vedioPath);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"screen onStop");
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }
}
