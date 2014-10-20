package com.maihoangonline.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

	public static void showDialogExit(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("Thoát")
				.setMessage("Bạn có muốn thoát ứng dụng không?")
				.setNegativeButton("Thoát",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) context).finish();
							}
						})
				.setNeutralButton("Không",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setCancelable(true).show();
	}

	public static void showInfoDialog(Context context, String message) {
		new AlertDialog.Builder(context).setTitle("Thông báo")
				.setMessage(message)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).setCancelable(true).show();
	}

}
