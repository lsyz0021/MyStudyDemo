package com.bandeng.sharesdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSSDK.initSDK(this, "167e6d89b1e3d", "8f8f8e0d423d2057d7fef4f93b1b0940");

        Button btn_onekey = (Button) findViewById(R.id.btn_oneKeyShare);
        Button btn_oneSms = (Button) findViewById(R.id.btn_oneSms);

        btn_onekey.setOnClickListener(this);
        btn_oneSms.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_oneKeyShare:
                showShare();
                break;

            case R.id.btn_oneSms:
                initSms();
                break;


        }
    }

    private void initSms() {

        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
//                super.afterEvent(i, i1, o);

                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    System.out.println("country = " + country + " phone= " + phone);
                    // 提交用户信息（此方法可以不调用）
//                        registerUser(country, phone);
                }

            }
        });

        registerPage.show(MainActivity.this);
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法

        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://blog.csdn.net/lsyz0021/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("欢迎访问我的博客 http://blog.csdn.net/lsyz0021/");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        String url = "http://avatar.csdn.net/D/3/A/1_lsyz0021.jpg";
        oks.setImageUrl(url);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://blog.csdn.net/lsyz0021/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }


}
