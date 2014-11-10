package com.maihoangonline.adapter;

import com.maihoangonline.fragments.HistoryPayCardFragment;
import com.maihoangonline.fragments.HistoryPayGameFragment;
import com.maihoangonline.fragments.HistoryTransationFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HistoryPagerAdapter extends FragmentPagerAdapter {

	public HistoryPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return HistoryPayCardFragment.getInstance();
		} else if (position == 1) {
			return HistoryPayGameFragment.getInstance();
		}
		return HistoryTransationFragment.getInstance();
	}

	@Override
	public int getCount() {
		return 3;
	}

}
