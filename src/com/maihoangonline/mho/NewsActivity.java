package com.maihoangonline.mho;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.maihoangonline.models.News;
import com.maihoangonline.utils.DisplayUtils;
import com.maihoangonline.utils.ModelDataUtils;
import com.maihoangonline.utils.ServiceConnection;

public class NewsActivity extends BaseActivity implements OnClickListener {
	/*private TextView tvNews;
	private TextView tvTitle;*/
	private News news;
	private ProgressDialog d;
	/*private SpannableStringBuilder html;
	private ArrayList<String> listFileName = new ArrayList<String>();*/

	WebView wv;
	private ActionBar bar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		wv = (WebView) findViewById(R.id.web);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		// wv.getSettings().setUseWideViewPort(true);
		// wv.getSettings().setLoadWithOverviewMode(true);
		// wv.setInitialScale(1);
		wv.setHorizontalScrollBarEnabled(false);
		wv.setScrollContainer(false);
		news = (News) getIntent().getSerializableExtra("news");
		setupActionBar();
		new LoadNewsInfo().execute(news);

		/*
		 * loadData(); setupView(); new LoadNewsInfo().execute(news);
		 */
	}

	private void setupActionBar() {

		bar = getSupportActionBar();
		bar.setCustomView(R.layout.actionbar_other);
		bar.setHomeButtonEnabled(false);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME);
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
		ImageView btBack = (ImageView) bar.getCustomView().findViewById(
				R.id.back);
		btBack.setOnClickListener(this);
		TextView tvTitle = (TextView) bar.getCustomView().findViewById(
				R.id.title);
		tvTitle.setText(news.getTitle());

	}

	private class LoadNewsInfo extends AsyncTask<News, Void, Void> {

		JSONObject jsonContent;
		int screenWidth;

		@Override
		protected Void doInBackground(News... params) {
			jsonContent = ServiceConnection.getNewsDetail(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				news.setContent(jsonContent.getString("Content"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

			news.setContent(ModelDataUtils.getListPicture(news, 340));
			wv.loadDataWithBaseURL(null, news.getContent(), "text/html",
					"UTF-8", null);
			if (d != null) {
				d.dismiss();
				d = null;
			}

		}

		@SuppressLint("NewApi")
		@Override
		protected void onPreExecute() {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
			DisplayUtils.log("Screen width = " + screenWidth
					+ " pixcels - Screen height = " + size.y + " pixcels");
			d = ProgressDialog.show(NewsActivity.this, "",
					"Đang tải nội dung tin tức");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(0, 0);
			break;
		}
	}

	/*
	 * private void setupView() { tvNews = (TextView) findViewById(R.id.news);
	 * tvTitle = (TextView) findViewById(R.id.title);
	 * tvTitle.setText(news.getTitle()); }
	 * 
	 * private void loadData() { news = (News)
	 * getIntent().getSerializableExtra("news");
	 * 
	 * }
	 * 
	 * private class LoadNewsInfo extends AsyncTask<News, Void, Void> {
	 * 
	 * JSONObject jsonContent;
	 * 
	 * @Override protected Void doInBackground(News... params) { jsonContent =
	 * ServiceConnection.getNewsDetail(params[0]); return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { try {
	 * news.setContent(jsonContent.getString("Content")); } catch (JSONException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } new
	 * LoadImageTask().execute(); }
	 * 
	 * @Override protected void onPreExecute() { d =
	 * ProgressDialog.show(NewsActivity.this, "", "Đang tải nội dung tin tức");
	 * }
	 * 
	 * }
	 * 
	 * private class LoadImageTask extends AsyncTask<Void, ImageSpan, Void>{
	 * 
	 * private int count = 0; DisplayMetrics metrics = new DisplayMetrics();
	 * 
	 * @Override protected Void doInBackground(Void... params) { ImageSpan[]
	 * image = (ImageSpan[])html.getSpans(0, html.length(), ImageSpan.class);
	 * int listImageSize = image.length; for(int i=0;i<listImageSize;i++){
	 * ImageSpan imageSpan = image[i]; String url = imageSpan.getSource();
	 * ServiceConnection.download(url, DataUtils.PATH_IMG,
	 * url.substring(url.lastIndexOf("/"), url.length()));
	 * listFileName.add(url.substring(url.lastIndexOf("/"), url.length()));
	 * publishProgress(imageSpan); } return null; }
	 * 
	 * 
	 * 
	 * @Override protected void onProgressUpdate(ImageSpan... values) { count++;
	 * try{ ImageSpan imageSpan = values[0]; File image = new
	 * File(DataUtils.PATH_IMG+listFileName.get(count-1)); //BitmapDrawable
	 * bitmapDrawable = new BitmapDrawable(getResources(),
	 * image.getAbsolutePath()); Bitmap bitmap =
	 * ModelDataUtils.decodeSampledBitmapFromFile(image.getAbsolutePath(),
	 * metrics.widthPixels, metrics.heightPixels); BitmapDrawable bitmapDrawable
	 * = new BitmapDrawable(getResources(), bitmap); int width =
	 * (int)(bitmapDrawable.getIntrinsicWidth()*metrics.density); int height =
	 * (int)(bitmapDrawable.getIntrinsicHeight()*metrics.density); int ratio=1;
	 * if(width>metrics.widthPixels){ ratio =
	 * bitmapDrawable.getIntrinsicHeight() * metrics.widthPixels /
	 * bitmapDrawable.getIntrinsicWidth(); }else{ ratio = height; }
	 * bitmapDrawable.setBounds(0, 0, metrics.widthPixels, ratio); ImageSpan
	 * resized = new ImageSpan(bitmapDrawable, imageSpan.getSource()); int start
	 * = html.getSpanStart(imageSpan); int end = html.getSpanEnd(imageSpan);
	 * html.removeSpan(imageSpan); html.setSpan(resized, start, end, 33);
	 * tvNews.setText(html); }catch(Exception e){ e.printStackTrace(); } return;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @Override protected void onPostExecute(Void result) { if(d!=null){
	 * d.dismiss(); d=null; } }
	 * 
	 * @Override protected void onPreExecute() {
	 * getWindowManager().getDefaultDisplay().getMetrics(metrics); Spanned
	 * htmlSpan = Html.fromHtml(news.getContent()); if(htmlSpan instanceof
	 * SpannableStringBuilder){ html = (SpannableStringBuilder)htmlSpan; }else{
	 * html = new SpannableStringBuilder(htmlSpan); } }
	 * 
	 * }
	 */

}
