package com.example.can.canmedia;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by can on 17-5-30.
 */

public class VideoManager {

    private static final int LOADTHUMB = 0x733;
    private static final String TAG = "MainActivity";
    private static VideoManager videoManager = new VideoManager();
    private Thread searchThread;
    private List<VideoInfo> videoInfoList;
    private boolean isRunning = false;
    private VideoCallback callback;

    private VideoManager() {
        videoInfoList = new ArrayList<>();
    }


    public static VideoManager getInstance() {
        return videoManager;
    }


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOADTHUMB) {
                if (!isRunning && callback != null) {
                    callback.videoCallBack(videoInfoList);
                }
            }
        }
    };


    public void getVideos(VideoCallback callback) {
        final Context context = VideoApplication.context;
        if (context == null) {
            Log.i(TAG, "loading file");
            return;
        }
        if (isRunning) {
            Log.e(TAG, "file collect is running !");
            return;
        }
        this.callback = callback;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    isRunning = true;
                    ContentResolver resolver = context.getContentResolver();
                    String[] projection = {
                            MediaStore.Video.Media.DISPLAY_NAME,
                            MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.DATE_TAKEN};
                    Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    Cursor cursor = resolver.query(videoUri, projection, null, null, null);
                    VideoInfo videoInfo;
                    while (cursor.moveToNext()) {
                        videoInfo = new VideoInfo();
                        videoInfo.name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
                        long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                        videoInfo.time = dateFormat.format(new Date(time));
                        videoInfo.filePath = "file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        videoInfoList.add(videoInfo);
                    }
                } catch (Exception eee) {
                    Log.i(TAG, "loading exeception : " + eee.getMessage());
                } finally {
                    mainHandler.sendEmptyMessage(LOADTHUMB);
                    isRunning = false;
                }
            }
        };

        searchThread = new Thread(r);
        searchThread.start();
    }


    public interface VideoCallback {
        void videoCallBack(List<VideoInfo> videos);
    }

}
