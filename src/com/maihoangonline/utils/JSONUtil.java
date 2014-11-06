package com.maihoangonline.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.util.Log;

public class JSONUtil {
	private static InputStream is = null;
	private static JSONObject jsonObject = null;
	private static String json = null;

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static JSONObject getJSONObject(String url, String[] tag,
			String[] value) {
		String url_get = url;
		for (int i = 0; i < tag.length; i++) {
			url_get = url_get + "&" + tag[i] + "=" + value[i];
		}
		DisplayUtils.log(url_get);
		StringBuilder sb = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url_get);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				json = sb.toString();
			} else
				DisplayUtils.log("Error");
		} catch (Exception e) {
			DisplayUtils.log(e.toString());
		}
		try {
			jsonObject = new JSONObject(json);
		} catch (Exception e) {
			DisplayUtils.log(e.toString());

		}

		return jsonObject;
	}

	public static String sendRequest(String url, String[] tag, String[] value) {
		String url_get = url;
		for (int i = 0; i < tag.length; i++) {
			url_get = url_get + "&" + tag[i] + "=" + value[i];
		}
		DisplayUtils.log(url_get);
		StringBuilder sb = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url_get);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				json = sb.toString();
			} else
				DisplayUtils.log("Error");
		} catch (Exception e) {
			DisplayUtils.log(e.toString());
		}
		return json;

	}

	public static JSONArray getJSONArr(String url, String[] tag, String[] value) {
		JSONArray jsonArr = null;
		String url_get = url;
		for (int i = 0; i < tag.length; i++) {
			url_get = url_get + "&" + tag[i] + "=" + value[i];
		}
		DisplayUtils.log(url_get);
		StringBuilder sb = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url_get);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				json = sb.toString();
			} else
				DisplayUtils.log("Error");
		} catch (Exception e) {
			DisplayUtils.log(e.toString());
		}
		try {
			jsonArr = new JSONArray(json);
		} catch (Exception e) {
			DisplayUtils.log(e.toString());

		}

		return jsonArr;
	}

	public static JSONObject getJSONObject(String url, List<NameValuePair> list) {
		try {
			DefaultHttpClient dhc = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(list));
			HttpResponse response = dhc.execute(httpPost);
			is = response.getEntity().getContent();
		} catch (Exception e) {
			Log.i("LOI 1", e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			do {
				line = reader.readLine();
				if (line != null)
					builder.append(line + "\n");
			} while (line != null);
			is.close();
			json = builder.toString();

		} catch (Exception e) {
			Log.i("LOI 2", e.toString());
		}
		try {

			jsonObject = new JSONObject(json);
		} catch (Exception e) {
			Log.i("LOI 3", e.toString());
			Log.i("LOI 3", json);
		}
		DisplayUtils.log("json " + json);
		return jsonObject;
	}

	public static void get(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void get(String url, JsonHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
}
