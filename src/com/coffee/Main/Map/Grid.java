package com.coffee.Main.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.coffee.Main.TileType;
import com.coffee.Main.Tools.Mouse;

public class Grid {
	
	public BufferedImage[] Chunks;
	public BufferedImage[] Bases;
	public int[][] Grid;
	
	public int Step = 0;
	
	public int CurIndex = 0;
	
	public TileType type = TileType.Block;
	public int padding;
	public int W_I = 0;
	public int H_I = 0;
	
	public int W_G, H_G;
	
	public Rectangle next_button;
	private int Width;
	private int Height;
	
	public Grid(TileType type, int Width, int Height) {
		this.Width = Width;
		this.Height = Height;
		padding = 6*Map.Scale;
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("/com/coffee/res/Templates/"+type.getName()+".png"));
		} catch (IOException e) {
			return;
		}
		Chunks = getChunks(image);
		if(type.equals(TileType.Block)) {
			W_G = 2;
			H_G = 3;
		}else if(type.equals(TileType.Wall)) {
			W_G = 2;
			H_G = 6;
		}	
		Grid = new int[47][W_G*H_G];
		for(int s = 0; s < Grid.length; s++)
			for(int i = 0; i < Grid[s].length; i++)
				Grid[s][i] = -1;
		this.type = type;
		buildBase();
		next_button = new Rectangle(Width - 22*Map.Scale, Height - 32*Map.Scale, 16*Map.Scale, 24*Map.Scale);
	}
	
	private BufferedImage[] getChunks(BufferedImage image) {
		W_I = image.getWidth()/8;
		H_I = image.getHeight()/8;
		BufferedImage[] chs = new BufferedImage[W_I*H_I];
		for(int chunkY = 0; chunkY < H_I; chunkY++) {
			for(int chunkX = 0; chunkX < W_I; chunkX++) {
				chs[chunkX+chunkY*W_I] = image.getSubimage(chunkX*8, chunkY*8, 8, 8);
			}
		}
		return chs;
	}
	
	private void buildBase() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("/com/coffee/res/Templates/Base.png"));
		} catch (IOException e) {
			return;
		}
		Bases = new BufferedImage[image.getWidth()/16];
		for(int i = 0; i < Bases.length; i++)
			Bases[i] = image.getSubimage(i*16, 0, 16, 24);
	}
	
	public void SaveGrid() {
		String fileName = JOptionPane.showInputDialog("Source Name:");
		File file = new File(fileName+".txt");
		FileWriter filer = null;
		BufferedWriter write = null;
		try {
			filer = new FileWriter(file.getPath());
			write = new BufferedWriter(filer);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String current = "";
		for(int T = 0; T < Grid.length; T++) {
			for(int C = 0; C < Grid[T].length; C++) {
				current += Grid[T][C];
				if(C < Grid[T].length-1) current+=":";
			}
			current +="/";
		}
		try {
			write.write(current);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {e.printStackTrace();}
	}
	
	public void tick() {
		if(MapWin.getMouse().clickLeftOn(next_button) && Step <= Grid.length-1) {
			Step++;
			Mouse.pressLeft = false;
		}
		if(MapWin.getMouse().clickRightOn(next_button) && Step > 0) {
			Step--;
			Mouse.pressRight = false;
		}
		if(Step == Grid.length) {
			Step = Grid.length;
			SaveGrid();
		}
	}
	
	public void render(Graphics g) {
		renderGetter(g);
		renderSetter(g);
		g.setFont(new Font("Bebas",Font.BOLD, 4*Map.Scale));
		g.setColor(Color.WHITE);
		g.drawString("Remain: " + (46-Step), 0, Height);
		g.setColor(Color.red);
		g.fillRect(next_button.x, next_button.y, next_button.width, next_button.height);
		g.drawImage(Bases[Step], next_button.x, next_button.y, next_button.width, next_button.height, null);
		g.setColor(new Color(255, 255, 255, 20));
		for(int y = 0; y < next_button.height; y+=Map.Scale) {
			for(int x = 0; x < next_button.width; x+=Map.Scale) {
				g.drawRect(next_button.x+x, next_button.y+y, Map.Scale, Map.Scale);
			}
		}
	}
	
	private void renderGetter(Graphics g) {
		for(int y = 0; y < H_I; y++) {
			for(int x = 0; x < W_I; x++) {
				int width = Chunks[0].getWidth()*Map.Scale;
				int height =  Chunks[0].getHeight()*Map.Scale;
				int index = x+y*W_I;
				g.drawImage(Chunks[index], padding + x*width, padding + y*height, width, height, null);
				if(index == CurIndex)
					g.setColor(Color.red);
				else
					g.setColor(new Color(255, 255, 255, 20));
				g.drawRect(padding + x*width + 1, padding + y*height + 1, width - 2, height - 2);
				Rectangle rec = new Rectangle(padding + x*width, padding + y*height, width, height);
				if(MapWin.getMouse().clickLeftOn(rec)) {
					Mouse.pressLeft = false;
					CurIndex = index;
				}
			}
		}
		int width = Chunks[0].getWidth()*Map.Scale * W_I;
		int height =  Chunks[0].getHeight()*Map.Scale * H_I;
		g.setColor(Color.red);
		g.drawRect(padding, padding, width, height);
	}
	
	private void renderSetter(Graphics g) {
		int width = Chunks[0].getWidth()*Map.Scale * W_G;
		int height =  Chunks[0].getHeight()*Map.Scale * H_G;
		int XX = Width - width - padding;
		int YY = padding;
		g.setColor(Color.red);
		g.fillRect(XX, YY, width, height);
		for(int y = 0; y < H_G; y++) {
			for(int x = 0; x < W_G; x++) {
				int w = Chunks[0].getWidth()*Map.Scale;
				int h =  Chunks[0].getHeight()*Map.Scale;
				int index = x+y*W_G;
				if(Grid[Step][index] != -1)
					g.drawImage(Chunks[Grid[Step][index]], XX + x*w, YY + y*h, w, h, null);
				Rectangle rec = new Rectangle(XX + x*w, YY + y*h, w, h);
				if(MapWin.getMouse().clickLeftOn(rec)) {
					Mouse.pressLeft = false;
					Grid[Step][index] = CurIndex;
				}
				if(MapWin.getMouse().clickRightOn(rec)) {
					Grid[Step][index] = -1;
				}
			}
		}
		g.setColor(new Color(255, 255, 255, 20));
		for(int y = 0; y < height; y+=Map.Scale) {
			for(int x = 0; x < width; x+=Map.Scale) {
				g.drawRect(XX+x, YY+y, Map.Scale, Map.Scale);
			}
		}
		g.setColor(Color.red);
		g.drawRect(XX, YY, width, height);
	}
	
}
