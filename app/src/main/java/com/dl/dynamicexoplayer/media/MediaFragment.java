package com.dl.dynamicexoplayer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

	private static final String EXTRA_POSITION = "com.dl.dynamicexoplayer.EXTRA_POSITION";
	private static final String EXTRA_MEDIA_URL = "com.dl.dynamicexoplayer.EXTRA_MEDIA_URL";

	private Context mContext;

	private int mPosition = 0;
	private String mMediaUrl = "";

	private boolean shouldBindWithPlayer = false;

	private Player.EventListener mPlayerEventListener = new Player.DefaultEventListener() {

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

			Log.d("danny", "MediaFragment" + mPosition + " changed state to " + stateString + " playWhenReady: " + playWhenReady);

			if (playWhenReady && playbackState == Player.STATE_READY) {
				// actually playing media
				Log.d("danny", "MediaFragment" + mPosition + ", Actually playing media");
			}
		}

		@Override
		public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
			Log.d("danny", "MediaFragment" + mPosition + ", onTracksChanged()");

			setDebugText();
		}

		@Override
		public void onPlayerError(ExoPlaybackException error) {

		}
	};

	@BindView(R.id.simple_exo_player_view_media)
	public SimpleExoPlayerView mSimpleExoPlayerView;

	@BindView(R.id.text_view_media_debug)
	public TextView mTextViewDebug;


	public static MediaFragment newInstance(int position, String url) {
		MediaFragment mediaFragment = new MediaFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_POSITION, position);
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
		getIntentData();
		initialize();

		Log.d("danny", "MediaFragment" + mPosition + ", onActivityCreated()");
	}

	private void getIntentData() {
		Bundle bundle = getArguments();

		if (bundle == null) {
			return;
		}

		mPosition = bundle.getInt(EXTRA_POSITION);
		mMediaUrl = bundle.getString(EXTRA_MEDIA_URL);
	}

	private void initialize() {
		addMediaToPlayerController();

		if (shouldBindWithPlayer) {
			bindWithPlayer();
		}
	}

	private void addMediaToPlayerController() {
		if (!TextUtils.isEmpty(mMediaUrl)) {
			PlayerController.getInstance(mContext).addMedia(mMediaUrl);

			Log.d("danny", "MediaFragment" + mPosition + ", Add media = " + mMediaUrl);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.d("danny", "MediaFragment" + mPosition + ", onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d("danny", "MediaFragment" + mPosition + ", onResume()");
	}

	@Override
	public void onPause() {
		super.onPause();

		Log.d("danny", "MediaFragment" + mPosition + ", onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.d("danny", "MediaFragment" + mPosition + ", onStop()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.d("danny", "MediaFragment" + mPosition + ", onDestroy()");
	}

	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		super.onFragmentVisibleChange(isVisible);

		if (mSimpleExoPlayerView == null) {
			Log.d("danny", "MediaFragment" + mPosition + ", mSimpleExoPlayerView is null");
			shouldBindWithPlayer = true;

			return;
		}

		if (isVisible) {
			bindWithPlayer();

		} else {
			unbindWithPlayer();
		}
	}

	private void bindWithPlayer() {
		PlayerController.getInstance(mContext).addEventListener(mPlayerEventListener);
		mSimpleExoPlayerView.setPlayer(PlayerController.getInstance(mContext).getExoPlayer());
		shouldBindWithPlayer = false;

		Log.d("danny", "MediaFragment" + mPosition + ", bind with player");
	}

	private void unbindWithPlayer() {
		PlayerController.getInstance(mContext).removeEventListener();
		mSimpleExoPlayerView.setPlayer(null);

		Log.d("danny", "MediaFragment" + mPosition + ", unbind with player");
	}

	@Override
	protected void onFragmentFirstVisible() {
		super.onFragmentFirstVisible();

		Log.d("danny", "MediaFragment" + mPosition + ", onFragmentFirstVisible()");
	}

	private void setDebugText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Position = " + mPosition);

		mTextViewDebug.setText(stringBuilder.toString());
	}
}
