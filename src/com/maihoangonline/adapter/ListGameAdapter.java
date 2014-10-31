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

		View v = convertView;
		ViewHolder viewHolder;
		if (v == null) {
			LayoutInflater inf = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.row_general_game_info, null);
			viewHolder = new ViewHolder();
			viewHolder.ivGameLogo = (ImageView) v.findViewById(R.id.game_logo);
			viewHolder.tvGameName = (TextView) v.findViewById(R.id.game_name);
			viewHolder.tvGameSize = (TextView) v.findViewById(R.id.game_size);
			viewHolder.tvGameDescription = (TextView) v
					.findViewById(R.id.game_description);
			viewHolder.star = new ImageView[5];
			viewHolder.star[0] = (ImageView) v.findViewById(R.id.star1);
			viewHolder.star[1] = (ImageView) v.findViewById(R.id.star2);
			viewHolder.star[2] = (ImageView) v.findViewById(R.id.star3);
			viewHolder.star[3] = (ImageView) v.findViewById(R.id.star4);
			viewHolder.star[4] = (ImageView) v.findViewById(R.id.star5);
			/*
			 * int rate = listGame.get(position).getRate();
			 * 
			 * if(rate<=4){ for(int i=rate;i<5;i++){
			 * star[i].setVisibility(View.GONE); } }
			 */
			// ivGameLogo.setImageBitmap(listLogo.get(position));

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}
		UrlImageViewHelper.setUrlDrawable(viewHolder.ivGameLogo,
				listGame.get(position).getPicture(),
				new UrlImageViewCallback() {

					@Override
					public void onLoaded(ImageView imageView,
							Bitmap loadedBitmap, String url,
							boolean loadedFromCache) {
						if (!loadedFromCache) {
							AlphaAnimation anim = new AlphaAnimation(0f, 1f);
							anim.setDuration(300);
							imageView.startAnimation(anim);
						}
					}
				});
		viewHolder.tvGameName.setText(listGame.get(position).getTitle());
		viewHolder.tvGameSize.setText(listGame.get(position).getSize());
		viewHolder.tvGameDescription.setText(Html.fromHtml(listGame.get(
				position).getDes()));
		return v;
	}

	private static class ViewHolder {
		ImageView ivGameLogo;
		TextView tvGameName;
		TextView tvGameSize;
		TextView tvGameDescription;
		ImageView[] star;

	}

}
