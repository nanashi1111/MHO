package com.maihoangonline.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MHOThread implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String name;
	private String user;
	private String des;
	private int IdSystem;
	private int Views;

	private ArrayList<Game> listGame;

	public ArrayList<Game> getListGame() {
		return listGame;
	}

	public void setListGame(ArrayList<Game> listGame) {
		this.listGame = listGame;
	}

	public MHOThread() {

	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getIdSystem() {
		return IdSystem;
	}

	public void setIdSystem(int idSystem) {
		IdSystem = idSystem;
	}

	public int getViews() {
		return Views;
	}

	public void setViews(int views) {
		Views = views;
	}

}
