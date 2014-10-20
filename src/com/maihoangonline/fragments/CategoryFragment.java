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
import android.widget.GridView;
import android.widget.ImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListCategoryAdapter;
import com.maihoangonline.mho.ListGameActivity;
import com.maihoangonline.mho.OtherActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Category;
import com.maihoangonline.utils.ServiceConnection;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements OnClickListener {

	private static CategoryFragment INSTANCE;
	private View rootView;
	private ImageView ivCategoryGame, ivCategoryApp, ivCategoryGameOnline;
	private GridView listCategory;
	private ArrayList<Category> listCategoryGame = new ArrayList<Category>();
	private ArrayList<Category> listCategoryApp = new ArrayList<Category>();
	private ArrayList<Category> listCategoryGameOnline = new ArrayList<Category>();
	private ProgressDialog d;

	private CategoryFragment() {

	}

	public static CategoryFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CategoryFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_category, null);
		ivCategoryGame = (ImageView) rootView.findViewById(R.id.category_game);
		ivCategoryGame.setOnClickListener(this);
		ivCategoryApp = (ImageView) rootView.findViewById(R.id.category_app);
		ivCategoryApp.setOnClickListener(this);
		ivCategoryGameOnline = (ImageView) rootView
				.findViewById(R.id.category_game_online);
		ivCategoryGameOnline.setOnClickListener(this);
		listCategory = (GridView) rootView.findViewById(R.id.list_category);
		if (listCategoryGame.size() == 0) {
			//new GetListCategory().execute(2);
			getListCategory(2);
		}
		listCategory.setAdapter(new ListCategoryAdapter(getActivity(),
				1, listCategoryGame));
		listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), ListGameActivity.class);
				Category cat = ((ListCategoryAdapter)listCategory.getAdapter()).getArrayList().get(arg2);
				intent.putExtra("category", cat);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		return rootView;
	}

	/*private class GetListCategory extends AsyncTask<Integer, Void, Void> {

		ProgressDialog d;
		int categoryType; // 1:App 2:Game 3:GameOnline
		JSONObject jsonCategory;

		@Override
		protected Void doInBackground(Integer... params) {
			categoryType = params[0];
			if (categoryType == 1) {
				jsonCategory = ServiceConnection.getListCategoryApp();
			} else if (categoryType == 2) {
				jsonCategory = ServiceConnection.getListCategoryGame();
			} else if (categoryType == 3) {
				jsonCategory = ServiceConnection.getListCategoryGameOnline();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray categoryArr = jsonCategory.getJSONArray("Data");
				for (int i = 0; i < categoryArr.length(); i++) {
					JSONObject categoryItem = categoryArr.getJSONObject(i);
					Category cat = new Category();
					cat.setId(categoryItem.getInt("Id"));
					cat.setName(categoryItem.getString("Name"));
					cat.setDescription(categoryItem.getString("Description"));
					cat.setBackround(categoryItem.getString("Backround"));
					cat.setImage(categoryItem.getString("Image"));
					cat.setView(categoryItem.getString("View"));
					cat.setSoLuong(categoryItem.getString("SoLuong"));
					cat.setStatus(categoryItem.getBoolean("Status"));
					if (categoryType == 1) {
						listCategoryApp.add(cat);
					} else if (categoryType == 2) {
						listCategoryGame.add(cat);
					} else if (categoryType == 3) {
						listCategoryGameOnline.add(cat);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ListCategoryAdapter adapter;
			if (categoryType == 1) {
				adapter = new ListCategoryAdapter(getActivity(), 1,
						listCategoryApp);
			} else if (categoryType == 2) {
				adapter = new ListCategoryAdapter(getActivity(), 1,
						listCategoryGame);
			} else {
				adapter = new ListCategoryAdapter(getActivity(), 1,
						listCategoryGameOnline);
			}
			listCategory.setAdapter(adapter);
			if (d != null) {
				d.dismiss();
				d = null;
			}

		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(getActivity(), "",
					"Đang tải danh sách thể loại...");
		}

	}*/
	
	private void getListCategory(final int type){
		d = ProgressDialog.show(getActivity(), "",
				"Đang tải danh sách thể loại...");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((OtherActivity)getActivity()).makeToast("Lỗi kết nối");
				if(d!=null){
					d.dismiss();
					d=null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject jsonCategory = response;
				try {
					JSONArray categoryArr = jsonCategory.getJSONArray("Data");
					for (int i = 0; i < categoryArr.length(); i++) {
						JSONObject categoryItem = categoryArr.getJSONObject(i);
						Category cat = new Category();
						cat.setId(categoryItem.getInt("Id"));
						cat.setName(categoryItem.getString("Name"));
						cat.setDescription(categoryItem.getString("Description"));
						cat.setBackround(categoryItem.getString("Backround"));
						cat.setImage(categoryItem.getString("Image"));
						cat.setView(categoryItem.getString("View"));
						cat.setSoLuong(categoryItem.getString("SoLuong"));
						cat.setStatus(categoryItem.getBoolean("Status"));
						if (type == 1) {
							listCategoryApp.add(cat);
						} else if (type == 2) {
							listCategoryGame.add(cat);
						} else if (type == 3) {
							listCategoryGameOnline.add(cat);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ListCategoryAdapter adapter;
				if (type == 1) {
					adapter = new ListCategoryAdapter(getActivity(), 1,
							listCategoryApp);
				} else if (type == 2) {
					adapter = new ListCategoryAdapter(getActivity(), 1,
							listCategoryGame);
				} else {
					adapter = new ListCategoryAdapter(getActivity(), 1,
							listCategoryGameOnline);
				}
				listCategory.setAdapter(adapter);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onSuccess(statusCode, headers, response);
			}

			
		};
		if(type==1){
			ServiceConnection.getListCategoryApp(handler);
		}else if(type==2){
			ServiceConnection.getListCategoryGame(handler);
		}else{
			ServiceConnection.getListCategoryGameOnline(handler);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.category_app:
			if (listCategoryApp.size() == 0) {
				//new GetListCategory().execute(1);
				getListCategory(1);
			} else {
				listCategory.setAdapter(new ListCategoryAdapter(getActivity(),
						1, listCategoryApp));
			}
			break;
		case R.id.category_game:
			if (listCategoryGame.size() == 0) {
				//new GetListCategory().execute(2);
				getListCategory(2);
			} else {
				listCategory.setAdapter(new ListCategoryAdapter(getActivity(),
						1, listCategoryGame));
			}
			break;
		case R.id.category_game_online:
			if (listCategoryGameOnline.size() == 0) {
				//new GetListCategory().execute(3);
				getListCategory(3);
			} else {
				listCategory.setAdapter(new ListCategoryAdapter(getActivity(),
						1, listCategoryGameOnline));
			}
			break;
		}
	}

}
