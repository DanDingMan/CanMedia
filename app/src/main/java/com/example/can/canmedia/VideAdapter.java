package com.example.can.canmedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by can on 17-5-29.
 */

public class VideAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private static final String TAG = "VideAdapter";
    private List<VideoInfo> videoInfos;
    private List<View> contentViews;
    private Context context;
    private DisplayImageOptions options;
    private LayoutInflater layoutInflater;
    private ImageLoadingListener animateFirstListener;

    public VideAdapter(Context context, List<VideoInfo> videoInfos) {
        this.videoInfos = videoInfos;
        contentViews = new ArrayList<>();
        this.context = context;
        animateFirstListener = new AnimateFirstDisplayListener();
        layoutInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    @Override
    public int getCount() {
        return videoInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return videoInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.video_item, parent, false);
            contentViews.add(convertView);
        }
        if (videoInfos == null) {
            return null;
        }
        VideoInfo videoInfo = videoInfos.get(position);
        setVideoView(convertView, videoInfo);
        return convertView;
    }

    private void setVideoView(View v, VideoInfo info) {
        TextView nameTv = (TextView) v.findViewById(R.id.name);
        TextView timeTv = (TextView) v.findViewById(R.id.time);
        ImageView thumbImg = (ImageView) v.findViewById(R.id.thumb);
        nameTv.setText(info.name);
        timeTv.setText(info.time);
        ImageLoader.getInstance().displayImage(info.filePath, thumbImg, options, animateFirstListener);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoInfo info = videoInfos.get(position);
        String vedioTitle = info.name;
        String vedioPath = info.filePath;
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEOTITLE, vedioTitle);
        intent.putExtra(VideoActivity.VIDEOPATH, vedioPath);
        context.startActivity(intent);
    }

    static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
