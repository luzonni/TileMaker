package com.coffee.Main.Map;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.coffee.Main.Tools.Mouse;

public class MapWin extends Canvas {
	
	
	private static final long serialVersionUID = 1L;
	
	public JFrame frame;
	
	private int Width;
	private int Height;
	
	private static Mouse mouse;

	public MapWin(int w, int h) {
		Width = w;
		Height = h;
		initFrame();
		mouse = new Mouse();
		addMouseListener(mouse);
	}
	
	public int getWidth() {
		return Width; 
	}
	
	public int getHeight() {
		return Height;
	}
	
	public static Mouse getMouse() {
		return mouse;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void initFrame() {
		frame = new JFrame("Cobrinha");
		frame.add(this);
		this.setPreferredSize(new Dimension(Width, Height));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		createBufferStrategy(2);
	}
}

