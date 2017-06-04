package com.example.can.canmedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by can on 17-5-30.
 */

public class VideoActivity extends AppCompatActivity {
    String vedioTitle;
    String vedioPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            vedioTitle = intent.getStringExtra("vedioTitle");
            vedioPath = intent.getStringExtra("vedioPath");
            setTitle(vedioTitle);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent.putExtra("vedioTitle", vedioTitle);
        intent.putExtra("vedioPath", vedioPath);
        super.onNewIntent(intent);
    }
}
