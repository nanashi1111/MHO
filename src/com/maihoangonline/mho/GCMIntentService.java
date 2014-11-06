package com.maihoangonline.mho;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gcm.GCMBaseIntentService;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.SharePreferenceUtils;

public class GCMIntentService extends GCMBaseIntentService{
	
	public GCMIntentService(){
		super(DataUtils.GCM_PROJECT_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Toast.makeText(arg0, "Error:"+arg1, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		//generateNotification(arg0, "Notification");
		String jsonMsg = arg1.getStringExtra("message");
		DisplayUtils.log(jsonMsg);
		generateNotification(arg0, jsonMsg);
		/*try {
			JSONObject json = new JSONObject(jsonMsg);
			//Toast.makeText(arg0, jsonMsg, Toast.LENGTH_LONG).show();
			DisplayUtils.log(jsonMsg);
			String msg = json.getString("Name");
			if(msg==null||msg.isEmpty()){
				msg = "Đã có bản cập nhật mới";
			}
			generateNotification(arg0, msg);
			Toast.makeText(arg0, jsonMsg, Toast.LENGTH_LONG).show();
			DisplayUtils.log(jsonMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		SharePreferenceUtils.putDeviceToken(arg0, arg1);
		DataUtils.DEVICE_TOKEN = arg1;
		DisplayUtils.log("Registered, devicetoken = "+arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		
	}
	
	@SuppressWarnings({ "deprecation" })
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.app_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, HomeActivity.class);
		notificationIntent.putExtra("command", "CLEAR");
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Vibrate if vibrate is enabled
		//notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

}
