package com.dl.dynamicexoplayer.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dl.dynamicexoplayer.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.upstream.HttpDataSource;

/**
 * Created by dannylin on 2018/3/15.
 */

public class PlayerController {

	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
	private static final String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6IjU5MTVhOWYzIn0.eyJzdWIiOiI1N2Q5M2E5ZjgyNTk1YjM0YjZkNDQyNDUiLCJpc3MiOiJhcGkudjIuc3dhZy5saXZlIiwiYXVkIjoiYXBpLnYyLnN3YWcubGl2ZSIsImlhdCI6MTUyMTUxNDg3MywiZXhwIjoxNTIyNzI0NDczLCJqdGkiOiJXckI1ZVZLUEVpMm9wTnZlIiwic2NvcGVzIjpbImN1cmF0b3IiXX0.q5KbZS9zPh6pFk6w-_uHcYPmfXuVhd66UAnygR14RYI";

	private Context mContext;

	private DynamicConcatenatingMediaSource mDynamicConcatenatingMediaSource;
	private SimpleExoPlayer mPlayer;

	private EventListener mEventListener;

	private int mCurrentMediaPosition = 0;


	public PlayerController(Context context, EventListener eventListener) {
		mContext = context;
		mDynamicConcatenatingMediaSource = new DynamicConcatenatingMediaSource();
		mEventListener = eventListener;

		setupPlayer();
	}

	private void setupPlayer() {
		mPlayer = ExoPlayerFactory.newSimpleInstance(
				new DefaultRenderersFactory(mContext),
				new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(BANDWIDTH_METER)),
				new DefaultLoadControl());

		mPlayer.setPlayWhenReady(true);
		mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
		mPlayer.addListener(mEventListener);
	}

	public void release() {
        mPlayer.removeListener(mEventListener);
		mPlayer.release();
	}

	/**
	 * Add media urls dynamically at run time
	 * Here we have added urls that are compatible with ExtractorMediaSource
	 * If you need other media source then pass media source as argument not the url
	 *
	 * @param url
	 */
	public void addMedia(String url) {
		MediaSource dashMediaSource = buildDashMediaSource(url);

		if (mDynamicConcatenatingMediaSource.getSize() == 0) {
			mDynamicConcatenatingMediaSource.addMediaSource(dashMediaSource);
			mPlayer.prepare(mDynamicConcatenatingMediaSource);

		} else {
			mDynamicConcatenatingMediaSource.addMediaSource(dashMediaSource);
		}
	}

	private MediaSource buildDashMediaSource(String uri) {
		// DataSource 是專門用來 load 資料的
		HttpDataSource.Factory httpDataSourceFactory = getHttpDataSourceFactoryWithJwt();

		return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(httpDataSourceFactory), httpDataSourceFactory).createMediaSource(Uri.parse(uri));
	}

	private HttpDataSource.Factory getHttpDataSourceFactoryWithJwt() {
		HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSourceFactory(mContext.getString(R.string.app_name), BANDWIDTH_METER);
//		httpDataSourceFactory.getDefaultRequestProperties().set("Authorization", "Bearer " + JWT);
//		httpDataSourceFactory.getDefaultRequestProperties().set("User-Agent", "swag/2.15.1 (Android; com.machipopo.swag; htc; HTC_U-3u; en-US)");

		return httpDataSourceFactory;
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
}
