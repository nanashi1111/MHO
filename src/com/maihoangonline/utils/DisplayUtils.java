package com.maihoangonline.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class DisplayUtils {

	public static void log(String msg) {
		Log.d("", msg);
	}

	public static void makeToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static Bitmap scaleBitmap(Bitmap b, float ratio) {
		Bitmap scaledBitmap = null;
		scaledBitmap = Bitmap.createScaledBitmap(b,
				(int) (b.getWidth() * ratio), (int) (b.getHeight() * ratio),
				false);
		return scaledBitmap;
	}
	
	public static Bitmap scaleBitmap(Bitmap b, float ratio, boolean b1) {
		Bitmap scaledBitmap = null;
		scaledBitmap = Bitmap.createScaledBitmap(b,
				(int) (b.getWidth() * ratio), (int) (b.getHeight()), false);
		return scaledBitmap;
	}

}
