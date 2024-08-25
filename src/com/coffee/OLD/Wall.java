package com.coffee.OLD;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Wall {
	
	public BufferedImage IMAGE_TILE;
	public BufferedImage[] chunks;
	public BufferedImage SpriteSheet;
	public String Image_Name;
	public String locateSaver;
	
	private int[][] Combination_System;
	
	private void getCombinations() {
		File file = new File("Wall.txt");
		if(!file.exists())
			throw new RuntimeException("Wall.txt not found");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file.getPath());
		} catch (FileNotFoundException e) { }
		BufferedReader reader = null;
		String line = "";
		if(file.exists()) {
			String singleLine = null;
			reader = new BufferedReader(fileReader);
			try {
				while((singleLine = reader.readLine()) != null) {
					line += singleLine;
				}
			}catch(IOException e) { }
		}
		String[] Tiles = line.split("/");
		for(int T = 0; T < Tiles.length; T++) {
			String[] chunks = Tiles[T].split(":");
			for(int i = 0; i < chunks.length; i++) {
				if(Combination_System == null)
					Combination_System = new int[Tiles.length][chunks.length];
				Combination_System[T][i] = Integer.parseInt(chunks[i]);
				System.out.print(Combination_System[T][i]+",");
			}
			System.out.println();
		}
	}
	
	public Wall() {
		getCombinations();
		IMAGE_TILE = getFile();
		if(IMAGE_TILE == null) {
			JOptionPane.showMessageDialog(null, "Any file selected!");
			return;
		}
		JOptionPane.showMessageDialog(null, null, "IMAGE_TILE", JOptionPane.YES_NO_OPTION, new ImageIcon(IMAGE_TILE.getScaledInstance(IMAGE_TILE.getWidth() * 16, IMAGE_TILE.getHeight() * 16, Image.SCALE_DEFAULT)));
		chunks = getChunks(IMAGE_TILE);
		SpriteSheet = createSpriteSheet(chunks);
		JOptionPane.showMessageDialog(null, null, "IMAGE_TILE", JOptionPane.YES_NO_OPTION, new ImageIcon(SpriteSheet.getScaledInstance(SpriteSheet.getWidth() * 3, SpriteSheet.getHeight() * 3, Image.SCALE_DEFAULT)));
		SaveSpriteSheet(SpriteSheet);
	}
	
	public BufferedImage getFile() {
		BufferedImage image = null;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Select sprite","png");
		chooser.setFileFilter(filter);
		int retorno = chooser.showOpenDialog(null);
		
		if(retorno == JFileChooser.APPROVE_OPTION) {
			try {
				image = ImageIO.read(chooser.getSelectedFile().getAbsoluteFile());
			} catch (IOException e) {}
			Image_Name = chooser.getSelectedFile().getName();
			locateSaver = chooser.getSelectedFile().getParent();
			if(image.getWidth() != 48 && image.getHeight() != 80) {
				JOptionPane.showMessageDialog(null, "Image must be 48x80!");
				return null;
			}
		}
		return image;
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
	
	private void SaveSpriteSheet(BufferedImage spritesheet)  {
		String file_name = JOptionPane.showInputDialog("New SpriteSheet Name");
		if(file_name == null || file_name.equals("")) {
			JOptionPane.showMessageDialog(null, "Invalid name");
			return;
		}
		File iconFile = new File(locateSaver+"/"+file_name+".png");
		if(!iconFile.exists()) {
			File f = new File(iconFile.getPath());
		    try {
				ImageIO.write(spritesheet, "png", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			JOptionPane.showMessageDialog(null, "File exists");
		}
	}
	
	public static void main(String[] args) {
		new Wall();
	}

}
