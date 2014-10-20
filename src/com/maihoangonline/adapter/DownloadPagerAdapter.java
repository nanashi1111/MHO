package com.maihoangonline.adapter;

import com.maihoangonline.fragments.DownloadedFragment;
import com.maihoangonline.fragments.DownloadingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DownloadPagerAdapter extends FragmentPagerAdapter {

	public DownloadPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return new DownloadingFragment();
		} else
			return DownloadedFragment.getInstance();
	}

	@Override
	public int getCount() {
		return 2;
	}

}
