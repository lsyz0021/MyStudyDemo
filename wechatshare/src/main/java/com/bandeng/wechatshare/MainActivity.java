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
        Button btn_button = (Button) findViewById(R.id.btn_button);

        btn_popWindow.setOnClickListener(this);
        btn_button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_popWindow:
                // 设置分享的内容
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("webpageUrl", "http://blog.csdn.net/lsyz0021/");
                data.put("title", "星空武哥的博客");
                data.put("des", "适合初学android的博客，我们可以一起交流学习android，欢迎访问。");

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

            case R.id.btn_button:
                Intent intent = new Intent(MainActivity.this, WeChatShareActivity.class);
                startActivity(intent);
                break;
        }
    }
}
