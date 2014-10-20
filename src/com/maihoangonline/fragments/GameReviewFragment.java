package com.maihoangonline.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.maihoangonline.adapter.ListNewsAdapter;
import com.maihoangonline.mho.NewsActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.News;
import com.maihoangonline.utils.ServiceConnection;

@SuppressLint("ValidFragment")
public class GameReviewFragment extends Fragment implements
		OnItemClickListener, OnLoadMoreListener {

	private View rootView;
	private LoadMoreListView listGameReview;
	private int pageIndex = 0;
	private ArrayList<News> listNews = new ArrayList<News>();
	private ListNewsAdapter adapter;
	public static GameReviewFragment INSTANCE;

	private GameReviewFragment() {

	}

	public static GameReviewFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GameReviewFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_game_review, null);
		listGameReview = (LoadMoreListView) rootView
				.findViewById(R.id.list_game_review);
		listGameReview.setOnItemClickListener(this);
		listGameReview.setOnLoadMoreListener(this);
		adapter = new ListNewsAdapter(getActivity(), 1, listNews);
		listGameReview.setAdapter(adapter);

		return rootView;
	}

	public void loadNewsFirstTime() {
		if (listNews.size() == 0) {
			new GetListGameReview().execute();
		}
	}

	private class GetListGameReview extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		JSONObject jsonListGameReview;

		@Override
		protected Void doInBackground(Void... params) {
			jsonListGameReview = ServiceConnection.getListGameReview(pageIndex);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pageIndex++;
			try {
				JSONArray arr = jsonListGameReview.getJSONArray("DataList");
				for (int i = 0; i < arr.length(); i++) {
					News news = new News();
					JSONObject jsonNewsItem = arr.getJSONObject(i);
					news.setID(jsonNewsItem.getInt("Id"));
					news.setTitle(jsonNewsItem.getString("Title"));
					news.setLead(jsonNewsItem.getString("Lead"));
					news.setContent(jsonNewsItem.getString("Content"));
					news.setImagePath("http://"
							+ jsonNewsItem.getString("ImagePath"));
					news.setCategoryID(jsonNewsItem.getInt("CategoryID"));
					news.setCategoryName(jsonNewsItem.getString("CategoryName"));
					listNews.add(news);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
			listGameReview.onLoadMoreComplete();
			listGameReview.smoothScrollToPosition(adapter.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải danh sách đánh giá game");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(), NewsActivity.class);
		intent.putExtra("news", listNews.get(arg2));
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(0, 0);
	}

	@Override
	public void onLoadMore() {
		new GetListGameReview().execute();
	}
}
