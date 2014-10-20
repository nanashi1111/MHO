package com.maihoangonline.mho;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
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
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.maihoangonline.adapter.HomePagerAdapter;
import com.maihoangonline.adapter.ListOptionAdapter;
import com.maihoangonline.fragments.GameReviewFragment;
import com.maihoangonline.fragments.NewsFragment;
import com.maihoangonline.models.Option;
import com.maihoangonline.utils.AnimationUtils;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DialogUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

public class HomeActivity extends BaseActivity implements TabListener,
		OnClickListener {

	private boolean isMenuShowing = false;
	private boolean attached = false;
	private SlidingMenu menu;
	private ProgressDialog dialog;
	private ActionBar bar;
	private ViewPager pager;

	private Button btTopic, btOther, btRank, btCategory;
	private LinearLayout footer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		File localFile1 = new File(DataUtils.BASE_PATH);
		if (!localFile1.exists())
			localFile1.mkdir();
		dbh.clearDownloaded();
		menu = new SlidingMenu(this);
		if (!SharePreferenceUtils.isLogged(this)) { // If not logged in
			setupSlidingMenu();
		} else {
			setupSlidingMenuLoggedIn();
		}
		setupActionBar();
		setupView();
		DataUtils.activityList.add(this);
	}

	private void setupView() {
		FragmentPagerAdapter adapter = new HomePagerAdapter(
				getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);

		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				bar.setSelectedNavigationItem(position);
				if (position == 1) {
					GameReviewFragment.getInstance().loadNewsFirstTime();
				} else if (position == 2) {
					NewsFragment.getInstance().loadNewsFirstTime();
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

		});
		bar.addTab(bar.newTab().setText("GAME HOT").setTabListener(this));
		bar.addTab(bar.newTab().setText("ĐÁNH GIÁ GAME").setTabListener(this));
		bar.addTab(bar.newTab().setText("TIN MỚI").setTabListener(this));

		btCategory = (Button) findViewById(R.id.category);
		btCategory.setOnClickListener(this);
		btRank = (Button) findViewById(R.id.rank);
		btRank.setOnClickListener(this);
		btOther = (Button) findViewById(R.id.other);
		btOther.setOnClickListener(this);
		btTopic = (Button) findViewById(R.id.topic);
		btTopic.setOnClickListener(this);
		
		footer = (LinearLayout)findViewById(R.id.footer);
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

		// menu = new SlidingMenu(this);
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
				SharePreferenceUtils.putLogInfo(HomeActivity.this, false);
				// menu.removeAllViews();
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
					intent = new Intent(HomeActivity.this,
							UserInfoActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
					break;
				case 1:
					menu.toggle();
					isMenuShowing = false;
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this,
							ListThreadActivity.class));
					overridePendingTransition(0, 0);
					break;
				case 3:
					intent = new Intent(HomeActivity.this,
							ListGameActivity.class);
					intent.putExtra("favorite", true);
					startActivity(intent);
					overridePendingTransition(0, 0);
					break;
				case 4:
					new GetIPWan().execute();
					break;
				}
			}
		});
	}
	
	private class GetIPWan extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog d;
		BufferedReader bReader;
		String sms;

		@Override
		protected Void doInBackground(Void... arg0) {
			URL url;
			try {
				url = new URL("http://checkip.amazonaws.com/");
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				InputStream is = con.getInputStream();
				InputStreamReader reader = new InputStreamReader(is);
				bReader = new BufferedReader(reader);
				sms = bReader==null?"Null":bReader.readLine();
				DisplayUtils.log(sms);
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(d!=null){
				d.dismiss();
				d=null;
			}
				makeToast("Ip wan:"+sms);
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(HomeActivity.this, "", "Getting WAN IP");
		}
		
	}

	private void setupActionBar() {
		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_home);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
					startActivity(new Intent(HomeActivity.this,DownloadProgressActivity.class));
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
				Intent intent = new Intent(HomeActivity.this,
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
			dialog = ProgressDialog.show(HomeActivity.this, "",
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
			dialog = ProgressDialog.show(HomeActivity.this, "",
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
					SharePreferenceUtils.putLogInfo(HomeActivity.this, false);

					showDialogRespose(false);
				} else {
					// Put logged info
					SharePreferenceUtils.putLogInfo(HomeActivity.this, true);
					JSONObject data = jsonLogin.getJSONObject("Data");
					int accID = data.getInt("AccountId");
					String requestToken = data.getString("RequestToken");
					String AccEmail = data.getString("AccountEmail");
					SharePreferenceUtils.putAccID(HomeActivity.this, accID);
					SharePreferenceUtils.putrequestToken(HomeActivity.this,
							requestToken);
					SharePreferenceUtils.putAccEmail(HomeActivity.this,
							AccEmail);
					new LoadUserInfo().execute();
					// showDialogRespose(true);
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

	}

	private class LoadUserInfo extends AsyncTask<Void, Void, Void> {

		JSONObject o;

		@Override
		protected Void doInBackground(Void... params) {
			o = ServiceConnection.getUserInfo(
					SharePreferenceUtils.getAccEmail(HomeActivity.this) + "",
					SharePreferenceUtils.getrequestToken(HomeActivity.this));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			DisplayUtils.log(o.toString());
			try {
				int tk1 = o.getInt("TK1");
				int tk2 = o.getInt("TK2");
				int tk3 = o.getInt("TK3");
				SharePreferenceUtils.putGoldInfo(HomeActivity.this, tk1, tk2,
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

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent(true);
		} else {
			DialogUtils.showDialogExit(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataUtils.listGameInDownloadProgress.clear();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(HomeActivity.this, OtherActivity.class);
		switch (v.getId()) {
		case R.id.category:
			intent.putExtra("tab", 5);
			break;
		case R.id.rank:
			intent.putExtra("tab", 4);
			break;
		case R.id.other:
			intent.putExtra("tab", 3);
			break;
		case R.id.topic:
			intent.putExtra("tab", 2);
			break;
		}
		startActivity(intent);
		overridePendingTransition(0, 0);

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

}
