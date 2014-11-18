package com.maihoangonline.adapter;

import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.utils.DisplayUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class BannerAdapter extends ArrayAdapter<String> {

	Context c;
	ArrayList<String> listLink;
	Gallery gal;

	public BannerAdapter(Context context, int resource,
			ArrayList<String> objects, Gallery gal) {
		super(context, resource, objects);
		c = context;
		listLink = objects;
		this.gal=gal;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		/*View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_banner, null);
		ImageView ivBanner = (ImageView) v.findViewById(R.id.banner);
		UrlImageViewHelper.setUrlDrawable(ivBanner, listLink.get(position),
				new UrlImageViewCallback() {

					@Override
					public void onLoaded(ImageView imageView,
							Bitmap loadedBitmap, String url,
							boolean loadedFromCache) {
						DisplayUtils.log("Loaded:"+listLink.get(position));
						if (!loadedFromCache) {
							AlphaAnimation anim = new AlphaAnimation(0f, 1f);
							anim.setDuration(300);
							imageView.startAnimation(anim);
						}
					}
				});*/
		ImageView ivBanner = new ImageView(c);
		ivBanner.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, gal.getLayoutParams().height));
		ivBanner.setScaleType(ImageView.ScaleType.FIT_XY);
		UrlImageViewHelper.setUrlDrawable(ivBanner, listLink.get(position),
				new UrlImageViewCallback() {

					@Override
					public void onLoaded(ImageView imageView,
							Bitmap loadedBitmap, String url,
							boolean loadedFromCache) {
						DisplayUtils.log("Loaded:"+listLink.get(position));
						if (!loadedFromCache) {
							AlphaAnimation anim = new AlphaAnimation(0f, 1f);
							anim.setDuration(300);
							imageView.startAnimation(anim);
						}
					}
				});
		
		return ivBanner;
	}

}
