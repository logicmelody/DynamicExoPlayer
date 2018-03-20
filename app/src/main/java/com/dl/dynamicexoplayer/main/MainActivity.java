package com.dl.dynamicexoplayer.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dl.dynamicexoplayer.R;
import com.dl.dynamicexoplayer.media.MediaFragment;
import com.dl.dynamicexoplayer.utils.FragmentUtils;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initialize();
	}

	private void initialize() {
		setupMediaContent();
	}

	private void setupMediaContent() {
		FragmentUtils.addFragmentTo(getSupportFragmentManager(), new MediaFragment(), MediaFragment.TAG, R.id.view_group_main_media_container);
	}
}
