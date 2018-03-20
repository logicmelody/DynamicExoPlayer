package com.dl.dynamicexoplayer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.Player.EventListener;

/**
 * Created by dannylin on 2018/3/15.
 */

public class MediaPlayer {

	private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

	private Context mContext;

	private DynamicConcatenatingMediaSource mDynamicConcatenatingMediaSource;
	private SimpleExoPlayer mPlayer;

	private EventListener mEventListener;

	private int mCurrentMediaPosition = 0;


	public MediaPlayer(Context context, EventListener eventListener) {
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
		DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(mContext.getString(R.string.app_name));
		DashChunkSource.Factory dashChunkSourceFactory =
				new DefaultDashChunkSource.Factory(new DefaultHttpDataSourceFactory(mContext.getString(R.string.app_name), BANDWIDTH_METER));

		return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(Uri.parse(uri));
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
