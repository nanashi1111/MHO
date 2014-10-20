package com.maihoangonline.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListNewsAdapter;
import com.maihoangonline.mho.HomeActivity;
import com.maihoangonline.mho.NewsActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.News;
import com.maihoangonline.utils.ServiceConnection;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class NewsFragment extends Fragment implements OnItemClickListener,
		OnLoadMoreListener {

	public static NewsFragment INSTANCE;
	private View rootView;
	private LoadMoreListView listNews;
	private int pageIndex = 0;
	private ArrayList<News> listNewsItem = new ArrayList<News>();
	private ListNewsAdapter adapter;
	private ProgressDialog d;

	private NewsFragment() {

	}

	public static NewsFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NewsFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_news, null);
		listNews = (LoadMoreListView) rootView.findViewById(R.id.list_news);
		listNews.setOnItemClickListener(this);
		listNews.setOnLoadMoreListener(this);
		adapter = new ListNewsAdapter(getActivity(), 1, listNewsItem);
		listNews.setAdapter(adapter);
		return rootView;
	}

	public void loadNewsFirstTime() {
		if (listNewsItem.size() == 0) {
			//new GetListNews().execute();
			getListNews(pageIndex);
		}
	}

	/*private class GetListNews extends AsyncTask<Void, Void, Void> {

		ProgressDialog d;
		JSONObject jsonListNews;

		@Override
		protected Void doInBackground(Void... params) {
			jsonListNews = ServiceConnection.getListNesw(pageIndex);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pageIndex++;
			try {
				JSONArray arr = jsonListNews.getJSONArray("DataList");
				for (int i = 0; i < arr.length(); i++) {
					News news = new News();
					JSONObject jsonNewsItem = arr.getJSONObject(i);
					news.setID(jsonNewsItem.getInt("ID"));
					news.setTitle(jsonNewsItem.getString("Title"));
					news.setLead(jsonNewsItem.getString("Lead"));
					news.setContent(jsonNewsItem.getString("Content"));
					news.setImagePath(editImagePath(jsonNewsItem
							.getString("ImagePath")));
					news.setSmallImagePath(jsonNewsItem
							.getString("SmallImagePath"));
					news.setCreator(jsonNewsItem.getString("Creator"));
					news.setTotalView(jsonNewsItem.getInt("TotalView"));
					news.setTotalComment(jsonNewsItem.getInt("TotalComment"));
					news.setCategoryID(jsonNewsItem.getInt("CategoryID"));
					news.setAuthor(jsonNewsItem.getString("Author"));
					news.setSource(jsonNewsItem.getString("Source"));
					news.setEnableComment(jsonNewsItem
							.getBoolean("EnableComment"));
					news.setKeyword(jsonNewsItem.getString("Keyword"));
					news.setAttachment(jsonNewsItem.getString("Attachment"));
					news.setExpriseDate(jsonNewsItem.getString("ExpriseDate"));
					news.setImageComment(jsonNewsItem.getString("ImageComment"));
					news.setCategoryName(jsonNewsItem.getString("CategoryName"));
					news.setPublishDate(jsonNewsItem.getString("PublishDate"));
					news.setType(jsonNewsItem.getInt("Type"));
					news.setNewsIdRelate(jsonNewsItem.getString("NewsIdRelate"));
					news.setGameID(jsonNewsItem.getInt("GameID"));
					news.setGameName(jsonNewsItem.getString("GameName"));
					news.setUrlDetail(jsonNewsItem.getString("UrlDetail"));
					listNewsItem.add(news);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
			listNews.onLoadMoreComplete();
			listNews.smoothScrollToPosition(adapter.getCount() - 20);
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải danh sách tin tức");
		}

	}*/
	
	private void getListNews(int pageIndex){
		d = ProgressDialog.show(getActivity(), "",
				"Đang tải danh sách tin tức");
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
				JSONObject jsonListNews = response;
				NewsFragment.this.pageIndex++;
				try {
					JSONArray arr = jsonListNews.getJSONArray("DataList");
					for (int i = 0; i < arr.length(); i++) {
						News news = new News();
						JSONObject jsonNewsItem = arr.getJSONObject(i);
						news.setID(jsonNewsItem.getInt("ID"));
						news.setTitle(jsonNewsItem.getString("Title"));
						news.setLead(jsonNewsItem.getString("Lead"));
						news.setContent(jsonNewsItem.getString("Content"));
						news.setImagePath(editImagePath(jsonNewsItem
								.getString("ImagePath")));
						news.setSmallImagePath(jsonNewsItem
								.getString("SmallImagePath"));
						news.setCreator(jsonNewsItem.getString("Creator"));
						news.setTotalView(jsonNewsItem.getInt("TotalView"));
						news.setTotalComment(jsonNewsItem.getInt("TotalComment"));
						news.setCategoryID(jsonNewsItem.getInt("CategoryID"));
						news.setAuthor(jsonNewsItem.getString("Author"));
						news.setSource(jsonNewsItem.getString("Source"));
						news.setEnableComment(jsonNewsItem
								.getBoolean("EnableComment"));
						news.setKeyword(jsonNewsItem.getString("Keyword"));
						news.setAttachment(jsonNewsItem.getString("Attachment"));
						news.setExpriseDate(jsonNewsItem.getString("ExpriseDate"));
						news.setImageComment(jsonNewsItem.getString("ImageComment"));
						news.setCategoryName(jsonNewsItem.getString("CategoryName"));
						news.setPublishDate(jsonNewsItem.getString("PublishDate"));
						news.setType(jsonNewsItem.getInt("Type"));
						news.setNewsIdRelate(jsonNewsItem.getString("NewsIdRelate"));
						news.setGameID(jsonNewsItem.getInt("GameID"));
						news.setGameName(jsonNewsItem.getString("GameName"));
						news.setUrlDetail(jsonNewsItem.getString("UrlDetail"));
						listNewsItem.add(news);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
				listNews.onLoadMoreComplete();
				listNews.smoothScrollToPosition(adapter.getCount() - 20);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}
			
			
		};
		
		ServiceConnection.getListNews(pageIndex, handler);
		
		
	}

	@Override
	public void onLoadMore() {
		//new GetListNews().execute();
		getListNews(pageIndex);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(), NewsActivity.class);
		intent.putExtra("news", listNewsItem.get(arg2));
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(0, 0);
	}

	private String editImagePath(String link) {
		StringBuilder sb = new StringBuilder(link);
		sb.insert(new String("http://farm.mho.vn/").length(),
				"srv_thumb.ashx?w=316&h=162&name=");
		return sb.toString();
	}

}
