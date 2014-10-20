package com.maihoangonline.models;

import java.io.Serializable;

public class News implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Attachment;
	private String Author;
	private int CategoryID;
	private String CategoryName;
	private String Content;
	private String Creator;
	private boolean EnableComment;
	private String ExpriseDate;
	private int GameID;
	private String GameName;
	private int ID;
	private String ImageComment;
	private String ImagePath;
	private String Keyword;
	private String NewsIdRelate;
	private String PublishDate;
	private String SmallImagePath;
	private String Source;
	private int SubCategoryID;
	private String Title;
	private int TotalComment;
	private int TotalView;
	private int Type;
	private String UrlDetail;
	private String lead;

	public String getAttachment() {
		return this.Attachment;
	}

	public String getAuthor() {
		return this.Author;
	}

	public int getCategoryID() {
		return this.CategoryID;
	}

	public String getCategoryName() {
		return this.CategoryName;
	}

	public String getContent() {
		return this.Content;
	}

	public String getCreator() {
		return this.Creator;
	}

	public String getExpriseDate() {
		return this.ExpriseDate;
	}

	public int getGameID() {
		return this.GameID;
	}

	public String getGameName() {
		return this.GameName;
	}

	public int getID() {
		return this.ID;
	}

	public String getImageComment() {
		return this.ImageComment;
	}

	public String getImagePath() {
		return this.ImagePath;
	}

	public String getKeyword() {
		return this.Keyword;
	}

	public String getLead() {
		return this.lead;
	}

	public String getNewsIdRelate() {
		return this.NewsIdRelate;
	}

	public String getPublishDate() {
		return this.PublishDate;
	}

	public String getSmallImagePath() {
		return this.SmallImagePath;
	}

	public String getSource() {
		return this.Source;
	}

	public int getSubCategoryID() {
		return this.SubCategoryID;
	}

	public String getTitle() {
		return this.Title;
	}

	public int getTotalComment() {
		return this.TotalComment;
	}

	public int getTotalView() {
		return this.TotalView;
	}

	public int getType() {
		return this.Type;
	}

	public String getUrlDetail() {
		return this.UrlDetail;
	}

	public boolean isEnableComment() {
		return this.EnableComment;
	}

	public void setAttachment(String paramString) {
		this.Attachment = paramString;
	}

	public void setAuthor(String paramString) {
		this.Author = paramString;
	}

	public void setCategoryID(int paramInt) {
		this.CategoryID = paramInt;
	}

	public void setCategoryName(String paramString) {
		this.CategoryName = paramString;
	}

	public void setContent(String paramString) {
		this.Content = paramString;
	}

	public void setCreator(String paramString) {
		this.Creator = paramString;
	}

	public void setEnableComment(boolean paramBoolean) {
		this.EnableComment = paramBoolean;
	}

	public void setExpriseDate(String paramString) {
		this.ExpriseDate = paramString;
	}

	public void setGameID(int paramInt) {
		this.GameID = paramInt;
	}

	public void setGameName(String paramString) {
		this.GameName = paramString;
	}

	public void setID(int paramInt) {
		this.ID = paramInt;
	}

	public void setImageComment(String paramString) {
		this.ImageComment = paramString;
	}

	public void setImagePath(String paramString) {
		this.ImagePath = paramString;
	}

	public void setKeyword(String paramString) {
		this.Keyword = paramString;
	}

	public void setLead(String paramString) {
		this.lead = paramString;
	}

	public void setNewsIdRelate(String paramString) {
		this.NewsIdRelate = paramString;
	}

	public void setPublishDate(String paramString) {
		this.PublishDate = paramString;
	}

	public void setSmallImagePath(String paramString) {
		this.SmallImagePath = paramString;
	}

	public void setSource(String paramString) {
		this.Source = paramString;
	}

	public void setSubCategoryID(int paramInt) {
		this.SubCategoryID = paramInt;
	}

	public void setTitle(String paramString) {
		this.Title = paramString;
	}

	public void setTotalComment(int paramInt) {
		this.TotalComment = paramInt;
	}

	public void setTotalView(int paramInt) {
		this.TotalView = paramInt;
	}

	public void setType(int paramInt) {
		this.Type = paramInt;
	}

	public void setUrlDetail(String paramString) {
		this.UrlDetail = paramString;
	}
}
