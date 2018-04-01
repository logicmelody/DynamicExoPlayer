package com.dl.dynamicexoplayer.okhttp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

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

		// 目前OkHttp的版本(3.10.0)與ExoPlayer做整合的時候，有時候會發生SocketTimeout的問題，
        // 原因是因為現在OkHttp的版本，當connection pool中的連線如果掛掉的時候，
        // 他不會將已經死掉的connection重啟，所以會導致SocketTimeout，目前就等OkHttp釋出新的版本來修掉這個bug
        // https://github.com/square/okhttp/issues/3146
        builder.connectionPool(new ConnectionPool(0, 1, TimeUnit.NANOSECONDS));

//		builder.cache(buildCache(context));

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

	public String run(String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = sOkHttpClient.newCall(request).execute();

		return response.body().string();
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
