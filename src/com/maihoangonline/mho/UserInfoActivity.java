package com.maihoangonline.mho;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maihoangonline.adapter.ListCardTypeAdapter;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

public class UserInfoActivity extends BaseActivity implements OnClickListener {

	private TextView tvUserName, tvMain, tvPromotion1, tvPromotion2;
	private ActionBar bar;
	private ProgressDialog d;
	private int typeCard = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setupActionBar();
		setupView();
		loadData();
	}

	private void setupView() {
		tvUserName = (TextView) findViewById(R.id.user_name);
		tvMain = (TextView) findViewById(R.id.main);
		tvPromotion1 = (TextView) findViewById(R.id.promotion1);
		tvPromotion2 = (TextView) findViewById(R.id.promotion2);
		findViewById(R.id.quit).setOnClickListener(this);
		findViewById(R.id.pay_view).setOnClickListener(this);
		findViewById(R.id.transfer_view).setOnClickListener(this);
		findViewById(R.id.get_acc_info_view).setOnClickListener(this);
		findViewById(R.id.history_view).setOnClickListener(this);
		findViewById(R.id.remain_bill_view).setOnClickListener(this);
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
		tvTitle.setText("Thông tin tài khoản");

	}

	private void loadData() {
		tvUserName
				.setText("Xin chào " + SharePreferenceUtils.getAccEmail(this));
		tvMain.setText("TK Chính:" + SharePreferenceUtils.getGold(this, 1)
				+ " gold");
		tvPromotion1.setText("TK KM1:" + SharePreferenceUtils.getGold(this, 2)
				+ " gold");
		tvPromotion2.setText("TK KM2:" + SharePreferenceUtils.getGold(this, 3)
				+ " gold");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit:
			SharePreferenceUtils.putLogInfo(UserInfoActivity.this, false);
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.pay_view:
			showDialogPayGold();
			break;
		case R.id.transfer_view:
			showDialogTransferGold();
			break;
		case R.id.get_acc_info_view:
			// getAccInfo(true);
			showDialogAccInfo();
			break;
		case R.id.history_view:
			getHistory();
			break;
		case R.id.remain_bill_view:
			getAccInfo(false);
			break;

		}
	}

	private void showDialogAccInfo() {
		new AlertDialog.Builder(this)
				.setTitle("Thông tin tài khoản")
				.setMessage(
						"Họ tên: chưa cập nhật\n" + "Email: "
								+ SharePreferenceUtils.getAccEmail(this)
								+ "\nSố tài khoản: "
								+ SharePreferenceUtils.getAccID(this)
								+ "\nSố điện thoại: Chưa cập nhật")
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).setCancelable(true).show();
	}

	private void showDialogPayGold() {
		final Dialog dialogPay = new Dialog(this);
		dialogPay.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPay.setContentView(R.layout.dialog_pay_gold);
		Spinner listCardType = (Spinner) dialogPay.findViewById(R.id.card_type);
		ArrayList<String> listCardTypeItem = new ArrayList<String>();
		listCardTypeItem.add("Vina");
		listCardTypeItem.add("Mobile");
		listCardTypeItem.add("Viettel");
		listCardTypeItem.add("Vcoin");
		listCardTypeItem.add("Zing");
		ListCardTypeAdapter adapter = new ListCardTypeAdapter(this, 1,
				listCardTypeItem);
		listCardType.setAdapter(adapter);
		listCardType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 <= 2) {
					typeCard = arg2 + 1;
				} else {
					typeCard = arg2 + 2;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		final EditText etCode = (EditText) dialogPay
				.findViewById(R.id.card_code);
		etCode.setText("");
		final EditText etSerial = (EditText) dialogPay
				.findViewById(R.id.card_serial);
		etSerial.setText("");
		Button btPay = (Button) dialogPay.findViewById(R.id.pay);
		btPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String code = etCode.getText().toString();
				if (code.isEmpty()) {
					etCode.setError("Bạn hãy nhập mã thẻ...");
					return;
				}
				String serial = etSerial.getText().toString();
				if (serial.isEmpty()) {
					etSerial.setError("Bạn chưa nhập số serial...");
					return;
				}
				dialogPay.dismiss();
				pay(typeCard, code, serial,
						SharePreferenceUtils.getAccID(UserInfoActivity.this));
			}

		});

		dialogPay.setCancelable(true);
		dialogPay.show();
	}

	private void pay(int typeCard, String code, String serial, int accID) {
		DisplayUtils.log("TypeCard=" + typeCard);
		d = ProgressDialog.show(this, "", "Đang nạp gold...");
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
				if (response != null) {
					try {
						makeToast(response.getString("Message"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					DisplayUtils.log(response.toString());
				} else {
					makeToast("Null");
				}
				/*
				 * if (d != null) { d.dismiss(); d = null; }
				 */
				super.onSuccess(statusCode, headers, response);
				getAccInfo(true);
			}

		};

		ServiceConnection.payGold(typeCard, code, serial, accID, handler);

	}

	private void showDialogTransferGold() {
		final Dialog dialogTransfer = new Dialog(this);
		dialogTransfer.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogTransfer.setContentView(R.layout.dialog_transfer_gold);
		final EditText etPassword = (EditText) dialogTransfer
				.findViewById(R.id.password);
		final EditText etToUser = (EditText) dialogTransfer
				.findViewById(R.id.to_user);
		final EditText etGold = (EditText) dialogTransfer
				.findViewById(R.id.gold);
		Button btTransfer = (Button) dialogTransfer.findViewById(R.id.transfer);
		btTransfer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etPassword.getText().toString().isEmpty()) {
					etPassword.setError("Mật khẩu không được để trống");
					return;
				}
				if (etToUser.getText().toString().isEmpty()) {
					etToUser.setError("Bạn chưa nhập thông tin người nhận");
					return;
				}
				if (etGold.getText().toString().isEmpty()) {
					etGold.setError("Bạn chưa nhập số gold");
					return;
				}
				dialogTransfer.dismiss();
				transfer(etPassword.getText().toString(), Integer
						.parseInt(etGold.getText().toString()), etToUser
						.getText().toString());
			}
		});
		dialogTransfer.setCancelable(true);
		dialogTransfer.show();
	}

	private void transfer(String password, int gold, String toUser) {
		d = ProgressDialog.show(this, "", "Đang chuyển gold...");
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
				if (response != null) {
					try {
						makeToast(response.getString("Message"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					DisplayUtils.log(response.toString());
				} else {
					makeToast("Null");
				}
				/*if (d != null) {
					d.dismiss();
					d = null;
				}*/
				super.onSuccess(statusCode, headers, response);
				getAccInfo(true);
			}

		};

		ServiceConnection.transferGold(SharePreferenceUtils.getAccID(this),
				password, toUser, gold, handler);
	}

	private void getAccInfo(final boolean isAfterPay) {
		/*
		 * if(d==null){ d = ProgressDialog .show(this, "",
		 * "Đang lấy thông tin tài khoản..."); }else{
		 * d.setMessage("Đang lấy thông tin tài khoản..."); }
		 */
		if (!isAfterPay) {
			d=ProgressDialog.show(this, "", "Đang lấy thông tin tài khoản...");
		}
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
				JSONObject o = response;
				if (d != null) {
					d.dismiss();
					d = null;
				}
				try {
					int tk1 = o.getInt("TK1");
					int tk2 = o.getInt("TK2");
					int tk3 = o.getInt("TK3");
					SharePreferenceUtils.putGoldInfo(UserInfoActivity.this,
							tk1, tk2, tk3);
				} catch (JSONException e) {

					e.printStackTrace();
				}
				DisplayUtils.log(response.toString());
				
				if (isAfterPay) {
					tvMain.setText("TK Chính:"
							+ SharePreferenceUtils.getGold(
									UserInfoActivity.this, 1) + " gold");
					tvPromotion1.setText("TK KM1:"
							+ SharePreferenceUtils.getGold(
									UserInfoActivity.this, 2) + " gold");
					tvPromotion2.setText("TK KM2:"
							+ SharePreferenceUtils.getGold(
									UserInfoActivity.this, 3) + " gold");
				}else{
					showDialogAccBillInfo();
				}
				super.onSuccess(statusCode, headers, response);
			}

		};
		ServiceConnection.getAccInfo(SharePreferenceUtils.getAccEmail(this),
				handler);
	}

	private void showDialogAccBillInfo() {
		final Dialog dialogInfo = new Dialog(this);
		dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogInfo.setContentView(R.layout.dialog_user_info);
		TextView tvEmail = (TextView) dialogInfo.findViewById(R.id.acc_email);
		tvEmail.setText("Tài khoản:" + SharePreferenceUtils.getAccEmail(this));
		TextView tvMainBill = (TextView) dialogInfo
				.findViewById(R.id.main_acc_bil);
		tvMainBill.setText("Tài khoản chính:"
				+ SharePreferenceUtils.getGold(this, 1) + " gold");
		TextView tvSecondary1 = (TextView) dialogInfo
				.findViewById(R.id.secondary_acc_bil_1);
		tvSecondary1.setText("Tài khoản khuyến mãi 1:"
				+ SharePreferenceUtils.getGold(this, 2) + " gold");
		TextView tvSecondary2 = (TextView) dialogInfo
				.findViewById(R.id.secondary_acc_bil_2);
		tvSecondary2.setText("Tài khoản khuyến mãi 2:"
				+ SharePreferenceUtils.getGold(this, 3) + " gold");
		Button btClose = (Button) dialogInfo.findViewById(R.id.close);
		btClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogInfo.dismiss();
			}
		});
		dialogInfo.setCancelable(true);
		dialogInfo.show();
	}

	private void getHistory() {
		d = ProgressDialog.show(this, "", "Đang lấy lịch sử giao dịch...");
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
				/*
				 * try { makeToast(response.getString("Message")); } catch
				 * (JSONException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
				if (d != null) {
					d.dismiss();
					d = null;
				}
				makeToast(response.toString());
				DisplayUtils.log("History:" + response.toString());
				super.onSuccess(statusCode, headers, response);
			}

		};
		ServiceConnection.getHistoryTransaction(
				SharePreferenceUtils.getAccID(this), 5, 0, handler);
	}

}
