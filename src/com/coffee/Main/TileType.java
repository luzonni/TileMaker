package com.coffee.Main;

public enum TileType {
	Block(0), Wall(1);
	
	private int index;
	private TileType(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
}
