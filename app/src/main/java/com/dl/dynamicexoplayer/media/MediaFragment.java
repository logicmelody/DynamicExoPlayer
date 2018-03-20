package com.dl.dynamicexoplayer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dl.dynamicexoplayer.R;
import com.dl.dynamicexoplayer.player.PlayerController;
import com.dl.dynamicexoplayer.utils.DetectVisibilityInViewPagerFragment;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dannylin on 2018/3/20.
 */

public class MediaFragment extends DetectVisibilityInViewPagerFragment {

	public static final String TAG = MediaFragment.class.getName();

	private static final String EXTRA_MEDIA_URL = "com.dl.dynamicexoplayer.EXTRA_MEDIA_URL";

	private Context mContext;

	private String mMediaUrl = "";

	@BindView(R.id.simple_exo_player_view)
	public SimpleExoPlayerView mSimpleExoPlayerView;


	public static MediaFragment newInstance(String url) {
		MediaFragment mediaFragment = new MediaFragment();

		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_MEDIA_URL, url);

		mediaFragment.setArguments(bundle);

		return mediaFragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_media, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ButterKnife.bind(this, getView());
		initialize();
	}

	private void initialize() {
		mMediaUrl = getArguments().getString(EXTRA_MEDIA_URL);

		setupPlayer();
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.d("danny", "MediaFragment onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d("danny", "MediaFragment onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();

		Log.d("danny", "MediaFragment onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.d("danny", "MediaFragment onStop()");
	}

	private void setupPlayer() {
		if (!TextUtils.isEmpty(mMediaUrl)) {
			PlayerController.getInstance(mContext).addMedia(mMediaUrl);
		}

		PlayerController.getInstance(mContext).addEventListener(new Player.DefaultEventListener() {

			@Override
			public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
				String stateString;

				switch (playbackState) {
					// The player has been instantiated but has not being prepared with a MediaSource yet..
					case Player.STATE_IDLE:
						stateString = "ExoPlayer.STATE_IDLE      -";
						break;

					// The player is not able to immediately play from the current position because not enough data is buffered.
					// ExoPlayer.EventListener.onPlaybackStateChanged() is called with STATE_BUFFERING.
					// Entering the buffering state happens naturally once at the very beginning and
					// after a user requested a seek to a position yet not available (eg. backward seeking).
					// All other occurrences of STATE_BUFFERING must be considered harmful for QoE.
					case Player.STATE_BUFFERING:
						stateString = "ExoPlayer.STATE_BUFFERING -";
						break;

					// The player is able to immediately play from the current position.
					// This means the player does actually play media when playWhenReady is true.
					// If it is false the player is paused.
					case Player.STATE_READY:
						stateString = "ExoPlayer.STATE_READY     -";
						break;

					// The player has finished playing the media.
					case Player.STATE_ENDED:
						stateString = "ExoPlayer.STATE_ENDED     -";
						break;

					default:
						stateString = "UNKNOWN_STATE             -";
						break;
				}

				Log.d("danny", "changed state to " + stateString + " playWhenReady: " + playWhenReady);

				if (playWhenReady && playbackState == Player.STATE_READY) {
					// actually playing media
					Log.d("danny", "Actually playing media");
				}
			}

			@Override
			public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

			}

			@Override
			public void onPlayerError(ExoPlaybackException error) {

			}
		});

		mSimpleExoPlayerView.setPlayer(PlayerController.getInstance(mContext).getExoPlayer());
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);

		Log.d("danny", "onFragmentVisibleChange() is " + isVisible);
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();

		Log.d("danny", "onFragmentFirstVisible()");
	}
}
