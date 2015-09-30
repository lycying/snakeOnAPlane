package info.u250.snakeonaplane.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MapEditor extends JFrame {
	
	private static final long serialVersionUID = -3565633279791151781L;
	private MapPanel mapPanel = new MapPanel();
	private JList list;
	private int selected;
	
	private JFileChooser chooser = new JFileChooser(".");
	
	public MapEditor() {
		super("Editor");
		
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem clear = new JMenuItem("Clear");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem quit = new JMenuItem("Quit");
		file.add(clear);
		file.addSeparator();
		file.add(load);
		file.add(save);
		file.addSeparator();
		file.add(quit);
		bar.add(file);
		setJMenuBar(bar);
		
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.clear();
			}
		});
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		Vector<String> items = new Vector<String>();
		items.add("Clear");
		items.add("Wall");
		items.add("Pellet");
		items.add("Exit");
		items.add("Start");
		items.add("White Pellet");
		items.add("Sticky");
		items.add("Teleport In");
		items.add("Teleport Out");
		items.add("Red Pellet");
		items.add("Gravity Switch");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		list = new JList(items);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				selected = list.getSelectedIndex();
				mapPanel.setTool(selected);
			}
		});
		JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(mapPanel), new JScrollPane(list));
		panel.setDividerSize(0);
		panel.setDividerLocation(480);
		panel.setResizeWeight(1.0f);
		
		setContentPane(panel);
		setSize(600,480);
		setVisible(true);
	}
	
	private void load() {
		int resp = chooser.showOpenDialog(this);
		if (resp == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				FileInputStream in = new FileInputStream(file);
				byte[] data = new byte[30*20];
				in.read(data, 0, 30*20);
				in.close();
				
				mapPanel.setMap(data);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Failed to load file: "+file);
			}
		}
	}
	
	private void save() {
		int resp = chooser.showSaveDialog(this);
		if (resp == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				FileOutputStream out = new FileOutputStream(file);
				out.write(mapPanel.getMap());
				out.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Failed to save file: "+file);
			}
		}
	}
	
	public static void main(String[] argv) {
		new MapEditor();
	}
}
