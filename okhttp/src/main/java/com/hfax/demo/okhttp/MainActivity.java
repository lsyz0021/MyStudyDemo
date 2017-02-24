package com.hfax.demo.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

	private OkHttpClient okHttpClient;
	private String tag = "MainActivity";

	private String url = "http://www.baidu.com";
	private String json = "{ \"wd:哈哈\" }";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		okHttpClient = new OkHttpClient();                            // 创建okhttp对象

	}

	MediaType mJson = MediaType.parse("appliction/json;charset=utf-8");

	private void post(String url, String json) {

		RequestBody requestBody = RequestBody.create(mJson, json);
		Request request = new Request.Builder()
				.url(url)
				.post(requestBody)
				.build();

		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.v(tag, "post请求异常了 url= " + call.request().url());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.v(tag, "post请求成功了 code= " + response.code() + " ====== " + response.body().string());
			}
		});

	}

	private void get() {
		Request request = new Request.Builder()                       // 创建一个请求
				.url(url)
				.build();
		Call call = okHttpClient.newCall(request);                    // 创建一个回调
		call.enqueue(new Callback() {                                 // 请求加入调度
			@Override
			public void onFailure(Call call, IOException e) {
				Log.v(tag, "get请求异常了 url= " + call.request().url());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.v(tag, "get请求成功了 code= " + response.code() + " ====== " + response.body().string());
			}
		});
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_mian_get:
				get();
				break;

			case R.id.btn_mian_post:
				post(url, json);
				break;
		}
	}

}
