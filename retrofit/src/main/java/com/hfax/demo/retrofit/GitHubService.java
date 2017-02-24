package com.hfax.demo.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 作者： lcw on 2016/7/14.
 * 博客： http://blog.csdn.net/lsyz0021/
 */
public interface GitHubService {

	@GET("users/{user}/repos")
	Call<List<Repo>> listRepos(@Path("user") String user);

}
