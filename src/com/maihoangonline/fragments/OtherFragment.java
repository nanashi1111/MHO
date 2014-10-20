package com.maihoangonline.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maihoangonline.mho.R;
import com.maihoangonline.utils.DataUtils;

@SuppressLint("ValidFragment")
public class OtherFragment extends Fragment {

	private View rootView;
	private ImageView banner;

	public static OtherFragment INSTANCE;

	private OtherFragment() {

	}

	public static OtherFragment getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OtherFragment();
		}
		return INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_other, null);
		banner = (ImageView) rootView.findViewById(R.id.banner);
		scaleBanner();
		return rootView;
	}

	private void scaleBanner() {

		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.banner_demo);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		DataUtils.SCREEN_WIDTH = width;
		float ratio = ((float) width) / b.getWidth();
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(b,
				(int) (b.getWidth() * ratio), (int) (b.getHeight() * ratio),
				true);
		banner.setImageBitmap(resizedBitmap);
	}

}
