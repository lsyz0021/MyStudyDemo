package com.bandeng.wechatshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private LinearLayout shareBlackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shareBlackView = (LinearLayout) findViewById(R.id.showblackview);
        Button btn_popWindow = (Button) findViewById(R.id.btn_popWindow);
        Button btn_popWindow2 = (Button) findViewById(R.id.btn_popWindow2);
        Button btn_button = (Button) findViewById(R.id.btn_button);

        btn_popWindow.setOnClickListener(this);
        btn_popWindow2.setOnClickListener(this);
        btn_button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        // 设置分享的内容
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("webpageUrl", "http://blog.csdn.net/lsyz0021/");
        data.put("title", "星空武哥的博客");
        data.put("des", "欢迎交流学习Android，点击访问博客 http://blog.csdn.net/lsyz0021/");
        switch (v.getId()) {

            case R.id.btn_popWindow:

                ShareWindow shareWindow = new ShareWindow(MainActivity.this, data);
                // PopupWindow显示的位置
                shareWindow.showAtLocation(shareBlackView, Gravity.BOTTOM | Gravity.FILL, 0, 0);
                shareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        shareBlackView.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.btn_popWindow2:

                ShareWindow2 shareWindow2 = new ShareWindow2(MainActivity.this, data);
                // PopupWindow显示的位置
                shareWindow2.showAtLocation(shareBlackView, Gravity.BOTTOM, 0, 0);
                shareWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        shareBlackView.setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.btn_button:
                Intent intent = new Intent(MainActivity.this, WeChatShareActivity.class);
                startActivity(intent);
                break;
        }
    }
}
