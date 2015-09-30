package info.u250.snakeonaplane.scene;

public interface PlatformFiles {
	public interface LevelDialogEvents{
		void pre();
		void menu();
		void next();
	}
	public void showLevelDialog(LevelDialogEvents callback);
}
