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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.adapter.ListTopicAdapter;
import com.maihoangonline.mho.GameDetailActivity;
import com.maihoangonline.mho.ListGameActivity;
import com.maihoangonline.mho.OtherActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;
import com.maihoangonline.models.Topic;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;

@SuppressLint("ValidFragment")
public class TopicFragment extends Fragment implements OnClickListener {

	private static TopicFragment INSTANCE;
	ProgressDialog d;
	private View rootView;
	private LoadMoreListView listNewest;
	private ListView listSuggest, listClassify;

	private ArrayList<Game> listNewestTopic = new ArrayList<Game>();
	private ListGameAdapter listNewestTopicAdapter;
	private int pageIndexNewest = 0;

	private ArrayList<Topic> listTopic = new ArrayList<Topic>();

	private TopicFragment() {

	}

	public static TopicFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TopicFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_topic, null);
		listNewest = (LoadMoreListView) rootView
				.findViewById(R.id.list_newest_topic);
		listNewestTopicAdapter = new ListGameAdapter(getActivity(), 1,
				listNewestTopic);
		listNewest.setAdapter(listNewestTopicAdapter);
		listNewest.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				new LoadNewestTopic().execute();
			}
		});
		listNewest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), GameDetailActivity.class);
				intent.putExtra("game", listNewestTopic.get(arg2));
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		listSuggest = (ListView) rootView
				.findViewById(R.id.list_sugguest_topic);
		listSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), ListGameActivity.class);
				Topic topic = listTopic.get(arg2);
				intent.putExtra("topic", topic);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		listClassify = (ListView) rootView
				.findViewById(R.id.list_classify_topic);
		rootView.findViewById(R.id.newest_topic).setOnClickListener(this);
		rootView.findViewById(R.id.suggest_topic).setOnClickListener(this);
		rootView.findViewById(R.id.classify_topic).setOnClickListener(this);
		if (listNewestTopic.size() == 0) {
			new LoadNewestTopic().execute();
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newest_topic:
			listNewest.setVisibility(View.VISIBLE);
			listSuggest.setVisibility(View.INVISIBLE);
			listClassify.setVisibility(View.INVISIBLE);
			if (listNewestTopic.size() == 0) {
				new LoadNewestTopic().execute();
			}
			break;
		case R.id.suggest_topic:
			listSuggest.setVisibility(View.VISIBLE);
			listNewest.setVisibility(View.INVISIBLE);
			listClassify.setVisibility(View.INVISIBLE);
			if(listTopic.size()==0){
				new GetListTopic().execute();
			}
			break;
		case R.id.classify_topic:
			listClassify.setVisibility(View.VISIBLE);
			listNewest.setVisibility(View.INVISIBLE);
			listSuggest.setVisibility(View.INVISIBLE);
			break;
		}
	}

	private class LoadNewestTopic extends AsyncTask<Void, Void, Void> {

		JSONObject jsonNewest;

		@Override
		protected Void doInBackground(Void... params) {
			jsonNewest = ServiceConnection.getNewestTopic(pageIndexNewest);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arrayGame = jsonNewest.getJSONArray("DataList");
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
					listNewestTopic.add(game);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pageIndexNewest++;
			listNewest.onLoadMoreComplete();
			listNewestTopicAdapter.notifyDataSetChanged();
			listNewest
					.smoothScrollToPosition(listNewestTopicAdapter.getCount() - 10);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải chuyên đề mới nhất");
		}

	}

	private class GetListTopic extends AsyncTask<Void, Void, Void> {

		JSONObject jsonListTopic;

		@Override
		protected Void doInBackground(Void... params) {
			jsonListTopic = ServiceConnection.getListTopic();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray arr = jsonListTopic.getJSONArray("Data");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject jsonItem = arr.getJSONObject(i);
					Topic topic = new Topic();
					topic.setId(jsonItem.getInt("Id"));
					topic.setName(jsonItem.getString("Name"));
					listTopic.add(topic);
					((OtherActivity) getActivity()).getDatabaseHelper()
							.insertTopic(topic);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			new GetListGameInTopic().execute();

		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải danh sách chuyên đề");
			((OtherActivity) getActivity()).getDatabaseHelper().clearTopic();
		}

	}

	private class GetListGameInTopic extends AsyncTask<Void, JSONObject, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < listTopic.size(); i++) {
				JSONObject jsonListGameInTopic = ServiceConnection
						.getListGameInTopic(listTopic.get(i), 0);
				try {
					jsonListGameInTopic
							.put("TopicId", listTopic.get(i).getId());
					jsonListGameInTopic.put("TopicName", listTopic.get(i)
							.getName());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				publishProgress(jsonListGameInTopic);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(JSONObject... values) {
			JSONObject jsonListGame = values[0];
			DisplayUtils.log(jsonListGame.toString());
			try {
				Topic topic = new Topic();
				topic.setId(jsonListGame.getInt("TopicId"));
				topic.setName(jsonListGame.getString("TopicName"));
				JSONArray arrayGame = jsonListGame.getJSONArray("DataList");
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
					((OtherActivity) getActivity()).getDatabaseHelper()
							.insertGameToTopic(game, topic);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			if (d != null) {
				d.dismiss();
				d = null;
			}
			listTopic.clear();
			listTopic = new ArrayList<Topic>();
			listTopic = ((OtherActivity) getActivity()).getDatabaseHelper().getAllTopic();
			ListTopicAdapter adapter = new ListTopicAdapter(getActivity(), 1, listTopic);
			listSuggest.setAdapter(adapter);
		}

	}

}
