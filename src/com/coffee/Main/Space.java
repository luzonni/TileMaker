package com.coffee.Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.coffee.Main.System.Floor;
import com.coffee.Main.System.Wall;
import com.coffee.Main.Tools.FontG;
import com.coffee.Main.Tools.Mouse;
import com.coffee.Main.Tools.SpriteSheet;

public class Space {
	
	private int Width, Height;
	public static int Scale = 4;
	private static final String sourcePath = "/com/coffee/res/Sources/";
	
	private static BufferedImage Compose;
	private static BufferedImage Sprite;
	private static String tileComposePath = "D:/Arquivos/My Project/Tools/TilesComposes";
	private static String SpriteNameSave;
	
	private TileType tileType = TileType.Block;
	
	public Space() {
		Width = Gears.window.getWidth();
		Height = Gears.window.getHeight();
	}
	
	public void tick() {
		
	}

	public void render(Graphics2D g) {
		//Close
		if(Button("Exit", Width - HorizontalPer(8.5f), VertivalPer(2f), 4, g)) {
			System.exit(1);
		}
		//Move
		if(Pressing("Move", Width - HorizontalPer(16f), VertivalPer(2f), 4, g)) {
			Gears.window.frame.setLocation(Mouse.getScreenX(), Mouse.getScreenY());
		}
		//Change Map
		if(Button("Help", Width - HorizontalPer(26f), VertivalPer(2f), 3, g)) {
			try {
	            // Verifica se a classe Desktop é suportada pelo sistema
	            if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                
	                // Verifica se é possível abrir URLs
	                if (desktop.isSupported(Desktop.Action.BROWSE)) {
	                    desktop.browse(new URI("https://github.com/luzonni/TileMaker"));
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		//Import
		if(tileComposePath != null)
			Text("Source Compose: "+tileComposePath, HorizontalPer(2f), VertivalPer(13.5f), 18, g);
		if(Button("Import", HorizontalPer(2f), VertivalPer(7f), 3, g)) {
			JFileChooser chooser = new JFileChooser(tileComposePath);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Select sprite","png");
			chooser.setFileFilter(filter);
			int retorno = chooser.showOpenDialog(null);
			
			if(retorno == JFileChooser.APPROVE_OPTION) {
				try {
					Compose = ImageIO.read(chooser.getSelectedFile().getAbsoluteFile());
				} catch (IOException e) {}
				tileComposePath = chooser.getSelectedFile().getAbsolutePath();
				
				if(Compose.getWidth() == 48 && Compose.getHeight() == 56) {
					this.tileType = TileType.Block;
					Floor floor = new Floor(Compose);
					Sprite = floor.getSprite();
				}else {
					
				}
				if(Compose.getWidth() == 48 && Compose.getHeight() == 80) {
					this.tileType = TileType.Wall;
					Wall wall = new Wall(Compose);
					Sprite = wall.getSprite();
				}else {
					
				}
			}
		}
		//Save
		if(Button("Save", Width - HorizontalPer(14f), Height - VertivalPer(9f), 4, g)) {
			File iconFile = new File(SpriteNameSave);
			File f = new File(iconFile.getPath());
		    try {
				ImageIO.write(Sprite, "png", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//LocalSave
		if(SpriteNameSave != null)
			Text("Save on: "+SpriteNameSave, HorizontalPer(1f), Height - VertivalPer(3.7f), 20, g);
		if(Button("Local", Width - HorizontalPer(29f), Height - VertivalPer(9f), 4, g)) {
			JFileChooser chooser = new JFileChooser(tileComposePath);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Select sprite","png");
			chooser.setFileFilter(filter);
			int retorno = chooser.showOpenDialog(null);
			
			if(retorno == JFileChooser.APPROVE_OPTION) {
				SpriteNameSave = chooser.getSelectedFile().getAbsolutePath();
			}
		}
		//Image
		Image(Compose, HorizontalPer(2f), VertivalPer(20.5f), 5, g);
		//Tiles
		if(Sprite != null) {
			int index = 0;
			int x = 0;
			int y = 0;
			while(index*16 < Sprite.getWidth()) {
				if(tileType.equals(TileType.Block)) {
					Image(Sprite.getSubimage(index*16, 0, 16, Sprite.getHeight()), HorizontalPer(30.5f+(x*6)), VertivalPer(20.5f+(y*10.5f)), 3, g);
					index++;
					x++;
					if(x > 10) {
						x = 0;
						y++;
					}
				}else {
					Image(Sprite.getSubimage(index*16, 0, 16, Sprite.getHeight()), HorizontalPer(29.5f+(x*4)), VertivalPer(20.5f+(y*13f)), 2, g);
					index++;
					x++;
					if(x > 16) {
						x = 0;
						y++;
					}
				}
			}
		}			
		BoundsRender(g);
	}
	
	private void BoundsRender(Graphics2D g) {
		g.setStroke(new BasicStroke(Scale));
		g.setColor(Color.black);
		g.drawRect(0, 0, Width-Scale/2, Height-Scale/2);
		g.setColor(Color.white);
		g.drawRect(Scale/2, Scale/2, Width-(Scale*2), Height-(Scale*2));
		Text("Tile Crater By: @Lucaszonzini_ Map", HorizontalPer(2f), VertivalPer(2f), 22, g);
	}
	
	public void Image(BufferedImage Image , int x, int y, int size, Graphics2D g) {
		if(Image == null)
			return;
		int W = Image.getWidth();
		int H = Image.getHeight();
		g.setColor(Color.black);
		g.fillRect(x+size, y+size, W*size, H*size);
		g.drawImage(Image, x, y, W*size, H*size, null);
	}
	
	public void Text(String Value, int x, int y, int size, Graphics2D g) {
		Font font = FontG.font(size);
		g.setFont(font);
		int hF = FontG.getHeight(Value, font);
		g.setColor(Color.black);
		g.drawString(Value, x+3, y+3+hF);
		g.setColor(Color.white);
		g.drawString(Value, x, y+hF);
	}
	
	public boolean Button(String Name,  int x, int y, int size, Graphics2D g) {
		Object[] button = new Object[2];
		button[0] = new SpriteSheet(sourcePath+Name+".png", size).getImage();
		button[1] = new Rectangle(x, y, ((BufferedImage)button[0]).getWidth(), ((BufferedImage)button[0]).getHeight());
		g.setColor(Color.black);
		g.fillRect(((Rectangle)button[1]).x+size, ((Rectangle)button[1]).y+size, ((BufferedImage)button[0]).getWidth(), ((BufferedImage)button[0]).getHeight());
		g.drawImage((BufferedImage)button[0], ((Rectangle)button[1]).x, ((Rectangle)button[1]).y, null);
		if(((Rectangle)button[1]).getBounds().contains(Mouse.getX(), Mouse.getY()) && Mouse.pressLeft) {
			Mouse.pressLeft = false;
			return true;
		}
		return false;
	}
	
	public boolean Pressing(String Name,  int x, int y, int size, Graphics2D g) {
		Object[] button = new Object[2];
		button[0] = new SpriteSheet(sourcePath+Name+".png", size).getImage();
		button[1] = new Rectangle(x, y, ((BufferedImage)button[0]).getWidth(), ((BufferedImage)button[0]).getHeight());
		g.setColor(Color.black);
		g.fillRect(((Rectangle)button[1]).x+size, ((Rectangle)button[1]).y+size, ((BufferedImage)button[0]).getWidth(), ((BufferedImage)button[0]).getHeight());
		g.drawImage((BufferedImage)button[0], ((Rectangle)button[1]).x, ((Rectangle)button[1]).y, null);
		if(((Rectangle)button[1]).getBounds().contains(Mouse.getX(), Mouse.getY()) && Mouse.pressLeft) {
			return true;
		}
		return false;
	}
	
	private int HorizontalPer(float value) {
		return (int)((value/100f)*Width);
	}
	
	private int VertivalPer(float value) {
		return (int)((value/100f)*Height);
	}

}
