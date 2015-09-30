package info.u250.snakeonaplane.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 8476864316556318049L;
	private int tool;
	private byte[] map = new byte[30*20];
	
	public MapPanel() {
		setMinimumSize(new Dimension(720,480));
		setMaximumSize(new Dimension(720,480));
		setPreferredSize(new Dimension(720,480));
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g) {
		for (int x=0;x<30;x++) {
			for (int y=0;y<20;y++) {
				int tile = map[x+(y*30)];
				if (tile == 0) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
				}
				if (tile == 1) {
					g.setColor(new Color(0x777700));
					g.fillRect(x*20, y*20, 20, 20);
				}
				if (tile == 2) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(new Color(0xFFFF00));
					g.fillOval((x*20)+2, (y*20)+2, 16, 16);
				}
				if (tile == 3) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(new Color(0x000000));
					g.fillOval((x*20)+2, (y*20)+2, 16, 16);
				}
				if (tile == 4) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(new Color(0x00FF00));
					g.fillOval((x*20)+2, (y*20)+2, 16, 16);
				}
				if (tile == 5) {
					g.setColor(new Color(0xFFFFFF));
					g.fillOval((x*20)+2, (y*20)+2, 16, 16);
				}
				if (tile == 6) {
					g.setColor(new Color(0x777700));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(new Color(0x444400));
					g.fillRect(x*20, (y*20)+16, 20, 4);
				}
				if (tile == 7) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(Color.white);
					g.drawRect(x*20, y*20, 19, 19);
					g.drawString("I", (x*20)+7, 14+(y*20));
				}
				if (tile == 8) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(Color.white);
					g.drawRect(x*20, y*20, 19, 19);
					g.drawString("O", (x*20)+7, 14+(y*20));
				}
				if (tile == 9) {
					g.setColor(new Color(0xFF0000));
					g.fillOval((x*20)+2, (y*20)+2, 16, 16);
				}
				if (tile == 10) {
					g.setColor(new Color(0x0044CC));
					g.fillRect(x*20, y*20, 20, 20);
					g.setColor(Color.white);
					g.drawRect(x*20, y*20, 19, 19);
					g.drawString("G", (x*20)+7, 14+(y*20));
				}
			}
		}
	}

	public void setTool(int selected) {
		tool = selected;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePressed(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	private void mousePressed(int x, int y) {
		x = x / 20;
		y = y / 20;
		if ((x >= 0) && (y >= 0) && (x < 30) && (y < 20)) {
			map[x+(y*30)] = (byte) tool;
			repaint(0);
		}
	}

	public byte[] getMap() {
		return map;
	}
	
	public void setMap(byte[] data) {
		map = data;
		repaint(0);
	}
	public void clear() {
		map = new byte[30*20];
		repaint(0);
	}
}
