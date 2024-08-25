package com.coffee.Main;

public enum TileType {
	Block(0, "block"), Wall(1, "wall");
	
	private int index;
	private String name;
	private TileType(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String getName() {
		return this.name;
	}
	
}
