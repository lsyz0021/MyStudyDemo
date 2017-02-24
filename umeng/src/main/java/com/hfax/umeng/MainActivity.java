package com.hfax.umeng;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 场景类型设置接口,设置为普通统计场景类型
        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

        String appkey = "";
        String channal = "";
        try {
            appkey = PackageManagerUitls.getMateDataValue(this, "UMENG_APPKEY");
            channal = PackageManagerUitls.getMateDataValue(this, "UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, appkey, channal);

        MobclickAgent.startWithConfigure(config);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
