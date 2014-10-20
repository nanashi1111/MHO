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
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.models.Category;
import com.maihoangonline.models.Game;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.models.Topic;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

public class ListGameActivity extends BaseActivity implements OnClickListener {

	private LoadMoreListView listGame;
	private int pageIndex = 0;
	private ArrayList<Game> listGameItem = new ArrayList<Game>();
	private Category cat;
	private Topic topic;
	private MHOThread thread;
	private ListGameAdapter adapter;
	private boolean favorite = false;
	private ActionBar bar;
	private ProgressDialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cat = (Category) getIntent().getSerializableExtra("category");
		topic = (Topic) getIntent().getSerializableExtra("topic");
		thread = (MHOThread)getIntent().getSerializableExtra("thread");
		favorite = getIntent().getBooleanExtra("favorite", false);
		setContentView(R.layout.activity_list_game);
		setupActionBar();
		setupView();
		if (cat != null) {
			//new GetListGameInCategory().execute(cat);
			getListGameInCategory(cat, pageIndex);
		}
		if (topic != null) {
			//new GetListGameInTopic().execute(topic);
			getListGameInTopic(topic, pageIndex);
		}
		if(thread!=null){
			listGameItem = dbh.getListGameInThread(thread);
			adapter = new ListGameAdapter(this, 1, listGameItem);
			listGame.setAdapter(adapter);
		}
		if(favorite){
			listGameItem = dbh.getListFavoriteGame(SharePreferenceUtils.getAccEmail(this));
			adapter = new ListGameAdapter(this, 1, listGameItem);
			listGame.setAdapter(adapter);
		}
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
		if (cat != null) {
			tvTitle.setText(cat.getName());
		}
		if (topic != null) {
			tvTitle.setText(topic.getName());
		}
		if(thread!=null){
			tvTitle.setText("BỘ SƯU TẬP");
		}
		if(favorite){
			tvTitle.setText("YÊU THÍCH");
		}

	}

	private void setupView() {
		listGame = (LoadMoreListView) findViewById(R.id.list_game);
		listGame.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				if (cat != null) {
					//new GetListGameInCategory().execute(cat);
					getListGameInCategory(cat, pageIndex);
				}
				if (topic != null) {
					//new GetListGameInTopic().execute(topic);
					getListGameInTopic(topic, pageIndex);
				}
			}
		});
		adapter = new ListGameAdapter(this, 1, listGameItem);
		listGame.setAdapter(adapter);
		listGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ListGameActivity.this,
						GameDetailActivity.class);
				intent.putExtra("game", listGameItem.get(arg2));
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
	}

	/*private class GetListGameInCategory extends AsyncTask<Category, Void, Void> {

		ProgressDialog d;
		JSONObject jsonGame;

		@Override
		protected Void doInBackground(Category... params) {
			jsonGame = ServiceConnection.getListGameInCategory(params[0],
					pageIndex);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pageIndex++;
			try {
				JSONArray arr = jsonGame.getJSONArray("DataList");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject gameItem = arr.getJSONObject(i);
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
				e.printStackTrace();
			}
			listGame.onLoadMoreComplete();
			adapter.notifyDataSetChanged();
			listGame.smoothScrollToPosition(adapter.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(ListGameActivity.this, "",
					"Đang tải danh sách game");
		}

	}*/
	
	private void getListGameInCategory(Category cat, int pageIndex){
		d = ProgressDialog.show(this, "",
				"Đang tải danh sách game...");
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
				JSONObject jsonGame = response;
				ListGameActivity.this.pageIndex++;
				try {
					JSONArray arr = jsonGame.getJSONArray("DataList");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject gameItem = arr.getJSONObject(i);
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
					e.printStackTrace();
				}
				listGame.onLoadMoreComplete();
				adapter.notifyDataSetChanged();
				listGame.smoothScrollToPosition(adapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		};
		ServiceConnection.getListGameInCategory(cat, pageIndex, handler);
	}

	/*private class GetListGameInTopic extends AsyncTask<Topic, Void, Void> {
		ProgressDialog d;
		JSONObject jsonListTopicGame;

		@Override
		protected Void doInBackground(Topic... params) {
			jsonListTopicGame = ServiceConnection.getListGameInTopic(params[0],
					pageIndex);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arrayGame = jsonListTopicGame
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
				e.printStackTrace();
			}
			pageIndex++;
			listGame.onLoadMoreComplete();
			adapter.notifyDataSetChanged();
			listGame.smoothScrollToPosition(adapter.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(ListGameActivity.this, "",
					"Đang tải danh sách game");
		}

	}*/
	
	private void getListGameInTopic(Topic topic, int pageIndex){
		d = ProgressDialog.show(this, "",
				"Đang tải danh sách game...");
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
				JSONObject jsonListTopicGame = response;
				try {
					JSONArray arrayGame = jsonListTopicGame
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
					e.printStackTrace();
				}
				ListGameActivity.this.pageIndex++;
				listGame.onLoadMoreComplete();
				adapter.notifyDataSetChanged();
				listGame.smoothScrollToPosition(adapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		};
		ServiceConnection.getListGameInTopic(topic, pageIndex, handler);
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
