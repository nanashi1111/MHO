package com.maihoangonline.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.BannerAdapter;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.mho.GameDetailActivity;
import com.maihoangonline.mho.GiftActivity;
import com.maihoangonline.mho.HomeActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;

@SuppressLint("ValidFragment")
public class GameHotFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	public static GameHotFragment INSTANCE;
	// View of fragment
	private View rootView;
	// Banner
	//private HorizontalListView listBanner;
	private Gallery listBanner;
	private ArrayList<String> listLinkBanner = new ArrayList<String>();
	private BannerAdapter bannerAdapter;
	// private ImageView ivBanner;
	// List game
	private LoadMoreListView listGame;
	private ArrayList<Game> listGameItem = new ArrayList<Game>();
	private ListGameAdapter gameHotAdapter;
	private int indexGameHot = 0;
	// List app
	private ArrayList<Game> listAppItem = new ArrayList<Game>();
	private ListGameAdapter appHotAdapter;
	private int indexAppHot = 0;
	// List new game app
	private ArrayList<Game> listNewGameAppItem = new ArrayList<Game>();
	private ListGameAdapter newGameAppAdapter;
	private int indexNewGameApp = 0;

	// Mode (game, app, new game app, event
	private int mode = 1; // 1 = GAME; 2 = APP; 3=NEW GAME APP
	private ProgressDialog d;

	private GameHotFragment() {

	}

	public static GameHotFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GameHotFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_game_hot, null);
		listGame = (LoadMoreListView) rootView.findViewById(R.id.list_game);
		gameHotAdapter = new ListGameAdapter(getActivity(), 1, listGameItem);
		appHotAdapter = new ListGameAdapter(getActivity(), 1, listAppItem);
		newGameAppAdapter = new ListGameAdapter(getActivity(), 1,
				listNewGameAppItem);
		listGame.setAdapter(gameHotAdapter);
		listGame.setOnItemClickListener(this);
		listGame.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				if (mode == 1) {
					// new LoadGameHot().execute(indexGameHot);
					loadGameHot();
				} else if (mode == 2) {
					// new LoadAppHot().execute(indexAppHot);
					loadAppHot();
				} else if (mode == 3) {
					loadNewGameApp();
				}
			}
		});
		scaleBanner();
		/*
		 * if (!ServiceConnection.checkConnection(getActivity())) {
		 * DialogUtils.showInfoDialog(getActivity(),
		 * "Không có kết nối, bạn hãy kiểm tra lại kết nối mạng!"); } else {
		 * //new LoadGameHot().execute(indexGameHot); loadGameHot(); }
		 */
		// loadGameHot();
		getListBanner();
		return rootView;
	}

	private void loadGameHot() {
		if (listGameItem.size() > 0) {
			return;
		}
		if (d == null) {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải danh sách game hot...");
		}
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((HomeActivity) getActivity()).makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				JSONObject jsonGameHot = response;
				try {
					JSONArray arrayGame = jsonGameHot.getJSONArray("DataList");
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
						game.setIdCategoryGame(gameItem
								.getInt("IdCategoryGame"));
						game.setHotGame(gameItem.getBoolean("HotGame"));
						game.setNewGame(gameItem.getBoolean("NewGame"));
						game.setFile(gameItem.getString("File"));
						game.setDownload(gameItem.getInt("Download"));
						game.setRate(gameItem.getInt("Rate"));
						game.setVersion(gameItem.getString("Version"));
						game.setSize(gameItem.getString("Size"));
						game.setView(gameItem.getInt("View"));
						game.setFreeGame(gameItem.getBoolean("FreeGame"));
						listGameItem.add(game);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gameHotAdapter.notifyDataSetChanged();
				indexGameHot++;
				listGame.onLoadMoreComplete();
				listGame.smoothScrollToPosition(gameHotAdapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}

		};
		ServiceConnection.getListGameHot(indexGameHot, handler);
	}

	private void loadNewGameApp() {
		if (listNewGameAppItem.size() > 0) {
			return;
		}
		d = ProgressDialog.show(getActivity(), "",
				"Đang tải danh sách game app mới...");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((HomeActivity) getActivity()).makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				JSONObject jsonNewGameApp = response;
				try {
					JSONArray arrayGame = jsonNewGameApp
							.getJSONArray("DataList");
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
						game.setIdCategoryGame(gameItem
								.getInt("IdCategoryGame"));
						game.setHotGame(gameItem.getBoolean("HotGame"));
						game.setNewGame(gameItem.getBoolean("NewGame"));
						game.setFile(gameItem.getString("File"));
						game.setDownload(gameItem.getInt("Download"));
						game.setRate(gameItem.getInt("Rate"));
						game.setVersion(gameItem.getString("Version"));
						game.setSize(gameItem.getString("Size"));
						game.setView(gameItem.getInt("View"));
						game.setFreeGame(gameItem.getBoolean("FreeGame"));
						listNewGameAppItem.add(game);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newGameAppAdapter.notifyDataSetChanged();
				indexNewGameApp++;
				listGame.onLoadMoreComplete();
				listGame.smoothScrollToPosition(newGameAppAdapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}

		};
		ServiceConnection.getListNewGameApp(indexNewGameApp, handler);

	}

	private void loadAppHot() {
		if (listAppItem.size() > 0) {
			return;
		}
		d = ProgressDialog.show(getActivity(), "",
				"Đang tải danh sách app hot...");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((HomeActivity) getActivity()).makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				JSONObject jsonAppHot = response;
				try {
					JSONArray arrayGame = jsonAppHot.getJSONArray("DataList");
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
						game.setIdCategoryGame(gameItem
								.getInt("IdCategoryGame"));
						game.setHotGame(gameItem.getBoolean("HotGame"));
						game.setNewGame(gameItem.getBoolean("NewGame"));
						game.setFile(gameItem.getString("File"));
						game.setDownload(gameItem.getInt("Download"));
						game.setRate(gameItem.getInt("Rate"));
						game.setVersion(gameItem.getString("Version"));
						game.setSize(gameItem.getString("Size"));
						game.setView(gameItem.getInt("View"));
						game.setFreeGame(gameItem.getBoolean("FreeGame"));
						listAppItem.add(game);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				appHotAdapter.notifyDataSetChanged();
				indexAppHot++;
				listGame.onLoadMoreComplete();
				listGame.smoothScrollToPosition(appHotAdapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}

		};
		ServiceConnection.getListAppHot(indexAppHot, handler);
	}

	/*
	 * private class LoadGameHot extends AsyncTask<Integer, Void, Void> {
	 * 
	 * ProgressDialog d; JSONObject jsonGameHot;
	 * 
	 * @Override protected Void doInBackground(Integer... arg0) { jsonGameHot =
	 * ServiceConnection.getListGameHot(arg0[0]); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { try { JSONArray
	 * arrayGame = jsonGameHot.getJSONArray("DataList"); for (int i = 0; i <
	 * arrayGame.length(); i++) { JSONObject gameItem =
	 * arrayGame.getJSONObject(i); Game game = new Game();
	 * game.setId(gameItem.getInt("Id"));
	 * game.setTitle(gameItem.getString("Title"));
	 * game.setIdGroup(gameItem.getInt("IdGroup"));
	 * game.setIdSystem(gameItem.getInt("IdSystem"));
	 * game.setPicture(gameItem.getString("Picture"));
	 * game.setPictureAlbum(gameItem.getString("PictureAlbum"));
	 * game.setDes(gameItem.getString("Des"));
	 * game.setDetail(gameItem.getString("Detail"));
	 * game.setIdCategoryGame(gameItem.getInt("IdCategoryGame"));
	 * game.setHotGame(gameItem.getBoolean("HotGame"));
	 * game.setNewGame(gameItem.getBoolean("NewGame"));
	 * game.setFile(gameItem.getString("File"));
	 * game.setDownload(gameItem.getInt("Download"));
	 * game.setRate(gameItem.getInt("Rate"));
	 * game.setVersion(gameItem.getString("Version"));
	 * game.setSize(gameItem.getString("Size"));
	 * game.setView(gameItem.getInt("View"));
	 * game.setFreeGame(gameItem.getBoolean("FreeGame"));
	 * listGameItem.add(game); } } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * gameHotAdapter.notifyDataSetChanged(); indexGameHot++;
	 * listGame.onLoadMoreComplete();
	 * listGame.smoothScrollToPosition(gameHotAdapter.getCount() - 10); if (d !=
	 * null) { d.dismiss(); d = null; } }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(getActivity(), "", "Đang tải danh sách game hot...");
	 * }
	 * 
	 * }
	 */

	/*
	 * private class LoadAppHot extends AsyncTask<Integer, Void, Void> {
	 * 
	 * ProgressDialog d; JSONObject jsonAppHot;
	 * 
	 * @Override protected Void doInBackground(Integer... params) { jsonAppHot =
	 * ServiceConnection.getListAppHot(indexAppHot); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { try { JSONArray
	 * arrayGame = jsonAppHot.getJSONArray("DataList"); for (int i = 0; i <
	 * arrayGame.length(); i++) { JSONObject gameItem =
	 * arrayGame.getJSONObject(i); Game game = new Game();
	 * game.setId(gameItem.getInt("Id"));
	 * game.setTitle(gameItem.getString("Title"));
	 * game.setIdGroup(gameItem.getInt("IdGroup"));
	 * game.setIdSystem(gameItem.getInt("IdSystem"));
	 * game.setPicture(gameItem.getString("Picture"));
	 * game.setPictureAlbum(gameItem.getString("PictureAlbum"));
	 * game.setDes(gameItem.getString("Des"));
	 * game.setDetail(gameItem.getString("Detail"));
	 * game.setIdCategoryGame(gameItem.getInt("IdCategoryGame"));
	 * game.setHotGame(gameItem.getBoolean("HotGame"));
	 * game.setNewGame(gameItem.getBoolean("NewGame"));
	 * game.setFile(gameItem.getString("File"));
	 * game.setDownload(gameItem.getInt("Download"));
	 * game.setRate(gameItem.getInt("Rate"));
	 * game.setVersion(gameItem.getString("Version"));
	 * game.setSize(gameItem.getString("Size"));
	 * game.setView(gameItem.getInt("View"));
	 * game.setFreeGame(gameItem.getBoolean("FreeGame")); listAppItem.add(game);
	 * } } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * appHotAdapter.notifyDataSetChanged(); indexAppHot++;
	 * listGame.onLoadMoreComplete();
	 * listGame.smoothScrollToPosition(appHotAdapter.getCount() - 10); if (d !=
	 * null) { d.dismiss(); d = null; } }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(getActivity(), "", "Đang tải danh sách game hot...");
	 * }
	 * 
	 * }
	 */

	private void scaleBanner() {
		/*
		 * ivBanner = (ImageView) rootView.findViewById(R.id.banner);
		 * ivBanner.setOnClickListener(this);
		 */
		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.banner_demo);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		DataUtils.SCREEN_WIDTH = width;
		float ratio = ((float) width) / b.getWidth();
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(b,
				(int) (b.getWidth() * ratio), (int) (b.getHeight() * ratio),
				true);
		// ivBanner.setImageBitmap(resizedBitmap);
		listBanner = (Gallery) rootView.findViewById(R.id.lv_banner);
		bannerAdapter = new BannerAdapter(getActivity(), 1, listLinkBanner, listBanner);
		
		listBanner.setAdapter(bannerAdapter);
		listBanner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN && v instanceof ViewGroup){
					((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
		/*listBanner = (HorizontalListView) rootView.findViewById(R.id.lv_banner);
		listBanner.setAdapter(bannerAdapter);
		listBanner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN && v instanceof ViewGroup){
					((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});*/
		
		DisplayUtils.log("Old width:" + b.getWidth() + " - Old height:"
				+ b.getHeight() + " - New width:" + resizedBitmap.getWidth()
				+ " - New height:" + resizedBitmap.getHeight());
		Bitmap bmGameHot = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_game);
		float btRatio = ((float) width) / bmGameHot.getWidth() / 4;
		bmGameHot = Bitmap.createScaledBitmap(bmGameHot,
				(int) (bmGameHot.getWidth() * btRatio),
				(int) (bmGameHot.getHeight() * btRatio), false);
		Bitmap bmApp = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_ung_dung);
		bmApp = Bitmap.createScaledBitmap(bmApp,
				(int) (bmApp.getWidth() * btRatio),
				(int) (bmApp.getHeight() * btRatio), false);
		Bitmap bmNewAppGame = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_game_app_moi);
		bmNewAppGame = Bitmap.createScaledBitmap(bmNewAppGame,
				(int) (bmNewAppGame.getWidth() * btRatio),
				(int) (bmNewAppGame.getHeight() * btRatio), false);
		Bitmap bmEvent = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_qua_event);
		bmEvent = Bitmap.createScaledBitmap(bmEvent,
				(int) (bmEvent.getWidth() * btRatio),
				(int) (bmEvent.getHeight() * btRatio), false);

		ImageView btGameHot = (ImageView) rootView.findViewById(R.id.hot_game);
		btGameHot.setImageBitmap(bmGameHot);
		btGameHot.setOnClickListener(this);
		ImageView btAppHot = (ImageView) rootView.findViewById(R.id.hot_app);
		btAppHot.setImageBitmap(bmApp);
		btAppHot.setOnClickListener(this);
		ImageView btNewGameApp = (ImageView) rootView
				.findViewById(R.id.game_app_new);
		btNewGameApp.setImageBitmap(bmNewAppGame);
		btNewGameApp.setOnClickListener(this);
		ImageView btEventGift = (ImageView) rootView
				.findViewById(R.id.event_gift);
		btEventGift.setImageBitmap(bmEvent);
		btEventGift.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hot_game:
			mode = 1;
			listGame.setAdapter(gameHotAdapter);
			// new LoadGameHot().execute(indexGameHot);
			loadGameHot();
			break;
		case R.id.hot_app:
			mode = 2;
			listGame.setAdapter(appHotAdapter);
			// new LoadAppHot().execute(indexAppHot);
			loadAppHot();
			break;
		case R.id.game_app_new:
			listGame.setAdapter(newGameAppAdapter);
			loadNewGameApp();
			break;
		case R.id.event_gift:
			Intent intent = new Intent(getActivity(), GiftActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(0, 0);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Game game;
		if (mode == 1) {
			game = listGameItem.get(arg2);
		} else {
			game = listAppItem.get(arg2);
		}
		Intent intent = new Intent(getActivity(), GameDetailActivity.class);
		intent.putExtra("game", game);
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(0, 0);
	}

	private void getListBanner() {
		d = ProgressDialog.show(getActivity(), "",
				"Đang tải danh sách game hot...");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((HomeActivity) getActivity()).makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					JSONArray arr = response.getJSONArray("Data");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject bannerItem = arr.getJSONObject(i);
						listLinkBanner.add(bannerItem.getString("Image"));
						
						/*listLinkBanner.add("http://mobile.mho.vn//Upload/ImgSlide/BIET THU2.ES1T.jpg");
						listLinkBanner.add("http://mobile.mho.vn//Upload/ImgSlide/BIET THU2.ES1T.jpg");*/
					}
					bannerAdapter.notifyDataSetChanged();
					Toast.makeText(getActivity(), "So luong anh:"+listLinkBanner.size(), Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
				
				loadGameHot();
			}
		};
		ServiceConnection.getListBanner(handler);
	}

}