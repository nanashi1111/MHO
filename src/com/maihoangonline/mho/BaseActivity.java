package com.maihoangonline.mho;

import com.maihoangonline.database.DBHelper;
import com.maihoangonline.utils.DisplayUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends ActionBarActivity {

	FragmentManager mFragmentManager;
	protected DBHelper dbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbh = new DBHelper(this);
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	public void onBackPressed() {
		// DialogUtils.showDialogExit(this);
		dbh.close();
		finish();
		overridePendingTransition(0, 0);
	}

	public void makeToast(String msg) {
		DisplayUtils.makeToast(this, msg);
	}

	public void switchContent(int contentId, Fragment fragment) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(contentId, fragment);
		transaction.commit();
	}
	
	public DBHelper getDatabaseHelper(){
		return dbh;
	}

}
