package com.bandeng.mystudydemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author：Li ChuanWu on 2016/12/16
 * Blog  ：http://blog.csdn.net/lsyz0021/
 */
public class SPUtils {

    private SharedPreferences mSP;


    public SharedPreferences builder(Context context) {
        if (mSP == null) {
            synchronized (SPUtils.class) {
                if (mSP == null) {
                    return mSP = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                }
            }
        }

        return mSP;
    }

}
