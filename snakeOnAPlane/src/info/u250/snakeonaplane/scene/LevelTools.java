package info.u250.snakeonaplane.scene;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LevelTools {
	private static Preferences preferences;

	public static boolean isLevelFinish(int level){
		if(level == 1){
			return true;
		}
		if(null==preferences){
			preferences = Gdx.app.getPreferences(Engine.getEngineConfig().configFile);
		}
		if(preferences.getString("__level__"+level).equals("NBA")){
			return true;
		}
		return false;
	}
	
	public static void setLevelFinish(int level){
		if(null==preferences){
			preferences = Gdx.app.getPreferences(Engine.getEngineConfig().configFile);
		}
		preferences.putString("__level__"+level, "NBA");
		preferences.flush();
	}
	
	
	public static boolean isSoundEnabled(){
		if(null == preferences){
			preferences = Gdx.app.getPreferences(Engine.getEngineConfig().configFile);
		}
		if(null==preferences.getString("__audio__") || "".equals(preferences.getString("__audio__"))){
			return true;
		}
		return preferences.getString("__audio__").equals("lxlol");
	}
	public static void enableAudio(){
		if(null == preferences){
			preferences = Gdx.app.getPreferences(Engine.getEngineConfig().configFile);
		}
		preferences.putString("__audio__", "lxlol");
		preferences.flush();
		
		Engine.getSoundManager().setVolume(1);
		Engine.getMusicManager().setVolume(1);
	}
	public static void disableAudio(){
		if(null == preferences){
			preferences = Gdx.app.getPreferences(Engine.getEngineConfig().configFile);
		}
		preferences.putString("__audio__", "ahahahahaha");
		preferences.flush();
		
		Engine.getSoundManager().setVolume(0);
		Engine.getMusicManager().setVolume(0);
	}
	
}
