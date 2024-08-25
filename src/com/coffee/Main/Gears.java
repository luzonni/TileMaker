package com.coffee.Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.coffee.Main.Tools.Window;

public class Gears implements Runnable {
	
	private static boolean isRunning;
	private static Thread thread;
	public static final double HZ = 60;
	public static int MaxFrames = 256;
	public static final double T = 1_000_000_000.0;
	public static int FRAMES;
	public static int HERTZ;
	
	public static Window window;
	public Space space;
	
	public Gears() {
		window = new Window(960, 800);
		space = new Space();
		start();
	}
	
	public static void main(String[] args) {
		new Gears();
	}
	
	public void tick() {
		space.tick();
	}
	
	public void render() {
		BufferStrategy bufferStrategy = window.getBufferStrategy();
		if(bufferStrategy == null) {
			window.createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
		g.setColor(new Color(34, 32, 52));
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		space.render(g);
		bufferStrategy.show();
		g.dispose();
	}
	
	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public static void stop() {
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
		double amountOfHz = HZ;
		double ns_HZ = T / amountOfHz;
		double delta_HZ = 0;
		
		long lastTimeFPS = System.nanoTime();
		double amountOfFPS = MaxFrames;
		double ns_FPS = T / amountOfFPS;
		double delta_FPS = 0;
		
		//To Show
		int Hz = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			try {
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
				if(System.currentTimeMillis() - timer >= 1000){
//					Engine.window.getFrame().setTitle(Engine.GameTag+" - Hz: " + Hz + " / Frames: " + frames);
					FRAMES = frames;
					frames = 0;
					HERTZ = Hz;
					Hz = 0;
					timer+=1000;
				}
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
