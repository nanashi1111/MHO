package com.maihoangonline.models;

import java.io.Serializable;

public class DownloadItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
	private int status; // 1=downloading 0=paused
	private int progress;
	
	public static int STATUS_DOWNLOADING = 1;
	public static int STATUS_PAUSED = 0;

	public DownloadItem(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getStatus() {
		return status;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean compare(DownloadItem o) {
		if (getGame().getId() == o.getGame().getId()) {
			return true;
		} else {
			return false;
		}
	}

}
