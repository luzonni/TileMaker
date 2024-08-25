package com.coffee.Main.Tools;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;

public class FontG {
	

	private static InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/coffee/res/Sources/Septem.ttf");
	private static Font font;
	
	public static void addFont() {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Font font(float size) {
		if(font == null)
			addFont();
		return font.deriveFont(size);
	}
	
	public static int getWidth(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getWidth());
	}
	
	public static int getHeight(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getHeight() - 0.29*font.getSize());
	}
	
	public static int getSize() {
		return font.getSize();
	}
	
}
