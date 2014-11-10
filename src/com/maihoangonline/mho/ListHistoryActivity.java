package com.maihoangonline.mho;

import com.maihoangonline.adapter.HistoryPagerAdapter;
import com.maihoangonline.fragments.HistoryPayGameFragment;
import com.maihoangonline.fragments.HistoryTransationFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListHistoryActivity extends BaseActivity implements TabListener{

	private ActionBar bar;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_transaction);
		setupActionBar();
		setupView();
	}

	

	private void setupView() {
		FragmentPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager)findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				bar.setSelectedNavigationItem(position);
				if (position == 1) {
					HistoryPayGameFragment.getInstance().loadFirstTime();
				} else if (position == 2) {
					HistoryTransationFragment.getInstance().loadFirstTime();
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

		});
		bar.addTab(bar.newTab().setText("NẠP TIỀN").setTabListener(this));
		bar.addTab(bar.newTab().setText("CHUYỂN TIỀN VÀO GAME").setTabListener(this));
		bar.addTab(bar.newTab().setText("CHUYỂN TIỀN CÁ NHÂN").setTabListener(this));
	}



	private void setupActionBar() {

		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_other);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		ImageView btBack = (ImageView) bar.getCustomView().findViewById(
				R.id.back);
		btBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(0, 0);
			}
		});
		TextView tvTitle = (TextView) bar.getCustomView().findViewById(
				R.id.title);
		tvTitle.setText("Lịch sử giao dịch");
		

	}



	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());		
	}



	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
