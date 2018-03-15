package com.dl.dynamicexoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private Player mPlayer;
	private int mViedoIndex = 0;

	private String mUrls[] = {
			"https://www.videvo.net/?page_id=123&desc=river_and_rocks_wide.mp4&vid=2540",
			"https://www.videvo.net/?page_id=123&desc=RainierFlowers720p.mov&vid=4162",
			"https://www.videvo.net/?page_id=123&desc=rivervideo.mov&vid=5500",
			"https://www.videvo.net/?page_id=123&desc=Trees__Snow.mp4&vid=3067",
			"https://www.videvo.net/?page_id=123&desc=Stars_time_lapse.mp4&vid=3447",
			"https://www.videvo.net/?page_id=123&desc=Macro_Shot_of_Fish_in_Water_2_1280x720_iPhone_66SPlus.mp4&vid=5172 "
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_main_add_media:
				Toast.makeText(this, "Add media", Toast.LENGTH_SHORT).show();

				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
