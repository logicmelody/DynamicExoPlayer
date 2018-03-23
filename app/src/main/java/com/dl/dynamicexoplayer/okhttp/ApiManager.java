package com.dl.dynamicexoplayer.okhttp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by dannylin on 2018/3/21.
 */

public class ApiManager {

	private volatile static ApiManager sApiManager;

	private static OkHttpClient sOkHttpClient;
	private volatile static Cache sCache;

	public static ApiManager getInstance(@NonNull Context context) {
		if (sApiManager == null) {
			synchronized (ApiManager.class) {
				if (sApiManager == null) {
					sApiManager = new ApiManager(context);
				}
			}
		}

		return sApiManager;
	}

	private ApiManager(@NonNull Context context) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		builder.addNetworkInterceptor(new StethoInterceptor());

		List<Protocol> protocols = new ArrayList<>();
		protocols.add(Protocol.HTTP_2);
		protocols.add(Protocol.HTTP_1_1);

		builder.protocols(protocols);
		builder.connectTimeout(30, TimeUnit.SECONDS);
		builder.readTimeout(30, TimeUnit.SECONDS);
		builder.retryOnConnectionFailure(false);

		builder.cache(buildCache(context));

		sOkHttpClient = builder.build();
	}

	private static Cache buildCache(@NonNull Context context) {
		return new Cache(context.getCacheDir(), 500 * 1024 * 1024);
	}

	public OkHttpClient getOkHttpClient() {
		return sOkHttpClient;
	}

	public void release() {
		sOkHttpClient = null;
		sApiManager = null;
	}

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
