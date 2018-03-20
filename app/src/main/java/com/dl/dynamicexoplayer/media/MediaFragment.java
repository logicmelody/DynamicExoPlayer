package com.dl.dynamicexoplayer.media;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dl.dynamicexoplayer.R;

/**
 * Created by dannylin on 2018/3/20.
 */

public class MediaFragment extends Fragment {

	private Context mContext;


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
		initialize();
	}

	private void initialize() {

	}
}
