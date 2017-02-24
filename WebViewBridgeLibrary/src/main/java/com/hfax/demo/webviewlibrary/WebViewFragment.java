package com.hfax.demo.webviewlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者： lcw on 2016/6/29.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public class WebViewFragment extends Fragment {

	private BridgeWebView webview;
	private FragmentActivity activity;
	private final String URL = "url";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.activity = getActivity();
		View view = inflater.inflate(R.layout.fragment_webview, container, false);
		webview = (BridgeWebView) view.findViewById(R.id.bwv_webview);
//		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		webview.setWebChromeClient(new WebChromeClient());

		Bundle arguments = getArguments();
		if (arguments != null) {
			System.out.println("获取了数据");
			String url = arguments.getString(URL);
			webview.loadUrl(url);
		}else {
			System.out.println("获取网址失败");
			webview.loadUrl("file:///android_assets/demo.html");
		}

		initWebView();
		return view;
	}

	private void initWebView() {

		webview.registerHandler("push", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				Bundle bundle = new Bundle();
				bundle.putString(URL, getUrl(data, URL));
				changFragment(activity, new WebViewFragment(), "webViewFragment", bundle);
			}
		});

		webview.registerHandler("present", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {

				Intent intent = new Intent(activity, WebViewActivity.class);
				intent.putExtra(URL, getUrl(data, URL));
				startActivity(intent);
//				((WebViewActivity)activity).overridePendingTransition(R.anim.activity_enter_in,R.anim.activity_enter_out);
			}
		});
		webview.registerHandler("pop", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				activity.onBackPressed();
			}
		});
		webview.registerHandler("close", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				activity.finish();
				function.onCallBack(data);
			}
		});


		webview.registerHandler("alert", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("title");
				builder.setMessage("哈哈");
				builder.create();
				builder.show();
			}
		});
	}

	private String getUrl(String data, String key) {

		try {
			JSONObject jsonObject = new JSONObject(data);
			String value = jsonObject.getString(key);
			return value;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "json解析错误";
	}

	private void changFragment(FragmentActivity activity, Fragment fragment, String tag, Bundle data) {
		fragment.setArguments(data);
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_enter_left_in, R.anim.fragment_enter_left_out, R.anim.fragment_pop_right_in, R.anim.fragment_pop_right_out);
		ft.add(R.id.fl_root_container, fragment, tag);
		ft.addToBackStack(tag);
		ft.commit();
	}

}
