package com.maihoangonline.mho;

import org.json.JSONObject;

import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GetGiftActivity extends BaseActivity implements OnClickListener {

	private ActionBar bar;
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_gift);
		setupActionBar();
		loadData();
		new GetGameGift().execute();
	}

	private void loadData() {
		game = (Game) getIntent().getSerializableExtra("game");
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
		tvTitle.setText("QUÀ TẶNG");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		}
	}

	private class GetGameGift extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		JSONObject jsonGift;

		@Override
		protected Void doInBackground(Void... params) {
			jsonGift = ServiceConnection.getGift(
					SharePreferenceUtils.getAccEmail(GetGiftActivity.this),
					game);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			DisplayUtils.log("GIFT:"+jsonGift.toString());
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(GetGiftActivity.this, "",
					"Đang tải danh sách quà tặng");
		}

	}

}
