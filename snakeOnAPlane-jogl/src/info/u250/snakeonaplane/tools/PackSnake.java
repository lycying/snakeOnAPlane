package info.u250.snakeonaplane.tools;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class PackSnake {
	public static void main(String[] args) {
		Settings setting = new Settings();
		setting.maxWidth = 1024;
		setting.maxHeight = 1024;
		TexturePacker2.process(setting, 
				"assets-raw/snake",
				"../snakeOnAPlane-android/assets/data/widgets",
				"snake");
	}
}
