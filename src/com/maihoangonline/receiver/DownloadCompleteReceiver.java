package com.maihoangonline.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.maihoangonline.database.DBHelper;
import com.maihoangonline.fragments.DownloadedFragment;
import com.maihoangonline.models.Game;
import com.maihoangonline.utils.DisplayUtils;

public class DownloadCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("download_complete")) {
			Game game = (Game) intent.getSerializableExtra("game");
			DBHelper dbh = new DBHelper(context);
			DisplayUtils.log("Before insert");
			DisplayUtils.log("Insert:" + dbh.insertGameToDownloaded(game));
			DisplayUtils.log("After insert");
			ArrayList<Game> listGame = dbh.getListDownloadedGame();
			dbh.close();
			Toast.makeText(
					context,
					game.getTitle() + " đã tải xong, số game đã tải là "
							+ listGame.size(), Toast.LENGTH_LONG).show();
			/*if (DataUtils.isInDownloadScreen) {
				try {
					DownloadedFragment.getInstance().loadDownloadedGame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			DownloadedFragment.getInstance().loadDownloadedGame();
			// new DownloadedFragment().loadDownloadedGame();
		} else {
			Toast.makeText(context, "Quá trình tải bị lỗi", Toast.LENGTH_LONG)
					.show();
		}
	}

}
