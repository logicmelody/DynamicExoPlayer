package com.dl.dynamicexoplayer.okhttp;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by dannylin on 2018/3/21.
 */

public class ApiManager {

	public volatile static OkHttpClient sHttpClient = new OkHttpClient();

//	public synchronized static OkHttpClient getHttpClientInstance(Context context) {
//		if (sHttpClient == null)
//			sHttpClient = buildHttpClient(context);
//		return sHttpClient;
//	}

//	private static OkHttpClient buildHttpClient(@NonNull Context context) {
//		OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//		builder.addInterceptor(interceptor);
//
//		builder.connectTimeout(5, TimeUnit.MINUTES);
//		return builder.build();
//	}
}
