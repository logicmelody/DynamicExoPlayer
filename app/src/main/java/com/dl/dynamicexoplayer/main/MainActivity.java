package com.dl.dynamicexoplayer.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dl.dynamicexoplayer.R;
import com.dl.dynamicexoplayer.viewpager.SlideActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.button_main_normal)
	public void onClickMainButton() {

	}

	@OnClick(R.id.button_main_slide)
	public void onClickSlideButton() {
		startActivity(new Intent(this, SlideActivity.class));
	}
}
