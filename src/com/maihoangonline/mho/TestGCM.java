package com.maihoangonline.mho;

import com.google.android.gcm.GCMRegistrar;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ServiceConnection;
import com.maihoangonline.utils.SharePreferenceUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

public class TestGCM extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar_home);
		new GetDeviceToken().execute();
	}

	private class GetDeviceToken extends AsyncTask<Void, Void, Void> {
		ProgressDialog d;
		boolean isConnected;

		@Override
		protected Void doInBackground(Void... params) {
			if (isConnected) {
				if(SharePreferenceUtils.getDeviceToken(TestGCM.this).isEmpty()){
				try {
					GCMRegistrar.checkDevice(TestGCM.this);
					GCMRegistrar.checkManifest(TestGCM.this);
					String regID = GCMRegistrar.getRegistrationId(TestGCM.this);
					if (regID.equals("")) {
						GCMRegistrar.register(TestGCM.this,
								DataUtils.GCM_PROJECT_ID);
					} else {
						SharePreferenceUtils
								.putDeviceToken(TestGCM.this, regID);
						if (!GCMRegistrar.isRegistered(TestGCM.this)) {
							GCMRegistrar.setRegisteredOnServer(TestGCM.this,
									true);
						}
					}
				} catch (Exception e) {
				}
			}
			}else{
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (d != null) {
				d.dismiss();
				d = null;
			}
			makeToast(SharePreferenceUtils.getDeviceToken(TestGCM.this));
			DisplayUtils.log(SharePreferenceUtils.getDeviceToken(TestGCM.this));
		}

		@Override
		protected void onPreExecute() {
			d = ProgressDialog.show(TestGCM.this, "", "Registering...");
			isConnected = ServiceConnection.checkConnection(TestGCM.this);
		}

	}

}
