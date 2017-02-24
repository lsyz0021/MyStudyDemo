package com.hfax.demo.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("").create();
		Gson gson = new Gson();
		Intent intent = new Intent();

		Bundle bundle = new Bundle();
		ArrayList<Object> objects = new ArrayList<>();
		intent.putExtra("", objects);
//		打印网络请求 日志
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

		Retrofit retrofit = new Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl("http://www.baidu.com")
				.addConverterFactory(GsonConverterFactory.create())		// 设置converter factory
				.build();

		GitHubService gitHubService = retrofit.create(GitHubService.class);


	}
}
