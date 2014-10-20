package com.maihoangonline.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.mho.GameDetailActivity;
import com.maihoangonline.mho.HomeActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.ServiceConnection;

@SuppressLint("ValidFragment")
public class RankFragment extends Fragment implements OnClickListener {

	private View rootView;
	private static RankFragment INSTANCE;
	private ProgressDialog d;
	private LoadMoreListView listRank;
	private LinearLayout btRankGame, btRankApp;
	private ArrayList<Game> listRankGame = new ArrayList<Game>();
	private ArrayList<Game> listRankApp = new ArrayList<Game>();
	private int pageIndexGame = 0;
	private int pageIndexApp = 0;
	int typeApp = 1;// 1=game 2=app

	private RankFragment() {

	}

	public static RankFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RankFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_rank, null);
		listRank = (LoadMoreListView) rootView.findViewById(R.id.list_top);
		listRank.setAdapter(new ListGameAdapter(getActivity(), 1, listRankGame));
		listRank.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				//new LoadRank().execute(typeApp);
				getRank();
			}
		});
		listRank.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						GameDetailActivity.class);
				if (typeApp == 1) {
					intent.putExtra("game", listRankGame.get(arg2));
				} else {
					intent.putExtra("game", listRankApp.get(arg2));
				}
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		if (listRankGame.size() == 0) {
			//new LoadRank().execute(1);
			getRank();
		}
		btRankGame = (LinearLayout) rootView.findViewById(R.id.top_game);
		btRankGame.setOnClickListener(this);
		btRankApp = (LinearLayout) rootView.findViewById(R.id.top_app);
		btRankApp.setOnClickListener(this);
		return rootView;
	}
	/*
	private class LoadRank extends AsyncTask<Integer, Void, Void> {

		ProgressDialog d;

		JSONObject jsonRank;

		@Override
		protected Void doInBackground(Integer... params) {
			typeApp = params[0];
			if (typeApp == 1) {
				jsonRank = ServiceConnection.getRankGame(pageIndexGame);
			} else {
				jsonRank = ServiceConnection.getRankApp(pageIndexApp);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (typeApp == 1) {
				pageIndexGame++;
			} else {
				pageIndexApp++;
			}
			try {
				JSONArray rankArr = jsonRank.getJSONArray("DataList");
				for (int i = 0; i < rankArr.length(); i++) {
					JSONObject rankObject = rankArr.getJSONObject(i);
					Game game = new Game();
					game.setId(rankObject.getInt("Id"));
					game.setTitle(rankObject.getString("Title"));
					game.setIdGroup(rankObject.getInt("IdGroup"));
					game.setIdSystem(rankObject.getInt("IdSystem"));
					game.setPicture(rankObject.getString("Picture"));
					game.setPictureAlbum(rankObject.getString("PictureAlbum"));
					game.setDes(rankObject.getString("Des"));
					game.setDetail(rankObject.getString("Detail"));
					game.setIdCategoryGame(rankObject.getInt("IdCategoryGame"));
					game.setHotGame(rankObject.getBoolean("HotGame"));
					game.setNewGame(rankObject.getBoolean("NewGame"));
					game.setFile(rankObject.getString("File"));
					game.setDownload(rankObject.getInt("Download"));
					game.setRate(rankObject.getInt("Rate"));
					game.setVersion(rankObject.getString("Version"));
					game.setSize(rankObject.getString("Size"));
					game.setView(rankObject.getInt("View"));
					game.setFreeGame(rankObject.getBoolean("FreeGame"));
					if (typeApp == 1) {
						listRankGame.add(game);
					} else {
						listRankApp.add(game);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ListGameAdapter adapter;
			if (typeApp == 1) {
				adapter = new ListGameAdapter(getActivity(), 1, listRankGame);
			} else {
				adapter = new ListGameAdapter(getActivity(), 1, listRankApp);
			}
			listRank.onLoadMoreComplete();
			listRank.setAdapter(adapter);
			listRank.smoothScrollToPosition(adapter.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "", "Đang tải");
		}

	}*/
	
	private void getRank(){
		d = ProgressDialog.show(getActivity(), "", "Đang tải");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((HomeActivity)getActivity()).makeToast("Lỗi kết nối");
				if(d!=null){
					d.dismiss();
					d=null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject jsonRank = response;
				if (typeApp == 1) {
					pageIndexGame++;
				} else {
					pageIndexApp++;
				}
				try {
					JSONArray rankArr = jsonRank.getJSONArray("DataList");
					for (int i = 0; i < rankArr.length(); i++) {
						JSONObject rankObject = rankArr.getJSONObject(i);
						Game game = new Game();
						game.setId(rankObject.getInt("Id"));
						game.setTitle(rankObject.getString("Title"));
						game.setIdGroup(rankObject.getInt("IdGroup"));
						game.setIdSystem(rankObject.getInt("IdSystem"));
						game.setPicture(rankObject.getString("Picture"));
						game.setPictureAlbum(rankObject.getString("PictureAlbum"));
						game.setDes(rankObject.getString("Des"));
						game.setDetail(rankObject.getString("Detail"));
						game.setIdCategoryGame(rankObject.getInt("IdCategoryGame"));
						game.setHotGame(rankObject.getBoolean("HotGame"));
						game.setNewGame(rankObject.getBoolean("NewGame"));
						game.setFile(rankObject.getString("File"));
						game.setDownload(rankObject.getInt("Download"));
						game.setRate(rankObject.getInt("Rate"));
						game.setVersion(rankObject.getString("Version"));
						game.setSize(rankObject.getString("Size"));
						game.setView(rankObject.getInt("View"));
						game.setFreeGame(rankObject.getBoolean("FreeGame"));
						if (typeApp == 1) {
							listRankGame.add(game);
						} else {
							listRankApp.add(game);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ListGameAdapter adapter;
				if (typeApp == 1) {
					adapter = new ListGameAdapter(getActivity(), 1, listRankGame);
				} else {
					adapter = new ListGameAdapter(getActivity(), 1, listRankApp);
				}
				listRank.onLoadMoreComplete();
				listRank.setAdapter(adapter);
				listRank.smoothScrollToPosition(adapter.getCount() - 10);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			
			
		};
		if(typeApp == 1){
			ServiceConnection.getRankGame(pageIndexGame, handler);
		}else if(typeApp == 2){
			ServiceConnection.getRankApp(pageIndexApp, handler);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_game:
			typeApp = 1;
			if (listRankGame.size() == 0) {
				//new LoadRank().execute(typeApp);
				getRank();
			} else {
				listRank.setAdapter(new ListGameAdapter(getActivity(), 1,
						listRankGame));
			}
			break;
		case R.id.top_app:
			typeApp = 2;
			if (listRankApp.size() == 0) {
				//new LoadRank().execute(typeApp);
				getRank();
			} else {
				listRank.setAdapter(new ListGameAdapter(getActivity(), 1,
						listRankApp));
			}
			break;
		}
	}
}
