package info.u250.snakeonaplane;

import info.u250.c2d.graphic.AdControl;
import info.u250.snakeonaplane.scene.PlatformFiles;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SnakeOnAPlaneDesktop {
	public static void main (String[] argv) {
		new LwjglApplication((ApplicationListener) new SnakeOnAPlane(new AdControl() {
			
			@Override
			public void show() {
				
			}
			
			@Override
			public void hide() {
				
			}
		},new PlatformFiles() {
			
			@Override
			public void showLevelDialog(LevelDialogEvents callback) {
				
			}
		},new LoadingFeedback() {
			
			@Override
			public void finish() {
				
			}
			
			@Override
			public void feedback(float percent) {
				
			}
		}), "SnakeOnAPlane",800,  480,false);		
	}
}
