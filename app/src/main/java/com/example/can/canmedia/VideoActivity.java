package com.example.can.canmedia;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
    private String mVideoPath;
    //    private Uri mVideoUri;
    private IjkVideoView mVideoView;
    private TextView mToastTextView;
    private TableLayout mHudView;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mRightDrawer;
    private Settings mSettings;
    private boolean mBackPressed;

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
            if (TextUtils.isEmpty(vedioTitle) || TextUtils.isEmpty(VIDEOPATH)) {
                finish();
                return;
            }
            setTitle(vedioTitle);
            initVideo();
        }
    }

    private void initVideo() {
        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);

        mToastTextView = (TextView) findViewById(R.id.toast_text_view);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);
        // prefer mVideoPath
        if (mVideoPath != null)
            mVideoView.setVideoPath(mVideoPath);
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mVideoView.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent.putExtra(VIDEOTITLE, vedioTitle);
        intent.putExtra(vedioPath, vedioPath);
        super.onNewIntent(intent);
    }
}
