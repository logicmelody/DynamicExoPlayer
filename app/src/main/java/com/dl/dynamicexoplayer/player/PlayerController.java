package com.dl.dynamicexoplayer.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dl.dynamicexoplayer.okhttp.ApiManager;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.Player.EventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dannylin on 2018/3/15.
 */

public class PlayerController {

	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
	private static final String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6IjU5MTVhOWYzIn0.eyJzdWIiOiI1N2Q5M2E5ZjgyNTk1YjM0YjZkNDQyNDUiLCJpc3MiOiJhcGkudjIuc3dhZy5saXZlIiwiYXVkIjoiYXBpLnYyLnN3YWcubGl2ZSIsImlhdCI6MTUyMTUxNDg3MywiZXhwIjoxNTIyNzI0NDczLCJqdGkiOiJXckI1ZVZLUEVpMm9wTnZlIiwic2NvcGVzIjpbImN1cmF0b3IiXX0.q5KbZS9zPh6pFk6w-_uHcYPmfXuVhd66UAnygR14RYI";

	private volatile static PlayerController sPlayerController = null;

	// <Fragment position, DynamicConcatenatingMediaSource index>
	private Map<Integer, Integer> mFragmentMediaSourcePositionMap;

	private DynamicConcatenatingMediaSource mDynamicConcatenatingMediaSource;
	private SimpleExoPlayer mPlayer;

	private EventListener mEventListener;

	private int mCurrentMediaPosition = 0;


	public static PlayerController getInstance(Context context) {
		if (sPlayerController == null) {
			synchronized (PlayerController.class) {
				if (sPlayerController == null) {
					sPlayerController = new PlayerController(context);
				}
			}
		}

		return sPlayerController;
	}

	private PlayerController(Context context) {
		mDynamicConcatenatingMediaSource = new DynamicConcatenatingMediaSource();
		mFragmentMediaSourcePositionMap = new HashMap<>();

		setupPlayer(context);
	}

	private void setupPlayer(Context context) {
		mPlayer = ExoPlayerFactory.newSimpleInstance(
				new DefaultRenderersFactory(context),
				new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(BANDWIDTH_METER)),
				new DefaultLoadControl());

		mPlayer.setPlayWhenReady(true);
		mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
	}

	public void release() {
		removeEventListener();

		mPlayer.release();
		sPlayerController = null;
	}

	/**
	 * Add media urls dynamically at run time
	 * Here we have added urls that are compatible with ExtractorMediaSource
	 * If you need other media source then pass media source as argument not the url
	 *
	 * @param url
	 */
	public synchronized void addMedia(Context context, int fragmentPosition, String url) {
		if (mFragmentMediaSourcePositionMap.containsKey(fragmentPosition)) {
			return;
		}

		MediaSource dashMediaSource = buildDashMediaSource(context, url);

		if (mDynamicConcatenatingMediaSource.getSize() == 0) {
			mDynamicConcatenatingMediaSource.addMediaSource(dashMediaSource);
			mPlayer.prepare(mDynamicConcatenatingMediaSource, true, true);

		} else {
			mDynamicConcatenatingMediaSource.addMediaSource(dashMediaSource);
		}

		mFragmentMediaSourcePositionMap.put(fragmentPosition, mDynamicConcatenatingMediaSource.getSize() - 1);
	}

	private MediaSource buildDashMediaSource(Context context, String uri) {
		// DataSource 是專門用來 load 資料的
		DataSource.Factory dataSourceFactory = getDataSourceFactoryWithJwt(context);

		return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(dataSourceFactory), dataSourceFactory).createMediaSource(Uri.parse(uri));
	}

	private DataSource.Factory getDataSourceFactoryWithJwt(Context context) {
		DataSource.Factory defaultDataSourceFactory =
				new DefaultDataSourceFactory(context, null, new OkHttpDataSourceFactory(ApiManager.getInstance(context).getOkHttpClient(), "user-agent", null));

//		HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSourceFactory("user-agent", BANDWIDTH_METER);

//				new OkHttpDataSourceFactory(
//						ApiManager.getHttpClientInstance(context),
//				new DefaultHttpDataSourceFactory(
//						"user-agent",
//						BANDWIDTH_METER);
//		httpDataSourceFactory.getDefaultRequestProperties().set("Authorization", "Bearer " + JWT);
//		httpDataSourceFactory.getDefaultRequestProperties().set("User-Agent", "swag/2.15.1 (Android; com.machipopo.swag; htc; HTC_U-3u; en-US)");

		return defaultDataSourceFactory;
	}

	public void switchToMedia(int fragmentPosition) {
		if (!mFragmentMediaSourcePositionMap.containsKey(fragmentPosition)) {
			return;
		}

		mPlayer.seekToDefaultPosition(mFragmentMediaSourcePositionMap.get(fragmentPosition));

		Log.d("danny", "PlayerController switchToMedia, fragment position = " + fragmentPosition + ", windowIndex = " + mFragmentMediaSourcePositionMap.get(fragmentPosition));
	}

	public void switchToPrevious() {
		if (!isPositionValid(mCurrentMediaPosition - 1)) {
			Log.d("danny", "Position is not valid");

			return;
		}

		mCurrentMediaPosition--;
		mPlayer.seekToDefaultPosition(mCurrentMediaPosition);

		Log.d("danny", "switchToPrevious position " + mCurrentMediaPosition);
	}

	public void switchToNext() {
		if (!isPositionValid(mCurrentMediaPosition + 1)) {
			Log.d("danny", "Position is not valid");

			return;
		}

		mCurrentMediaPosition++;
		mPlayer.seekToDefaultPosition(mCurrentMediaPosition);

		Log.d("danny", "switchToNext position " + mCurrentMediaPosition);
	}

	private boolean isPositionValid(int position) {
		return position >= 0 && position < mDynamicConcatenatingMediaSource.getSize();
	}

	public SimpleExoPlayer getExoPlayer() {
		return mPlayer;
	}

	public void addEventListener(EventListener eventListener) {
		removeEventListener();

		mEventListener = eventListener;
		mPlayer.addListener(mEventListener);
	}

	public void removeEventListener() {
		if (mEventListener != null) {
			mPlayer.removeListener(mEventListener);
			mEventListener = null;
		}
	}
}
