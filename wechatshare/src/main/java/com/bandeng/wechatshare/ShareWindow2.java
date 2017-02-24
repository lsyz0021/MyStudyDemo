package com.bandeng.wechatshare;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;


/**
 * Created by WangSummer on 15/11/13.
 */
public class ShareWindow2 extends PopupWindow implements OnClickListener {

    private static IWXAPI wxApi;

    private ImageView btnShareWeiXin, btnShareSMS, btnShareWeiXinQuan;
    private View mMenuView;
    Context context;
    private int scaledBitmapSize = 240;                    // 压缩图片值
    private Map<String, Object> hashMap;
    private static String weChatAppID;

    public ShareWindow2(Context context, Map<String, Object> shareMsm) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_share2, null);
        this.context = context;
        this.hashMap = shareMsm;
        try {
            // 获取清单文件里的AppID
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName()
                    , PackageManager.GET_META_DATA);
            weChatAppID = info.metaData.getString("WXAPPID");
            wxApi = WXAPIFactory.createWXAPI(context, weChatAppID, true);
            wxApi.registerApp(weChatAppID);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btnShareWeiXin = (ImageView) mMenuView.findViewById(R.id.shareweixin);
        btnShareWeiXinQuan = (ImageView) mMenuView.findViewById(R.id.sharefriendquan);
        btnShareSMS = (ImageView) mMenuView.findViewById(R.id.sharesms);

        btnShareWeiXinQuan.setOnClickListener(this);
        btnShareSMS.setOnClickListener(this);
        btnShareWeiXin.setOnClickListener(this);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((int) event.getY() < mMenuView.findViewById(R.id.pop_layout).getTop()) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.shareweixin:
                shareWeChat(0);
                break;
            case R.id.sharefriendquan:
                shareWeChat(1);
                break;
            case R.id.sharesms:
                sendShareSMS();
                break;
        }
    }

    /**
     * 短信分享
     */
    private void sendShareSMS() {
        Uri smsToUri = Uri.parse("smsto:");
        String message = (String) hashMap.get("des");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }

    /**
     * 分享到微信好友或者朋友圈
     *
     * @param flag 0：分享到微信好友，1：分享到朋友圈
     */
    private void shareWeChat(int flag) {

        // 判断是否安装了微信客户端
        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "你还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();                   // 初始化WXWebpageObject一个填写url
        webpage.webpageUrl = (String) hashMap.get("webpageUrl");        // 设置点击分享打开的url
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = (String) hashMap.get("title");                        // 设置分享显示的标题
        msg.description = (String) hashMap.get("des");                    // 设置分享显示的网页描述
        // 设置分享显示的图标，这里要注意，Bitmap要压缩否则有可能打不开分享功能，图片最好是png格式
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_share_thumb);
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

}
