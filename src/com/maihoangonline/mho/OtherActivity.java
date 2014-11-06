package com.maihoangonline.mho;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.maihoangonline.adapter.ListOptionAdapter;
import com.maihoangonline.fragments.CategoryFragment;
import com.maihoangonline.fragments.OtherFragment;
import com.maihoangonline.fragments.RankFragment;
import com.maihoangonline.fragments.TopicFragment;
import com.maihoangonline.models.Option;
import com.maihoangonline.utils.AnimationUtils;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnCloseListener;

public class OtherActivity extends BaseActivity implements OnClickListener {

	private SlidingMenu menu;
	private boolean isMenuShowing = false;
	private boolean attached = false;
	private ProgressDialog dialog;
	private ActionBar bar;

	private Button btGeneral, btCategory, btRank, btOther, btTopic;
	private int tab;
	private LinearLayout footer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other);
		tab = getIntent().getIntExtra("tab", -1);
		menu = new SlidingMenu(this);
		if (!SharePreferenceUtils.isLogged(this)) { // If not logged in
			setupSlidingMenu();
		} else {
			setupSlidingMenuLoggedIn();
		}
		setupActionBar();
		setupView();
		if (tab == 5) {
			switchContent(R.id.content, CategoryFragment.getInstance());
		} else if (tab == 4) {
			switchContent(R.id.content, RankFragment.getInstance());
		} else if (tab == 3) {
			switchContent(R.id.content, OtherFragment.getInstance());
		} else if (tab == 2) {
			switchContent(R.id.content, TopicFragment.getInstance());
		}
		DataUtils.activityList.add(this);
		getIpWan();
	}

	private void setupView() {
		btGeneral = (Button) findViewById(R.id.general);
		btGeneral.setBackgroundResource(R.drawable.btn_tong_hop);
		btGeneral.setOnClickListener(this);
		btCategory = (Button) findViewById(R.id.category);
		btCategory.setOnClickListener(this);
		if (tab == 5) {
			btCategory.setBackgroundResource(R.drawable.btn_the_loai_focus);
		}
		btRank = (Button) findViewById(R.id.rank);
		btRank.setOnClickListener(this);
		if (tab == 4) {
			btRank.setBackgroundResource(R.drawable.btn_xep_hang_focus);
		}
		btOther = (Button) findViewById(R.id.other);
		btOther.setOnClickListener(this);
		if (tab == 3) {
			btOther.setBackgroundResource(R.drawable.btn_khac_focus);
		}
		btTopic = (Button) findViewById(R.id.topic);
		btTopic.setOnClickListener(this);
		if (tab == 2) {
			btTopic.setBackgroundResource(R.drawable.btn_chuyen_de_focus);
		}
		
		footer = (LinearLayout)findViewById(R.id.footer);
	}

	private void setupActionBar() {
		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_home);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		final ImageView btSearch = (ImageView) bar.getCustomView()
				.findViewById(R.id.search);
		final ImageView btProgressDownload = (ImageView) bar.getCustomView()
				.findViewById(R.id.download);
		ImageView btToggleSlidingMenu = (ImageView) bar.getCustomView()
				.findViewById(R.id.toggle);
		View.OnClickListener clickListenr = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.toggle:
					if (isMenuShowing) {
						isMenuShowing = false;
					} else {
						isMenuShowing = true;
					}
					menu.toggle(true);
					break;
				case R.id.search:
					btSearch.setVisibility(View.INVISIBLE);
					SearchView sv = (SearchView) bar.getCustomView()
							.findViewById(R.id.search_bar);
					sv.setVisibility(View.VISIBLE);
					sv.setIconified(false);
					/*
					 * TextView tv = (TextView)
					 * bar.getCustomView().findViewById( R.id.home_text);
					 * tv.setVisibility(View.GONE);
					 */
					FrameLayout frameTitle = (FrameLayout) bar.getCustomView()
							.findViewById(R.id.frame_title);
					frameTitle.setVisibility(View.GONE);
					break;
				case R.id.download:
					startActivity(new Intent(OtherActivity.this,DownloadProgressActivity.class));
					overridePendingTransition(0, 0);
					break;
				}
			}
		};
		btSearch.setOnClickListener(clickListenr);
		btProgressDownload.setOnClickListener(clickListenr);
		btToggleSlidingMenu.setOnClickListener(clickListenr);
		final SearchView sv = (SearchView) bar.getCustomView().findViewById(
				R.id.search_bar);

		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				Intent intent = new Intent(OtherActivity.this,
						SearchActivity.class);
				intent.putExtra("querry", query);
				startActivity(intent);
				overridePendingTransition(0, 0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
		sv.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				sv.setVisibility(View.GONE);
				/*
				 * ((TextView) bar.getCustomView().findViewById(R.id.home_text))
				 * .setVisibility(View.VISIBLE);
				 */
				((FrameLayout) bar.getCustomView().findViewById(
						R.id.frame_title)).setVisibility(View.VISIBLE);
				btSearch.setVisibility(View.VISIBLE);
				return false;
			}
		});
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
	}

	private void setupSlidingMenu() {
		// *********** Setup sliding menu ***************//

		menu.setMode(SlidingMenu.LEFT);
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_);
		menu.setFadeDegree(0.35f);
		if (!attached) {
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			attached = true;
		}
		menu.setMenu(R.layout.sliding_menu_guess);
		menu.setShadowWidth(50);
		menu.setBehindOffset(220);

		// *********** Setup button login and register ***************//

		View.OnClickListener l = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.login) {
					menu.toggle();
					isMenuShowing = false;
					showDialogLogin();
				} else if (v.getId() == R.id.register) {
					menu.toggle();
					isMenuShowing = false;
					showDialogSignUp();
				}
			}
		};
		Button btLoign = (Button) menu.findViewById(R.id.login);

		if (btLoign != null) {
			btLoign.setOnClickListener(l);
		}
		Button btSignUp = (Button) menu.findViewById(R.id.register);
		if (btSignUp != null) {
			btSignUp.setOnClickListener(l);
		}

		// *********** Setup list option ***************//

		ListView lvOption = (ListView) menu.findViewById(R.id.list_option);
		ArrayList<Option> listOption = new ArrayList<Option>();
		listOption.add(new Option(R.drawable.icon_info, "THÔNG TIN TÀI KHOẢN"));
		listOption.add(new Option(R.drawable.icon_home, "HOME"));
		listOption.add(new Option(R.drawable.icon_bo_suu_tap, "BỘ SƯU TẬP"));
		listOption.add(new Option(R.drawable.icon_favorite, "YÊU THÍCH"));
		listOption.add(new Option(R.drawable.icon_su_kien, "SỰ KIỆN"));
		listOption.add(new Option(R.drawable.icon_language, "LANGUAGE"));
		listOption.add(new Option(R.drawable.icon_setting, "TÙY CHỌN"));
		ListOptionAdapter adapter = new ListOptionAdapter(this, 1, listOption);
		lvOption.setAdapter(adapter);
		lvOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0 || arg2 == 2 || arg2 == 3) {
					makeToast("Bạn phải đăng nhập để thực hiện chức năng này");
				}
				if (arg2 == 1) {
					menu.toggle();
					isMenuShowing = false;
				}
			}
		});
	}

	private void setupSlidingMenuLoggedIn() {
		// *********** Setup sliding menu ***************//

		menu.setMode(SlidingMenu.LEFT);

		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_);
		menu.setFadeDegree(0.35f);
		if (!attached) {
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			attached = true;
		}
		menu.setMenu(R.layout.sliding_menu);
		// menu.setLayoutParams(new
		// FrameLayout.LayoutParams(getScreenSize().widthPixels/2,
		// getScreenSize().heightPixels));
		menu.setShadowWidth(50);
		menu.setBehindOffset(220);

		TextView tvUserName = (TextView) menu.findViewById(R.id.user_name);
		tvUserName.setText(SharePreferenceUtils.getAccEmail(this));

		// *********** Setup button quit ***************//
		Button btQuit = (Button) menu.findViewById(R.id.quit);
		btQuit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharePreferenceUtils.putLogInfo(OtherActivity.this, false);
				// menu.removeAllViews();
				// setUpSlidingMenu();
				setupSlidingMenu();
			}
		});

		// *********** Setup list option ***************//

		ListView lvOption = (ListView) menu.findViewById(R.id.list_option);
		ArrayList<Option> listOption = new ArrayList<Option>();
		listOption.add(new Option(R.drawable.icon_info, "THÔNG TIN TÀI KHOẢN"));
		listOption.add(new Option(R.drawable.icon_home, "HOME"));
		listOption.add(new Option(R.drawable.icon_bo_suu_tap, "BỘ SƯU TẬP"));
		listOption.add(new Option(R.drawable.icon_favorite, "YÊU THÍCH"));
		listOption.add(new Option(R.drawable.icon_su_kien, "SỰ KIỆN"));
		listOption.add(new Option(R.drawable.icon_language, "LANGUAGE"));
		listOption.add(new Option(R.drawable.icon_setting, "TÙY CHỌN"));
		ListOptionAdapter adapter = new ListOptionAdapter(this, 1, listOption);
		lvOption.setAdapter(adapter);
		lvOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent;
				switch (arg2) {
				case 0:
					intent = new Intent(OtherActivity.this,
							UserInfoActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
					break;
				case 1:
					menu.toggle();
					isMenuShowing = false;
					break;
				case 2:
					startActivity(new Intent(OtherActivity.this,
							ListThreadActivity.class));
					overridePendingTransition(0, 0);
					break;
				case 3:
					intent = new Intent(OtherActivity.this,
							ListGameActivity.class);
					intent.putExtra("favorite", true);
					startActivity(intent);
					overridePendingTransition(0, 0);
					break;
				}
			}
		});
	}

	private void showDialogLogin() {
		final Dialog dialogLogin = new Dialog(this);
		dialogLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogLogin.setContentView(R.layout.dialog_login);
		final EditText etUserName = (EditText) dialogLogin
				.findViewById(R.id.user_name);
		etUserName.setText("dtv1111@gmail.com");
		final EditText etPassword = (EditText) dialogLogin
				.findViewById(R.id.password);
		etPassword.setText("qqqqqq");
		Button btLogin = (Button) dialogLogin.findViewById(R.id.login);
		btLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String userName = etUserName.getText().toString();
				if (userName.isEmpty()) {
					etUserName.setError("Bạn hãy nhập tên đăng nhập...");
					return;
				}
				String password = etPassword.getText().toString();
				if (password.isEmpty()) {
					etPassword.setError("Bạn chưa nhập mật khẩu...");
					return;
				}
				new LoginTask().execute(userName, password);
				dialogLogin.dismiss();
			}
		});
		dialogLogin.setCancelable(true);
		dialogLogin.show();
	}

	private void showDialogSignUp() {
		final Dialog dialogSignUp = new Dialog(this);
		dialogSignUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSignUp.setContentView(R.layout.dialog_sign_up);
		final EditText etEmail = (EditText) dialogSignUp
				.findViewById(R.id.user_name);
		final EditText etPassword = (EditText) dialogSignUp
				.findViewById(R.id.password);
		final EditText etRePassword = (EditText) dialogSignUp
				.findViewById(R.id.repassword);
		final EditText etPhone = (EditText) dialogSignUp
				.findViewById(R.id.phone);
		final EditText etPMail = (EditText) dialogSignUp
				.findViewById(R.id.pmail);
		final Button btSignup = (Button) dialogSignUp.findViewById(R.id.signup);
		btSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etEmail.getText().toString().isEmpty()) {
					etEmail.setError("Email đăng ký không được để trống");
					return;
				}
				if (etPassword.getText().toString().isEmpty()) {
					etPassword.setError("Mật khẩu không được để trống");
					return;
				}
				if (etRePassword.getText().toString().isEmpty()) {
					etRePassword.setError("Bạn chưa nhập lại mật khẩu");
					return;
				}
				if (etPhone.getText().toString().isEmpty()) {
					etPhone.setError("Số điện thoại không được để trống");
					return;
				}
				if (!etPassword.getText().toString()
						.endsWith(etRePassword.getText().toString())) {
					etPassword.setError("Mật khẩu không khớp");
					etRePassword.setError("Mật khẩu không khớp");
					return;
				}

				if (etPMail.getText().toString().isEmpty()) {
					new SignUpTask().execute(etEmail.getText().toString(),
							etPassword.getText().toString(), etPhone.getText()
									.toString(), "");
				} else {
					new SignUpTask().execute(etEmail.getText().toString(),
							etPassword.getText().toString(), etPhone.getText()
									.toString(), etPMail.getText().toString());
				}
				dialogSignUp.dismiss();
			}
		});
		dialogSignUp.setCancelable(true);
		dialogSignUp.show();
	}

	private class SignUpTask extends AsyncTask<String, Void, Void> {

		JSONObject jsonSignUp;

		@Override
		protected Void doInBackground(String... params) {
			jsonSignUp = ServiceConnection.signUp(params[0], params[1],
					params[2], params[3]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				makeToast(jsonSignUp.getString("Message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(OtherActivity.this, "",
					"Đang đăng ký...");
		}

	}

	private class LoginTask extends AsyncTask<String, Void, Void> {

		JSONObject jsonLogin;

		@Override
		protected Void doInBackground(String... params) {
			String email = params[0];
			String password = params[1];
			jsonLogin = ServiceConnection.login(email, password);
			return null;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(OtherActivity.this, "",
					"Đang đăng nhập...");
		}

		@Override
		protected void onPostExecute(Void result) {

			// Response code
			try {
				DisplayUtils.log(jsonLogin.toString());
				int ret = jsonLogin.getInt("Ret");

				if (DataUtils.isInErrCode(ret)) {
					// Put logged info
					SharePreferenceUtils.putLogInfo(OtherActivity.this, false);

					showDialogRespose(false);
				} else {
					// Put logged info
					SharePreferenceUtils.putLogInfo(OtherActivity.this, true);
					JSONObject data = jsonLogin.getJSONObject("Data");
					int accID = data.getInt("AccountId");
					String requestToken = data.getString("RequestToken");
					String AccEmail = data.getString("AccountEmail");
					SharePreferenceUtils.putAccID(OtherActivity.this, accID);
					SharePreferenceUtils.putrequestToken(OtherActivity.this,
							requestToken);
					SharePreferenceUtils.putAccEmail(OtherActivity.this,
							AccEmail);
					new LoadUserInfo().execute();
					// showDialogRespose(true);
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

	}

	private void showDialogRespose(final boolean success) {
		String msg;
		if (success) {
			msg = "Đăng nhập thành công";
		} else {
			msg = "Đăng nhập thất bại";
		}
		new AlertDialog.Builder(this).setTitle("").setMessage(msg)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setupSlidingMenuLoggedIn();
					}
				}).setCancelable(false).show();
	}

	private class LoadUserInfo extends AsyncTask<Void, Void, Void> {

		JSONObject o;

		@Override
		protected Void doInBackground(Void... params) {
			o = ServiceConnection.getUserInfo(
					SharePreferenceUtils.getAccEmail(OtherActivity.this) + "",
					SharePreferenceUtils.getrequestToken(OtherActivity.this));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			DisplayUtils.log(o.toString());
			try {
				int tk1 = o.getInt("TK1");
				int tk2 = o.getInt("TK2");
				int tk3 = o.getInt("TK3");
				SharePreferenceUtils.putGoldInfo(OtherActivity.this, tk1, tk2,
						tk3);
			} catch (JSONException e) {

				e.printStackTrace();
			}
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			showDialogRespose(true);
		}

		@Override
		protected void onPreExecute() {

		}

	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent(true);
		} else {
			showDialogExit();
		}
	}

	public void showDialogExit() {
		new AlertDialog.Builder(this)
				.setTitle("Thoát")
				.setMessage("Bạn có muốn thoát ứng dụng không?")
				.setNegativeButton("Thoát",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								for (int i = 0; i < DataUtils.activityList
										.size(); i++) {
									DataUtils.activityList.get(i).finish();
								}
							}
						})
				.setNeutralButton("Không",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setCancelable(true).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.general:
			DataUtils.activityList.remove(OtherActivity.this);
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.category:
			if (tab != 5) {
				tab = 5;
				switchContent(R.id.content, CategoryFragment.getInstance());
				btCategory.setBackgroundResource(R.drawable.btn_the_loai_focus);
				btRank.setBackgroundResource(R.drawable.btn_xep_hang);
				btOther.setBackgroundResource(R.drawable.btn_khac);
				btTopic.setBackgroundResource(R.drawable.btn_chuyen_de);
			}
			break;
		case R.id.rank:

			if (tab != 4) {
				tab = 4;
				switchContent(R.id.content, RankFragment.getInstance());
				btRank.setBackgroundResource(R.drawable.btn_xep_hang_focus);
				btCategory.setBackgroundResource(R.drawable.btn_the_loai);
				btOther.setBackgroundResource(R.drawable.btn_khac);
				btTopic.setBackgroundResource(R.drawable.btn_chuyen_de);
			}
			break;
		case R.id.other:
			if (tab != 3) {
				tab = 3;
				switchContent(R.id.content, OtherFragment.getInstance());
				btOther.setBackgroundResource(R.drawable.btn_khac_focus);
				btRank.setBackgroundResource(R.drawable.btn_xep_hang);
				btCategory.setBackgroundResource(R.drawable.btn_the_loai);
				btTopic.setBackgroundResource(R.drawable.btn_chuyen_de);
			}
			break;
		case R.id.topic:
			if (tab != 2) {
				tab = 2;
				switchContent(R.id.content, TopicFragment.getInstance());
				btTopic.setBackgroundResource(R.drawable.btn_chuyen_de_focus);
				btOther.setBackgroundResource(R.drawable.btn_khac);
				btRank.setBackgroundResource(R.drawable.btn_xep_hang);
				btCategory.setBackgroundResource(R.drawable.btn_the_loai);

			}
			break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (SharePreferenceUtils.isLogged(this)) { // If logged in
			setupSlidingMenuLoggedIn();
		} else {
			setupSlidingMenu();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			if(DataUtils.SHOW_FOOTER){
				AnimationUtils.setFadeOutAnim(footer);
				DataUtils.SHOW_FOOTER = false;
			}else{
				AnimationUtils.setFadeInAnim(footer);
				DataUtils.SHOW_FOOTER = true;
			}
			break;
		
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getIpWan(){
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				((TextView)menu.findViewById(R.id.ip_wan)).setText("Ip wan:"+new String(arg2));
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				//makeToast("Lỗi kết nối");
			}
		};
		ServiceConnection.getIpWan(handler);
	}

}
