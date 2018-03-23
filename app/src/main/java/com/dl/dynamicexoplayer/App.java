package com.dl.dynamicexoplayer;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by dannylin on 2018/3/23.
 */

public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}
}
