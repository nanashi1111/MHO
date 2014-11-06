package com.maihoangonline.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.maihoangonline.models.Category;
import com.maihoangonline.models.Game;
import com.maihoangonline.models.MHOThread;
import com.maihoangonline.models.News;
import com.maihoangonline.models.Topic;

public class ServiceConnection {

	// Link API
	private static final String URL_LOGIN = "http://api.mho.vn/services/account?type=login";
	private static final String URL_SIGNUP = "http://api.mho.vn/services/account?type=register";
	private static final String URL_GET_USER_INFO = "http://paygate.mho.vn/AccountInfo/GetInfo?serviceId=200001";
	private static final String URL_GET_GAME_HOT = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=1&isHot=1&isNews=0&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_APP_HOT = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=2&isHot=1&isNews=0&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_VOTE = "http://mobile.mho.vn/services/Rating?key=mho123190456$@!";
	private static final String URL_GET_RELATED = "http://mobile.mho.vn/services/Game?IdSystem=1&IdGroup=1&isHot=0&isNews=0&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_LIST_CATEGORY_APP = "http://mobile.mho.vn/services/cat?IdNhom=2&IdSystem=1&key=mho123190456$@!";
	private static final String URL_GET_LIST_CATEGORY_GAME = "http://mobile.mho.vn/services/cat?IdNhom=1&IdSystem=1&key=mho123190456$@!";
	private static final String URL_GET_LIST_CATEGORY_GAME_ONLINE = "http://mobile.mho.vn/services/cat?IdNhom=3&IdSystem=1&key=mho123190456$@!";
	private static final String URL_GET_APP_RANK = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=2&isHot=0&isNews=0&isMostView=1&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_GAME_RANK = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=1&isHot=0&isNews=0&isMostView=1&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_GAME_IN_CATEGORY = "http://mobile.mho.vn/services/Game?IdSystem=1&IdGroup=1&isHot=0&isNews=0&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_GAME_REVIEW = "http://mobile.mho.vn/services/news?Type=4&pageSize=20&key=mho123190456$@!";
	private static final String URL_GET_NEWS = "http://api.mho.vn/services/news?gameid=0&pagesize=20";
	private static final String URL_NEWS_DETAIL = "http://api.mho.vn/services/news?";
	private static final String URL_SEARCH = "http://mobile.mho.vn/services/searchgame?IdSystem=1&s=20&key=mho123190456$@!";
	private static final String URL_INC_VIEW_GAME = "http://api.mho.vn/services/gameex?type=incview&key=mho123190456$@!";
	private static final String URL_GET_NEWEST_TOPIC = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=0&isHot=0&isNews=1&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_LIST_TOPIC = "http://mobile.mho.vn/services/TopicCat?IdCat=0&key=mho123190456$@!";
	private static final String URL_GET_GAME_IN_TOPIC = "http://mobile.mho.vn/services/topic?IdSystem=1&IdGroup=1&pageSize=10&key=mho123190456$@!";
	private static final String URL_PUBLIC_GIFT = "http://mobile.mho.vn/services/GameQT?idsystem=1&pageSize=10&key=mho123190456$@!";
	private static final String URL_GET_GIFT = "http://mobile.mho.vn/services/GiftCode?key=mho123190456$@!";
	private static final String URL_GET_LIST_THREAD = "http://mobile.mho.vn/services/chude?IdSystem=1&pageIndex=0&pageSize=100&key=mho123190456$@!";
	private static final String URL_CREATE_THREAD = "http://mobile.mho.vn/services/ChuDe?Type=1&IdSystem=1&key=mho123190456$@!";
	private static final String URL_ADD_GAME_TO_THREAD = "http://mobile.mho.vn/services/ChuDe?Type=1&key=mho123190456$@!";
	private static final String URL_GET_GAME_IN_THREAD = "http://mobile.mho.vn/services/chude?gameid=0&pageIndex=0&pageSize=100&key=mho123190456$@!";
	private static final String URL_DELETE_THREAD = "http://mobile.mho.vn/services/ChuDe?Type=3&IdSystem=1&Name=1&Des=1&key=mho123190456$@!";
	private static final String URL_GET_GAME_NEW = "http://mobile.mho.vn/services/Game?IdCategoryGame=0&IdSystem=1&IdGroup=1&isHot=0&isNews=1&isMostView=0&pageSize=10&key=mho123190456$@!";
	private static final String URL_PAY_GOLD = "http://paygate.mho.vn/Recharge/Card";
	private static final String URL_SEND_DEVICE_TOKEN = "http://mobile.mho.vn/services/NotificationTongBu?IdSystem=1&key=mho123190456$@!";
	private static final String URL_TRANSFER_GOLD = "http://paygate.mho.vn/Transfer/MHOTransferGoldToUser";
	private static final String URL_GET_ACC_INFO = "http://paygate.mho.vn/AccountInfo/GetInfo";
	private static final String URL_GET_HISTORY_TRANSACTION = "http://paygate.mho.vn/Transfer/HistoryTransaction?pageSize=10";
	private static final String URL_GET_IP_WAN = "http://checkip.amazonaws.com";
	private static final String URL_UPDATE_INFO = "http://mobile.mho.vn/services/updateApp?Idsystem=5&key=mho123190456$@!";
	// Constant
	private static final String SERVICED = "2";
	private static final String SERVICE_TOKEN = "757c7aa7-2c95-4f5f-910c-54c862f1b271";

	public static JSONObject login(String email, String password) {
		return JSONUtil.getJSONObject(URL_LOGIN, new String[] { "serviceID",
				"servicekey", "email", "pwd" }, new String[] { "200001",
				"BE543DBC2336", email, password });
	}

	public static JSONObject signUp(String email, String pass, String phone,
			String pemail) {
		return JSONUtil.getJSONObject(URL_SIGNUP, new String[] { "serviceID",
				"servicekey", "email", "pwd", "phone", "pemail" },
				new String[] { "200001", "BE543DBC2336", email, pass, phone,
						pemail });
	}

	public static JSONObject getUserInfo(String email, String token) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userEmail", email));
		list.add(new BasicNameValuePair("token", token));
		return JSONUtil.getJSONObject(URL_GET_USER_INFO, list);
	}

	/*
	 * public static JSONObject getListGameHot(int pageIndex) { return
	 * JSONUtil.getJSONObject(URL_GET_GAME_HOT, new String[] { "pageIndex" },
	 * new String[] { Integer.toString(pageIndex) }); }
	 */

	public static void getListGameHot(int pageIndex,
			JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_GAME_HOT, params, responseHandler);
	}

	/*
	 * public static JSONObject getListAppHot(int pageIndex) { return
	 * JSONUtil.getJSONObject(URL_GET_APP_HOT, new String[] { "pageIndex" }, new
	 * String[] { Integer.toString(pageIndex) }); }
	 */

	public static void getListAppHot(int pageIndex,
			JsonHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_APP_HOT, params, responseHandler);
	}

	/*
	 * public static String vote(Game game, int rate) { return
	 * JSONUtil.sendRequest(URL_VOTE, new String[] { "IdGame", "Rating" }, new
	 * String[] { game.getId() + "", rate + "" }); }
	 */

	public static void vote(Game game, int rate,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("IdGame", game.getId());
		params.put("Rating", rate);
		JSONUtil.get(URL_VOTE, params, handler);
	}

	/*
	 * public static JSONObject getListRelated(Game game, int pageIndex) {
	 * return JSONUtil.getJSONObject( URL_GET_RELATED, new String[] { "IdGame",
	 * "IdCategoryGame", "pageIndex" }, new String[] {
	 * Integer.toString(game.getId()),
	 * Integer.toString(game.getIdCategoryGame()), Integer.toString(pageIndex)
	 * }); }
	 */

	public static void getListRelated(Game game, int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("IdGame", game.getId());
		params.put("IdCategoryGame", game.getIdCategoryGame());
		params.put("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_RELATED, params, handler);
	}

	/*
	 * public static JSONObject getListCategoryApp() { return
	 * JSONUtil.getJSONObject(URL_GET_LIST_CATEGORY_APP, new String[] {}, new
	 * String[] {}); }
	 */

	public static void getListCategoryApp(JsonHttpResponseHandler handler) {
		JSONUtil.get(URL_GET_LIST_CATEGORY_APP, handler);
	}

	/*
	 * public static JSONObject getListCategoryGame() { return
	 * JSONUtil.getJSONObject(URL_GET_LIST_CATEGORY_GAME, new String[] {}, new
	 * String[] {}); }
	 */

	public static void getListCategoryGame(JsonHttpResponseHandler handler) {
		JSONUtil.get(URL_GET_LIST_CATEGORY_GAME, handler);
	}

	/*
	 * public static JSONObject getListCategoryGameOnline() { return
	 * JSONUtil.getJSONObject(URL_GET_LIST_CATEGORY_GAME_ONLINE, new String[]
	 * {}, new String[] {}); }
	 */

	public static void getListCategoryGameOnline(JsonHttpResponseHandler handler) {
		JSONUtil.get(URL_GET_LIST_CATEGORY_GAME_ONLINE, handler);
	}

	/*
	 * public static JSONObject getRankGame(int pageIndex) { return
	 * JSONUtil.getJSONObject(URL_GET_GAME_RANK, new String[] { "pageIndex" },
	 * new String[] { Integer.toString(pageIndex) }); }
	 */

	public static void getRankGame(int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_GAME_RANK, params, handler);
	}

	/*
	 * public static JSONObject getRankApp(int pageIndex) { return
	 * JSONUtil.getJSONObject(URL_GET_APP_RANK, new String[] { "pageIndex" },
	 * new String[] { Integer.toString(pageIndex) }); }
	 */

	public static void getRankApp(int pageIndex, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_APP_RANK, params, handler);
	}

	/*
	 * public static JSONObject getListGameInCategory(Category cat, int
	 * pageIndex) { return JSONUtil.getJSONObject( URL_GET_GAME_IN_CATEGORY, new
	 * String[] { "IdCategoryGame", "pageIndex" }, new String[] {
	 * Integer.toString(cat.getId()), Integer.toString(pageIndex) }); }
	 */

	public static void getListGameInCategory(Category cat, int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("IdCategoryGame", cat.getId());
		params.put("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_GAME_IN_CATEGORY, params, handler);
	}

	public static JSONObject getListGameReview(int pageIndex) {
		return JSONUtil.getJSONObject(URL_GAME_REVIEW,
				new String[] { "pageIndex" },
				new String[] { Integer.toString(pageIndex) });
	}

	/*
	 * public static JSONObject getListNesw(int pageIndex) { return
	 * JSONUtil.getJSONObject(URL_GET_NEWS, new String[] { "pageIndex" }, new
	 * String[] { Integer.toString(pageIndex) }); }
	 */

	public static void getListNews(int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_NEWS, params, handler);
	}

	public static JSONObject getNewsDetail(News news) {
		return JSONUtil.getJSONObject(URL_NEWS_DETAIL,
				new String[] { "newsid" },
				new String[] { Integer.toString(news.getID()) });
	}

	/*
	 * public static JSONObject search(String q, int p) { return
	 * JSONUtil.getJSONObject(URL_SEARCH, new String[] { "q", "p" }, new
	 * String[] { q, Integer.toString(p) }); }
	 */

	public static void search(String q, int p, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("q", q);
		params.put("p", p);
		JSONUtil.get(URL_SEARCH, params, handler);
	}

	/*
	 * public static JSONObject incViewGame(Game game) { return
	 * JSONUtil.getJSONObject(URL_INC_VIEW_GAME, new String[] { "g" }, new
	 * String[] { Integer.toString(game.getId()) }); }
	 */

	public static void incViewGame(Game game, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("g", game.getId());
		JSONUtil.get(URL_INC_VIEW_GAME, params, handler);
	}

	public static JSONObject incViewCategory(Category category) {
		return null;

	}

	public static JSONObject getNewestTopic(int pageIndex) {
		return JSONUtil.getJSONObject(URL_GET_NEWEST_TOPIC,
				new String[] { "pageIndex" },
				new String[] { Integer.toString(pageIndex) });
	}

	public static JSONObject getListTopic() {
		return JSONUtil.getJSONObject(URL_GET_LIST_TOPIC, new String[] {},
				new String[] {});
	}

	public static JSONObject getListGameInTopic(Topic topic, int pageIndex) {
		return JSONUtil.getJSONObject(
				URL_GET_GAME_IN_TOPIC,
				new String[] { "IdToppic", "pageIndex" },
				new String[] { Integer.toString(topic.getId()),
						Integer.toString(pageIndex) });
	}

	public static void getListGameInTopic(Topic topic, int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("IdToppic", topic.getId());
		params.put("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_GAME_IN_TOPIC, params, handler);
	}

	/*
	 * public static JSONObject getPublicGift() { return
	 * JSONUtil.getJSONObject(URL_PUBLIC_GIFT, new String[] {}, new String[]
	 * {}); }
	 */

	public static void getPublicGift(JsonHttpResponseHandler handler) {
		JSONUtil.get(URL_PUBLIC_GIFT, handler);
	}

	public static JSONObject getGift(String email, Game game) {
		return JSONUtil.getJSONObject(URL_GET_GIFT, new String[] { "Email",
				"GameId" },
				new String[] { email, Integer.toString(game.getId()) });
	}

	/*
	 * public static JSONObject getListThread(String email) { return
	 * JSONUtil.getJSONObject(URL_GET_LIST_THREAD, new String[] { "User" }, new
	 * String[] { email }); }
	 */

	public static void getListThread(String email,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams("User", email);
		JSONUtil.get(URL_GET_LIST_THREAD, params, handler);
	}

	/*
	 * @SuppressWarnings("deprecation") public static String createThread(int
	 * Id, String email, String name, String des) { return
	 * JSONUtil.sendRequest(URL_CREATE_THREAD, new String[] { "Id", "Email",
	 * "Name", "Des" }, new String[] { Integer.toString(Id), email,
	 * URLEncoder.encode(name), URLEncoder.encode(des) }); }
	 */

	@SuppressWarnings("deprecation")
	public static void createThread(int Id, String email, String name,
			String des, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("Id", Id);
		params.put("Email", email);
		params.put("Name", URLEncoder.encode(name));
		params.put("Des", URLEncoder.encode(des));
		JSONUtil.get(URL_CREATE_THREAD, params, handler);
	}

	/*
	 * public static String addGameToThread(Game game, MHOThread thread) {
	 * return JSONUtil.sendRequest( URL_ADD_GAME_TO_THREAD, new String[] {
	 * "GameId", "ChuDeId" }, new String[] { Integer.toString(game.getId()),
	 * Integer.toString(thread.getId()) }); }
	 */

	public static void addGameToThread(Game game, MHOThread thread,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("GameId", game.getId());
		params.put("ChuDeId", thread.getId());
		JSONUtil.get(URL_ADD_GAME_TO_THREAD, params, handler);
	}

	public static void getListNewGameApp(int pageIndex,
			JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams("pageIndex", pageIndex);
		JSONUtil.get(URL_GET_GAME_NEW, params, handler);
	}

	public static JSONObject getGameInThread(MHOThread thread) {
		return JSONUtil.getJSONObject(URL_GET_GAME_IN_THREAD,
				new String[] { "chudeid" },
				new String[] { Integer.toString(thread.getId()) });
	}

	public static void payGold(int typeCard, String codeCard,
			String serialCard, int userId, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("typecard", typeCard);
		params.put("codecard", codeCard);
		params.put("serialcard", serialCard);
		params.put("userid", userId);
		params.put("serviceid", SERVICED);
		params.put(
				"token",
				ModelDataUtils.md5(Integer.toString(typeCard) + codeCard
						+ serialCard + Integer.toString(userId) + SERVICED
						+ SERVICE_TOKEN));
		JSONUtil.get(URL_PAY_GOLD, params, handler);
		
	}

	public static void transferGold(int userId, String password,
			String toUserIdOrMail, int gold, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("fromUserId", userId);
		params.put("password", ModelDataUtils.md5(password));
		params.put("toUserIdOrEmail", toUserIdOrMail);
		params.put("goldTransfer", gold);
		params.put("serviceid", SERVICED);
		params.put(
				"token",
				ModelDataUtils.md5(Integer.toString(userId)
						+ ModelDataUtils.md5(password) + toUserIdOrMail
						+ Integer.toString(gold) + SERVICED + SERVICE_TOKEN));
		JSONUtil.get(URL_TRANSFER_GOLD, params, handler);
		
	}

	public static void sendDeviceToken(String token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("DeviceId", token);
		JSONUtil.get(URL_SEND_DEVICE_TOKEN, params, handler);
	}

	public static void deleteThread(MHOThread thread, String email,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("Id", thread.getId());
		params.put("Email", email);
		JSONUtil.get(URL_DELETE_THREAD, params, handler);
	}

	public static void getAccInfo(String email, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userEmail", email);
		params.put("serviceid", SERVICED);
		params.put("token",
				ModelDataUtils.md5(email + SERVICED + SERVICE_TOKEN));
		JSONUtil.get(URL_GET_ACC_INFO, params, handler);
	}

	public static void getHistoryTransaction(int userId, int actionId,
			int pageIndex, JsonHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("actionId", actionId);
		params.put("pageIndex", 0);
		params.put("serviceid", SERVICED);
		params.put(
				"token",
				ModelDataUtils.md5(Integer.toString(userId)
						+ Integer.toString(actionId)
						+ Integer.toString(pageIndex) + Integer.toString(10)
						+ SERVICED + SERVICE_TOKEN));
		JSONUtil.get(URL_GET_HISTORY_TRANSACTION, params, handler);
	}

	public static void getIpWan(AsyncHttpResponseHandler handler) {
		JSONUtil.get(URL_GET_IP_WAN, handler);
	}

	public static void getUpdateInfo(JsonHttpResponseHandler handler) {
		JSONUtil.get(URL_UPDATE_INFO, handler);
	}

	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static void download(String link, String path, String fileName) {
		File localFile1 = new File(path);
		if (!localFile1.exists())
			localFile1.mkdir();
		try {
			Log.d("Downloading", link);
			URL localURL = new URL(link);
			File localFile2 = new File(path +"/"+ fileName);
			BufferedInputStream localBufferedInputStream = new BufferedInputStream(
					localURL.openConnection().getInputStream());
			ByteArrayBuffer localByteArrayBuffer = new ByteArrayBuffer(50);
			while (true) {
				int i = localBufferedInputStream.read();
				if (i == -1) {
					FileOutputStream localFileOutputStream = new FileOutputStream(
							localFile2);
					localFileOutputStream.write(localByteArrayBuffer
							.toByteArray());
					localFileOutputStream.close();
					Log.d("Finish Downloading", link);
					return;
				}
				localByteArrayBuffer.append((byte) i);
			}
		} catch (Exception localException) {
			Log.i("Loi", localException.toString());
		}
	}

}
