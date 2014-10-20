package com.maihoangonline.models;

import java.io.Serializable;

public class Category implements Serializable{
	
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
	private String View;
	private String SoLuong;

	public Category() {

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

	public String getView() {
		return View;
	}

	public void setView(String view) {
		View = view;
	}

	public String getSoLuong() {
		return SoLuong;
	}

	public void setSoLuong(String soLuong) {
		SoLuong = soLuong;
	}
}
