package com.hfax.umeng;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 作者：lcw 16-9-8
 * 博客：http://blog.csdn.net/lsyz0021/
 */
public class PackageManagerUitls {
    /**
     * 获取 AndroidManifest中application节点下meta-data节点的值
     *
     * @param context
     * @param key
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getMateDataValue(Context context, String key) throws PackageManager.NameNotFoundException {

        ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

        String msg = appInfo.metaData.getString(key);
        return msg;
    }

}
