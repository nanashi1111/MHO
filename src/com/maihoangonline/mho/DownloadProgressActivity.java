package com.maihoangonline.mho;

import com.maihoangonline.adapter.DownloadPagerAdapter;
import com.maihoangonline.fragments.DownloadingFragment;
import com.maihoangonline.utils.DataUtils;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadProgressActivity extends BaseActivity implements TabListener, OnClickListener{
	
	private ViewPager pager;
	private ActionBar bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);DataUtils.isInDownloadScreen = true;
		setContentView(R.layout.activity_download_progress);
		setupActionBar();
		setupView();
	}
	
	private void setupActionBar(){
		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_other);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		TextView tvTitle = (TextView) bar.getCustomView().findViewById(
				R.id.title);
		tvTitle.setText("DOWNLOAD");
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		ImageView btBack = (ImageView) bar.getCustomView().findViewById(
				R.id.back);
		btBack.setOnClickListener(this);
		
	}
	
	private void setupView(){
		FragmentPagerAdapter adapter = new DownloadPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager)findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				bar.setSelectedNavigationItem(position);
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

		});
		
		bar.addTab(bar.newTab().setText("ĐANG TẢI").setTabListener(this));
		bar.addTab(bar.newTab().setText("ĐÃ TẢI").setTabListener(this));
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataUtils.isInDownloadScreen=false;
		DownloadingFragment.getInstance().unregisterReceiver();
	}

}
