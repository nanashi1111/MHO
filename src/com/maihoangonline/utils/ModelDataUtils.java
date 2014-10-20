package com.maihoangonline.utils;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.maihoangonline.models.Game;
import com.maihoangonline.models.News;

public class ModelDataUtils {

	public static ArrayList<String> getPictureAlbum(Game game) {
		ArrayList<String> listPicture = new ArrayList<String>();
		Document doc = Jsoup.parse(game.getPictureAlbum());
		Elements media = doc.select("[src]");
		for (Element src : media) {
			if (src.tagName().equals("img")) {
				listPicture.add("http://mobile.mho.vn/" + src.attr("src"));
				
			}

		}
		return listPicture;
	}

	public static String getListPicture(News news, int screenWidth) {
		ArrayList<String> listPicture = new ArrayList<String>();
		Document doc = Jsoup.parse(news.getContent());
		Elements media = doc.select("[src]");
		for (Element src : media) {
			if (src.tagName().equals("img")||src.tagName().endsWith("iframe")) {
				listPicture.add(src.attr("src"));
				src.attr("width", Integer.toString(screenWidth));
				if(src.tagName().endsWith("iframe")){
					src.attr("height", "300");
				}
				//src.attr("height", "50");
			}

		}
		DisplayUtils.log("AFTER = "+doc.toString());
		return doc.toString();
		
	}

	public static ArrayList<String> getListFileName(ArrayList<String> listURL) {
		ArrayList<String> listFileName = new ArrayList<String>();
		for (int i = 0; i < listURL.size(); i++) {
			String fileName = listURL.get(i).substring(
					listURL.get(i).lastIndexOf("/") + 1,
					listURL.get(i).length());
			listFileName.add(fileName);
		}
		return listFileName;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String paramString,
			int paramInt1, int paramInt2) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(paramString, localOptions);
		localOptions.inSampleSize = calculateInSampleSize(localOptions,
				paramInt1, paramInt2);
		localOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(paramString, localOptions);
	}

}
