package com.maihoangonline.mho;

import com.maihoangonline.utils.SharePreferenceUtils;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends BaseActivity implements OnClickListener {

	private TextView tvUserName, tvMain, tvPromotion1, tvPromotion2;
	private ActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setupActionBar();
		setupView();
		loadData();
	}

	private void setupView() {
		tvUserName = (TextView) findViewById(R.id.user_name);
		tvMain = (TextView) findViewById(R.id.main);
		tvPromotion1 = (TextView) findViewById(R.id.promotion1);
		tvPromotion2 = (TextView) findViewById(R.id.promotion2);
		findViewById(R.id.quit).setOnClickListener(this);
	}
	
	private void setupActionBar() {

		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_other);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		ImageView btBack = (ImageView) bar.getCustomView().findViewById(
				R.id.back);
		btBack.setOnClickListener(this);
		TextView tvTitle = (TextView) bar.getCustomView().findViewById(
				R.id.title);
		tvTitle.setText("Thông tin tài khoản");

	}

	private void loadData() {
		tvUserName.setText(SharePreferenceUtils.getAccEmail(this));
		tvMain.setText("TK Chính:" + SharePreferenceUtils.getGold(this, 1)
				+ " gold");
		tvPromotion1.setText("TK KM1:" + SharePreferenceUtils.getGold(this, 2)
				+ " gold");
		tvPromotion2.setText("TK KM2:" + SharePreferenceUtils.getGold(this, 3)
				+ " gold");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit:
			SharePreferenceUtils.putLogInfo(UserInfoActivity.this, false);
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		}
	}

}
