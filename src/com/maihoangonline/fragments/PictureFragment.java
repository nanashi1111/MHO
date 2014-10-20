package com.maihoangonline.fragments;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.maihoangonline.mho.R;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class PictureFragment extends Fragment {
	
	private String pictureLink;
	
	public PictureFragment(String pictureLink){
		this.pictureLink = pictureLink;
	}
	
	

	public String getPictureLink() {
		return pictureLink;
	}

	public void setPictureLink(String pictureLink) {
		this.pictureLink = pictureLink;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_picture, null);
		ImageView picture = (ImageView)rootView.findViewById(R.id.picture);
		//picture.setLayoutParams(new LinearLayout.LayoutParams(DataUtils.SCREEN_WIDTH, 229));
		UrlImageViewHelper.setUrlDrawable(picture, pictureLink, new UrlImageViewCallback() {
			
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
		return rootView;
	}
	
	

}
