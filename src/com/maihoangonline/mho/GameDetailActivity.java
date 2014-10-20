package com.maihoangonline.mho;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.adapter.ListThreadAdapter;
import com.maihoangonline.adapter.PictureAdapter;
import com.maihoangonline.models.Game;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ModelDataUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

public class GameDetailActivity extends BaseActivity implements OnClickListener {

	// Facebook
	// private Facebook facebook;
	private UiLifecycleHelper uiHelper;
	private StatusCallback callBack;

	// UI
	private ImageView ivIcon;
	private TextView tvGameName;
	private TextView tvGameVersion1;
	private TextView tvGameVersion2;
	private TextView tvGameSize;
	private TextView tvTotalDownload;
	private ImageView[] ivRate;
	private ImageView btDanhGia;
	private ImageView btShareFB;
	private ImageView btDownload;
	private ImageView btChiTiet;
	private ImageView btDanhGiaNhanXet;
	private ImageView btBoSuuTap;
	private ImageView btLienQuan;
	// Game
	Game game;
	private int vote = 1;
	// Current tab
	private int currenTab = -1; // 1 = Chi tiet; 2 = Danh gia nhan xet; 3 = Bo
								// suu tap; 4=Lien quan
	// Child view
	private View viewGameDescription;
	private ViewPager pager;
	private TextView tvGameDescription;

	private View viewRelated;
	private LoadMoreListView listRelated;
	private int indexRelated = 0;
	private ArrayList<Game> listGameRelated = new ArrayList<Game>();
	private ListGameAdapter adapterRelated;

	private View viewComment;

	private View viewCollection;
	private ImageView btAddToThread;
	private ImageView btAddToFavorite;
	private ListView listFavorite;
	private ArrayList<Game> listFavoriteGame = new ArrayList<Game>();
	private ListGameAdapter listFavoritAdapter;
	private TextView tvNumberFavorite;

	private ActionBar bar;

	private ProgressDialog d;
	private ArrayList<MHOThread> listThread = new ArrayList<MHOThread>();
	private ListThreadAdapter threadAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_detail);
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.maihoangonline.mho", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				DisplayUtils.log("KeyHash:"
						+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
		// facebook = new Facebook(DataUtils.APP_ID);
		callBack = new StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				// Log.e("FBError============", exception.toString());
			}
		};

		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		loadData();
		setupActionBar();
		setupView();
		// new IncViewGame().execute(game);
		incViewGame();
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
		tvTitle.setText(game.getTitle());

	}

	private void loadData() {
		game = (Game) getIntent().getSerializableExtra("game");

	}

	private void setupView() {
		ivIcon = (ImageView) findViewById(R.id.icon_game);
		tvGameName = (TextView) findViewById(R.id.game_name);
		tvGameVersion1 = (TextView) findViewById(R.id.game_version);
		tvGameVersion2 = (TextView) findViewById(R.id.game_version_2);
		tvGameSize = (TextView) findViewById(R.id.game_size);
		tvTotalDownload = (TextView) findViewById(R.id.total_download);
		ivRate = new ImageView[5];
		ivRate[0] = (ImageView) findViewById(R.id.rate1);
		ivRate[1] = (ImageView) findViewById(R.id.rate2);
		ivRate[2] = (ImageView) findViewById(R.id.rate3);
		ivRate[3] = (ImageView) findViewById(R.id.rate4);
		ivRate[4] = (ImageView) findViewById(R.id.rate5);
		Bitmap bmGoldStar = BitmapFactory.decodeResource(getResources(),
				R.drawable.rate_larg_100_per);
		final Bitmap bmGoldStarScaled = DisplayUtils.scaleBitmap(bmGoldStar,
				DataUtils.SCREEN_WIDTH / 10.285f / bmGoldStar.getWidth());
		Bitmap bmGrayStar = BitmapFactory.decodeResource(getResources(),
				R.drawable.rate_larg_gray);
		final Bitmap bmGrayStarScaled = DisplayUtils.scaleBitmap(bmGrayStar,
				DataUtils.SCREEN_WIDTH / 10.285f / bmGrayStar.getWidth());
		for (int i = 0; i <= 4; i++) {
			ivRate[i].setImageBitmap(bmGrayStarScaled);
		}
		View.OnClickListener ivListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.rate1:
					vote = 1;
					ivRate[0].setImageBitmap(bmGoldStarScaled);
					for (int i = 1; i <= 4; i++) {
						ivRate[i].setImageBitmap(bmGrayStarScaled);
					}
					break;
				case R.id.rate2:
					vote = 2;
					for (int i = 0; i <= 1; i++) {
						ivRate[i].setImageBitmap(bmGoldStarScaled);
					}
					for (int i = 2; i <= 4; i++) {
						ivRate[i].setImageBitmap(bmGrayStarScaled);
					}
					break;
				case R.id.rate3:
					vote = 3;
					for (int i = 0; i <= 2; i++) {
						ivRate[i].setImageBitmap(bmGoldStarScaled);
					}
					for (int i = 3; i <= 4; i++) {
						ivRate[i].setImageBitmap(bmGrayStarScaled);
					}
					break;
				case R.id.rate4:
					vote = 4;
					for (int i = 0; i <= 3; i++) {
						ivRate[i].setImageBitmap(bmGoldStarScaled);
					}

					ivRate[4].setImageBitmap(bmGrayStarScaled);

					break;
				case R.id.rate5:
					vote = 5;
					for (int i = 0; i <= 4; i++) {
						ivRate[i].setImageBitmap(bmGoldStarScaled);
					}
					break;
				}
			}
		};
		for (int i = 0; i < 5; i++) {
			ivRate[i].setOnClickListener(ivListener);
		}

		btDanhGia = (ImageView) findViewById(R.id.danh_gia);
		Bitmap bmDanhGiaDetail = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_danh_gia_122);
		Bitmap bmDanhGiaDetailScaled = DisplayUtils.scaleBitmap(
				bmDanhGiaDetail, DataUtils.SCREEN_WIDTH / 4.8f
						/ bmDanhGiaDetail.getWidth(), true);
		btDanhGia.setImageBitmap(bmDanhGiaDetailScaled);
		btDanhGia.setOnClickListener(this);

		btShareFB = (ImageView) findViewById(R.id.share);
		Bitmap bitmapShareFB = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_share_face_focus);
		bitmapShareFB = DisplayUtils.scaleBitmap(bitmapShareFB,
				(float) (DataUtils.SCREEN_WIDTH / 3.2f / bitmapShareFB
						.getWidth()));
		btShareFB.setImageBitmap(bitmapShareFB);
		btShareFB.setOnClickListener(this);

		btDownload = (ImageView) findViewById(R.id.download);
		Bitmap bitmapDownload = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_download_113);
		bitmapDownload = DisplayUtils.scaleBitmap(bitmapDownload,
				(float) (DataUtils.SCREEN_WIDTH / 5.5f / bitmapShareFB
						.getWidth()));
		btDownload.setImageBitmap(bitmapDownload);
		btDownload.setOnClickListener(this);
		btChiTiet = (ImageView) findViewById(R.id.chi_tiet);
		Bitmap bmChiTiet = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_chi_tiet_focus);
		bmChiTiet = DisplayUtils.scaleBitmap(bmChiTiet,
				(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmChiTiet.getWidth()),
				true);
		btChiTiet.setImageBitmap(bmChiTiet);
		btChiTiet.setOnClickListener(this);

		btDanhGiaNhanXet = (ImageView) findViewById(R.id.danh_gia_nhan_xet);
		Bitmap bmDanhGia = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_danh_gia_nhan_xet);
		bmDanhGia = DisplayUtils.scaleBitmap(bmDanhGia,
				(float) (DataUtils.SCREEN_WIDTH / 2.7f / bmDanhGia.getWidth()),
				true);
		btDanhGiaNhanXet.setImageBitmap(bmDanhGia);
		btDanhGiaNhanXet.setOnClickListener(this);
		btBoSuuTap = (ImageView) findViewById(R.id.bo_suu_tap);
		Bitmap bmBoSuuTap = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_bo_suu_tap_1);
		bmBoSuuTap = DisplayUtils
				.scaleBitmap(bmBoSuuTap,
						(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmBoSuuTap
								.getWidth()), true);
		btBoSuuTap.setImageBitmap(bmBoSuuTap);
		btBoSuuTap.setOnClickListener(this);
		btLienQuan = (ImageView) findViewById(R.id.lien_quan);
		Bitmap bmLienQuan = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_lien_quan);
		bmLienQuan = DisplayUtils
				.scaleBitmap(bmLienQuan,
						(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmLienQuan
								.getWidth()), true);
		btLienQuan.setImageBitmap(bmLienQuan);
		btLienQuan.setOnClickListener(this);

		// Set icon
		UrlImageViewHelper.setUrlDrawable(ivIcon, game.getPicture());
		// Set texts
		tvGameName.setText(game.getTitle());
		tvGameVersion1.setText(game.getVersion());
		tvGameVersion2.setText("Phiên bản:" + game.getVersion());
		tvGameSize.setText((game.getSize().isEmpty() ? "500" : game.getSize())
				+ "MB");
		tvTotalDownload.setText("Lượt tải:" + game.getDownload());
		// setViewGameDescription();
		setViewGameDescription();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			share();
			break;
		case R.id.danh_gia:
			//new VoteTask().execute();
			vote();
			break;
		case R.id.chi_tiet:
			setViewGameDescription();
			break;
		case R.id.lien_quan:
			setViewRelated();
			break;
		case R.id.bo_suu_tap:
			setViewCollection();
			break;
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.add_to_favorite:
			if (SharePreferenceUtils.isLogged(GameDetailActivity.this)) {
				boolean add = dbh.insertGameToFavorite(game,
						SharePreferenceUtils
								.getAccEmail(GameDetailActivity.this));
				if (add) {
					listFavoriteGame.add(game);
					listFavoritAdapter.notifyDataSetChanged();
					makeToast("Đã thêm " + game.getTitle()
							+ " vào danh sách yêu thích");
				} else {
					makeToast(game.getTitle()
							+ " đã có sẵn trong danh sách yêu thích");
				}
			} else {
				makeToast("Bạn phải đăng nhập để thực hiện chức năng này");
			}
			break;
		case R.id.add_to_thread:
			if (SharePreferenceUtils.isLogged(GameDetailActivity.this)) {
				showDialogSelectThread();
			} else {
				makeToast("Bạn phải đăng nhập để thực hiện chức năng này");
			}
			break;
		case R.id.download:
			/*
			 * boolean downloaded = dbh.checkGameInDownloaded(game);
			 * if(downloaded){
			 * makeToast(game.getTitle()+" đã được tải về trước đây"); return; }
			 */
			Intent intent = new Intent(GameDetailActivity.this,
					DownloadProgressActivity.class);
			intent.putExtra("game", game);
			if (!DataUtils.checkGameInProgressDownLoad(game)) {
				DataUtils.listGameInDownloadProgress.add(game);
				DataUtils.listGameInDownloadState.put(game, DataUtils.NONE);

			}
			startActivity(intent);
			overridePendingTransition(0, 0);
			break;

		}

	}

	private void share() {
		FacebookDialog localFacebookDialog = new FacebookDialog.ShareDialogBuilder(
				this).setLink("www.mho.vn").build();
		this.uiHelper.trackPendingDialogCall(localFacebookDialog.present());
	}

	/*private class VoteTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		String resultText;

		@Override
		protected Void doInBackground(Void... params) {
			resultText = ServiceConnection.vote(game, vote);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (d != null) {
				d.dismiss();
				d = null;
			}
			makeToast(resultText);
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(GameDetailActivity.this, "", "");

		}

	}*/
	
	private void vote(){
		d = ProgressDialog.show(GameDetailActivity.this, "", "");
		/*JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){


			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if(d!=null){
					d.dismiss();
					d=null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				String resultText=responseString;
				if (d != null) {
					d.dismiss();
					d = null;
				}
				makeToast(resultText);
				super.onSuccess(statusCode, headers, responseString);
			}

		};*/
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String resultText=new String(arg2);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				makeToast(resultText);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				makeToast("Lỗi kết nối");
				if(d!=null){
					d.dismiss();
					d=null;
				}
			}
		};
		ServiceConnection.vote(game, vote, handler);
	}

	private void setViewGameDescription() {

		changeButtonStyle(1);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llGameDetailScreen);
		if (viewGameDescription == null) {
			LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewGameDescription = inf.inflate(R.layout.view_game_description,
					null);
			FragmentStatePagerAdapter adapter = new PictureAdapter(
					getSupportFragmentManager(), game);
			pager = (ViewPager) viewGameDescription.findViewById(R.id.pager);
			pager.setAdapter(adapter);
			pager.setOffscreenPageLimit(ModelDataUtils.getPictureAlbum(game)
					.size() - 1);
			pager.setCurrentItem(0);
			pager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
			tvGameDescription = (TextView) viewGameDescription
					.findViewById(R.id.game_description);
			tvGameDescription.setText(Html.fromHtml(game.getDetail()));
		}

		if (currenTab == 1) {
			return;
		} else if (currenTab == 2) {
			ll.removeView(viewComment);
		} else if (currenTab == 3) {
			ll.removeView(viewCollection);
		} else if (currenTab == 4) {
			ll.removeView(viewRelated);
		}

		ll.addView(viewGameDescription);
		currenTab = 1;
	}

	private void setViewCollection() {
		changeButtonStyle(3);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llGameDetailScreen);
		if (viewCollection == null) {
			LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewCollection = inf.inflate(R.layout.view_collection, null);
			tvNumberFavorite = (TextView) viewCollection
					.findViewById(R.id.number_favotite_app);
			tvNumberFavorite.setText("TỔNG SỐ: "
					+ dbh.getNumberFavorite(SharePreferenceUtils
							.getAccEmail(this)) + " Game, Ứng dụng");
			btAddToThread = (ImageView) this.viewCollection
					.findViewById(R.id.add_to_thread);
			Bitmap bmAddToThread = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_add_chu_de);
			btAddToThread.setImageBitmap(DisplayUtils.scaleBitmap(
					bmAddToThread, DataUtils.SCREEN_WIDTH / 2.2F
							/ bmAddToThread.getWidth()));
			btAddToThread.setOnClickListener(this);
			btAddToFavorite = (ImageView) this.viewCollection
					.findViewById(R.id.add_to_favorite);
			Bitmap bmAddToFavorite = BitmapFactory.decodeResource(
					getResources(), R.drawable.btn_add_yeu_thich);
			btAddToFavorite.setImageBitmap(DisplayUtils.scaleBitmap(
					bmAddToFavorite, DataUtils.SCREEN_WIDTH / 2.2F
							/ bmAddToFavorite.getWidth()));
			btAddToFavorite.setOnClickListener(this);
			listFavorite = (ListView) viewCollection
					.findViewById(R.id.list_favorite);
			listFavoriteGame = dbh.getListFavoriteGame(SharePreferenceUtils
					.getAccEmail(this));
			listFavoritAdapter = new ListGameAdapter(this, 1, listFavoriteGame);
			listFavorite.setAdapter(listFavoritAdapter);
			listFavorite
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							game = listFavoriteGame.get(arg2);
							listGameRelated.clear();
							if (adapterRelated != null) {
								adapterRelated.notifyDataSetChanged();
							}
							indexRelated = 0;
							if (viewGameDescription != null) {
								viewGameDescription = null;
							}
							setViewGameDescription();
							UrlImageViewHelper.setUrlDrawable(ivIcon,
									game.getPicture());
							tvGameName.setText(game.getTitle());
							tvGameVersion1.setText(game.getVersion());
							tvGameVersion2.setText("Phiên bản:"
									+ game.getVersion());
							tvGameSize
									.setText((game.getSize().isEmpty() ? "500"
											: game.getSize()) + "MB");
							tvTotalDownload.setText("Lượt tải:"
									+ game.getDownload());

						}
					});
		}
		if (currenTab == 1) {
			ll.removeView(viewGameDescription);
		} else if (currenTab == 2) {
			ll.removeView(viewComment);
		} else if (currenTab == 3) {
			return;
		} else if (currenTab == 4) {
			ll.removeView(viewRelated);
		}
		ll.addView(viewCollection);
		currenTab = 3;
	}

	private void setViewRelated() {
		changeButtonStyle(4);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llGameDetailScreen);
		if (viewRelated == null) {
			LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewRelated = inf.inflate(R.layout.view_related, null);
			listRelated = (LoadMoreListView) viewRelated
					.findViewById(R.id.related);
			adapterRelated = new ListGameAdapter(this, 1, listGameRelated);
			listRelated.setAdapter(adapterRelated);
			listRelated.setOnLoadMoreListener(new OnLoadMoreListener() {

				@Override
				public void onLoadMore() {
					//new GetRelatedGame().execute();
					getRelatedGame();
				}
			});
			listRelated.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					game = listGameRelated.get(arg2);
					listGameRelated.clear();
					adapterRelated.notifyDataSetChanged();
					indexRelated = 0;
					if (viewGameDescription != null) {
						viewGameDescription = null;
					}
					setViewGameDescription();
					UrlImageViewHelper.setUrlDrawable(ivIcon, game.getPicture());
					tvGameName.setText(game.getTitle());
					tvGameVersion1.setText(game.getVersion());
					tvGameVersion2.setText("Phiên bản:" + game.getVersion());
					tvGameSize.setText((game.getSize().isEmpty() ? "500" : game
							.getSize()) + "MB");
					tvTotalDownload.setText("Lượt tải:" + game.getDownload());

				}
			});

		}

		if (currenTab == 1) {
			ll.removeView(viewGameDescription);
		} else if (currenTab == 2) {
			ll.removeView(viewComment);
		} else if (currenTab == 3) {
			ll.removeView(viewCollection);
		} else if (currenTab == 4) {
			return;
		}
		ll.addView(viewRelated);
		if (indexRelated == 0) {
			//new GetRelatedGame().execute();
			getRelatedGame();
		}
		currenTab = 4;
	}

	/*private class GetRelatedGame extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		JSONObject jsonRelate;

		@Override
		protected Void doInBackground(Void... params) {
			jsonRelate = ServiceConnection.getListRelated(game, indexRelated);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arrayGame = jsonRelate.getJSONArray("DataList");
				for (int i = 0; i < arrayGame.length(); i++) {
					JSONObject gameItem = arrayGame.getJSONObject(i);
					Game game = new Game();
					game.setId(gameItem.getInt("Id"));
					game.setTitle(gameItem.getString("Title"));
					game.setIdGroup(gameItem.getInt("IdGroup"));
					game.setIdSystem(gameItem.getInt("IdSystem"));
					game.setPicture(gameItem.getString("Picture"));
					game.setPictureAlbum(gameItem.getString("PictureAlbum"));
					game.setDes(gameItem.getString("Des"));
					game.setDetail(gameItem.getString("Detail"));
					game.setIdCategoryGame(gameItem.getInt("IdCategoryGame"));
					game.setHotGame(gameItem.getBoolean("HotGame"));
					game.setNewGame(gameItem.getBoolean("NewGame"));
					game.setFile(gameItem.getString("File"));
					game.setDownload(gameItem.getInt("Download"));
					game.setRate(gameItem.getInt("Rate"));
					game.setVersion(gameItem.getString("Version"));
					game.setSize(gameItem.getString("Size"));
					game.setView(gameItem.getInt("View"));
					game.setFreeGame(gameItem.getBoolean("FreeGame"));
					listGameRelated.add(game);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			indexRelated++;
			listRelated.onLoadMoreComplete();
			adapterRelated.notifyDataSetChanged();
			listRelated.smoothScrollToPosition(adapterRelated.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(GameDetailActivity.this, "",
					"Đang tải các ứng dụng liên quan");
		}

	}*/
	
	private void getRelatedGame(){
		d = ProgressDialog.show(GameDetailActivity.this, "",
				"Đang tải các ứng dụng liên quan");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if(d!=null){
					d.dismiss();
					d=null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject jsonRelate= response;
				try {
					JSONArray arrayGame = jsonRelate.getJSONArray("DataList");
					for (int i = 0; i < arrayGame.length(); i++) {
						JSONObject gameItem = arrayGame.getJSONObject(i);
						Game game = new Game();
						game.setId(gameItem.getInt("Id"));
						game.setTitle(gameItem.getString("Title"));
						game.setIdGroup(gameItem.getInt("IdGroup"));
						game.setIdSystem(gameItem.getInt("IdSystem"));
						game.setPicture(gameItem.getString("Picture"));
						game.setPictureAlbum(gameItem.getString("PictureAlbum"));
						game.setDes(gameItem.getString("Des"));
						game.setDetail(gameItem.getString("Detail"));
						game.setIdCategoryGame(gameItem.getInt("IdCategoryGame"));
						game.setHotGame(gameItem.getBoolean("HotGame"));
						game.setNewGame(gameItem.getBoolean("NewGame"));
						game.setFile(gameItem.getString("File"));
						game.setDownload(gameItem.getInt("Download"));
						game.setRate(gameItem.getInt("Rate"));
						game.setVersion(gameItem.getString("Version"));
						game.setSize(gameItem.getString("Size"));
						game.setView(gameItem.getInt("View"));
						game.setFreeGame(gameItem.getBoolean("FreeGame"));
						listGameRelated.add(game);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				indexRelated++;
				listRelated.onLoadMoreComplete();
				adapterRelated.notifyDataSetChanged();
				listRelated.smoothScrollToPosition(adapterRelated.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			

		};
		
		ServiceConnection.getListRelated(game, indexRelated, handler);
	}

	private void changeButtonStyle(int focusButton) {
		if (currenTab == focusButton) {
			return;
		}
		if (currenTab == 1) {
			Bitmap bmChiTiet = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_chi_tiet);
			bmChiTiet = DisplayUtils.scaleBitmap(bmChiTiet,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmChiTiet
							.getWidth()), true);
			btChiTiet.setImageBitmap(bmChiTiet);
		} else if (currenTab == 2) {
			Bitmap bmDanhGia = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_danh_gia_nhan_xet);
			bmDanhGia = DisplayUtils.scaleBitmap(bmDanhGia,
					(float) (DataUtils.SCREEN_WIDTH / 2.7f / bmDanhGia
							.getWidth()), true);
			btDanhGiaNhanXet.setImageBitmap(bmDanhGia);
		} else if (currenTab == 3) {
			Bitmap bmBoSuuTap = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_bo_suu_tap_1);
			bmBoSuuTap = DisplayUtils.scaleBitmap(bmBoSuuTap,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmBoSuuTap
							.getWidth()), true);
			btBoSuuTap.setImageBitmap(bmBoSuuTap);
		} else if (currenTab == 4) {
			Bitmap bmLienQuan = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_lien_quan);
			bmLienQuan = DisplayUtils.scaleBitmap(bmLienQuan,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmLienQuan
							.getWidth()), true);
			btLienQuan.setImageBitmap(bmLienQuan);
		}

		if (focusButton == 1) {
			Bitmap bmChiTiet = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_chi_tiet_focus);
			bmChiTiet = DisplayUtils.scaleBitmap(bmChiTiet,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmChiTiet
							.getWidth()), true);
			btChiTiet.setImageBitmap(bmChiTiet);
		} else if (focusButton == 2) {
			Bitmap bmDanhGia = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_danh_gia_nhan_xet_focus);
			bmDanhGia = DisplayUtils.scaleBitmap(bmDanhGia,
					(float) (DataUtils.SCREEN_WIDTH / 2.7f / bmDanhGia
							.getWidth()), true);
			btDanhGiaNhanXet.setImageBitmap(bmDanhGia);
		} else if (focusButton == 3) {
			Bitmap bmBoSuuTap = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_bo_suu_tap_1_focus);
			bmBoSuuTap = DisplayUtils.scaleBitmap(bmBoSuuTap,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmBoSuuTap
							.getWidth()), true);
			btBoSuuTap.setImageBitmap(bmBoSuuTap);
		} else if (focusButton == 4) {
			Bitmap bmLienQuan = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_lien_quan_focus);
			bmLienQuan = DisplayUtils.scaleBitmap(bmLienQuan,
					(float) (DataUtils.SCREEN_WIDTH / 5.5f / bmLienQuan
							.getWidth()), true);
			btLienQuan.setImageBitmap(bmLienQuan);
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {

					@Override
					public void onError(PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity=====",
								String.format("Error: %s", error.toString()));
						error.printStackTrace();
					}

					@Override
					public void onComplete(PendingCall pendingCall, Bundle data) {
						Log.i("Activity=====", "Success!");
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	/*
	 * private class IncViewGame extends AsyncTask<Game, Void, Void> {
	 * 
	 * JSONObject json;
	 * 
	 * @Override protected Void doInBackground(Game... params) { json =
	 * ServiceConnection.incViewGame(game); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) {
	 * DisplayUtils.log(json.toString()); new GetListThread().execute(); }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(GameDetailActivity.this, "",
	 * "Đang tải dữ liệu ứng dụng"); }
	 * 
	 * }
	 */

	private void incViewGame() {
		d = ProgressDialog.show(GameDetailActivity.this, "",
				"Đang tải dữ liệu ứng dụng");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject json = response;
				DisplayUtils.log(json.toString());
				getListThread();
				super.onSuccess(statusCode, headers, response);
			}

		};
		
		/*AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String response = new String(arg2);
				DisplayUtils.log(response);
				getListThread();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}				
			}
		};*/

		ServiceConnection.incViewGame(game, handler);
	}

	private void getListThread() {
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject jsonListThread = response;
				listThread.clear();
				try {
					JSONArray arr = jsonListThread.getJSONArray("DataList");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject threadItem = arr.getJSONObject(i);
						MHOThread thread = new MHOThread();
						thread.setId(threadItem.getInt("Id"));
						thread.setName(URLDecoder.decode(threadItem.getString("Name")));
						thread.setUser(threadItem.getString("User"));
						thread.setDes(URLDecoder.decode(threadItem.getString("Des")));
						thread.setIdSystem(threadItem.getInt("IdSystem"));
						thread.setViews(threadItem.getInt("Views"));
						listThread.add(thread);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				threadAdapter = new ListThreadAdapter(GameDetailActivity.this,
						1, listThread);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}

		};
		ServiceConnection.getListThread(SharePreferenceUtils.getAccEmail(this),
				handler);
	}

	/*
	 * private class GetListThread extends AsyncTask<Void, Void, Void> {
	 * 
	 * JSONObject jsonListThread;
	 * 
	 * @Override protected Void doInBackground(Void... params) { jsonListThread
	 * = ServiceConnection .getListThread(SharePreferenceUtils
	 * .getAccEmail(GameDetailActivity.this)); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { listThread.clear();
	 * try { JSONArray arr = jsonListThread.getJSONArray("DataList"); for (int i
	 * = 0; i < arr.length(); i++) { JSONObject threadItem =
	 * arr.getJSONObject(i); MHOThread thread = new MHOThread();
	 * thread.setId(threadItem.getInt("Id"));
	 * thread.setName(threadItem.getString("Name"));
	 * thread.setUser(threadItem.getString("User"));
	 * thread.setDes(threadItem.getString("Des"));
	 * thread.setIdSystem(threadItem.getInt("IdSystem"));
	 * thread.setViews(threadItem.getInt("Views")); listThread.add(thread); } }
	 * catch (JSONException e) { e.printStackTrace(); }
	 * 
	 * threadAdapter = new ListThreadAdapter(GameDetailActivity.this, 1,
	 * listThread); if (d != null) { d.dismiss(); d = null; } }
	 * 
	 * }
	 */

	private void showDialogSelectThread() {
		final Dialog diaglogSelectThread = new Dialog(this);
		diaglogSelectThread.requestWindowFeature(Window.FEATURE_NO_TITLE);
		diaglogSelectThread.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		diaglogSelectThread.setContentView(R.layout.dialog_select_thread);
		ListView lvThread = (ListView) diaglogSelectThread
				.findViewById(R.id.list_thread);
		if (threadAdapter != null) {
			lvThread.setAdapter(threadAdapter);
		}
		lvThread.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				diaglogSelectThread.dismiss();
				/*
				 * new AddGameToThread().execute(game.getId(), listThread
				 * .get(arg2).getId());
				 */
				addGameToThread(game, listThread.get(arg2));

			}
		});
		Button btCreateThread = (Button) diaglogSelectThread
				.findViewById(R.id.create_thread);
		btCreateThread.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				diaglogSelectThread.dismiss();
				showDialogCreateThread();
			}
		});
		diaglogSelectThread.setCancelable(true);
		diaglogSelectThread.show();
	}

	private void showDialogCreateThread() {
		final Dialog dialogCreateThread = new Dialog(this);
		dialogCreateThread.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogCreateThread.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialogCreateThread.setContentView(R.layout.dialog_create_thread);
		final EditText etThreadName = ((EditText) dialogCreateThread
				.findViewById(R.id.thread_name));
		final EditText etDes = ((EditText) dialogCreateThread
				.findViewById(R.id.thread_des));
		Button btCreateThread = ((Button) dialogCreateThread
				.findViewById(R.id.create_thread));
		btCreateThread.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etThreadName.getText().toString().isEmpty()) {
					etThreadName.setError("Tên chủ đề không được để trống");
					return;
				}
				if (etDes.getText().toString().isEmpty()) {
					etDes.setError("Mô tả chủ đề không được để trống");
					return;
				}
				dialogCreateThread.dismiss();
				createThread(0, SharePreferenceUtils
						.getAccEmail(GameDetailActivity.this), etThreadName
						.getText().toString(), etDes.getText().toString());
			}
		});
		dialogCreateThread.setCancelable(true);
		dialogCreateThread.show();
	}

	/*
	 * private class CreateThread extends AsyncTask<String, Void, Void> {
	 * 
	 * String serverResponse;
	 * 
	 * @Override protected Void doInBackground(String... params) {
	 * serverResponse = ServiceConnection.createThread(0,
	 * SharePreferenceUtils.getAccEmail(GameDetailActivity.this), params[0],
	 * params[1]); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) {
	 * makeToast(serverResponse); //new GetListThread().execute();
	 * getListThread(); }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(GameDetailActivity.this, "", "Đang tạo chủ đề"); }
	 * 
	 * }
	 */

	private void createThread(int Id, String email, String name, String des) {
		d = ProgressDialog.show(GameDetailActivity.this, "", "Đang tạo chủ đề");
		
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String serverResponse = new String(arg2);
				makeToast(serverResponse);
				getListThread();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}				
			}
		};
		ServiceConnection.createThread(Id, email, name, des, handler);
	}

	/*
	 * private class AddGameToThread extends AsyncTask<Integer, Void, Void> {
	 * 
	 * String serverResponse;
	 * 
	 * @Override protected Void doInBackground(Integer... params) { Game game =
	 * new Game(); game.setId(params[0]); MHOThread thread = new MHOThread();
	 * thread.setId(params[1]); serverResponse =
	 * ServiceConnection.addGameToThread(game, thread); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { if (d != null) {
	 * d.dismiss(); d = null; } makeToast(serverResponse); }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(GameDetailActivity.this, "", "Đang cập nhật chủ đề");
	 * }
	 * 
	 * }
	 */

	private void addGameToThread(Game game, MHOThread thread) {
		d = ProgressDialog.show(GameDetailActivity.this, "",
				"Đang cập nhật chủ đề");
		/*JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				String serverResponse = responseString;
				if (d != null) {
					d.dismiss();
					d = null;
				}
				makeToast(serverResponse);
				super.onSuccess(statusCode, headers, responseString);
			}

		};
		*/
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String serverResponse = new String(arg2);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				makeToast(serverResponse);				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}
		};

		ServiceConnection.addGameToThread(game, thread, handler);
	}

}
