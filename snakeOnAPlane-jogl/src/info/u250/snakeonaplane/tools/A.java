package info.u250.snakeonaplane.tools;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class A extends Applet implements Runnable {
	
	private static final long serialVersionUID = 3730809621109586513L;
	private static final int FRAME_TIME = 16;
	private static final int BLOCK_SIZE = 16;
	private static final int MAX_LEVELS = 10;
	
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
	
	private KeyEvent lastEvent;
	
	public void start() {
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		new Thread(this).start();
	}

	public void run() {
		boolean running = true;
		long lastUpdate = System.currentTimeMillis();
		boolean title = true;
		
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
		
		BufferedImage buffer = new BufferedImage(480, 320, BufferedImage.TYPE_INT_ARGB);
		
		requestFocus();
		while (running) {
			if (System.currentTimeMillis() - lastUpdate > FRAME_TIME) {
				count++;
				lastUpdate = System.currentTimeMillis();
			}
			
			if (restart) {
				title = false;
				gravity = 1;
				moves = 0;
				snake = new int[100][2];
				try {
					InputStream in = A.class.getResourceAsStream("/level.bin");
					for (int i=0;i<level;i++) {
						in.read(map,0,map.length);
					}
				} catch (IOException e) {
				}
				
				length = 2;
				pCount = 0;
				for (int x=0;x<30;x++) {
					for (int y=0;y<20;y++) {
						int tile = map[x+(y*30)];
						if (tile == PELLET) {
							pCount++;
						}
						if (tile == WHITE_PELLET) {
							pCount++;
						}
						if (tile == RED_PELLET) {
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
			
			Graphics2D g = (Graphics2D) buffer.getGraphics();
			g.setPaint(new GradientPaint(0,0,new Color(0x0066FF), 0,400,Color.WHITE));
			g.fillRect(0,0,480,320);

			// draw black outline
			g.setStroke(new BasicStroke(2.0f));
			for (int x=0;x<30;x++) {
				for (int y=0;y<20;y++) {
					g.setColor(Color.BLACK);
					
					int tile = map[x+(y*30)];
					if (tile == GRAVITY_SWITCH) {
						final int offset = 4;
						Graphics2D g2 = (Graphics2D) g.create();
						g2.setColor(Color.green);
						g2.rotate(count * 0.1, ((x+0.5f)*BLOCK_SIZE),((y+0.5f)*BLOCK_SIZE));
						g2.drawRect((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,(-offset*2)+BLOCK_SIZE,(-offset*2)+BLOCK_SIZE);
						g2.rotate(count * -0.1, ((x+0.5f)*BLOCK_SIZE),((y+0.5f)*BLOCK_SIZE));
					}
					if ((tile == WALL) || (tile == STICKY)) {
						g.drawRect(x*BLOCK_SIZE,y*BLOCK_SIZE,BLOCK_SIZE,BLOCK_SIZE);
					}
					if ((tile == PELLET) || (tile == WHITE_PELLET) || (tile == RED_PELLET)) {
						final int offset = 4;
						g.drawOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
					}
					if (tile == EXIT) {
						final int offset = 0;
						g.setColor(Color.black);
						g.drawOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
					}
					if (tile == TELEPORT_IN) {
						final int offset = 1+((count / 2) % 7);
						g.setColor(Color.red);
						g.drawRect((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,(-offset*2)+BLOCK_SIZE,(-offset*2)+BLOCK_SIZE);
					}
					if (tile == TELEPORT_OUT) {
						final int offset = 8-((count / 2) % 7);
						g.setColor(Color.blue);
						g.drawRect((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,(-offset*2)+BLOCK_SIZE,(-offset*2)+BLOCK_SIZE);
					}
				}
			}
			// colour it in
			for (int x=0;x<30;x++) {
				for (int y=0;y<20;y++) {
					int tile = map[x+(y*30)];
					if ((tile == WALL) || (tile == STICKY)) {
						g.setColor(Color.yellow.darker());
						g.fillRect(x*BLOCK_SIZE,y*BLOCK_SIZE,BLOCK_SIZE,BLOCK_SIZE);
						if (y > 0) {
							if (map[x+((y-1)*30)] != 1) {
								g.setColor(new Color(0xCCCC00));
								g.fillRect(x*BLOCK_SIZE,y*BLOCK_SIZE,BLOCK_SIZE,3);
							}
						}
						if (tile == STICKY) {
							g.setColor(new Color(0x000000));
							g.fillRect(x*BLOCK_SIZE,(y*BLOCK_SIZE)+(BLOCK_SIZE-3),BLOCK_SIZE,3);
						}
					}
					if (tile == PELLET) {
						final int offset = 4;
						g.setColor(Color.yellow);
						g.fillOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
					}
					if (tile == WHITE_PELLET) {
						final int offset = 4;
						g.setColor(Color.white);
						g.fillOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
					}
					if (tile == RED_PELLET) {
						final int offset = 4;
						g.setColor(Color.red);
						g.fillOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
					}
					if (tile == EXIT) {
						if (pCount == 0) {
							final int offset = 2;
							g.setColor(Color.black);
							g.fillOval((x*BLOCK_SIZE)+offset,(y*BLOCK_SIZE)+offset,-(offset*2)+BLOCK_SIZE,-(offset*2)+BLOCK_SIZE);
						}
					}
				}
			}
			// draw black outline
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(2.0f));
			for (int i=0;i<length;i++) {
				if ((snake[i][0] == 0) && (snake[i][1] == 0)) {
					continue;
				}
				g.drawRoundRect(snake[i][0]*BLOCK_SIZE, (snake[i][1]*BLOCK_SIZE)+fall+1, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
			}
			// colour it in
			g.setStroke(new BasicStroke(1.0f));
			for (int i=0;i<length;i++) {
				if ((snake[i][0] == 0) && (snake[i][1] == 0)) {
					continue;
				}
				g.setColor(Color.GREEN);
				if ((i > 0) || (leaving)) {
					g.setColor(g.getColor().darker());
				} 
				g.fillRoundRect(snake[i][0]*BLOCK_SIZE, (snake[i][1]*BLOCK_SIZE)+fall+1, BLOCK_SIZE, BLOCK_SIZE, 10, 10);
				g.setColor(g.getColor().darker());
				g.drawArc((snake[i][0]*BLOCK_SIZE)+2, (snake[i][1]*BLOCK_SIZE)+fall+2+1, -4+BLOCK_SIZE, -4+BLOCK_SIZE, 270, 90);
			}
			
			g.setColor(new Color(0,0,0,0.3f));
			g.fillRect(0,0,480,20);
			g.fillRect(0,300,480,20);
			g.setFont(g.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));
			g.setColor(Color.black);
			String str = "Level "+level;
			g.drawString(str, 5, 15);
			g.setColor(Color.white);
			g.drawString(str, 5, 13);
			
			str = "(R)estart";
			g.setColor(Color.black);
			g.drawString(str, (475-g.getFontMetrics().stringWidth(str)), 315);
			g.setColor(Color.white);
			g.drawString(str, (475-g.getFontMetrics().stringWidth(str)), 313);
			str = "Pellets: "+pCount;
			g.setColor(Color.black);
			g.drawString(str, 5, 315);
			g.setColor(Color.white);
			g.drawString(str, 5, 313);

			str = "Moves: "+moves;
			g.setColor(Color.black);
			g.drawString(str, (475-g.getFontMetrics().stringWidth(str)), 15);
			g.setColor(Color.white);
			g.drawString(str, (475-g.getFontMetrics().stringWidth(str)), 13);
			
			if (complete) {
				if (title) {
					g.setFont(g.getFont().deriveFont(80.0f));
					int xp = (480 - g.getFontMetrics().stringWidth("Snake")) / 2;
					g.setColor(Color.black);
					g.drawString("Snake", xp, 153);
					g.setColor(Color.green);
					g.drawString("Snake", xp, 150);
					g.setFont(g.getFont().deriveFont(40.0f));
					xp = (480 - g.getFontMetrics().stringWidth("On a Plane")) / 2;
					g.setColor(Color.black);
					g.drawString("On a Plane", xp, 213);
					g.setColor(Color.green.darker());
					g.drawString("On a Plane", xp, 210);
				} else {
					g.setFont(g.getFont().deriveFont(40.0f));
					int xp = (480 - g.getFontMetrics().stringWidth("Complete!")) / 2;
					g.setColor(Color.black);
					g.drawString("Complete!", xp, 173);
					g.setColor(Color.white);
					g.drawString("Complete!", xp, 170);
				}
			}
			getGraphics().drawImage(buffer, 0, 0, null);

			// logic
			if ((!acting) || (complete)) {
				if (!complete) {
					for (int i=0;i<length;i++) {
						int x = snake[i][0];
						int y = snake[i][1];
						int tile = map[x+(y*30)];
						
						if (i == 0) {
							if (tile == TELEPORT_IN) {
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
							map[x+(y*30)] = EMPTY;
							gravity = -gravity;
						}
						if ((tile == PELLET) || (tile == WHITE_PELLET) || (tile == RED_PELLET))  {
							map[x+(y*30)] = EMPTY;
							if (tile == PELLET) {
								length++;
							}
							if (tile == RED_PELLET) {
								length--;
								snake[length][0] = 0;
								snake[length][1] = 0;
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
							restart = true;
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
									leaving = true;
								}
							}
						}
					}
				}
				
				if ((!falling) && (!leaving)) {
					KeyEvent event = null;
					
					synchronized (this) {
						event = lastEvent;
						lastEvent = null;
					}
					
					if (event != null) {
						if (complete) {
							complete = false;
							restart = true;
						} else {
							int dx = snake[0][0];
							int dy = snake[0][1];
							if (event.getKeyCode() == KeyEvent.VK_N) {
								restart = true;
								level++;
								if (level > MAX_LEVELS) {
									level = 1;
								}
							}
							if (event.getKeyCode() == KeyEvent.VK_R) {
								restart = true;
							}
							if (event.getKeyCode() == KeyEvent.VK_LEFT) {
								dx--;
							}
							if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
								dx++;
							}
							if (event.getKeyCode() == KeyEvent.VK_UP) {
								dy--;
							}
							if (event.getKeyCode() == KeyEvent.VK_DOWN) {
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
								level++;
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
			
			/*
			 * Loop time and timing logic.
			 */
//			long timeNow = System.currentTimeMillis();
//			frameAverage = (frameAverage * 20 + (timeNow - lastFrame)) / 21;
//			lastFrame = timeNow;
//			yield += yield * (((float) FRAME_TIME / frameAverage) - 1) * 0.1f
//					+ 0.05f;
//			for (p = 0; p < yield; p++)
			try { Thread.sleep(1); } catch (Exception e) {};

			if (!isActive()) {
				return;
			}
		}
	}
	
	@Override
    public void processEvent(AWTEvent e)
    {
		KeyEvent event = (KeyEvent) e;
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			synchronized (this) {
				lastEvent = event;
			}
		}
	}
}
