package com.coffee.Main.Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.coffee.Main.TileType;

public class Map implements Runnable {
	
	public boolean isRunning = true;
	public Thread thread;
	
	public MapWin window;
	public BufferStrategy buffer;
	public int FRAMES;
	public int HERTZ;
	
	public Grid grid;
	
	public static int Scale = 8;
	
	public Map(TileType type) {
		window = new MapWin(800, 800);
		grid = new Grid(type, window.getWidth(), window.getHeight());
		start();
	}
	
	public static void main(String[] args) {
		new Map(TileType.Block);
	}
	
	public MapWin getWindow() {
		return window;
	}

	public void tick() {
		grid.tick();
	}
	
	public void render() {
		if(buffer == null) {
			window.createBufferStrategy(2);
			buffer = window.getBufferStrategy();
		}
		Graphics g = buffer.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		grid.render(g);
		g.dispose();
		buffer.show();
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = 60;
		double ns_HZ = 1000000000.0 / amountOfHz;
		double delta_HZ = 0;
		
		long lastTimeFPS = System.nanoTime();
		double amountOfFPS = 144;
		double ns_FPS = 1000000000.0 / amountOfFPS;
		double delta_FPS = 0;
		
		//To Show
		int Hz = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			
			long nowHZ = System.nanoTime();
			delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
			lastTimeHZ = nowHZ;
			if(delta_HZ >= 1) {
				tick();
				Hz++;
				delta_HZ--;
			}
			
			long nowFPS = System.nanoTime();
			delta_FPS += (nowFPS - lastTimeFPS) / ns_FPS;
			lastTimeFPS = nowFPS;
			if(delta_FPS >= 1) {
				render();
				frames++;
				delta_FPS--;
			}
			//Show fps
			if(System.currentTimeMillis() - timer >= 1000){
				window.getFrame().setTitle(" - Hz: " + Hz + " / Frames: " + frames);
				FRAMES = frames;
				frames = 0;
				HERTZ = Hz;
				Hz = 0;
				timer+=1000;
			}
		}
		stop();
	}
	
}
