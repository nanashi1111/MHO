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
		View v = convertView;
		ViewHolder viewHolder;
		if (v == null) {
			LayoutInflater inf = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.row_category, null);
			viewHolder = new ViewHolder();
			viewHolder.ivCategoryBackground = (ImageView) v
					.findViewById(R.id.category_background);
			viewHolder.tvCategoryName = (TextView) v
					.findViewById(R.id.category_name);
			viewHolder.tvCategorySize = (TextView) v
					.findViewById(R.id.number_game);
			viewHolder.tvCategoryView = (TextView) v
					.findViewById(R.id.number_view);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}
		UrlImageViewHelper.setUrlDrawable(viewHolder.ivCategoryBackground,
				listCategory.get(position).getImage(),
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
		viewHolder.tvCategoryName.setText(listCategory.get(position).getName());
		viewHolder.tvCategorySize.setText(listCategory.get(position)
				.getSoLuong() + " game");
		viewHolder.tvCategoryView.setText(listCategory.get(position).getView()
				+ " lượt xem");
		return v;
	}

	public ArrayList<Category> getArrayList() {
		return listCategory;
	}

	private static class ViewHolder {
		ImageView ivCategoryBackground;
		TextView tvCategoryName;
		TextView tvCategorySize;
		TextView tvCategoryView;
	}

}
