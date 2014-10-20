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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.mho.R;
import com.maihoangonline.models.Topic;
import com.maihoangonline.utils.DataUtils;

public class ListTopicAdapter extends ArrayAdapter<Topic> {

	Context c;
	ArrayList<Topic> listTopic;
	int maxImage;

	public ListTopicAdapter(Context context, int resource,
			ArrayList<Topic> objects) {
		super(context, resource, objects);
		c = context;
		listTopic = objects;
		maxImage = DataUtils.SCREEN_WIDTH / 150;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_topic, null);
		TextView tvTopicName = (TextView) v.findViewById(R.id.topic_name);
		tvTopicName.setText(listTopic.get(position).getName());
		if (listTopic.get(position).getListGame().size() > 0) {
			LinearLayout llTopicContent = (LinearLayout) v
					.findViewById(R.id.topic_content);
			int usedImage;
			if (listTopic.get(position).getListGame().size() > maxImage) {
				usedImage = maxImage;
			} else {
				usedImage = listTopic.get(position).getListGame().size();
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					150, 150);
			layoutParams.setMargins(5, 5, 5, 5);
			for (int i = 0; i < usedImage; i++) {
				ImageView gameIcon = new ImageView(c);
				gameIcon.setLayoutParams(layoutParams);
				llTopicContent.addView(gameIcon);
				UrlImageViewHelper.setUrlDrawable(gameIcon,
						listTopic.get(position).getListGame().get(i)
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
			}
		}
		return v;
	}

}
