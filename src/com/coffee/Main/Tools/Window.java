package com.coffee.Main.Tools;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends Canvas {
	
	private static final long serialVersionUID = 1L;
	
	public JFrame frame;
	
	private int Width;
	private int Height;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private static Mouse mouse;

	public Window(int w, int h) {
		Width = w;
		Height = h;
		initFrame();
		mouse = new Mouse();
		addMouseListener(mouse);
	}
	
	public int getWidth() {
		Component c = frame.getComponent(0);
		return c.getWidth();
	}
	
	public int getHeight() {
		Component c = frame.getComponent(0);
		return c.getHeight();
	}
	
	public static Mouse getMouse() {
		return mouse;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void initFrame() {
		frame = new JFrame("Tile Createer");
		frame.add(this);
		frame.setUndecorated(true);
		this.setPreferredSize(new Dimension(Width, Height));
		frame.pack();
		try {
			Image icone = ImageIO.read(getClass().getResource("/com/coffee/res/Sources/Icon.png"));
			Image cursor = ImageIO.read(getClass().getResource("/com/coffee/res/Sources/Cursor.png"));
			Cursor c = toolkit.createCustomCursor(cursor, new Point(0,0), "cursor");
			frame.setCursor(c);
			frame.setIconImage(icone);
		}catch(Exception e) { e.printStackTrace(); }
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		createBufferStrategy(2);
	}
}


