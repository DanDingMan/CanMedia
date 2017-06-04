package com.example.can.canmedia;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoManager.VideoCallback {
    private static final String TAG = "MainActivity";
    private ListView videoListView;
    private List<VideoInfo> videoInfos;
    private VideAdapter videAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        videoListView = (ListView) findViewById(R.id.videlist);
        checkPermission();

    }

    private void checkPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0x701);
        } else {
            loadVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x701 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideos();
        }
    }

    private void loadVideos() {
        if (videoInfos == null) {
            videoInfos = new ArrayList<>();
        } else {
            videoInfos.clear();
        }
        videAdapter = new VideAdapter(this, videoInfos);
        videoListView.setAdapter(videAdapter);
        videoListView.setOnItemClickListener(videAdapter);
        VideoManager.getInstance().getVideos(this);
    }


    @Override
    public void videoCallBack(List<VideoInfo> videos) {
        if (videoInfos == null) {
            this.videoInfos = videos;
            return;
        }
        if (videoInfos != videos) {
            videoInfos.clear();
            videoInfos.addAll(videos);
        }
        videAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.videoInfos = null;
    }
}
