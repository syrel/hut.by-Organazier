package by.hut.flat.calendar.cell;

import android.graphics.Color;
import by.hut.flat.calendar.utils.Dimension;

public class FlatCellDayBackground extends FlatCellBackground {
	
	private static final int holidaysBgColor = 0xFFFFF5F5;
	
	public FlatCellDayBackground(Dimension dimension, int dayOfWeek) {
		super(dimension, dayOfWeek);
	}
	
	@Override
	protected void drawBackground(){
		if (dayOfWeek == 5 || dayOfWeek == 6) bgPaint.setColor(holidaysBgColor);
		else bgPaint.setColor(Color.WHITE);
		this.canvas.drawRect(0, 0, this.dimension.width, this.dimension.height, bgPaint);
	}

}
