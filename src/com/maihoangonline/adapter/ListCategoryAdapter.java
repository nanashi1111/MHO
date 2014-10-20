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
import com.maihoangonline.models.Category;

public class ListCategoryAdapter extends ArrayAdapter<Category> {

	Context c;
	ArrayList<Category> listCategory;

	public ListCategoryAdapter(Context context, int resource,
			ArrayList<Category> objects) {
		super(context, resource, objects);
		this.c = context;
		this.listCategory = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_category, null);
		ImageView ivCategoryBackground = (ImageView) v
				.findViewById(R.id.category_background);
		UrlImageViewHelper.setUrlDrawable(ivCategoryBackground, listCategory
				.get(position).getImage(), new UrlImageViewCallback() {
					
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
		TextView tvCategoryName = (TextView) v.findViewById(R.id.category_name);
		tvCategoryName.setText(listCategory.get(position).getName());
		TextView tvCategorySize = (TextView) v.findViewById(R.id.number_game);
		tvCategorySize.setText(listCategory.get(position).getSoLuong()
				+ " game");
		TextView tvCategoryView = (TextView) v.findViewById(R.id.number_view);
		tvCategoryView.setText(listCategory.get(position).getView()
				+ " lượt xem");

		return v;
	}
	
	public ArrayList<Category> getArrayList(){
		return listCategory;
	}

}
