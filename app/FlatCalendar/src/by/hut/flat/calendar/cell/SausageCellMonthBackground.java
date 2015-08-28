 package by.hut.flat.calendar.cell;

import android.graphics.Color;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class SausageCellMonthBackground extends SausageCellBackground{
	
	public SausageCellMonthBackground(Dimension dimension, Date date, Date today,int dayOfWeek,boolean isFirst, boolean isLast,boolean isToday) {
		super(dimension, date, today, dayOfWeek, isFirst, isLast,isToday);
	}
	
	@Override
	protected void drawBorder(){
		drawBorderTop(1);		// should be drawn before left and right
		drawBorderBottom(1);	// should be drawn before left and right
		drawBorderRight(1);		// should be drawn after top and bottom
		drawBorderLeft(1);		// should be drawn after top and bottom
	}
	@Override
	protected void drawBackground(){
		this.bgPaint.setColor(Color.WHITE);
		this.canvas.drawRect(0, 0, this.dimension.width, this.dimension.height, bgPaint);
	}
}
