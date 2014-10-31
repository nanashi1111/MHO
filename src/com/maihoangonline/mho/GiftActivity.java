package com.maihoangonline.mho;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.costum.android.widget.LoadMoreListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;

public class GiftActivity extends BaseActivity implements OnClickListener {

	private ImageView btGeneralGift, btMyGift;
	private LoadMoreListView lvGift;
	private ArrayList<Game> listGameGift = new ArrayList<Game>();
	private ListGameAdapter giftAdapter;
	private ActionBar bar;
	private ProgressDialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift);
		setupActionBar();
		setupView();
		//new GetPublicGift().execute();
		getPublicGift();
	}

	private void setupView() {
		ImageView banner = (ImageView) findViewById(R.id.banner);
		Bitmap bmBanner = BitmapFactory.decodeResource(getResources(),
				R.drawable.banner_demo);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		DataUtils.SCREEN_WIDTH = width;
		float ratio =(float) width / bmBanner.getWidth();
		DisplayUtils.log("ratio = "+ratio);
		banner.setImageBitmap(Bitmap.createScaledBitmap(bmBanner,
				(int) (ratio * bmBanner.getWidth()),
				(int) (ratio * bmBanner.getHeight()),true));

		btGeneralGift = (ImageView) findViewById(R.id.general_gift);
		Bitmap bmGeneralGift = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_gift_all);
		Bitmap bmGeneralGiftScaled = DisplayUtils.scaleBitmap(bmGeneralGift,
				width / 2.0F / bmGeneralGift.getWidth());
		btGeneralGift.setImageBitmap(bmGeneralGiftScaled);
		btGeneralGift.setOnClickListener(this);

		btMyGift = (ImageView) findViewById(R.id.my_gift);
		Bitmap bmMyGift = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_my_gift);
		Bitmap bmMyGiftScaled = DisplayUtils.scaleBitmap(bmMyGift,
				width / 2.0F / bmMyGift.getWidth());
		btMyGift.setImageBitmap(bmMyGiftScaled);
		btMyGift.setOnClickListener(this);

		lvGift = (LoadMoreListView) findViewById(R.id.list_gift);
		giftAdapter = new ListGameAdapter(this, 1, listGameGift);
		lvGift.setAdapter(giftAdapter);
		lvGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(GiftActivity.this, GetGiftActivity.class);
				intent.putExtra("game", listGameGift.get(arg2));
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});

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
	
	private void getPublicGift(){
		d = ProgressDialog.show(GiftActivity.this, "",
				"Đang tải danh sách quà tặng");
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
				super.onSuccess(statusCode, headers, response);
				JSONObject jsonListGift = response;
				try {
					JSONArray arrayGame = jsonListGift.getJSONArray("DataList");
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
						listGameGift.add(game);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				giftAdapter.notifyDataSetChanged();
				lvGift.onLoadMoreComplete();
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}
			
		};
		ServiceConnection.getPublicGift(handler);
	}

	/*private class GetPublicGift extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		JSONObject jsonListGift;

		@Override
		protected Void doInBackground(Void... params) {
			jsonListGift = ServiceConnection.getPublicGift();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arrayGame = jsonListGift.getJSONArray("DataList");
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
					listGameGift.add(game);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			giftAdapter.notifyDataSetChanged();
			lvGift.onLoadMoreComplete();
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(GiftActivity.this, "",
					"Đang tải danh sách quà tặng");
		}

	}*/
}
