package com.bandeng.jpush;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：lcw 16-8-23
 * 博客：http://blog.csdn.net/lsyz0021/
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
