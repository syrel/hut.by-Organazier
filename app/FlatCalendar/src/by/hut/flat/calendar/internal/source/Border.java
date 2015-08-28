package by.hut.flat.calendar.internal.source;

import android.graphics.Color;

public class Border {
	private int orientation;
	private int width = 4;
	private int color = Color.BLACK;
	private int style;
	
	public int getWidth() {
		return width;
	}
	public Border setWidth(int width) {
		this.width = width;
		return this;
	}
	public int getColor() {
		return color;
	}
	public Border setColor(int color) {
		this.color = color;
		return this;
	}
	public int getStyle() {
		return style;
	}
	public Border setStyle(int style) {
		this.style = style;
		return this;
	}
	public int getOrientation() {
		return orientation;
	}
	public Border setOrientation(int orientation) {
		this.orientation = orientation;
		return this;
	}
	public Border(int Style) {
		this.style = Style;
	}
}