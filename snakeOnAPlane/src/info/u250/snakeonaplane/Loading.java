package info.u250.snakeonaplane;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.load.startup.StartupLoading;

public class Loading extends StartupLoading {
	LoadingFeedback feedback ;
	public Loading() {
		feedback = ((SnakeOnAPlane)Engine.get()).loadingFeedback;
	}
	@Override
	protected void inLoadingRender(float delta) {
		feedback.feedback(this.percent());
	}
	@Override
	protected void finishLoadingCleanup() {
		feedback.finish();
	}
}
