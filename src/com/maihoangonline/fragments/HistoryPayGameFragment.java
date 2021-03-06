package com.maihoangonline.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListHistoryAdapter;
import com.maihoangonline.mho.ListHistoryActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Transaction;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class HistoryPayGameFragment extends Fragment {

	public static HistoryPayGameFragment INSTANCE;
	private View rootView;
	private ListView lv;
	private ArrayList<Transaction> listPayGame = new ArrayList<Transaction>();
	private ListHistoryAdapter adapter;
	private ProgressDialog d;

	private HistoryPayGameFragment() {

	}

	public static HistoryPayGameFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HistoryPayGameFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_pay_game, null);
		lv = (ListView) rootView.findViewById(R.id.history_pay_game);
		return rootView;
	}

	private void getHistory() {
		DisplayUtils.log("PARAMS=3");
		listPayGame.clear();
		d = ProgressDialog.show(getActivity(), "",
				"Đang lấy lịch sử giao dịch...");
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				((ListHistoryActivity) getActivity()).makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				if (d != null) {
					d.dismiss();
					d = null;
				}
				// makeToast(response.toString());
				DisplayUtils.log("History:" + response.toString());
				// parse json
				try {
					JSONObject jsonHistory = new JSONObject(response.toString()
							.replace("History:", ""));
					JSONArray arr = jsonHistory.getJSONArray("ListItems");
					for (int i = 0; i < arr.length(); i++) {
						listPayGame.add(new Transaction(arr.getJSONObject(i)
								.getString("Description"), arr.getJSONObject(i)
								.getString("TransactionDate")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*if (adapter == null) {
					adapter = new ListHistoryAdapter(getActivity(), 1,
							listPayGame);
					lv.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}*/
				adapter = new ListHistoryAdapter(getActivity(), 1,
						listPayGame);
				lv.setAdapter(adapter);

				super.onSuccess(statusCode, headers, response);
			}

		};
		ServiceConnection.getHistoryTransaction(
				SharePreferenceUtils.getAccID(getActivity()), 3, 0, handler);
	}
	
	public void loadFirstTime() {
		
			//new GetListNews().execute();
			getHistory();
		
	}
	
	/*@Override
	public void onResume() {
		super.onResume();
		if(adapter!=null){
			lv.setAdapter(adapter);
		}
	}*/

}
