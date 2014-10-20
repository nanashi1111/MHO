package com.maihoangonline.fragments;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.DownloadManager;
import com.maihoangonline.utils.SharePreferenceUtils;

@SuppressLint("ValidFragment")
public class DownloadingFragment extends Fragment {

	private static DownloadingFragment INSTANCE;
	private View rootView;
	private static LinearLayout llProgressContent;
	private static Context mContext;
	private boolean resumed = false;
	
	private static ArrayList<BroadcastReceiver> listReceiver = new ArrayList<BroadcastReceiver>();

	public DownloadingFragment() {

	}

	public static DownloadingFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DownloadingFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_downloading, null);
		llProgressContent = (LinearLayout) rootView
				.findViewById(R.id.download_progress_content);

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public static void addGameProgress(final Game game) {
		View progressDownloadView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.row_download_progress, null);
		ImageView icon = (ImageView) progressDownloadView
				.findViewById(R.id.icon);
		UrlImageViewHelper.setUrlDrawable(icon, game.getPicture());
		final TextView tvStatus = (TextView) progressDownloadView
				.findViewById(R.id.status_download);
		tvStatus.setText("Đang tải");
		final ImageView btPause = (ImageView) progressDownloadView
				.findViewById(R.id.pause_download);
		btPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (DataUtils.listGameInDownloadState.get(game) == DataUtils.PAUSED) {

					synchronized (DataUtils.listGameInDownloadState) {
						DataUtils.listGameInDownloadState.put(game,
								DataUtils.DOWNLOADING);
					}
					tvStatus.setText("Đang tải");
					btPause.setImageResource(R.drawable.icon_pause);
					DisplayUtils.log("Truoc khi resume");
					resumeDownloading(game);
				} else if (DataUtils.listGameInDownloadState.get(game) == DataUtils.DOWNLOADING) {

					synchronized (DataUtils.listGameInDownloadState) {
						DataUtils.listGameInDownloadState.put(game,
								DataUtils.PAUSED);
					}
					btPause.setImageResource(R.drawable.icon_continue);
					tvStatus.setText("Tạm ngừng tải");
				}
			}
		});
		TextView tvTitle = (TextView) progressDownloadView
				.findViewById(R.id.game_name);
		tvTitle.setText(game.getTitle());
		TextView tvProgress = (TextView) progressDownloadView
				.findViewById(R.id.progress_text);
		tvProgress.setText("0%");
		ProgressBar bar = (ProgressBar) progressDownloadView
				.findViewById(R.id.progress_bar);
		bar.setProgress(0);
		progressDownloadView.setTag(game);
		llProgressContent.addView(progressDownloadView);
	};

	public static void resumeDownloading(final Game game) {
		BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int i = intent.getIntExtra("progress", -1);
				setGameProgress(game, i);
				if (i == 100) {
					((TextView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.status_download))
							.setText("Đã tải xong");
					((TextView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.progress_text)).setText("");
					((ImageView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.pause_download))
							.setVisibility(View.INVISIBLE);
					DataUtils.listGameInDownloadState.put(game,
							DataUtils.DOWNLOADED);
				}
			}

		};

		IntentFilter filter = new IntentFilter(game.getTitle());
		mContext.registerReceiver(receiver, filter);
		listReceiver.add(receiver);
		DownloadManager.getInstance().resume(game, mContext, receiver);
	}

	public static void setGameProgress(Game game, int i) {
		((ProgressBar) llProgressContent.findViewWithTag(game).findViewById(
				R.id.progress_bar)).setProgress(i);
		((TextView) llProgressContent.findViewWithTag(game).findViewById(
				R.id.progress_text)).setText(i + "%");
	}

	public static void startDowloading(final Game game) {
		BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (DataUtils.listGameInDownloadState.get(game) == DataUtils.ERROR) {
					((TextView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.status_download))
							.setText("Quá trình tải bị lỗi");
					((TextView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.status_download))
							.setTextColor(Color.RED);
					((TextView) llProgressContent.findViewWithTag(game)
							.findViewById(R.id.progress_text)).setText("");
				} else {
					int i = intent.getIntExtra("progress", -1);
					if (i < 100) {
						setGameProgress(game, i);
					} else {
						setGameProgress(game, i);
						((TextView) llProgressContent.findViewWithTag(game)
								.findViewById(R.id.status_download))
								.setText("Đã tải xong");
						((TextView) llProgressContent.findViewWithTag(game)
								.findViewById(R.id.progress_text)).setText("");
						((ImageView) llProgressContent.findViewWithTag(game)
								.findViewById(R.id.pause_download))
								.setVisibility(View.INVISIBLE);
						DataUtils.listGameInDownloadState.put(game,
								DataUtils.DOWNLOADED);
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter(game.getTitle());
		mContext.registerReceiver(receiver, filter);
		listReceiver.add(receiver);
		DownloadManager.getInstance().download(game, mContext, receiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!resumed){
			resumed = true;
		for (int i = 0; i < DataUtils.listGameInDownloadProgress.size(); i++) {

			addGameProgress(DataUtils.listGameInDownloadProgress.get(i));
			final Game game = DataUtils.listGameInDownloadProgress.get(i);
			if (DataUtils.listGameInDownloadState
					.get(DataUtils.listGameInDownloadProgress.get(i)) == DataUtils.NONE) {
				DataUtils.listGameInDownloadState.put(
						DataUtils.listGameInDownloadProgress.get(i),
						DataUtils.DOWNLOADING);
				startDowloading(DataUtils.listGameInDownloadProgress.get(i));
			} else if (DataUtils.listGameInDownloadState
					.get(DataUtils.listGameInDownloadProgress.get(i)) == DataUtils.DOWNLOADING) {
				BroadcastReceiver receiver = new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						int i = intent.getIntExtra("progress", -1);
						setGameProgress(game, i);
						if (i == 100) {
							((TextView) llProgressContent.findViewWithTag(game)
									.findViewById(R.id.status_download))
									.setText("Đã tải xong");
							((TextView) llProgressContent.findViewWithTag(game)
									.findViewById(R.id.progress_text))
									.setText("");
							((ImageView) llProgressContent
									.findViewWithTag(game).findViewById(
											R.id.pause_download))
									.setVisibility(View.INVISIBLE);
							
						}
					}
				};
				IntentFilter filter = new IntentFilter(game.getTitle());
				mContext.registerReceiver(receiver, filter);
				listReceiver.add(receiver);
			} else if (DataUtils.listGameInDownloadState
					.get(DataUtils.listGameInDownloadProgress.get(i)) == DataUtils.DOWNLOADED) {
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.status_download))
						.setText("Đã tải xong");
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.progress_text)).setText("");
				((ProgressBar) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.progress_bar)).setProgress(100);
				((ImageView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.pause_download))
						.setVisibility(View.INVISIBLE);
			} else if (DataUtils.listGameInDownloadState
					.get(DataUtils.listGameInDownloadProgress.get(i)) == DataUtils.PAUSED) {
				int progress = SharePreferenceUtils.getProgressDownload(
						mContext, game);
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.status_download))
						.setText("Tạm ngừng tải");
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.progress_text)).setText(progress
						+ "%");
				((ImageView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.pause_download))
						.setImageResource(R.drawable.icon_continue);
				((ProgressBar) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.progress_bar)).setProgress(progress);

			} else if (DataUtils.listGameInDownloadState
					.get(DataUtils.listGameInDownloadProgress.get(i)) == DataUtils.ERROR) {
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.status_download))
						.setText("Quá trình tải bị lỗi");
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.status_download))
						.setTextColor(Color.RED);
				((TextView) llProgressContent.findViewWithTag(game)
						.findViewById(R.id.progress_text)).setText("");
			}

		}
	}}
	
	public void unregisterReceiver(){
		for(int i=0;i<listReceiver.size();i++){
			mContext.unregisterReceiver(listReceiver.get(i));
		}
		listReceiver.clear();
	}

}
