package info.u250.snakeonaplane;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import info.u250.snakeonaplane.R;

public class LoadingProgressAndroid {
	ProgressDialog pDialog;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pDialog.dismiss();
		}
	};

	public LoadingProgressAndroid(SnakeOnAPlaneAndroidActivity main) {
		pDialog = new ProgressDialog(main);
		pDialog.setCancelable(false);
		
	}

	public void showLoading() {
		pDialog.show();
		Window window = pDialog.getWindow();
		window.setContentView(R.layout.loading);
	}

	public void hideLoading() {
		handler.sendEmptyMessage(0);
	}

}
