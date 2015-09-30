package info.u250.snakeonaplane;

import info.u250.snakeonaplane.LoadingFeedback;

public class LoadingFeedbackAndroid implements LoadingFeedback {
	SnakeOnAPlaneAndroidActivity main;
	public LoadingFeedbackAndroid(SnakeOnAPlaneAndroidActivity main){
		this.main = main;
	}
	@Override
	public void feedback(final float percent) {
	
	}

	@Override
	public void finish() {
		main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				main.getLoadingView().hideLoading();
			}
		});
		
	}

}
