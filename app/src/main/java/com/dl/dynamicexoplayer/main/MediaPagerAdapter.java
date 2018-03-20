package com.dl.dynamicexoplayer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.dl.dynamicexoplayer.media.MediaFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dannylin on 2018/3/20.
 */

public class MediaPagerAdapter extends FragmentStatePagerAdapter {

	private List<String> mMediaUrls = new ArrayList<>();


	public MediaPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return MediaFragment.newInstance(mMediaUrls.get(position));
	}

	@Override
	public int getCount() {
		return mMediaUrls.size();
	}

	public void add(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}

		mMediaUrls.add(url);
	}
}
