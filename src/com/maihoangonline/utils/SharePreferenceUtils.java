package com.maihoangonline.utils;

import com.maihoangonline.models.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtils {

	// Put logged info
	public static void putLogInfo(Context c, boolean isLogged) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putBoolean("is_logged_in", isLogged);
		e.commit();
	}

	// Get logged info
	public static boolean isLogged(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE).getBoolean("is_logged_in", false);
	}

	// Put account_id when compete login
	public static void putAccID(Context c, int accID) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putInt("accID", accID);
		e.commit();
	}

	// Get accID
	public static int getAccID(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE).getInt("accID", -1);
	}

	// Put request token when compete login
	public static void putrequestToken(Context c, String requestToken) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString("requestToken", requestToken);
		e.commit();
	}

	// Get requestToken
	public static String getrequestToken(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE).getString("requestToken", "none");
	}

	// Put acc email when compete login
	public static void putAccEmail(Context c, String email) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString("email", email);
		e.commit();
	}

	// Get acc email
	public static String getAccEmail(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE).getString("email", "none");
	}

	public static void putAccPhonenumber(Context c, String phone) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putString("phone", phone);
		e.commit();
	}

	public static String getAccPhonenumber(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE).getString("phone", "none");
	}

	public static void putAccessToken(Context c, String token) {
		SharedPreferences pref = c.getSharedPreferences(
				DataUtils.PREF_FACEBOOK, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("access_token", token);
		editor.commit();
	}

	public static String getAccessToken(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_FACEBOOK,
				Context.MODE_PRIVATE).getString("access_token", "null");
	}

	public static void putExpire(Context c, long expire) {
		SharedPreferences pref = c.getSharedPreferences(
				DataUtils.PREF_FACEBOOK, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong("expire", expire);
		editor.commit();
	}

	public static long getExpire(Context c) {
		return c.getSharedPreferences(DataUtils.PREF_FACEBOOK,
				Context.MODE_PRIVATE).getLong("expire", 0);
	}

	public static void putGoldInfo(Context c, int tk1, int tk2, int tk3) {
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_LOGGING,
				Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putInt("tk1", tk1);
		e.putInt("tk2", tk2);
		e.putInt("tk3", tk3);
		e.commit();
	}

	public static int getGold(Context c, int type) {
		if (type == 1) {
			return c.getSharedPreferences(DataUtils.PREF_LOGGING,
					Context.MODE_PRIVATE).getInt("tk1", 0);
		} else if (type == 2) {
			return c.getSharedPreferences(DataUtils.PREF_LOGGING,
					Context.MODE_PRIVATE).getInt("tk2", 0);
		} else {
			return c.getSharedPreferences(DataUtils.PREF_LOGGING,
					Context.MODE_PRIVATE).getInt("tk3", 0);
		}
	}
	
	public static void putProgressDownload(Context c, Game game, int progress){
		SharedPreferences pref = c.getSharedPreferences(DataUtils.PREF_DOWNLOAD, Context.MODE_PRIVATE);
		Editor e = pref.edit();
		e.putInt(game.getTitle(), progress);
		e.commit();
	}
	
	public static int getProgressDownload(Context c, Game game){
		return c.getSharedPreferences(DataUtils.PREF_DOWNLOAD, Context.MODE_PRIVATE).getInt(game.getTitle(), -1);
	}

}
