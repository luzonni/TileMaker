package com.coffee.Main.Tools;

import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	
	private static int xMouse, yMouse;
	
	public static boolean pressLeft, pressRight;
	
	public boolean clickRightOn(Rectangle rec) {
		if(rec.contains(xMouse, yMouse) && pressRight) {
			return true;
		}
		return false;
	}
	
	public boolean clickLeftOn(Rectangle rec) {
		if(rec.contains(xMouse, yMouse) && pressLeft) {
			return true;
		}
		return false;
	}
	
	public static int getX() {
		return xMouse;
	}
	
	public static int getY() {
		return yMouse;
	}
	
	public static int getScreenX() {
		return MouseInfo.getPointerInfo().getLocation().x - xMouse;
	}
	
	public static int getScreenY() {
		return MouseInfo.getPointerInfo().getLocation().y - yMouse;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(MouseEvent.BUTTON1 == e.getButton()) {
			pressLeft = true;
		}
		if(MouseEvent.BUTTON3 == e.getButton()) {
			pressRight = true;
		}
		xMouse = e.getX();
		yMouse = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(MouseEvent.BUTTON1 == e.getButton()) {
			pressLeft = false;
		}
		if(MouseEvent.BUTTON3 == e.getButton()) {
			pressRight = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
