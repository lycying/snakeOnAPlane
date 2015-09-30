package info.u250.snakeonaplane.scene;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.Scene;
import info.u250.c2d.graphic.AnimationSprite;
import info.u250.c2d.graphic.C2dStage;
import info.u250.c2d.graphic.parallax.ParallaxGroup;
import info.u250.snakeonaplane.SnakeOnAPlane;
import info.u250.snakeonaplane.SnakeOnAPlaneEngineDrive;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SceneMain extends C2dStage implements Scene {

	Image splash ,title, head,start;
	AnimationSprite green,red,blue ;
	
	public SceneMain(){
		TextureAtlas wd = Engine.resource("WD");
		splash = new Image(wd.findRegion("splash"));
		title = new Image(wd.findRegion("title"));
		title.setPosition(100, 350);
		title.setOrigin(title.getWidth()/2, title.getHeight()/2);
		title.addAction(Actions.forever(Actions.sequence(Actions.rotateTo(3,1),Actions.rotateTo(-3,1))));
		
		green =  new AnimationSprite(0.1f, wd, "green");
		green.setPosition(15, 250);
		red =  new AnimationSprite(0.1f, wd, "red");
		red.setPosition(100, 280);
		blue =  new AnimationSprite(0.1f, wd, "blue");
		blue.setPosition(85, 310);
		
		
		head = new Image(wd.findRegion("head"));
		head.setPosition(700, 30);
		head.setOrigin(head.getWidth()/2, head.getHeight()/2);
		head.setRotation(180);
		head.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, 300,1,Interpolation.pow2Out),Actions.moveBy(0, -300,1,Interpolation.pow2In))));
		
		start = new Image(wd.findRegion("play"));
		start.setPosition(300, 170);
		start.setOrigin(start.getWidth()/2, start.getHeight()/2);
		start.addAction(Actions.forever(Actions.sequence(Actions.color(Color.YELLOW,0.1f),Actions.color(Color.WHITE, 0.1f))));
		
		this.addActor(title);
		this.addActor(splash);
		this.addActor(head);
		this.addActor(start);
		
		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Engine.getEventManager().fire(SnakeOnAPlaneEngineDrive.EVENT_GAME);
			}
		});
	}
	@Override
	public void update(float delta) {
	}

	@Override
	public void render(float delta) {
		Engine.resource("RBG", ParallaxGroup.class).render(delta);
		this.act(delta);
		this.draw();
		Engine.getSpriteBatch().begin();
		green.render(delta);
		red.render(delta);
		blue.render(delta);
		Engine.getSpriteBatch().end();
	}

	@Override
	public void show() {
		((SnakeOnAPlane)Engine.get()).adControl.hide();
	}

	@Override
	public void hide() {
	}

	@Override
	public InputProcessor getInputProcessor() {
		return this;
	}

	@Override
	public boolean keyDown(int keyCode) {
		if (Gdx.app.getType() == ApplicationType.Android) {
			if (keyCode == Keys.BACK) {
				System.exit(0);
			}
		} else {
			if (keyCode == Keys.DEL) {
				//do nothing
			}
		}
		return true;
	}
}
