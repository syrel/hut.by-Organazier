package by.hut.flat.calendar.cell;

import by.hut.flat.calendar.utils.Color;
import by.hut.flat.calendar.utils.Dimension;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class MaidenBackground {
	
	public static final int MAIDEN_OVERFLOW = -2;
	public static final int MAIDEN_DEFAULT = -1;
	public static final int MAIDEN_NO_CLEANING = 0;
	public static final int MAIDEN_CHECKERS = 1;
	public static final int MAIDEN_VERTICAL_LINES = 2;
	public static final int MAIDEN_HORIZONTAL_LINES = 3;
	
	public static final int CHECKERS_NUM = 9;
	public static final boolean CHECKERS_FIRST_SQUARE = true;
	public static final boolean CHECKERS_OTHER_DIMENSION_ODD = true; // odd = nechetnyi
	
	public static final int VERTICAL_LINES_NUM = 9;
	public static final boolean VERTICAL_LINES_FIRST_LINE = false;
	
	public static final int HORIZONTAL_LINES_NUM = 13;
	public static final boolean HORIZONTAL_LINES_FIRST_LINE = false;
	
	private static final Paint whitePaint = new Paint();
	static {
		whitePaint.setColor(android.graphics.Color.WHITE);
	}
	
	public static void draw(Canvas canvas, Paint paint,Dimension dimension, int background){
		switch(background){
			case MAIDEN_OVERFLOW: {
				fourSquares(canvas,paint,dimension);
				break;
			}
			case MAIDEN_DEFAULT: {
				defaultBG(canvas, paint,dimension);
				break;
			}
			case MAIDEN_NO_CLEANING:{
				noCleaning(canvas, paint,dimension);
				break;
			}
			case MAIDEN_CHECKERS: {
				checkers(canvas, paint,dimension);
				break;
			}
			case MAIDEN_VERTICAL_LINES: {
				verticalLines(canvas, paint,dimension);
				break;
			}
			case MAIDEN_HORIZONTAL_LINES: {
				horizontalLines(canvas, paint,dimension);
				break;
			}
		}
	}
	
	private static void fourSquares(Canvas canvas, Paint paint,Dimension dimension){
		if (dimension.filter){
			int colorFilter = createFilter(paint);
			paint.setColorFilter(new LightingColorFilter(colorFilter , 0));
		}
		
		/**
		 * [top-left][left,top,right,bottm]
		 * [top-right][left,top,right,bottm]
		 * [bottom-right][left,top,right,bottm]
		 * [bottom-left][left,top,right,bottm]
		 */
		int[][] squares = new int[4][4];
		squares[0][0] = 0;
		squares[0][1] = 0;
		squares[0][2] = dimension.width/2;
		squares[0][3] = dimension.height/2;
		squares[1][0] = squares[0][2];
		squares[1][1] = 0;
		squares[1][2] = dimension.width;
		squares[1][3] = squares[0][3];
		squares[2][0] = squares[0][2];
		squares[2][1] = squares[1][3];
		squares[2][2] = dimension.width;
		squares[2][3] = dimension.height;
		squares[3][0] = 0;
		squares[3][1] = squares[0][3];
		squares[3][2] = squares[0][2];
		squares[3][3] = dimension.height;
		
		canvas.drawRect(squares[0][0], squares[0][1],squares[0][2], squares[0][3], whitePaint);
		canvas.drawRect(squares[1][0], squares[1][1],squares[1][2], squares[1][3], paint);
		canvas.drawRect(squares[2][0], squares[2][1],squares[2][2], squares[2][3], whitePaint);
		canvas.drawRect(squares[3][0], squares[3][1],squares[3][2], squares[3][3], paint);
		
	}
	
	private static void defaultBG(Canvas canvas, Paint paint,Dimension dimension){
		if (dimension.filter){
			int colorFilter = createFilter(paint);
			paint.setColorFilter(new LightingColorFilter(colorFilter , 0));
		}
		canvas.drawRect(0,0, dimension.width,dimension.height,paint);
	}
	
	private static void noCleaning(Canvas canvas, Paint paint,Dimension dimension){
		canvas.drawRect(0,0, dimension.width,dimension.height,paint);
	}
	
	private static void checkers(Canvas canvas, Paint paint,Dimension dimension){
		if (dimension.filter){
			int colorFilter = createFilter(paint);
			paint.setColorFilter(new LightingColorFilter(colorFilter , 0));		
		}
		
		int size[][] = calcCheckers(dimension);
		int h = 0;
		for (int y = 0,lengthY = size[1].length; y < lengthY; y++){
			int w = 0;
			for (int x = 0,lengthX = size[0].length; x < lengthX; x++){
				canvas.drawRect(w, h, w+size[0][x], h+size[1][y], isOdd(x+((CHECKERS_FIRST_SQUARE) ? 0 : 1)+y,paint));
				w += size[0][x];
			}
			h += size[1][y];
		}
	}
	
	private static void verticalLines(Canvas canvas, Paint paint,Dimension dimension){
		if (dimension.filter){
			int colorFilter = createFilter(paint);
			paint.setColorFilter(new LightingColorFilter(colorFilter , 0));
		}
		
		int size[] = calcVerticalLines(dimension);
		int w = 0;
		for (int x = 0,length = size.length; x < length; x++){
			canvas.drawRect(w, 0, w+size[x], dimension.height, isOdd(x+((VERTICAL_LINES_FIRST_LINE) ? 0 : 1),paint));
			w += size[x];
		}
	}
	
	private static void horizontalLines(Canvas canvas, Paint paint,Dimension dimension){
		if (dimension.filter){
			int colorFilter = createFilter(paint);
			paint.setColorFilter(new LightingColorFilter(colorFilter , 0));
		}
		
		int size[] = calcHorizontalLines(dimension);
		int h = 0;
		for (int y = 0,length = size.length; y < length; y++){
			canvas.drawRect(0, h, dimension.width, h+size[y], isOdd(y+((HORIZONTAL_LINES_FIRST_LINE) ? 0 : 1),paint));
			h += size[y];
		}
	}
	
	/**
	 * Calculates checker's squares width and height
	 * int[x]
	 * int[x][x1]
	 * int[x][x2]
	 * ...
	 * int[y]
	 * int[y][y1]
	 * int[y][y2]
	 * ...
	 * @param dimension
	 * @return
	 */
	private static int[][] calcCheckers(Dimension dimension){
		int x = 0; 			// square width 
		int y = 0; 			// square height
		int x_num = 0;		// squares num x
		int y_num = 0;		// squares num y
		
		if (dimension.height > dimension.width){
			x = (int)Math.floor((double)dimension.width / (double)CHECKERS_NUM);
			x_num = CHECKERS_NUM;
			
			y = x;
			y_num = (int)Math.floor((double)dimension.height / (double)y);
			y_num += ((CHECKERS_OTHER_DIMENSION_ODD) ? ((y_num%2==0) ? 1 : 0) : ((y_num%2==0) ? 0 : 1));
			y  = (int)Math.floor((double)dimension.height / (double)y_num);
		}
		else {
			y = (int)Math.floor((double)dimension.height / (double) CHECKERS_NUM);
			y_num = CHECKERS_NUM;
			
			x = y + ((CHECKERS_OTHER_DIMENSION_ODD) ? ((y%2==0) ? 1 : 0) : ((y%2==0) ? 0 : 1));
			x_num = (int)Math.floor((double)dimension.width / (double)x);
			x_num += ((CHECKERS_OTHER_DIMENSION_ODD) ? ((x_num%2==0) ? 1 : 0) : ((x_num%2==0) ? 0 : 1));
			x  = (int)Math.floor((double)dimension.width / (double)x_num);
		}
		
		int dx =  dimension.width - (x_num * x);
		int dy =  dimension.height - (y_num * y);
		
		assert dx >= 0;
		assert dx < x_num;
		assert dy >= 0;
		assert dy < y_num;
		
		int[][] size = new int[2][];
		
		int[] x_size = new int[x_num];
		int[] y_size = new int[y_num];
		
		int x_start = (int)Math.floor((double)(x_num-dx) / (double)2);
		int y_start = (int)Math.floor((double)(y_num-dy) / (double)2);
		
		for (int i = 0; i < x_num; i++){
			x_size[i] = x;
		}
		
		for (int i = 0; i < y_num; i++){
			y_size[i] = y;
		}
		
		for (int i = x_start; i < x_start + dx; i++){
			x_size[i] += 1; 
		}
		
		for (int i = y_start; i < y_start + dy; i++){
			y_size[i] += 1; 
		}
		
		size[0] = x_size;
		size[1] = y_size;
		
		return size;
		
	}
	/**
	 * Calculates lines width
	 * int[x1]
	 * int[x2]
	 * ...
	 * @param dimension
	 * @return
	 */
	private static int[] calcVerticalLines(Dimension dimension){
		int x = 0; 			// square width 
		int x_num = 0;		// squares num x
		
		x = (int)Math.floor((double)dimension.width / (double)VERTICAL_LINES_NUM);
		x_num = VERTICAL_LINES_NUM;
			
		int dx =  dimension.width - (x_num * x);
		
		assert dx >= 0;
		assert dx < x_num;
				
		int[] x_size = new int[x_num];
		
		int x_start = (int)Math.floor((double)(x_num-dx) / (double)2);
		
		for (int i = 0; i < x_num; i++){
			x_size[i] = x;
		}

		for (int i = x_start; i < x_start + dx; i++){
			x_size[i] += 1; 
		}
		
		
		return x_size;
		
	}
	
	/**
	 * Calculates lines height
	 * int[x1]
	 * int[x2]
	 * ...
	 * @param dimension
	 * @return
	 */
	private static int[] calcHorizontalLines(Dimension dimension){
		int y = 0; 			// square width 
		int y_num = 0;		// squares num x
		
		y = (int)Math.floor((double)dimension.height / (double)HORIZONTAL_LINES_NUM);
		y_num = HORIZONTAL_LINES_NUM;
			
		int dy =  dimension.height - (y_num * y);
		
		assert dy >= 0;
		assert dy < y_num;
				
		int[] y_size = new int[y_num];
		
		int y_start = (int)Math.floor((double)(y_num-dy) / (double)2);
		
		for (int i = 0; i < y_num; i++){
			y_size[i] = y;
		}

		for (int i = y_start; i < y_start + dy; i++){
			y_size[i] += 1; 
		}
		
		
		return y_size;
		
	}
	
	private static Paint isOdd(int num,Paint paint){
    	int a = num%2;
		if (a==0){
			return paint;
		}
		return whitePaint;
    }
	
	public static int createFilter(Paint paint){
		int colorFilter = 0x000000;
		Color color = new Color(paint.getColor());
		int red = color.r;
		int green = color.g;
		int blue = color.b;
		if (red > green){
			if (red > blue) {
				colorFilter = 0xfee1e1;   	// red - max
			}
			else {
				colorFilter = 0xcceeff;		// blue - max
			}
		}
		else {
			if (green > blue){
				colorFilter = 0xa1eea1;		// green - max
			}
			else if (blue > green){
				colorFilter = 0xcceeff;		// blue - max
			}
			else if (red == 255 && green == 255 && blue == 255){
				colorFilter = 0xfff8a8;		// all are 255
			}
		}
		return colorFilter;
	}
}
