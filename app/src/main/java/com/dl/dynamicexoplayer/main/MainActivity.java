package com.dl.dynamicexoplayer.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dl.dynamicexoplayer.player.PlayerController;
import com.dl.dynamicexoplayer.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private String mUrls[] = {
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab07bce769aa52e5f2175f0.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab07b4a74913a33de6748ef.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab079e5cb499330f301220e.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab077e9b85c9212288df431.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab075cdb35051155743442b.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab06ff220cc2911bb40dd65.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab0692020cc295f9a40dd66.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab067deb2724c3651bb55bc.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab0667920cc294dcb40dd64.mpd",
			"https://storage.googleapis.com/asia.public.swag.live/DJSMPGZV4BlnLTOw7a8a6NgFLxXYkdiC/5ab0649e20cc29397040dd6b.mpd"
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
		PlayerController.getInstance(this).addEventListener(new Player.DefaultEventListener() {

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

		for (String url : mUrls) {
			PlayerController.getInstance(this).addMedia(url);
		}

		mSimpleExoPlayerView.setPlayer(PlayerController.getInstance(this).getExoPlayer());
	}

	@Override
	protected void onDestroy() {
		PlayerController.getInstance(this).release();

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
				PlayerController.getInstance(this).switchToPrevious();

				return true;

			case R.id.menu_item_main_next_media:
				PlayerController.getInstance(this).switchToNext();

				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
