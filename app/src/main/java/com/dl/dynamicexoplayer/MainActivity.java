package com.dl.dynamicexoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private Player mPlayer;

	private String mUrls[] = {
			"https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd",
			"https://s3.amazonaws.com/_bc_dml/example-content/sintel_dash/sintel_vod.mpd"
	};

	@BindView(R.id.simple_exo_player_view)
	public SimpleExoPlayerView mSimpleExoPlayerView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initialize();
	}

	private void initialize() {
		setupPlayer();
	}

	private void setupPlayer() {
		mPlayer = new Player(this, mSimpleExoPlayerView, new com.google.android.exoplayer2.Player.EventListener() {
			@Override
			public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

			}

			@Override
			public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
				Log.d("danny", "onTracksChanged");
			}

			@Override
			public void onLoadingChanged(boolean isLoading) {

			}

			@Override
			public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
				Log.d("danny", "onPlayerStateChanged");

				switch (playbackState) {
					case com.google.android.exoplayer2.Player.STATE_BUFFERING:
						Log.d("danny", "Player is in STATE_BUFFERING");

						break;

					case com.google.android.exoplayer2.Player.STATE_READY:
						Log.d("danny", "Player is in STATE_READY");

						break;
				}
			}

			@Override
			public void onRepeatModeChanged(int repeatMode) {

			}

			@Override
			public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

			}

			@Override
			public void onPlayerError(ExoPlaybackException error) {
				Log.d("danny", "onPlayerError");

				error.printStackTrace();
			}

			@Override
			public void onPositionDiscontinuity(int reason) {

			}

			@Override
			public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

			}

			@Override
			public void onSeekProcessed() {

			}
		});

		for (String url : mUrls) {
			mPlayer.addMedia(url);
		}
	}

	@Override
	protected void onDestroy() {
		mPlayer.release();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_main_previous_media:
				mPlayer.switchToPrevious();

				return true;

			case R.id.menu_item_main_next_media:
				mPlayer.switchToNext();

				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
