package info.u250.snakeonaplane;

import info.u250.c2d.accessors.SpriteAccessor;
import info.u250.c2d.engine.CoreProvider.TransitionType;
import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;
import info.u250.c2d.engine.events.Event;
import info.u250.c2d.engine.events.EventListener;
import info.u250.c2d.engine.resources.AliasResourceManager;
import info.u250.c2d.graphic.parallax.ParallaxGroup;
import info.u250.c2d.graphic.parallax.ParallaxLayer;
import info.u250.c2d.graphic.parallax.SpriteParallaxLayerDrawable;
import info.u250.snakeonaplane.scene.SceneMain;
import info.u250.snakeonaplane.scene.SceneSnakeOnAPlane;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class SnakeOnAPlaneEngineDrive implements EngineDrive {

	@Override
	public void onResourcesRegister(AliasResourceManager<String> reg) {
		reg.textureAtlas("WD", "data/widgets/snake.atlas");
		reg.font("GameFont", "data/fonts/purisa.fnt");
		reg.font("UiFont", "data/fonts/font.fnt");
		reg.sound("SoundClick", "data/sound/click.ogg");
		reg.sound("SoundWin", "data/sound/win.ogg");
		reg.sound("SoundFuck", "data/sound/fuck.ogg");
		reg.sound("SoundPellet", "data/sound/pellet.ogg");
		reg.sound("SoundTeleport", "data/sound/teleport-in.ogg");
		reg.sound("SoundGravity", "data/sound/gravity.ogg");
		
		reg.music("MusicMain", "data/music/bg.ogg");
		
		TextureAtlas wd = Engine.resource("WD");
		ParallaxGroup  bg =  new ParallaxGroup(Engine.getEngineConfig().width, Engine.getEngineConfig().height, new Vector2(30,0), false);
		Sprite sprite_bg = wd.createSprite("bg");
		Timeline.createSequence()
		.push(Tween.to(sprite_bg, SpriteAccessor.RGB, 5000).target(0.9f,1,0.5f))
		.push(Tween.to(sprite_bg, SpriteAccessor.RGB, 5000).target(1,1,1))
		.repeatYoyo(-1, 0)
		.start(Engine.getTweenManager());
		bg.add(new ParallaxLayer("bg", new SpriteParallaxLayerDrawable(sprite_bg), new Vector2(), new Vector2(), -1, -1));
		bg.add(new ParallaxLayer("cloud", new SpriteParallaxLayerDrawable(wd.createSprite("cloud")), new Vector2(1,0), new Vector2(0,1000), -1, -1,new Vector2(0,150)));
		reg.object("RBG", bg);
	}
	@Override
	public void dispose() {}
	@Override
	public EngineOptions onSetupEngine() {
		EngineOptions options = new EngineOptions(
				new String[]{"data/"},800,480);
		options.configFile = "com.joyboat6.climbingsnake.cnf";
		options.loading = Loading.class.getName();
		options.debug  = false;
		return options;
	}

	@Override
	public void onLoadedResourcesCompleted() {
		Gdx.input.setCatchBackKey(true);
		game = new SceneSnakeOnAPlane();
		home = new SceneMain();
		Engine.getMusicManager().playMusic("MusicMain", true);
		Engine.getEventManager().register(EVENT_HOME, new EventListener() {
			@Override
			public void onEvent(Event event) {
				Engine.setMainScene(home,TransitionType.Fade);
			}
		});
		Engine.getEventManager().register(EVENT_GAME, new EventListener() {
			@Override
			public void onEvent(Event event) {
				Engine.setMainScene(game,TransitionType.Fade);
			}
		});
		Engine.setMainScene(new SceneMain());
	}
	
	public final static String EVENT_HOME = "snakeOnAPlane_event__home";
	public final static String EVENT_GAME = "snakeOnAPlane_event__game";
	public final static String EVENT_SHOW_AD = "snakeOnAPlane_event__show_ad";
	public final static String EVENT_HIDE_AD = "snakeOnAPlane_event__hide_ad";
	
	SceneSnakeOnAPlane game;
	SceneMain home;
}
