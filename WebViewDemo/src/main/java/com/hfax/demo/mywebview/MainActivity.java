package com.hfax.demo.mywebview;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

	private WebView webview;
	private String url = "http://www.jd.com/";
	private String url2 = "http://www.youku.com/";
	private String url3 = "https://www.12306.cn/";
	private String url4 = "http://10.88.4.74:8080/sdk/sdk/v2.0/n/filtration?applicationId=fdf&authKey=340de1cedd07a6ae2ddc3cd5f488ff7df8c63c248ebce38b621f2e226518c2b1&merchantId=10001&merchantKey=fdf&mobile=13512345678&userId=12345&userName=zhangsan";

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("玩命加载中……");
		}

		webview = (WebView) findViewById(R.id.wv_webView);

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setSaveFormData(true);

		settings.setAppCacheEnabled(false);// 不使用缓存
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);


		webview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 拦截网址跳转指定网址
//				if (url != null && url.contains("youku")) {
//					webview.loadUrl("https://www.12306.cn/");
//					System.out.println("url = "+ url);
//				}
				webview.loadUrl(url);
				return false;
			}

			// SSL错误会调用此方法
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//				super.onReceivedSslError(view, handler, error);

				System.out.println("网页错误了SSL");
				webview.loadUrl("file:///android_asset/web.html");		// 显示错误后加载指定的本地网页
//				handler.proceed();	//错误后还继续加载
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				System.out.println("网页错误了");
				webview.loadUrl("file:///android_asset/web.html");
			}
			// 网页加载中
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressDialog.show();
			}
			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressDialog.dismiss();
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				// 另一种显示进度条的方式
//				if (newProgress == 100){
//					progressDialog.dismiss();
//				}else {
//					progressDialog.show();
//				}
			}
		});

		// 加载网页
		webview.loadUrl(url2);

		//自定义返回键功能
		webview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						// 如果webView可以后退，则后退，否则finish掉当前的activity
						if (webview.canGoBack()) {
							webview.goBack();
							return true;
						} else {
							finish();
//							return false;
						}
					}
				}
				return false;
			}
		});

	}


	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_1:

				// 点击按钮加载网页
				webview.loadUrl(url4);

				break;
			case R.id.btn_2:

				webview.loadUrl("file:///android_asset/web.html");

				break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
