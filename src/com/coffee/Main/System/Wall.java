package com.coffee.Main.System;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Wall {
	
	public BufferedImage IMAGE_TILE;
	public BufferedImage[] chunks;
	public BufferedImage SpriteSheet;
	public String Image_Name;
	public String locateSaver;
	
	private int[][] Combination_System;
	
	private void getCombinations() {
		Reader fileReader = null;
		try {
			fileReader = new InputStreamReader(getClass().getResource("/com/coffee/res/Templates/Wall.txt").openStream());
		} catch (IOException e) { }
		BufferedReader reader = null;
		String line = "";
		String singleLine = null;
		reader = new BufferedReader(fileReader);
		try {
			while((singleLine = reader.readLine()) != null) {
				line += singleLine;
			}
		}catch(IOException e) { }
		String[] Tiles = line.split("/");
		for(int T = 0; T < Tiles.length; T++) {
			String[] chunks = Tiles[T].split(":");
			for(int i = 0; i < chunks.length; i++) {
				if(Combination_System == null)
					Combination_System = new int[Tiles.length][chunks.length];
				Combination_System[T][i] = Integer.parseInt(chunks[i]);
			}
		}
	}
	
	public Wall(BufferedImage Compose) {
		getCombinations();
		IMAGE_TILE = Compose;
		chunks = getChunks(IMAGE_TILE);
		SpriteSheet = createSpriteSheet(chunks);
	}
	
	public BufferedImage[] getChunks(BufferedImage image) {
		int w = image.getWidth()/8;
		int h = image.getHeight()/8;
		BufferedImage[] chs = new BufferedImage[w*h];
		for(int chunkY = 0; chunkY < h; chunkY++) {
			for(int chunkX = 0; chunkX < w; chunkX++) {
				chs[chunkX+chunkY*w] = image.getSubimage(chunkX*8, chunkY*8, 8, 8);
			}
		}
		return chs;
	}
	
	public BufferedImage createSpriteSheet(BufferedImage[] chunks) {
		BufferedImage spritesheet = new BufferedImage(752, 48, BufferedImage.TYPE_INT_ARGB);
		int length = spritesheet.getWidth()/16;
		Graphics g = spritesheet.getGraphics();
		for(int i = 0; i < length; i++) {
			int position = i*16;
			g.drawImage(getTile(chunks, i), position, 0, null);
		}
		return spritesheet;
	}
	
	private BufferedImage getTile(BufferedImage[] chunks, int index) {
		BufferedImage tile = new BufferedImage(16, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tile.getGraphics();
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 2; x++) {
				if(Combination_System[index].length-1 < (x+y*2)) continue;
				if(Combination_System[index][x+y*2] != -1)
				g.drawImage(chunks[Combination_System[index][x+y*2]], x*8, y*8, null);
			}
		}
		return tile;
	}
	
	public BufferedImage getSprite() {
		return SpriteSheet;
	}

}
