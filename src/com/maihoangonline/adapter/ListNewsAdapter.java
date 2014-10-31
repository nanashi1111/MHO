package com.maihoangonline.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.maihoangonline.models.News;

public class ListNewsAdapter extends ArrayAdapter<News> {

	Context context;
	ArrayList<News> listNews;

	public ListNewsAdapter(Context context, int resource,
			ArrayList<News> objects) {
		super(context, resource, objects);
		this.context = context;
		this.listNews = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder viewHolder;
		if(v==null){
		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_news, null);
		viewHolder = new ViewHolder();
		viewHolder.ivLogo = (ImageView) v.findViewById(R.id.new_logo);
		viewHolder.tvTitle = (TextView) v.findViewById(R.id.news_title);
		viewHolder.tvDescription = (TextView) v
				.findViewById(R.id.news_description);
		viewHolder.tvPubdate = (TextView) v.findViewById(R.id.news_pubdate);
		v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		UrlImageViewHelper.setUrlDrawable(viewHolder.ivLogo, listNews.get(position)
				.getImagePath(), new UrlImageViewCallback() {
					
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
		viewHolder.tvTitle.setText(listNews.get(position).getTitle());
		viewHolder.tvDescription.setText(listNews.get(position).getLead());
		viewHolder.tvPubdate.setText("19-06-2014");
		return v;
	}
	
	@Override
	public int getCount() {
		return listNews.size();
	}
	
	private static class ViewHolder{
		ImageView ivLogo;
		TextView tvTitle;
		TextView tvDescription;
		TextView tvPubdate;
	}

}
