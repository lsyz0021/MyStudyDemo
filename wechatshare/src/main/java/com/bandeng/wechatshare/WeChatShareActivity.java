package com.bandeng.wechatshare;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： lcw on 2016/7/18.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class WeChatShareActivity extends Activity {

    private static IWXAPI wxApi;
    private static String weChatShareAppID = "";
    private Map<String, String> map = new HashMap<String, String>();
    private int scaledBitmapSize = 240;                    // 压缩图片值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin);

        try {
            // 获取清单文件里的AppID
            ApplicationInfo info = this.getPackageManager().getApplicationInfo(getPackageName()
                    , PackageManager.GET_META_DATA);

            weChatShareAppID = info.metaData.getString("WXAPPID");
            wxApi = WXAPIFactory.createWXAPI(this, weChatShareAppID, true);
            wxApi.registerApp(weChatShareAppID);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 设置分享的内容
        map.put("webpageUrl", "http://blog.csdn.net/lsyz0021/");
        map.put("title", "星空武哥的博客");
        map.put("des", "适合初学android的博客，我们可以一起交流学习android，欢迎访问。");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:    // 分享到好友
                shareWeChat(0);
                break;
            case R.id.btn_2:    // 分享到朋友圈
                shareWeChat(1);
                break;
            case R.id.btn_3:    // 短信分享
                sendShareSMS();
                break;
        }
    }

    /**
     * 分享到微信好友或者朋友圈
     *
     * @param flag 0：分享到微信好友，1：分享到朋友圈
     */
    private void shareWeChat(int flag) {

        // 判断是否安装了微信客户端
        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(this, "你还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();                // 初始化WXWebpageObject一个填写url
        webpage.webpageUrl = map.get("webpageUrl");                     // 设置点击分享打开的url

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = map.get("title");                                    // 设置分享显示的标题
        msg.description = map.get("des");                                // 设置分享显示的网页描述
        // 设置分享显示的图标，这里要注意，Bitmap要压缩否则有可能打不开分享功能，图片最好是png格式
        Bitmap thumb = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_share_thumb);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumb, scaledBitmapSize, scaledBitmapSize, true);
        thumb.recycle();
        msg.setThumbImage(scaledBitmap);                                 // 设置显示的图标
        SendMessageToWX.Req req = new SendMessageToWX.Req();             // 构造一个Req
        req.transaction = String.valueOf(System.currentTimeMillis());    // 设置一个唯一标识请求
        req.message = msg;                                               // 设置请求内容设置给请求信息
        // 判断是分享给好友还是朋友圈
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        wxApi.sendReq(req);                                              // 调用api接口发送数据到微信
    }

    /**
     * 短信分享
     */
    private void sendShareSMS() {
        Uri smsToUri = Uri.parse("smsto:");
        String message = map.get("des");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);
        this.startActivity(intent);
    }


}
