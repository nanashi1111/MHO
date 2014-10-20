package com.maihoangonline.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Game;

public class ListGameAdapter extends ArrayAdapter<Game> {

	private ArrayList<Game> listGame;
	private Context c;

	public ListGameAdapter(Context context, int textViewResourceId,
			ArrayList<Game> listGame) {
		super(context, textViewResourceId, listGame);
		c = context;
		this.listGame = listGame;

	}

	@Override
	public int getCount() {
		return listGame.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_general_game_info, null);
		ImageView ivGameLogo = (ImageView) v.findViewById(R.id.game_logo);
		TextView tvGameName = (TextView) v.findViewById(R.id.game_name);
		TextView tvGameSize = (TextView) v.findViewById(R.id.game_size);
		TextView tvGameDescription = (TextView) v
				.findViewById(R.id.game_description);
		ImageView[] star = new ImageView[5];
		star[0] = (ImageView) v.findViewById(R.id.star1);
		star[1] = (ImageView) v.findViewById(R.id.star2);
		star[2] = (ImageView) v.findViewById(R.id.star3);
		star[3] = (ImageView) v.findViewById(R.id.star4);
		star[4] = (ImageView) v.findViewById(R.id.star5);
		/*
		 * int rate = listGame.get(position).getRate();
		 * 
		 * if(rate<=4){ for(int i=rate;i<5;i++){
		 * star[i].setVisibility(View.GONE); } }
		 */
		// ivGameLogo.setImageBitmap(listLogo.get(position));
		UrlImageViewHelper.setUrlDrawable(ivGameLogo, listGame.get(position)
				.getPicture(), new UrlImageViewCallback() {
					
					@Override
					public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
							boolean loadedFromCache) {
						if(!loadedFromCache){
							AlphaAnimation anim = new AlphaAnimation(0f, 1f);
							anim.setDuration(300);
							imageView.startAnimation(anim);
						}
					}
				});
		tvGameName.setText(listGame.get(position).getTitle());
		tvGameSize.setText(listGame.get(position).getSize());
		tvGameDescription.setText(Html.fromHtml(listGame.get(position)
				.getDes()));
		return v;
	}

}
