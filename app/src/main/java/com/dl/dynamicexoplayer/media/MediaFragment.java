package com.dl.dynamicexoplayer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dl.dynamicexoplayer.R;
import com.dl.dynamicexoplayer.player.PlayerController;
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

public class MediaFragment extends Fragment {

	public static final String TAG = MediaFragment.class.getName();

	private Context mContext;

	@BindView(R.id.simple_exo_player_view)
	public SimpleExoPlayerView mSimpleExoPlayerView;


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
		setupPlayer();
	}

	private void setupPlayer() {
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

//		for (String url : mUrls) {
//			PlayerController.getInstance(this).addMedia(url);
//		}

		PlayerController.getInstance(mContext).addMedia("https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab07bce769aa52e5f2175f0.mpd");

		mSimpleExoPlayerView.setPlayer(PlayerController.getInstance(mContext).getExoPlayer());
	}

	@Override
	public void onDestroy() {
		PlayerController.getInstance(mContext).release();

		super.onDestroy();
	}
}
