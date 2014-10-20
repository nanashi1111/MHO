package com.maihoangonline.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Topic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String Name;
	private String Description;
	private String Backround;
	private String Image;
	private boolean Status;
	private int View;
	private int SoLuong;
	private ArrayList<Game> listGame;

	public ArrayList<Game> getListGame() {
		return listGame;
	}

	public void setListGame(ArrayList<Game> listGame) {
		this.listGame = listGame;
	}

	public Topic() {

	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getBackround() {
		return Backround;
	}

	public void setBackround(String backround) {
		Backround = backround;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public int getView() {
		return View;
	}

	public void setView(int view) {
		View = view;
	}

	public int getSoLuong() {
		return SoLuong;
	}

	public void setSoLuong(int soLuong) {
		SoLuong = soLuong;
	}

}
