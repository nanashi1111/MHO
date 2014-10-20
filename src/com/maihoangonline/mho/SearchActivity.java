package com.maihoangonline.mho;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.ServiceConnection;

public class SearchActivity extends BaseActivity implements
		OnItemClickListener, OnLoadMoreListener, OnClickListener {

	private LoadMoreListView listGame;
	private ArrayList<Game> listGameItem = new ArrayList<Game>();
	private ListGameAdapter adapter;
	private int pageIndex = 0;
	private String querryString;
	private ActionBar bar;
	private ProgressDialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		loadData();
		setupActionBar();
		setupView();
		//new SearchGame().execute(querryString);
		search();
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
		tvTitle.setText("Kết quả tìm kiếm \"" + querryString + "\"");

	}

	private void setupView() {
		listGame = (LoadMoreListView) findViewById(R.id.list_game);
		adapter = new ListGameAdapter(this, 1, listGameItem);
		listGame.setAdapter(adapter);
		listGame.setOnLoadMoreListener(this);
		listGame.setOnItemClickListener(this);
	}

	private void loadData() {
		querryString = getIntent().getStringExtra("querry");
	}

	/*private class SearchGame extends AsyncTask<String, Void, Void> {

		ProgressDialog d;
		JSONObject jsonSearch;

		@Override
		protected Void doInBackground(String... params) {
			jsonSearch = ServiceConnection.search(params[0], pageIndex);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arrayGame = jsonSearch.getJSONArray("Data");
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
					listGameItem.add(game);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			adapter.notifyDataSetChanged();
			pageIndex++;
			listGame.onLoadMoreComplete();
			listGame.smoothScrollToPosition(adapter.getCount() - 20);
			if (d != null) {
				d.dismiss();
				d = null;
			}

		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(SearchActivity.this, "", "Đang tìm");
		}

	}*/
	
	private void search(){
		d = ProgressDialog.show(SearchActivity.this, "", "Đang tìm");
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
				JSONObject jsonSearch = response;
				try {
					JSONArray arrayGame = jsonSearch.getJSONArray("Data");
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
						listGameItem.add(game);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				adapter.notifyDataSetChanged();
				pageIndex++;
				listGame.onLoadMoreComplete();
				listGame.smoothScrollToPosition(adapter.getCount() - 20);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			
			
		};
		
		ServiceConnection.search(querryString, pageIndex, handler);
	}

	@Override
	public void onLoadMore() {
		//new SearchGame().execute(querryString);
		search();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(SearchActivity.this,
				GameDetailActivity.class);
		intent.putExtra("game", listGameItem.get(arg2));
		startActivity(intent);
		overridePendingTransition(0, 0);
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

}
