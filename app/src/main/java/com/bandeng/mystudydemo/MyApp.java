package com.bandeng.mystudydemo;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Author：Li ChuanWu on 2016/10/26
 * Blog  ：http://blog.csdn.net/lsyz0021/
 */
public class MyApp extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        if (!quickStart()) {
            if (needWait(base)) {
                waitForDexopt(base);
            }
            MultiDex.install(this);
        } else {
            return;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (quickStart()) {
            return;
        }
    }

    private void waitForDexopt(Context base) {

    }

    private boolean needWait(Context base) {
        return false;
    }


    public boolean quickStart() {
        if (Thread.currentThread().getName().equals(":mini")) {
            return true;
        }

        return false;
    }


}
