package com.maihoangonline.adapter;

import com.maihoangonline.fragments.GameHotFragment;
import com.maihoangonline.fragments.GameReviewFragment;
import com.maihoangonline.fragments.NewsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

	public HomePagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return GameHotFragment.getInstance();
		} else if (position == 1) {
			return GameReviewFragment.getInstance();
		} else
			return NewsFragment.getInstance();
	}

	@Override
	public int getCount() {
		return 3;
	}
	
}
