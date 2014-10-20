package com.maihoangonline.adapter;

import java.net.URLDecoder;
import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.mho.ListThreadActivity;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.utils.DataUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListThreadDetailAdapter extends ArrayAdapter<MHOThread> {

	ArrayList<MHOThread> listThread;
	Context c;
	int maxImage;

	public ListThreadDetailAdapter(Context context, int resource,
			ArrayList<MHOThread> objects) {
		super(context, resource, objects);
		c = context;
		listThread = objects;
		maxImage = DataUtils.SCREEN_WIDTH / 150;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_thread_detail, null);
		TextView tvThreadName = (TextView) v.findViewById(R.id.thread_name);
		tvThreadName.setText(URLDecoder.decode(listThread.get(position)
				.getName()));
		TextView tvThreadView = (TextView) v.findViewById(R.id.thread_view);
		tvThreadView.setText("Lượt xem:" + listThread.get(position).getViews());
		ImageView btDeleteThread = (ImageView) v
				.findViewById(R.id.delete_thread);
		btDeleteThread.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((ListThreadActivity) c).showDialogDeleteThread(listThread
						.get(position));
			}
		});
		if (listThread.get(position).getListGame().size() > 0) {
			LinearLayout llThreadContent = (LinearLayout) v
					.findViewById(R.id.thread_content);
			int usedImage;
			if (listThread.get(position).getListGame().size() > maxImage) {
				usedImage = maxImage;
			} else {
				usedImage = listThread.get(position).getListGame().size();
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					150, 150);
			layoutParams.setMargins(5, 5, 5, 5);
			for (int i = 0; i < usedImage; i++) {
				ImageView gameIcon = new ImageView(c);
				gameIcon.setLayoutParams(layoutParams);
				llThreadContent.addView(gameIcon);
				UrlImageViewHelper.setUrlDrawable(gameIcon,
						listThread.get(position).getListGame().get(i)
								.getPicture(), new UrlImageViewCallback() {

							@Override
							public void onLoaded(ImageView imageView,
									Bitmap loadedBitmap, String url,
									boolean loadedFromCache) {
								if (!loadedFromCache) {
									AlphaAnimation anim = new AlphaAnimation(
											0f, 1f);
									anim.setDuration(300);
									imageView.startAnimation(anim);
								}
							}
						});
			}
		}
		return v;
	}

}
