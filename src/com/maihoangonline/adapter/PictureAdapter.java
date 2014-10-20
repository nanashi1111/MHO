package com.maihoangonline.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maihoangonline.fragments.PictureFragment;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.ModelDataUtils;

public class PictureAdapter extends FragmentStatePagerAdapter{
	
	private Game game;
	private ArrayList<String> listPicture;

	public PictureAdapter(FragmentManager fm, Game game) {
		super(fm);
		this.game = game;
		listPicture= ModelDataUtils.getPictureAlbum(game);
	}

	@Override
	public Fragment getItem(int arg0) {
		return new PictureFragment(listPicture.get(arg0));
	}

	@Override
	public int getCount() {
		return listPicture.size();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public ArrayList<String> getListPicture() {
		return listPicture;
	}

	public void setListPicture(ArrayList<String> listPicture) {
		this.listPicture = listPicture;
	}
	
	

}
