package by.hut.flat.calendar.cell;

import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class SausageCellDayBackground extends SausageCellBackground {

	private static final int todayBackgroundColor = 0xffff6969;
	
	public SausageCellDayBackground(Dimension dimension, Date date, Date today,int dayOfWeek,boolean isFirst, boolean isLast,boolean isToday) {
		super(dimension, date, today, dayOfWeek, isFirst, isLast,isToday);
	}
	
	@Override
	protected void drawBorder(){
		drawBorderTop(-1);			// should be drawn before left and right
		drawBorderBottom(-1);		// should be drawn before left and right
		drawBorderRight(-1);		// should be drawn after top and bottom
		drawBorderLeft(-1);			// should be drawn after top and bottom
	}
	
	@Override
	protected void drawBackground(){
		if (this.isToday) bgPaint.setColor(todayBackgroundColor);
		else if (dayOfWeek == 5 || dayOfWeek == 6) bgPaint.setColor(holidaysBgColor);
		this.canvas.drawRect(0, 0, this.dimension.width, this.dimension.height, bgPaint);
	}

}
