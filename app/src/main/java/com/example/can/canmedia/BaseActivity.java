package com.example.can.canmedia;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by can on 17-6-6.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    /**
     * 显示进度条
     */
    public void showProgressDialog() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(BaseActivity.this);
                    mProgressDialog.setMessage("正在加载,请稍候···");
                    mProgressDialog
                            .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(true);
                }
                mProgressDialog.show();
            }
        });

    }

    /**
     * 隐藏进度条
     */
    public void dismissProgressDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismissProgressDialog();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
}
