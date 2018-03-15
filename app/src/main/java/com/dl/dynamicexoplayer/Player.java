package com.dl.dynamicexoplayer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.Player.EventListener;

/**
 * Created by dannylin on 2018/3/15.
 */

public class Player {

	private Context mContext;
	private DynamicConcatenatingMediaSource mDynamicConcatenatingMediaSource;
	private SimpleExoPlayerView mPlayerView;
	private SimpleExoPlayer mPlayer;

	private EventListener mEventListener;

	private int mCurrentMediaPosition = 0;


	public Player(Context context, SimpleExoPlayerView playerView, EventListener eventListener) {
		this.mContext = context;
		this.mDynamicConcatenatingMediaSource = new DynamicConcatenatingMediaSource();
		this.mPlayerView = playerView;
		mEventListener = eventListener;

		setupPlayer();
	}

	private void setupPlayer() {
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
		TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

		mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
		mPlayer.setPlayWhenReady(true);
		mPlayer.setRepeatMode(com.google.android.exoplayer2.Player.REPEAT_MODE_ONE);
		mPlayer.addListener(mEventListener);

		mPlayerView.setPlayer(mPlayer);
	}

	/**
	 * Add media urls dynamically at run time
	 * Here we have added urls that are compatible with ExtractorMediaSource
	 * If you need other media source then pass media source as argument not the url
	 *
	 * @param url
	 */
	public void addMedia(String url) {
		DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
		DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NepTechSansaar"), bandwidthMeterA);

		ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
		MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
				dataSourceFactory, extractorsFactory, null, null);

		if (mDynamicConcatenatingMediaSource.getSize() == 0) {
			mDynamicConcatenatingMediaSource.addMediaSource(mediaSource);
			mPlayer.prepare(mDynamicConcatenatingMediaSource);

		} else {
			mDynamicConcatenatingMediaSource.addMediaSource(mediaSource);
		}
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
}
