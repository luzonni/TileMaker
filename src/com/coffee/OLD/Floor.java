package com.coffee.OLD;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Floor {
	
	public BufferedImage IMAGE_TILE;
	public BufferedImage[] chunks;
	public BufferedImage SpriteSheet;
	public String Image_Name;
	public String locateSaver;
	
	private final int[][] Combination_System = {
			{0, 5, 30, 35, 36, 41}, 	//0
			{0, 5, 6, 11}, 				//1
			{12, 17, 18, 23}, 			//2
			{24, 29, 30, 35, 36, 41}, 	//3
			{0, 1, 30, 31, 36, 37}, 	//4
			{2, 3, 32, 33, 38, 39}, 	//5
			{4, 5, 34, 35, 40, 41}, 	//6
			{0, 1, 6, 7}, 				//7
			{0, 1, 6, 21}, 				//8
			{2, 3, 8, 9},	 			//9
			{2, 3, 20, 9}, 				//10
			{2, 3, 20, 21}, 			//11
			{2, 3, 8, 21}, 				//12
			{4, 5, 10, 11}, 			//13
			{4, 5, 20, 11}, 			//14
			{12, 13, 18, 19}, 			//15
			{12, 13, 18, 21}, 			//16
			{12, 15, 18, 21}, 			//17
			{12, 15, 18, 19}, 			//18
			{16, 17, 22, 23}, 			//19
			{14, 17, 22, 23}, 			//20
			{14, 17, 20, 23}, 			//21
			{16, 17, 20, 23}, 			//22
			{24, 25, 30, 31, 36, 37}, 	//23
			{24, 15, 30, 31, 36, 37}, 	//24
			{26, 27, 32, 33, 38, 39}, 	//25
			{14, 27, 32, 33, 38, 39}, 	//26
			{14, 15, 32, 33, 38, 39}, 	//27
			{26, 15, 32, 33, 38, 39}, 	//28
			{28, 29, 34, 35, 40, 41}, 	//29
			{14, 29, 34, 35, 40, 41}, 	//30
			{14, 15, 20, 21}, 			//31
			{26, 27, 20, 21}, 			//32
			{14, 15, 8, 9}, 			//33
			{26, 15, 8, 21}, 			//34
			{14, 13, 20, 19}, 			//35
			{26, 15, 20, 9}, 			//36
			{14, 27, 8, 21}, 			//37
			{26, 15, 20, 21}, 			//38
			{14, 27, 20, 21}, 			//39
			{14, 15, 20, 9}, 			//40
			{14, 15, 8, 21}, 			//41
			{26, 27, 8, 21}, 			//42
			{26, 27, 20, 9}, 			//43
			{14, 27, 8, 9}, 			//44
			{16, 15, 8, 9}, 			//45
			{26, 27, 8, 9}, 			//46
			
			
	};
	
	public Floor() {
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
			if(image.getWidth() != 48 && image.getHeight() != 56) {
				JOptionPane.showMessageDialog(null, "Image must be 48x56!");
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
		BufferedImage spritesheet = new BufferedImage(752, 24, BufferedImage.TYPE_INT_ARGB);
		int length = spritesheet.getWidth()/16;
		Graphics g = spritesheet.getGraphics();
		for(int i = 0; i < length; i++) {
			int position = i*16;
			g.drawImage(getTile(chunks, i), position, 0, null);
		}
		return spritesheet;
	}
	
	private BufferedImage getTile(BufferedImage[] chunks, int index) {
		BufferedImage tile = new BufferedImage(16, 24, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tile.getGraphics();
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 2; x++) {
				if(Combination_System[index].length-1 < (x+y*2)) continue;
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
		new Floor();
	}

}
