package com.maihoangonline.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Environment;

import com.maihoangonline.models.DownloadItem;
import com.maihoangonline.models.Game;

public class DataUtils {

	// Error code
	public static final int[] ERROR_CODE = { -1000, -1001, -1002, -100, -53,
			-50, -48, -99, -9, -8, -7, -6, -2 };

	// Preference Login
	public static final String PREF_LOGGING = "pref_logging";
	public static final String PREF_FACEBOOK = "pref_facebook";
	public static final String PREF_DOWNLOAD = "pref_download";

	// Path to save files

	public static final String BASE_PATH = Environment
			.getExternalStorageDirectory().getPath();
	public static final String PATH_APP = BASE_PATH + "/App";
	public static final String PATH_IMG = BASE_PATH + "/TempImage";

	public static final String APP_ID = "539331289516926";

	public static boolean isInErrCode(int code) {
		int count = 0;
		for (int i = 0; i < ERROR_CODE.length; i++) {
			if (code == ERROR_CODE[i]) {
				count++;
			}
		}
		if (count == 0) {
			return false;
		}
		return true;
	}

	public static boolean SHOW_FOOTER = true;

	public static int SCREEN_WIDTH = 0;

	public static ArrayList<Activity> activityList = new ArrayList<Activity>();
	public static ArrayList<DownloadItem> listDownloadItem = new ArrayList<DownloadItem>();

	public static boolean checkDownloadItemInList(DownloadItem downloadItem) {
		for (int i = 0; i < listDownloadItem.size(); i++) {
			if (downloadItem.compare(listDownloadItem.get(i))) {
				return true;
			}
		}
		return false;
	}
	//Download manager
	
	public static int NONE = 0;
	public static int DOWNLOADING = 1;
	public static int PAUSED = 2;
	public static int DOWNLOADED = 3;
	public static int ERROR = 4;
	public static ArrayList<Game> listGameInDownloadProgress = new ArrayList<Game>();
	public static Map<Game, Integer> listGameInDownloadState = new HashMap<Game, Integer>();

	public static boolean checkGameInProgressDownLoad(Game paramGame) {
		for (int i = 0;; i++) {
			if (i >= listGameInDownloadProgress.size())
				return false;
			if (paramGame.getTitle().endsWith(
					((Game) listGameInDownloadProgress.get(i)).getTitle()))
				return true;
		}
	}
	
	public static boolean isInDownloadScreen = false;
	public static final String GCM_PROJECT_ID = "982233253442";
	public static String DEVICE_TOKEN="";
	
	public static String PASS_WIFI;
	public static String USER_WIFI;

}
