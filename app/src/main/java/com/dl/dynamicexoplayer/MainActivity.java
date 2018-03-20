package com.dl.dynamicexoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private MediaPlayer mPlayer;

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
		mPlayer = new MediaPlayer(this, new Player.DefaultEventListener() {

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
			mPlayer.addMedia(url);
		}

		mSimpleExoPlayerView.setPlayer(mPlayer.getExoPlayer());
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
