package com.maihoangonline.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.maihoangonline.models.Game;

public class DownloadManager {

	private static DownloadManager INSTANCE;

	private DownloadManager() {

	}

	public static DownloadManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DownloadManager();
		}
		return INSTANCE;
	}

	public void download(Game game, Context c, BroadcastReceiver receiver) {
		new DownloadThread(game, c, receiver).start();
	}

	public void resume(Game game, Context c, BroadcastReceiver receiver) {
		new ResumeThread(game, c, receiver).start();
	}

	private class DownloadThread extends Thread {
		Game game;
		Context c;
		@SuppressWarnings("unused")
		BroadcastReceiver receiver;

		DownloadThread(Game game, Context c, BroadcastReceiver receiver) {
			this.game = game;
			this.c = c;
			this.receiver = receiver;
		}

		@Override
		public void run() {
			File f = new File(DataUtils.PATH_APP);
			if (!f.exists()) {
				f.mkdir();
			}
			File apk = new File(DataUtils.PATH_APP + "/" + game.getTitle()
					+ ".apk");
			if (apk.exists()) {
				apk.delete();
			}
			try {
				URL url = new URL(game.getFile());
				URLConnection connection = url.openConnection();
				connection.connect();
				int fileLength = connection.getContentLength();
				InputStream input = new BufferedInputStream(
						connection.getInputStream());
				OutputStream output = new FileOutputStream(DataUtils.PATH_APP
						+ "/" + game.getTitle() + ".apk");

				byte[] data = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					synchronized (DataUtils.listGameInDownloadState) {
						if (DataUtils.listGameInDownloadState.get(game) == DataUtils.DOWNLOADED) {
							Intent intent = new Intent("download_complete");
							intent.putExtra("game", game);
							c.sendBroadcast(intent);
							break;
						}
					}

					total += count;
					synchronized (DataUtils.listGameInDownloadState) {
						if (DataUtils.listGameInDownloadState.get(game) == DataUtils.PAUSED) {
							SharePreferenceUtils.putProgressDownload(c, game,
									(int) (total * 100 / fileLength));
							break;
						}
					}
					/*DisplayUtils.log("Progress:"
							+ (int) (total * 100 / fileLength));*/
					Intent intent = new Intent(game.getTitle());
					intent.putExtra("progress",
							(int) (total * 100 / fileLength));
					c.sendBroadcast(intent);
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

				if ((int) (total * 100 / fileLength) == 100) {
					DisplayUtils.log("Trang thai:"
							+ DataUtils.listGameInDownloadState.get(game));
					Intent intent = new Intent("download_complete");
					intent.putExtra("game", game);
					c.sendBroadcast(intent);
					DataUtils.listGameInDownloadState.put(game,
							DataUtils.DOWNLOADED);
				}

			} catch (MalformedURLException e) {
				synchronized (DataUtils.listGameInDownloadState) {
					Intent intentErr = new Intent("download_error");
					intentErr.putExtra("game", game);
					this.c.sendBroadcast(intentErr);
					DataUtils.listGameInDownloadState.put(game,
							Integer.valueOf(DataUtils.ERROR));
					this.c.sendBroadcast(new Intent(game.getTitle()));
					e.printStackTrace();
				}
			} catch (IOException e) {
				synchronized (DataUtils.listGameInDownloadState) {
					Intent intentErr = new Intent("download_error");
					intentErr.putExtra("game", game);
					this.c.sendBroadcast(intentErr);
					DataUtils.listGameInDownloadState.put(game,
							Integer.valueOf(DataUtils.ERROR));
					this.c.sendBroadcast(new Intent(game.getTitle()));
					e.printStackTrace();
				}
			}
		}

	}

	private class ResumeThread extends Thread {
		Context c;
		Game game;
		@SuppressWarnings("unused")
		BroadcastReceiver reciver;

		ResumeThread(Game game, Context c, BroadcastReceiver receiver) {
			this.game = game;
			this.c = c;
			this.reciver = receiver;
			synchronized (DataUtils.listGameInDownloadState) {
				DataUtils.listGameInDownloadState.put(game,
						DataUtils.DOWNLOADING);
			}
			DisplayUtils.log("Trang thai khoi tao resume " + game.getTitle()
					+ +DataUtils.listGameInDownloadState.get(game));
		}

		@Override
		public void run() {
			File path = new File(DataUtils.PATH_APP);
			if (!path.exists()) {
				path.mkdir();
			}
			File downloadedFile = new File(DataUtils.PATH_APP + "/"
					+ game.getTitle() + ".apk");
			long downloadedSize = downloadedFile.length();
			try {
				OutputStream output = new FileOutputStream(DataUtils.PATH_APP
						+ "/" + game.getTitle() + ".apk", true);
				URL url = new URL(game.getFile());
				URL tmpURL = new URL(game.getFile());
				URLConnection connection = url.openConnection();
				URLConnection tmpConnection = tmpURL.openConnection();
				tmpConnection.connect();
				connection.setRequestProperty("Range", "bytes="
						+ downloadedFile.length() + "-");
				connection.connect();
				int lengthOfFile = tmpConnection.getContentLength();
				InputStream input = new BufferedInputStream(
						connection.getInputStream());
				byte[] data = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// send broadcast
					if (DataUtils.listGameInDownloadState.get(game) == DataUtils.DOWNLOADED) {
						DisplayUtils.log("Trang thai resume" + game.getTitle()
								+ +DataUtils.listGameInDownloadState.get(game));
						Intent intent = new Intent("download_complete");
						intent.putExtra("game", game);
						c.sendBroadcast(intent);
						break;
					}
					if (DataUtils.listGameInDownloadState.get(game) == DataUtils.PAUSED) {
						DisplayUtils.log("Trang thai resume" + game.getTitle()
								+ +DataUtils.listGameInDownloadState.get(game));
						SharePreferenceUtils
								.putProgressDownload(
										c,
										game,
										(int) ((total + downloadedSize) * 100 / lengthOfFile));
						break;
					}
					/*DisplayUtils
							.log("Progress:"
									+ (int) ((total + downloadedSize) * 100 / lengthOfFile)
									+ "%");*/
					Intent i = new Intent(game.getTitle());
					i.putExtra(
							"progress",
							(int) ((total + downloadedSize) * 100 / lengthOfFile));
					c.sendBroadcast(i);
					output.write(data, 0, count);

				}
				output.flush();
				output.close();
				input.close();
				synchronized (DataUtils.listGameInDownloadState) {
					if ((int) ((total + downloadedSize) * 100 / lengthOfFile) == 100) {
						DisplayUtils.log("Trang thai:"
								+ DataUtils.listGameInDownloadState.get(game));
						Intent intent = new Intent("download_complete");
						intent.putExtra("game", game);
						c.sendBroadcast(intent);
						DataUtils.listGameInDownloadState.put(game,
								DataUtils.DOWNLOADED);
					}
				}

			} catch (FileNotFoundException e) {
				Intent intentErr = new Intent("download_error");
				intentErr.putExtra("game", game);
				this.c.sendBroadcast(intentErr);
				DataUtils.listGameInDownloadState.put(game,
						Integer.valueOf(DataUtils.ERROR));
				this.c.sendBroadcast(new Intent(game.getTitle()));
				e.printStackTrace();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Intent intentErr = new Intent("download_error");
				intentErr.putExtra("game", game);
				this.c.sendBroadcast(intentErr);
				DataUtils.listGameInDownloadState.put(game,
						Integer.valueOf(DataUtils.ERROR));
				this.c.sendBroadcast(new Intent(game.getTitle()));
				e.printStackTrace();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
