package com.bandeng.buglytinker;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Author：Li ChuanWu on 2017/1/16
 * Blog  ：http://blog.csdn.net/lsyz0021/
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "78c1bf767f", false);
    }
}
