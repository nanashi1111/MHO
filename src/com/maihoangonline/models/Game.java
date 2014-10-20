package com.maihoangonline.models;

import java.io.Serializable;

public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String Title;
	private int IdGroup;
	private int IdSystem;
	private String Picture;
	private String Des;
	private String Detail;
	private String PictureAlbum;
	private int IdCategoryGame;
	private boolean HotGame;
	private boolean NewGame;
	private String File;
	private int Download;
	private int Rate;
	private String Version;
	private String Size;
	private int View;
	private boolean FreeGame;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public int getIdGroup() {
		return IdGroup;
	}

	public void setIdGroup(int idGroup) {
		IdGroup = idGroup;
	}

	public int getIdSystem() {
		return IdSystem;
	}

	public void setIdSystem(int idSystem) {
		IdSystem = idSystem;
	}

	public String getPicture() {
		return Picture;
	}

	public void setPicture(String picture) {
		Picture = picture;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public String getPictureAlbum() {
		return PictureAlbum;
	}

	public void setPictureAlbum(String pictureAlbum) {
		PictureAlbum = pictureAlbum;
	}

	public int getIdCategoryGame() {
		return IdCategoryGame;
	}

	public void setIdCategoryGame(int idCategoryGame) {
		IdCategoryGame = idCategoryGame;
	}

	public boolean isHotGame() {
		return HotGame;
	}

	public void setHotGame(boolean hotGame) {
		HotGame = hotGame;
	}

	public boolean isNewGame() {
		return NewGame;
	}

	public void setNewGame(boolean newGame) {
		NewGame = newGame;
	}

	public String getFile() {
		return File;
	}

	public void setFile(String file) {
		File = file;
	}

	public int getDownload() {
		return Download;
	}

	public void setDownload(int download) {
		Download = download;
	}

	public int getRate() {
		return Rate;
	}

	public void setRate(int rate) {
		Rate = rate;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public int getView() {
		return View;
	}

	public void setView(int view) {
		View = view;
	}

	public boolean getFreeGame() {
		return FreeGame;
	}

	public void setFreeGame(boolean freeGame) {
		FreeGame = freeGame;
	}

}
