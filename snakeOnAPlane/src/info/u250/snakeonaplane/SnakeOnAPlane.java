package info.u250.snakeonaplane;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;
import info.u250.c2d.graphic.AdControl;
import info.u250.snakeonaplane.scene.PlatformFiles;

public class SnakeOnAPlane extends Engine {
	public AdControl adControl;
	public PlatformFiles platformFiles;
	public LoadingFeedback loadingFeedback;
	public SnakeOnAPlane(AdControl ad,PlatformFiles platformFiles, LoadingFeedback loadingFeedback){
		this.adControl = ad;
		this.platformFiles = platformFiles;
		this.loadingFeedback = loadingFeedback;
	}

	@Override
	protected EngineDrive onSetupEngineDrive() {
		return new SnakeOnAPlaneEngineDrive();
	}

}
