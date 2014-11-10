package com.maihoangonline.models;


public class Transaction {

	private String description;
	private String date;

	public Transaction(String description, String date) {
		this.date = date;
		this.description = description;
	}
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
