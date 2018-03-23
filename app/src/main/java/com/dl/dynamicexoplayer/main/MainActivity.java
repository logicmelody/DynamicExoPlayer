package com.dl.dynamicexoplayer.main;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dl.dynamicexoplayer.R;
import com.dl.dynamicexoplayer.okhttp.ApiManager;
import com.dl.dynamicexoplayer.player.PlayerController;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

	private MediaPagerAdapter mMediaPagerAdapter;

	private Timer mTimer;

	private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	@BindView(R.id.view_pager_main_media)
	public ViewPager mViewPagerMedia;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initialize();
		startLoopingHttpRequest();
	}

	private void initialize() {
		mTimer = new Timer();

		setupMediaViewPager();
	}

	private void setupMediaViewPager() {
		mMediaPagerAdapter = new MediaPagerAdapter(getSupportFragmentManager());

		for (String url : mUrls) {
			mMediaPagerAdapter.add(url);
		}

		mViewPagerMedia.setAdapter(mMediaPagerAdapter);
		mViewPagerMedia.addOnPageChangeListener(mOnPageChangeListener);
	}

	private void startLoopingHttpRequest() {
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Observable.create(new ObservableOnSubscribe<String>() {
					@Override
					public void subscribe(ObservableEmitter<String> emitter) throws Exception {
						emitter.onNext(ApiManager.getInstance(MainActivity.this).run("https://raw.github.com/square/okhttp/master/README.md"));
					}

				})
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<String>() {
							@Override
							public void onSubscribe(Disposable d) {

							}

							@Override
							public void onNext(String s) {
								Toast.makeText(MainActivity.this, "Http request successful", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onComplete() {

							}
						});
			}

		}, 0, 5000);
	}

	@Override
	protected void onDestroy() {
		mViewPagerMedia.removeOnPageChangeListener(mOnPageChangeListener);
		mTimer.cancel();

		ApiManager.getInstance(this).release();
		PlayerController.getInstance(this).release();

		super.onDestroy();
	}
}
