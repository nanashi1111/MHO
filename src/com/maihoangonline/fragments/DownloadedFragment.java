package com.maihoangonline.fragments;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.pm.ApplicationManager;
import android.content.pm.OnInstalledPackaged;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.maihoangonline.adapter.ListGameAdapter;
import com.maihoangonline.mho.DownloadProgressActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;

@SuppressLint("ValidFragment")
public class DownloadedFragment extends Fragment {

	private static DownloadedFragment INSTANCE;
	private View rootView;
	private ListView listDownloaded;
	private ArrayList<Game> listGameDownloaded = new ArrayList<Game>();
	private ListGameAdapter adapter;

	private DownloadedFragment() {

	}

	public static DownloadedFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DownloadedFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_downloaded, null);
		listDownloaded = (ListView)rootView.findViewById(R.id.list_downloaded);
		loadDownloadedGame();
		return rootView;
	}
	
	public void loadDownloadedGame(){
		listGameDownloaded = ((DownloadProgressActivity)getActivity()).getDatabaseHelper().getListDownloadedGame();
		adapter = new ListGameAdapter(getActivity(), 1, listGameDownloaded);
		listDownloaded.setAdapter(adapter);
		listDownloaded.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				try {
					final ApplicationManager am = new ApplicationManager(getActivity());
					am.setOnInstalledPackaged(new OnInstalledPackaged() {

						public void packageInstalled(String packageName, int returnCode) {
							if (returnCode == ApplicationManager.INSTALL_SUCCEEDED) {
								DisplayUtils.log("Install succeeded");
							} else {
								DisplayUtils.log("Install failed");
							}
						}
					});
					try {
						am.installPackage(DataUtils.PATH_APP+"/"+listGameDownloaded.get(arg2).getTitle()+".apk");
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadDownloadedGame();
	}

}
