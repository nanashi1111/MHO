package com.maihoangonline.mho;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListThreadDetailAdapter;
import com.maihoangonline.models.Game;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

public class ListThreadActivity extends BaseActivity implements OnClickListener {

	private ListView listThread;
	private TextView tvNumberThread;
	private ImageView btCreateThread;
	private ArrayList<MHOThread> listThreadItem = new ArrayList<MHOThread>();
	private ListThreadDetailAdapter adapter;
	private ActionBar bar;
	private ProgressDialog d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_thread);
		setupActionBar();
		setupView();

		// new LoadUserThread().execute();
		loadUserThread();
	}

	private void setupView() {
		listThread = (ListView) findViewById(R.id.list_thread);
		listThread
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(ListThreadActivity.this,
								ListGameActivity.class);
						intent.putExtra("thread", listThreadItem.get(arg2));
						startActivity(intent);
						overridePendingTransition(0, 0);
					}
				});
		tvNumberThread = (TextView) findViewById(R.id.number_thread);
		btCreateThread = (ImageView) findViewById(R.id.create_thread);
		Bitmap bmCreate = BitmapFactory.decodeResource(getResources(),
				R.drawable.export_android_418);
		Bitmap bmCreateResized = DisplayUtils.scaleBitmap(bmCreate, 1.2F
				* (DataUtils.SCREEN_WIDTH / 3) / bmCreate.getWidth());
		this.btCreateThread.setImageBitmap(bmCreateResized);
		btCreateThread.setOnClickListener(this);
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
		tvTitle.setText("BỘ SƯU TẬP");
	}

	/*
	 * private class LoadUserThread extends AsyncTask<Void, Void, Void> {
	 * 
	 * JSONObject jsonListThread;
	 * 
	 * @Override protected Void doInBackground(Void... params) { jsonListThread
	 * = ServiceConnection .getListThread(SharePreferenceUtils
	 * .getAccEmail(ListThreadActivity.this)); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { try { JSONArray arr
	 * = jsonListThread.getJSONArray("DataList"); for (int i = 0; i <
	 * arr.length(); i++) { JSONObject threadItem = arr.getJSONObject(i);
	 * MHOThread thread = new MHOThread();
	 * thread.setId(threadItem.getInt("Id"));
	 * thread.setName(threadItem.getString("Name"));
	 * thread.setUser(threadItem.getString("User"));
	 * thread.setDes(threadItem.getString("Des"));
	 * thread.setIdSystem(threadItem.getInt("IdSystem"));
	 * thread.setViews(threadItem.getInt("Views")); listThreadItem.add(thread);
	 * dbh.insertThread(thread); } } catch (JSONException e) {
	 * e.printStackTrace(); } new GetGameInThread().execute(); }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(ListThreadActivity.this, "", "Đang tải bộ sưu tập");
	 * dbh.clearThread(); }
	 * 
	 * }
	 */

	private void loadUserThread() {
		d = ProgressDialog.show(ListThreadActivity.this, "",
				"Đang tải bộ sưu tập");
		dbh.clearThread();
		listThreadItem.clear();
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONObject jsonListThread = response;
				try {
					JSONArray arr = jsonListThread.getJSONArray("DataList");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject threadItem = arr.getJSONObject(i);
						MHOThread thread = new MHOThread();
						thread.setId(threadItem.getInt("Id"));
						thread.setName(threadItem.getString("Name"));
						thread.setUser(threadItem.getString("User"));
						thread.setDes(threadItem.getString("Des"));
						thread.setIdSystem(threadItem.getInt("IdSystem"));
						thread.setViews(threadItem.getInt("Views"));
						listThreadItem.add(thread);
						dbh.insertThread(thread);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				new GetGameInThread().execute();
				super.onSuccess(statusCode, headers, response);
			}

		};

		ServiceConnection.getListThread(SharePreferenceUtils.getAccEmail(this),
				handler);
	}

	private class GetGameInThread extends AsyncTask<Void, JSONObject, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < listThreadItem.size(); i++) {
				JSONObject jsonListGameInThread = ServiceConnection
						.getGameInThread(listThreadItem.get(i));
				try {
					jsonListGameInThread.put("ThreadId", listThreadItem.get(i)
							.getId());
					jsonListGameInThread.put("ThreadName", listThreadItem
							.get(i).getName());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				publishProgress(jsonListGameInThread);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			listThreadItem.clear();
			listThreadItem = dbh.getAllThread();
			adapter = new ListThreadDetailAdapter(ListThreadActivity.this, 1,
					listThreadItem);
			listThread.setAdapter(adapter);
			tvNumberThread.setText("CHỦ ĐỀ CỦA BẠN " + "("
					+ listThreadItem.size() + ")");
			if (d != null) {
				d.dismiss();
				d = null;
			}
		}

		@Override
		protected void onProgressUpdate(JSONObject... values) {
			JSONObject jsonListGame = values[0];
			MHOThread thread = new MHOThread();
			try {
				thread.setId(jsonListGame.getInt("ThreadId"));
				thread.setName(jsonListGame.getString("ThreadName"));
				JSONArray arr = jsonListGame.getJSONArray("DataList");
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
					dbh.insertGameToThread(game, thread);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	private void showDialogCreateThread() {
		final Dialog dialogCreateThread = new Dialog(this);
		dialogCreateThread.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogCreateThread.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialogCreateThread.setContentView(R.layout.dialog_create_thread);
		final EditText etThreadName = ((EditText) dialogCreateThread
				.findViewById(R.id.thread_name));
		final EditText etDes = ((EditText) dialogCreateThread
				.findViewById(R.id.thread_des));
		Button btCreateThread = ((Button) dialogCreateThread
				.findViewById(R.id.create_thread));
		btCreateThread.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etThreadName.getText().toString().isEmpty()) {
					etThreadName.setError("Tên chủ đề không được để trống");
					return;
				}
				if (etDes.getText().toString().isEmpty()) {
					etDes.setError("Mô tả chủ đề không được để trống");
					return;
				}
				dialogCreateThread.dismiss();
				createThread(0, SharePreferenceUtils
						.getAccEmail(ListThreadActivity.this), etThreadName
						.getText().toString(), etDes.getText().toString());
			}
		});
		dialogCreateThread.setCancelable(true);
		dialogCreateThread.show();
	}

	private void createThread(int Id, String email, String name, String des) {
		d = ProgressDialog.show(this, "", "Đang tạo chủ đề");

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String serverResponse = new String(arg2);
				makeToast(serverResponse);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				loadUserThread();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}
		};
		ServiceConnection.createThread(Id, email, name, des, handler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.create_thread:
			showDialogCreateThread();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void showDialogDeleteThread(final MHOThread thread) {
		new AlertDialog.Builder(this)
				.setTitle("Xóa chủ đề")
				.setMessage(
						"Bạn có muốn xóa chủ đề "
								+ URLDecoder.decode(thread.getName())
								+ " không?")
				.setNegativeButton("Không",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						})
				.setNeutralButton("Có", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteThread(thread);
					}
				}).setCancelable(true).show();
	}

	private void deleteThread(MHOThread thread) {
		d = ProgressDialog.show(this, "", "Đang xóa chủ đề");
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String response = new String(arg2);
				makeToast(response);
				if (d != null) {
					d.dismiss();
					d = null;
				}
				loadUserThread();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				makeToast("Lỗi kết nối");
				if (d != null) {
					d.dismiss();
					d = null;
				}
			}
		};
		ServiceConnection.deleteThread(thread,
				SharePreferenceUtils.getAccEmail(this), handler);
	}

}
