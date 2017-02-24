package com.bandeng.mystudydemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String phoneNumber = "10086";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 拨打电话
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 大于等于6.0的系统在动态申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                    } else {
//                        callPhone(phoneNumber);
//                    }
                    callDial(phoneNumber);
                } else {
                    callPhone(phoneNumber);
                }
            }
        });

        // 进入拨号界面
        findViewById(R.id.btn_dial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callDial(phoneNumber);
                openUrl(MainActivity.this, "https://");
            }
        });
    }

    /**
     * 直接拨打电话，需要添加拨打电话的权限 android.permission.CALL_PHONE
     * android 6.0及以上系统需要动态申请打电话权限
     *
     * @param phoneNumber 要拨打的电话号码
     */
    public void callPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phoneNumber);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 调到拨号界面
     *
     * @param phoneNumber 要拨打的电话号码
     */
    public void callDial(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri uri = Uri.parse("tel:" + phoneNumber);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone("10086");
            } else {
                Toast.makeText(this, "请开启拨打电话权限后使用", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 用浏览器打开指定网址，可用于下载文件
     *
     * @param context context
     * @param url     打开的网址
     * @return 是否能成功打开网址
     */
    private boolean openUrl(Context context, String url) {
        if (!(TextUtils.isEmpty(url)) && (url.startsWith("http://") || url.startsWith("https://"))) {
            //直接下载方式
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            context.startActivity(intent);
            return true;
        } else {
            Toast.makeText(context, "网址格式错误!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    WebView mWebView;   // 当前的webView
    LinearLayout mLinearLayout; // webView所在的外面的布局

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mLinearLayout.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }


}
