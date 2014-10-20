package com.maihoangonline.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimationUtils {

	public static void setFadeInAnim(final View view) {
		AlphaAnimation fadein = new AlphaAnimation(0f, 1f);
		fadein.setDuration(200);
		fadein.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}
		});
		view.startAnimation(fadein);
	}

	public static void setFadeOutAnim(final View view) {
		AlphaAnimation fadeout = new AlphaAnimation(1f, 0f);
		fadeout.setDuration(200);
		fadeout.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.INVISIBLE);
			}
		});
		view.startAnimation(fadeout);

	}

}
