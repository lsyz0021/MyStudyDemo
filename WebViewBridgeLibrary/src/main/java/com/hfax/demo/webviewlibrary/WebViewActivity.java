package com.hfax.demo.webviewlibrary;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;

/**
 * 作者： lcw on 2016/6/29.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class WebViewActivity extends FragmentActivity {

	private String url = "http://10.64.1.53/Website/JSBridgeSite/";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_root);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("url");
		}

		Bundle data = new Bundle();
		data.putString("url", url);
		changFragment(new WebViewFragment(), "tag", data);
	}

	private void changFragment(Fragment fragment, String tag, Bundle data) {
		fragment.setArguments(data);
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(R.id.fl_root_container, fragment, tag);
		ft.commit();
	}

}
