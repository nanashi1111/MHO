package com.maihoangonline.database;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.maihoangonline.models.Game;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.models.Topic;
import com.maihoangonline.utils.DataUtils;
import com.maihoangonline.utils.DisplayUtils;

public class DBHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "MaiHoangOnline";
	private static int VERSION = 7;
	private static String CREATE_TABLE_TOPIC = "create table MHOTopic (id integer not null, Name text);";
	private static String CREATE_TABLE_GAME_IN_TOPIC = "create table MHOTopicGame (idt integer not null, idg integer not null, Title text, Picture text, Description text, Size text)";
	private static String CREATE_TABLE_FAVORITE_GAME = "create table MHOFavorite (Email text, Id integer not null, Title text, Picture text, Description text, Size text, PictureAlbum text, Detail text)";
	private static String CREATE_TABLE_THREAD = "create table MHOThread (id integer not null, name text, user text, des text, IdSystem integer, Views integer)";
	private static String CREATE_TABLE_GAME_IN_THREAD = "create table MHOThreadGame (idt integer not null, idg integer not null, Title text, Picture text, Description text, Size text, PictureAlbum text, Detail text)";
	private static String CREATE_TABLE_DOWNLOADED_GAME = "create table MHODownloadedGame (id integer not null, Title text, Picture text, Description text, Size text, PictureAlbum text, Detail text)";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, VERSION);

	}

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TOPIC);
		db.execSQL(CREATE_TABLE_GAME_IN_TOPIC);
		db.execSQL(CREATE_TABLE_FAVORITE_GAME);
		db.execSQL(CREATE_TABLE_THREAD);
		db.execSQL(CREATE_TABLE_GAME_IN_THREAD);
		db.execSQL(CREATE_TABLE_DOWNLOADED_GAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists MHOTopic");
		db.execSQL("drop table if exists MHOTopicGame");
		db.execSQL("drop table if exists MHOFavorite");
		db.execSQL("drop table if exists MHOThread");
		db.execSQL("drop table if exists MHOThreadGame");
		db.execSQL("drop table if exists MHODownloadedGame");
		onCreate(db);
	}

	public void insertTopic(Topic topic) {
		ContentValues cv = new ContentValues();
		cv.put("id", topic.getId());
		cv.put("Name", topic.getName());
		getWritableDatabase().insert("MHOTopic", null, cv);
	}

	public void insertGameToTopic(Game game, Topic topic) {
		ContentValues cv = new ContentValues();
		cv.put("idt", topic.getId());
		cv.put("idg", game.getId());
		cv.put("Title", game.getTitle());
		cv.put("Picture", game.getPicture());
		cv.put("Description", game.getDes());
		cv.put("Size", game.getSize());
		getWritableDatabase().insert("MHOTopicGame", null, cv);
	}

	public ArrayList<Topic> getAllTopic() {
		ArrayList<Topic> listTopic = new ArrayList<Topic>();
		Cursor cursorTopic = getReadableDatabase().rawQuery(
				"select * from MHOTopic", null);
		if (cursorTopic.getCount() > 0) {
			cursorTopic.moveToFirst();
			do {
				Topic topic = new Topic();
				topic.setId(cursorTopic.getInt(0));
				topic.setName(cursorTopic.getString(1));
				listTopic.add(topic);
			} while (cursorTopic.moveToNext());
			for (int i = 0; i < listTopic.size(); i++) {
				ArrayList<Game> listGame = getListGameFromTopic(listTopic
						.get(i));
				listTopic.get(i).setListGame(listGame);
			}
		}
		return listTopic;
	}

	public ArrayList<Game> getListGameFromTopic(Topic topic) {
		ArrayList<Game> listGame = new ArrayList<Game>();
		String cmd = "select * from MHOTopicGame where idt = " + topic.getId();
		Cursor cursor = getReadableDatabase().rawQuery(cmd, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Game game = new Game();
				game.setId(cursor.getInt(1));
				game.setTitle(cursor.getString(2));
				game.setPicture(cursor.getString(3));
				game.setDes(cursor.getString(4));
				game.setSize(cursor.getString(5));
				listGame.add(game);
			} while (cursor.moveToNext());
		}
		return listGame;
	}

	public int getNumberTopic() {
		return getReadableDatabase().rawQuery("select * from MHOTopic", null)
				.getCount();
	}

	public int getNumberGameTopic() {
		return getReadableDatabase().rawQuery("select * from MHOTopicGame",
				null).getCount();
	}

	public void clearTopic() {
		String cmd1 = "delete from MHOTopic";
		String cmd2 = "delete from MHOTopicGame";
		getWritableDatabase().execSQL(cmd1);
		getWritableDatabase().execSQL(cmd2);
	}

	public boolean insertGameToFavorite(Game game, String email) {
		String checkCmd = "select * from MHOFavorite where Id = "
				+ game.getId() + " and Email = \'" + email + "\'";
		Cursor checkCursor = getReadableDatabase().rawQuery(checkCmd, null);
		if (checkCursor.getCount() > 0) {
			return false;
		} else {
			ContentValues cv = new ContentValues();
			cv.put("Email", email);
			cv.put("Id", game.getId());
			cv.put("Title", game.getTitle());
			cv.put("Picture", game.getPicture());
			cv.put("Description", game.getDes());
			cv.put("Size", game.getSize());
			cv.put("PictureAlbum", game.getPictureAlbum());
			cv.put("Detail", game.getDetail());
			getWritableDatabase().insert("MHOFavorite", null, cv);
			return true;
		}
	}

	public ArrayList<Game> getListFavoriteGame(String email) {
		ArrayList<Game> listGame = new ArrayList<Game>();
		String cmd = "select * from MHOFavorite where Email = \'" + email
				+ "\'";
		Cursor cursor = getReadableDatabase().rawQuery(cmd, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Game game = new Game();
				game.setId(cursor.getInt(1));
				game.setTitle(cursor.getString(2));
				game.setPicture(cursor.getString(3));
				game.setDes(cursor.getString(4));
				game.setSize(cursor.getString(5));
				game.setPictureAlbum(cursor.getString(6));
				game.setDetail(cursor.getString(7));
				listGame.add(game);
			} while (cursor.moveToNext());
		}
		return listGame;
	}

	public int getNumberFavorite(String email) {
		String cmd = "select * from MHOFavorite where Email = \'" + email
				+ "\'";
		Cursor cursor = getReadableDatabase().rawQuery(cmd, null);
		return cursor.getCount();
	}

	public void insertThread(MHOThread thread) {
		ContentValues cv = new ContentValues();
		cv.put("id", thread.getId());
		cv.put("name", thread.getName());
		cv.put("user", thread.getUser());
		cv.put("des", thread.getDes());
		cv.put("IdSystem", thread.getIdSystem());
		cv.put("Views", thread.getViews());
		getWritableDatabase().insert("MHOThread", null, cv);
	}

	public void insertGameToThread(Game game, MHOThread thread) {
		ContentValues cv = new ContentValues();
		cv.put("idt", thread.getId());
		cv.put("idg", game.getId());
		cv.put("Title", game.getTitle());
		cv.put("Picture", game.getPicture());
		cv.put("Description", game.getDes());
		cv.put("Size", game.getSize());
		cv.put("PictureAlbum", game.getPictureAlbum());
		cv.put("Detail", game.getDetail());
		getWritableDatabase().insert("MHOThreadGame", null, cv);
		DisplayUtils.log("DBH - Inserted " + game.getTitle() + " to thread "
				+ thread.getName());
	}

	public ArrayList<Game> getListGameInThread(MHOThread thread) {
		ArrayList<Game> listGame = new ArrayList<Game>();
		String cmd = "select * from MHOThreadGame where idt = "
				+ thread.getId();
		Cursor cursor = getReadableDatabase().rawQuery(cmd, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Game game = new Game();
				game.setId(cursor.getInt(1));
				game.setTitle(cursor.getString(2));
				game.setPicture(cursor.getString(3));
				game.setDes(cursor.getString(4));
				game.setSize(cursor.getString(5));
				game.setPictureAlbum(cursor.getString(6));
				game.setDetail(cursor.getString(7));
				listGame.add(game);
			} while (cursor.moveToNext());
		}
		return listGame;
	}

	public ArrayList<MHOThread> getAllThread() {
		ArrayList<MHOThread> listThread = new ArrayList<MHOThread>();
		String cmd = "select * from MHOThread";
		Cursor threadCursor = getReadableDatabase().rawQuery(cmd, null);
		if (threadCursor.getCount() > 0) {
			threadCursor.moveToFirst();
			do {
				MHOThread thread = new MHOThread();
				thread.setId(threadCursor.getInt(0));
				thread.setName(threadCursor.getString(1));
				thread.setUser(threadCursor.getString(2));
				thread.setDes(threadCursor.getString(3));
				thread.setIdSystem(threadCursor.getInt(4));
				thread.setViews(threadCursor.getInt(5));
				thread.setListGame(getListGameInThread(thread));
				listThread.add(thread);
			} while (threadCursor.moveToNext());
		}
		return listThread;
	}

	public void clearThread() {
		String cmd1 = "delete from MHOThread";
		String cmd2 = "delete from MHOThreadGame";
		getWritableDatabase().execSQL(cmd1);
		getWritableDatabase().execSQL(cmd2);
	}

	public boolean checkGameInDownloaded(Game game) {
		File file = new File(DataUtils.PATH_APP + "/" + game.getTitle()
				+ ".apk");
		boolean con1 = file.exists();
		String cmd = "select * from MHODownloadedGame where id = "
				+ game.getId();
		Cursor cursor = getReadableDatabase().rawQuery(cmd, null);
		boolean con2 = cursor.getCount() > 0;
		return con1 && con2;
	}

	public long insertGameToDownloaded(Game game) {

			ContentValues cv = new ContentValues();
			cv.put("id", game.getId());
			cv.put("Title", game.getTitle());
			cv.put("Picture", game.getPicture());
			cv.put("Description", game.getDes());
			cv.put("Size", game.getSize());
			cv.put("PictureAlbum", game.getPictureAlbum());
			cv.put("Detail", game.getDetail());
			return getWritableDatabase().insert("MHODownloadedGame", null, cv);
	}
	
	public ArrayList<Game> getListDownloadedGame(){
		ArrayList<Game> listGame = new ArrayList<Game>();
		String cmd = "select * from MHODownloadedGame";
		Cursor cursor = getWritableDatabase().rawQuery(cmd, null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			do{
				Game game = new Game();
				game.setId(cursor.getInt(0));
				game.setTitle(cursor.getString(1));
				game.setPicture(cursor.getString(2));
				game.setDes(cursor.getString(3));
				game.setSize(cursor.getString(4));
				game.setPictureAlbum(cursor.getString(5));
				game.setDetail(cursor.getString(6));
				listGame.add(game);
			}while(cursor.moveToNext());
		}
		return listGame;
	}
	
	public void clearDownloaded(){
		String cmd = "delete from MHODownloadedGame";
		getWritableDatabase().execSQL(cmd);
	}
	
	

}
