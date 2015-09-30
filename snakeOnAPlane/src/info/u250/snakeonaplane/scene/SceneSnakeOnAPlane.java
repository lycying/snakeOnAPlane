package info.u250.snakeonaplane.scene;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.Scene;
import info.u250.c2d.graphic.AdvanceSprite;
import info.u250.c2d.graphic.AnimationSprite;
import info.u250.c2d.graphic.C2dStage;
import info.u250.c2d.graphic.parallax.ParallaxGroup;
import info.u250.c2d.input.SimpleDirectionGestureDetector;
import info.u250.c2d.input.SimpleDirectionGestureDetector.DirectionListener;
import info.u250.c2d.utils.UiUtils;
import info.u250.snakeonaplane.SnakeOnAPlane;
import info.u250.snakeonaplane.SnakeOnAPlaneEngineDrive;
import info.u250.snakeonaplane.scene.PlatformFiles.LevelDialogEvents;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class SceneSnakeOnAPlane implements Scene {

	private HUD hud ;
	
	private static final int FRAME_TIME = 14;
	private static final float BLOCK_SIZE = 24f;
	private static final int MAX_LEVELS = 27;
	
	private static final int EMPTY = 0;
	private static final int WALL = 1;
	private static final int PELLET = 2;
	private static final int EXIT = 3;
	private static final int START = 4;
	private static final int WHITE_PELLET = 5;
	private static final int STICKY = 6;
	private static final int TELEPORT_IN = 7;
	private static final int TELEPORT_OUT = 8;
	private static final int RED_PELLET = 9;
	private static final int GRAVITY_SWITCH = 10;
	
	
	
	boolean running = true;
	long lastUpdate = System.currentTimeMillis();
	
	int[][] snake = new int[100][2];
	byte[] map = new byte[30*20];
	
	int fall = 0;
	boolean falling = false;
	boolean leaving = false;
	boolean acting = false;
	int length = 0;
	boolean restart = false;
	boolean complete = true;
	int count = 0;
	int level = 1;
	int pCount = 0;
	int moves = 0;
	int gravity = 1;
	float  xoffset = 0;
	float screenHeight = Engine.getEngineConfig().height - BLOCK_SIZE ;
	
	Sprite wall ;
	AnimationSprite pellet ;
	Sprite exit  ;
//	Sprite start ;
	AnimationSprite white_pellet;
	Sprite sticky ;
	AnimationSprite teleport_in;
	AnimationSprite teleport_out;
	AnimationSprite red_pellet;
	Sprite gravity_switch;
	AdvanceSprite head;
	Sprite body;
	Sprite grid;	
	
	public SceneSnakeOnAPlane(){
//		mask = new MeshMask();
		
		hud = new HUD();
//		story = new StoryHandler();
		TextureAtlas wd = Engine.resource("WD");
		
		wall = wd.createSprite("wall");
		pellet = new AnimationSprite(0.5f, wd, "green");
		exit = wd.createSprite("exit");
//		start= wd.createSprite("start");
		white_pellet = new AnimationSprite(0.5f, wd, "blue");
		sticky = wd.createSprite("sticky");
		teleport_in= new AnimationSprite(0.1f, wd, "in");
		teleport_out=new AnimationSprite(0.1f, wd, "out");
		red_pellet=new AnimationSprite(0.5f, wd, "red");
		gravity_switch=wd.createSprite("gravity-switch");
		head= new AdvanceSprite(wd.findRegion("head"));
		head.setSize(BLOCK_SIZE, BLOCK_SIZE);
		head.setOrigin(head.getWidth()/2, head.getHeight()/2);
		body = wd.createSprite("body");
		grid = wd.createSprite("grid");
		
		wall.setSize(BLOCK_SIZE, BLOCK_SIZE);
		pellet.setSize(BLOCK_SIZE, BLOCK_SIZE);
		white_pellet.setSize(BLOCK_SIZE, BLOCK_SIZE);
		red_pellet.setSize(BLOCK_SIZE, BLOCK_SIZE);
		exit.setSize(BLOCK_SIZE, BLOCK_SIZE);
//		start.setSize(BLOCK_SIZE, BLOCK_SIZE);
		sticky.setSize(BLOCK_SIZE, BLOCK_SIZE);
		teleport_in.setSize(BLOCK_SIZE, BLOCK_SIZE);
		teleport_out.setSize(BLOCK_SIZE, BLOCK_SIZE);
		gravity_switch.setSize(BLOCK_SIZE, BLOCK_SIZE);
		body.setSize(BLOCK_SIZE, BLOCK_SIZE);
		grid.setSize(BLOCK_SIZE, BLOCK_SIZE);
		

		head.setOrigin(head.getWidth()/2, head.getHeight()/2);
		
		xoffset = 0;

		teleport_in.setOrigin(teleport_in.getWidth()/2, teleport_in.getHeight()/2);
		teleport_out.setOrigin(teleport_out.getWidth()/2, teleport_out.getHeight()/2);
	}
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(float delta) {
//		story.left = pCount;
		// logic
				if ((!acting) || (complete)) {
					if (!complete) {
						for (int i=0;i<length;i++) {
							int x = snake[i][0];
							int y = snake[i][1];
							int tile = map[x+(y*30)];
							
							if (i == 0) {
								if (tile == TELEPORT_IN) {
									Engine.getSoundManager().playSound("SoundTeleport");
									for (int xp=0;xp<30;xp++) {
										for (int yp=0;yp<20;yp++) {
											if (map[xp+(yp*30)] == TELEPORT_OUT) {
												snake[i][0] = xp;
												snake[i][1] = yp;
												i--;
												continue;
											}
										}
									}
								}
							}
							if (tile == GRAVITY_SWITCH) {
								Engine.getSoundManager().playSound("SoundGravity");
								map[x+(y*30)] = EMPTY;
								gravity = -gravity;
							}
							if ((tile == PELLET) || (tile == WHITE_PELLET) || (tile == RED_PELLET))  {
								Engine.getSoundManager().playSound("SoundPellet");
								map[x+(y*30)] = EMPTY;
								if (tile == PELLET) {
									length++;
								}
								if (tile == RED_PELLET) {
									length--;
									snake[length][0] = 0;
									snake[length][1] = 0;
									Engine.getDefaultCamera().shake();
								}
								pCount--;
							}
						}
						
						falling = true;
						// check for support
						for (int i=0;i<length;i++) {
							int x = snake[i][0];
							int y = snake[i][1];
							if (y > 18) {
								Engine.getSoundManager().playSound("SoundFuck");
								this.restart();
								falling = false;
							} else {
								if ((y == 0) && (gravity < 0)) {
									falling = false;
								}
								else if ((y != 0) || (x != 0)) {
									int tile = map[x+((y+gravity)*30)];
									int tile2 = y > 0 ? map[x+((y-1)*30)] : 0;
									if ((tile == WALL) || (tile2 == STICKY) || (tile == STICKY))  {
										falling = false;
										break;
									}
								}
							}
						}
						
						if (!falling) {
							if (pCount == 0) {
								for (int i=0;i<length;i++) {
									int x = snake[i][0];
									int y = snake[i][1];
									if (map[x+(y*30)] == EXIT) {
										//save it 
										LevelTools.setLevelFinish(level);
										//sound
										Engine.getSoundManager().playSound("SoundWin");
										//hud
//										hud.next.action(Forever.$(Sequence.$(MoveBy.$(3, 3, 0.2f),MoveBy.$(-3, -3, 0.2f))));
										((SnakeOnAPlane)Engine.get()).platformFiles.showLevelDialog(new LevelDialogEvents() {
											
											@Override
											public void pre() {
												level--;
												if(level<1)level=1;
												Engine.getSoundManager().playSound("SoundClick");
												prototypeEvent(Keys.R);
												
											}
											
											@Override
											public void next() {
												level++;
												if(level>27)level=27;
												Engine.getSoundManager().playSound("SoundClick");
												prototypeEvent(Keys.R);
											}
											
											@Override
											public void menu() {
												Engine.getEventManager().fire(SnakeOnAPlaneEngineDrive.EVENT_HOME, null);
											}
										});
										leaving = true;
									}
								}
							}
						}
					}
					
					if ((!falling) && (!leaving)) {
						
					} else {
						acting = true;
					}
				} else {
					if (!complete) {
						if (leaving) {
							if (count % 5 == 0) {
								length--;
								count++;
								if (length <= 0) {
//									level++;
									if (level > MAX_LEVELS) {
										level = 1;
									}
									
									complete = true;
									acting = false;
									leaving = false;
									falling = false;
									
								}
							}
						}
						if (falling) {
							fall += gravity;
							if (Math.abs(fall) >= BLOCK_SIZE) {
								fall = 0;
								falling = false;
								acting = false;
								for (int i=0;i<length;i++) {
									if ((snake[i][0] != 0) || (snake[i][1] != 0)) {
										snake[i][1] += gravity;
									}
								}
							}
						}
					}
				}
				
				
				// render 
//		Gdx.gl.glClearColor(29f/255f, 55f/255f, 111f/255f, 1);
		if (System.currentTimeMillis() - lastUpdate > FRAME_TIME) {
			count++;
			lastUpdate = System.currentTimeMillis();
		}
		
		if (restart) {
			gravity = 1;
			moves = 0;
			snake = new int[100][2];
			try {
				InputStream in = this.getClass().getResourceAsStream("/level.bin");
				for (int i=0;i<level;i++) {
					in.read(map,0,map.length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			length = 2;
			pCount = 0;
			for (int x=0;x<30;x++) {
				for (int y=0;y<20;y++) {
					int tile = map[x+(y*30)];
					if (tile == PELLET) {
						pCount++;
					}else if (tile == WHITE_PELLET) {
						pCount++;
					}else if (tile == RED_PELLET) {
						pCount++;
					}
					if (tile == START) {
						snake[0][0] = x;
						snake[0][1] = y;
						snake[1][0] = x+1;
						snake[1][1] = y;
						map[x+(y*30)] = EMPTY;
					}
				}
			}
			falling = false;
			fall = 0;
			acting = false;
			restart = false;
			
		}
		

		Engine.resource("RBG", ParallaxGroup.class).render(delta);
		
		Engine.getSpriteBatch().begin();
		// draw black outline
		for (int x=0;x<30;x++) {
			for (int y=0;y<20;y++) {
				grid.setPosition(xoffset+x*BLOCK_SIZE,screenHeight - y*BLOCK_SIZE);
				grid.draw(Engine.getSpriteBatch(),0.1f);
				//grid
				final int jump_offset = 0;
				switch(map[x+(y*30)]){
				case GRAVITY_SWITCH:
					gravity_switch.setPosition(xoffset+(x*BLOCK_SIZE), screenHeight - ((y*BLOCK_SIZE)));
					gravity_switch.draw(Engine.getSpriteBatch());
					break;
				case PELLET:
					pellet.setPosition(xoffset+(x*BLOCK_SIZE),screenHeight - ((y*BLOCK_SIZE)));
					pellet.render(delta);
					break;
				case WHITE_PELLET:
					white_pellet.setPosition(xoffset+(x*BLOCK_SIZE),screenHeight - ((y*BLOCK_SIZE)));
					white_pellet.render(delta);
					break;
				case RED_PELLET:
					red_pellet.setPosition(xoffset+(x*BLOCK_SIZE),screenHeight - ((y*BLOCK_SIZE)));
					red_pellet.render(delta);
					break;
				case WALL:
					wall.setPosition(xoffset+x*BLOCK_SIZE,screenHeight - y*BLOCK_SIZE);
					wall.draw(Engine.getSpriteBatch());
					break;
				case STICKY:
					sticky.setPosition(xoffset+x*BLOCK_SIZE,screenHeight - y*BLOCK_SIZE);
					sticky.draw(Engine.getSpriteBatch());
					break;
				case EXIT:
					exit.setPosition(xoffset+(x*BLOCK_SIZE),screenHeight - ((y*BLOCK_SIZE)));
					exit.draw(Engine.getSpriteBatch());
					break;
				case TELEPORT_IN:
					
					teleport_in.setSize((-jump_offset*2)+BLOCK_SIZE,(-jump_offset*2)+BLOCK_SIZE);
					teleport_in.setPosition(xoffset+(x*BLOCK_SIZE)+jump_offset,screenHeight - ((y*BLOCK_SIZE)+jump_offset));
					teleport_in.render(delta);
					break;
				case TELEPORT_OUT:
					teleport_out.setSize((-jump_offset*2)+BLOCK_SIZE,(-jump_offset*2)+BLOCK_SIZE);
					teleport_out.setPosition(xoffset+(x*BLOCK_SIZE)+jump_offset,screenHeight - ((y*BLOCK_SIZE)+jump_offset));
					teleport_out.render(delta);
					break;
				}
			}
		}
		for (int i=length-1;i>=0;i--) {
			if ((snake[i][0] == 0) && (snake[i][1] == 0)) {
				continue;
			}
			if ((i > 0) || (leaving)) {
				body.setPosition(xoffset+snake[i][0]*BLOCK_SIZE,screenHeight-( (snake[i][1]*BLOCK_SIZE)+fall+1));
				body.draw(Engine.getSpriteBatch());
			} else{
				head.setPosition(xoffset+snake[i][0]*BLOCK_SIZE,screenHeight-( (snake[i][1]*BLOCK_SIZE)+fall+1));
				head.render(delta);
				
			}
		}

		Engine.getSpriteBatch().end();

		hud.act(delta);
		hud.draw();
	
	}

	@Override
	public void show() {
		this.hud.showLevelBoard();
	}

	@Override
	public void hide() {
	}

	private void restart(){
		this.restart = true;
		this.head.setRotation(-90);
	}
	private void prototypeEvent(int keycode){
		if ((!falling) && (!leaving)) {
			if (keycode == Keys.R) {
				restart();
			}
			if (keycode == Keys.N) {
				if(LevelTools.isLevelFinish(level)){
					level++;
					if (level > MAX_LEVELS) {
						level = 1;
					}
				}
				restart();
			}
			if (keycode != -1) {
				if (complete) {
					complete = false;
//					restart();
				} else {
					int dx = snake[0][0];
					int dy = snake[0][1];
					
					
					if (keycode == Keys.LEFT) {
						this.head.setRotation(-90);
						dx--;
					}
					if (keycode == Keys.RIGHT) {
						this.head.setRotation(90);
						dx++;
					}
					if (keycode == Keys.UP) {
						this.head.setRotation(180);
						dy--;
					}
					if (keycode == Keys.DOWN) {
						this.head.setRotation(0);
						dy++;
					}
					boolean blocked = false;
					if ((dx < 0) || (dy < 0) || (dx > 29)) {
						blocked = true;
					} else {
						if (map[dx+(dy*30)] == STICKY) {
							blocked = true;
						}
						if (map[dx+(dy*30)] == WALL) {
							blocked = true;
						}
						for (int i=0;i<length-1;i++) {
							if ((snake[i][0] == dx) && (snake[i][1] == dy)) {
								blocked = true;
							}
						}
					}
					if (!blocked) {
						moves++;
						for (int i=length-1;i>0;i--) {
							snake[i][0] = snake[i-1][0];
							snake[i][1] = snake[i-1][1];
						}
						snake[0][0] = dx;
						snake[0][1] = dy;
					}
				}
			}
		}
	}
	@Override
	public InputProcessor getInputProcessor() {
		InputMultiplexer m = new InputMultiplexer();
		m.addProcessor(hud);
		m.addProcessor(new InputAdapter(){
			@Override
			public boolean keyDown(int keycode) {
				if (Gdx.app.getType() == ApplicationType.Android) {
					if (keycode == Keys.BACK) {
						Engine.getEventManager().fire(SnakeOnAPlaneEngineDrive.EVENT_HOME, null);
					}
				}else{
					if (keycode == Keys.DEL) {
						Engine.getEventManager().fire(SnakeOnAPlaneEngineDrive.EVENT_HOME, null);
					}
				}
				prototypeEvent(keycode);
				return super.keyUp(keycode);
			}
			
		});
		m.addProcessor(new SimpleDirectionGestureDetector(new DirectionListener() {
			@Override
			public void onUp() {
				Engine.getSoundManager().playSound("SoundClick");
				prototypeEvent(Keys.UP);
			}
			
			@Override
			public void onRight() {
				Engine.getSoundManager().playSound("SoundClick");
				prototypeEvent(Keys.RIGHT);
			}
			
			@Override
			public void onLeft() {
				Engine.getSoundManager().playSound("SoundClick");
				prototypeEvent(Keys.LEFT);
			}
			
			@Override
			public void onDown() {
				Engine.getSoundManager().playSound("SoundClick");
				prototypeEvent(Keys.DOWN);
			}
		}));
		return m;
	}
	
	
	private class HUD extends C2dStage {
		
		private Table levelContainer;
		
	
		
		Image left;
		Image right;
		Image up;
		Image down;
		Image menu ;
		
		Group menuGroup;
		
		
		void hideMenu(){
			this.menuGroup.clearActions();
			menuGroup.addAction(Actions.sequence(Actions.scaleTo(0, 0,0.15f),Actions.run(new Runnable() {
				
				@Override
				public void run() {
					menuGroup.remove();
				}
			})));	
		}
		void showMenu(){
			this.addActor(menuGroup);
			menuGroup.clearActions();
			menuGroup.setScale(0);
			menuGroup.addAction(Actions.scaleTo(1, 1,0.3f,Interpolation.swingOut));
		}
		public HUD() {			
			levelContainer = new Table();
			levelContainer.setSize(Engine.getEngineConfig().width, Engine.getEngineConfig().height);
			UiUtils.centerActor(levelContainer);
			
			
			TextureAtlas wd = Engine.resource("WD");
			
			
			menuGroup = new Group();
			menuGroup.setPosition(30, 30);
			menuGroup.addActor(new Image(wd.findRegion("menu-bg")));
			Image menu_restart = new Image(wd.findRegion("menu-restart"));
			menu_restart.addListener(new ClickListener() {
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Engine.getSoundManager().playSound("SoundClick");
					SceneSnakeOnAPlane.this.restart();
					hideMenu();
				}
			});
			Image menu_menu = new Image(wd.findRegion("menu-menu"));
			menu_menu.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Engine.getEventManager().fire(SnakeOnAPlaneEngineDrive.EVENT_HOME, null);
					hideMenu();
				}
			});
			Image menu_start = new Image(wd.findRegion("menu-start"));
			menu_start.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					hideMenu();
					showLevelBoard();
				}
			});
			menu_start.setPosition(45, 100);
			menu_restart.setPosition(45, 150);
			menu_menu.setPosition(45, 50);

			menuGroup.setSize(267, 237);
			menuGroup.addActor(menu_restart);
			menuGroup.addActor(menu_menu);
			menuGroup.addActor(menu_start);
			
			
			left= new Image(wd.findRegion("left"));
			this.addActor(left);
			
			right= new Image(wd.findRegion("right"));
			this.addActor(right);
			
			up= new Image(wd.findRegion("up"));
			this.addActor(up);
			
			down= new Image(wd.findRegion("down"));
			this.addActor(down);
			
			menu = new Image(wd.findRegion("pause"));
			menu.setPosition(30, 20);
			this.addActor(menu);
			
			
			menu.addListener(new ClickListener() {
				
				@Override
				public void clicked(InputEvent event, float x, float y) {
					showMenu();
				}
			});
			
			
			left.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					left.addAction(Actions.sequence(Actions.alpha(0.3f, 0.1f),Actions.alpha(1f, 0.1f)));
					Engine.getSoundManager().playSound("SoundClick");
					prototypeEvent(Keys.LEFT);
				}
			});
			right.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					right.addAction(Actions.sequence(Actions.alpha(0f, 0.1f),Actions.alpha(1f, 0.1f)));
					Engine.getSoundManager().playSound("SoundClick");
					prototypeEvent(Keys.RIGHT);
				}
			});
			up.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					up.addAction(Actions.sequence(Actions.alpha(0.3f, 0.1f),Actions.alpha(1f, 0.1f)));
					Engine.getSoundManager().playSound("SoundClick");
					prototypeEvent(Keys.UP);
				}
			});
			down.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					down.addAction(Actions.sequence(Actions.alpha(0.3f, 0.1f),Actions.alpha(1f, 0.1f)));
					Engine.getSoundManager().playSound("SoundClick");
					prototypeEvent(Keys.DOWN);
				}
			});
			
			float offsetx = 20;
			float offsety = 150;
			
			
			left.setX( offsetx+16 );
			left.setY( offsety );
			
			right.setX( offsetx+128+16 );
			right.setY( offsety );
			
			up.setX( offsetx+64+32 );
			up.setY( offsety+64-16 );
			
			down.setX( offsetx+64+32 );
			down.setY( offsety-64 );
		}
		

		private void showLevelBoard(){
			if(!this.getActors().contains(levelContainer,false)){
				for(int i=0;i<map.length;i++){
					map[i]=0x00;
				}
				for(int i=0;i<snake.length;i++){
					for(int j=0;j<snake[i].length;j++){
						snake[i][j] = 0x00;
					}
				}
				running = true;
				lastUpdate = System.currentTimeMillis();
			
				fall = 0;
				falling = false;
				leaving = false;
				acting = false;
				length = 0;
				restart = false;
				complete = true;
				count = 0;
				level = 1;
				pCount = 0;
				moves = 0;
				gravity = 1;
				
				
				((SnakeOnAPlane)Engine.get()).adControl.show();
				levelContainer.clear();
				levelContainer.getColor().a = 0;
				this.levelContainer.setY( 0 );
				addActor(levelContainer);
				levelContainer.addAction(Actions.fadeIn(0.2f));

				TextureAtlas wd = Engine.resource("WD");
				
				Table table = new Table();
				table.padTop(50);

				ScrollPane scroll = new ScrollPane(table);
				scroll.setScrollingDisabled(true, false);
				
				
				levelContainer.add(scroll).expand().fillX();
				levelContainer.row();
				levelContainer.add(new Image(wd.findRegion("choose-level"))).height(78);

				
//				BitmapFont font = Engine.resource("GameFont");
//				
//				NinePatchDrawable bup = new NinePatchDrawable( new NinePatch(wd.findRegion("lb")));
//				NinePatchDrawable bdown = new NinePatchDrawable(new NinePatch(wd.findRegion("lb")));
//				NinePatchDrawable bchecked = new NinePatchDrawable(new NinePatch(wd.findRegion("lb")));
				for (int i = 0; i < MAX_LEVELS; i++) {
					if(i%4==0){
						table.row();
					}
//					TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(bdown, bup, bchecked);
//					style.font = font;
//					TextButton button = new TextButton((i+1)+"", style);
					Image button = new Image(wd.findRegion(""+(i+1)));
					table.add(button).spaceLeft(30).spaceTop(20);
					
					
					
					final int this_level = i+1;
					
					if(LevelTools.isLevelFinish(this_level) || LevelTools.isLevelFinish(this_level-1)){
						button.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								Engine.getSoundManager().playSound("SoundClick");
								
								SceneSnakeOnAPlane.this.level = this_level;
								prototypeEvent(Keys.R);
								levelContainer.addAction(
										Actions.sequence(
												Actions.parallel(Actions.fadeOut( 0.3f),Actions.moveTo(0, levelContainer.getHeight(), 0.2f)),
												Actions.run(new Runnable() {
													
													@Override
													public void run() {
														levelContainer.remove();
													}
												})
												
								));
								
							}
							
						});
					}else{
						button.getColor().set(Color.GRAY);
					}
					
				}
				scroll.setClamp(true);
				
				levelContainer.row();
				levelContainer.setBackground(new NinePatchDrawable(new  NinePatch(wd.findRegion("level-bg"))));
				
			}
			
		}

	}
}


