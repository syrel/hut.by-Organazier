package by.hut.flat.calendar.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.Day;
import by.hut.flat.calendar.internal.FastBitmapDrawable;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class SausageCellBackground {
	protected Paint bgPaint = new Paint();
	protected final static Paint borderPaint = new Paint();
	protected final static Paint redBorderPaint = new Paint();
	protected final static Paint monthBorderPaint = new Paint();
	protected final static Paint debtPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG);
	protected final static Paint prepayPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG);
	
	private static final int borderSize = 2;
	private static final int redBorderSize = 3;
	private static final int monthBorderSize = 4;	
	private static final int differentColorsPadding = 0;
	private static boolean differentColorsTodayBorder = false;
	private static float debtTextSizePercent = 0.3f;
	private static float prepayTextSizePercent = 0.3f;
	private static float debtMarginTopPercent = 0.0f;
	private static float prepayMarginTopPercent = 0.0f;
	private static float debtMarginPercent = 0.07f;
	private static float prepayMarginPercent = 0.07f;
	
	private static final int borderSizePartLittle = calcPartLittle(borderSize);
	private static final int borderSizePartBig = calcPartBig(borderSize);
	private static final int redBorderSizePartLitte = calcPartLittle(redBorderSize);
	private static final int redBorderSizePartBig = calcPartBig(redBorderSize);
	private float debtTextSize;
	private float prepayTextSize;
	private float debtMarginTop;
	private float prepayMarginTop;
	private float debtMargin;
	private float prepayMargin;
	
	private static final int borderColor = Color.LTGRAY;
	private static final int redBorderColor = 0xffff0000;
	private static final int monthBorderColor = 0x55ff0000;
	protected static final int holidaysBgColor = 0xFFFFF5F5;
	private static final int debtColor = 0xff2bc0ff;
	private static final int prepayColor = 0xff00d100;
	
	static {
		borderPaint.setColor(borderColor);
		redBorderPaint.setColor(redBorderColor);
		monthBorderPaint.setColor(monthBorderColor);
		debtPaint.setColor(debtColor);
		debtPaint.setFakeBoldText(true);
		prepayPaint.setColor(prepayColor);
		prepayPaint.setFakeBoldText(true);
	}
	private Day day;
	private final Date date;
	private final Date today;
	protected Dimension dimension;
	
	protected FastBitmapDrawable background;
	protected Canvas canvas;
	private Bitmap bitmap;
	
	protected final boolean isToday;
	private final boolean isFirst;
	private final boolean isLast;
	protected final int dayOfWeek;
	
	private boolean invariant(){
		return dimension != null;
	}
	
	public SausageCellBackground(Dimension dimension,Date date, Date today, int dayOfWeek, boolean isFirst, boolean isLast,boolean isToday){
		this.date = date;
		this.today = today;
		this.isToday = isToday;
		this.isFirst = isFirst;
		this.isLast = isLast;
		this.dayOfWeek = dayOfWeek;
		
		this.dimension = dimension;
		
		bgPaint.setColor(Color.WHITE);		
		assert invariant();
		
		initBackground();
	}
	
	public SausageCellBackground(Dimension dimension,Day day){
		this.day = day;
		this.date = day.date;
		this.today = Config.INST.SYSTEM.TODAY;
		this.isToday = date.isEqual(today);
		this.isFirst = day.id == 0;
		this.isLast = day.isLast;
		this.dayOfWeek = day.dayOfWeek;
		
		this.dimension = dimension;
		
		bgPaint.setColor(day.bgColor);
		
		this.debtTextSize = calcDebtTextSize();
		this.debtMarginTop = this.calcDebtMarginTop();
		this.debtMargin = this.calcDebtMargin();
		this.prepayTextSize = calcPrepayTextSize();
		this.prepayMargin = this.calcPrepayMargin();
		this.prepayMarginTop = this.calcPrepayMarginTop();
		
		debtPaint.setTextSize(debtTextSize);
		prepayPaint.setTextSize(prepayTextSize);
		assert invariant();
		
		initBackground();
	}
	
	private void initBackground(){
		bitmap = Bitmap.createBitmap(dimension.width, dimension.height, Bitmap.Config.RGB_565);
		canvas = new Canvas(bitmap);
		
		drawBackground();
		drawBorder();
		
		background = new FastBitmapDrawable(bitmap);
		
		canvas = null;
		bitmap = null;
	}
	/* Draw background */
	protected void drawBackground(){
		if ((dayOfWeek == 5 || dayOfWeek == 6) && day.background == 0 && day.state == 0) bgPaint.setColor(holidaysBgColor);
		MaidenBackground.draw(canvas,bgPaint,dimension,day.background);
		
		drawDebt();
		drawPrepay();
	}
	
	private void drawDebt(){
		if (day.debt > 0){
			canvas.drawText(""+day.debt, debtMargin, debtMarginTop+this.debtTextSize, debtPaint);
		}
	}
	
	private void drawPrepay(){
		if (day.prepay > 0){
			canvas.drawText(""+day.prepay, this.dimension.width-prepayMargin-prepayPaint.measureText(""+day.prepay), prepayMarginTop+this.prepayTextSize, prepayPaint);
		}
	}
	/*------------------------------------------------------------
	--------------------------- B O R D E R ----------------------
	------------------------------------------------------------*/
	protected void drawBorder(){
		drawBorderTop(0);		// should be drawn before left and right
		drawBorderBottom(0);	// should be drawn before left and right
		drawBorderRight(0);		// should be drawn after top and bottom
		drawBorderLeft(0);		// should be drawn after top and bottom
	}
	protected void drawBorderTop(int mode){
		if (mode == 0 || mode == 1){
			if (date.isLastDayInMonth() && isToday && redBorderColor != monthBorderColor && differentColorsTodayBorder){
				if (day.FlatID != 0) canvas.drawRect(0, 0, dimension.width-monthBorderSize-differentColorsPadding, borderSizePartLittle, borderPaint);
			}
			else if (date.isFirstDayInMonth() && isToday && redBorderColor != monthBorderColor && differentColorsTodayBorder){
				if (day.FlatID != 0) canvas.drawRect(monthBorderSize+differentColorsPadding, 0, dimension.width, borderSizePartLittle, borderPaint);
			}
			else {
				if (mode == 1 || !(day.FlatID == 0 && isToday)){
					canvas.drawRect(0, 0, dimension.width, borderSizePartLittle, borderPaint);
				}
			}
		}
	}
	
	protected void drawBorderBottom(int mode){
		if (!(mode == -1 && isToday)){
			if (date.isLastDayInMonth() && isToday && redBorderColor != monthBorderColor && differentColorsTodayBorder){
				canvas.drawRect(0, dimension.height-borderSizePartBig, dimension.width-monthBorderSize-differentColorsPadding, dimension.height, borderPaint);
			}
			else if (date.isFirstDayInMonth() && isToday && redBorderColor != monthBorderColor && differentColorsTodayBorder){
				canvas.drawRect(monthBorderSize+differentColorsPadding, dimension.height-borderSizePartBig, dimension.width, dimension.height, borderPaint);
			}
			else {
				canvas.drawRect(0, dimension.height-borderSizePartBig, dimension.width, dimension.height, borderPaint);
			}
		}
	}
	
	protected void drawBorderRight(int mode){
		if (date.isLastDayInMonth() && isToday){
			if (redBorderColor == monthBorderColor || !differentColorsTodayBorder)	{
				canvas.drawRect(dimension.width-Math.max(redBorderSize, monthBorderSize), 0, dimension.width, dimension.height, monthBorderPaint);
			}
			else {
				canvas.drawRect(dimension.width-redBorderSize-monthBorderSize-differentColorsPadding, 0, dimension.width-monthBorderSize-differentColorsPadding, dimension.height, redBorderPaint);
				canvas.drawRect(dimension.width-monthBorderSize, 0, dimension.width, dimension.height, monthBorderPaint);
			}
		}
		else if (date.isLastDayInMonth() && !isLast){
			canvas.drawRect(dimension.width-monthBorderSize, 0, dimension.width, dimension.height, monthBorderPaint);
		}
		else if (mode != 1 && date.isPrev(today)){
			canvas.drawRect(dimension.width-redBorderSizePartLitte, 0, dimension.width, dimension.height, redBorderPaint);
		}
		else if (mode != 1 && isToday){
			canvas.drawRect(dimension.width-redBorderSizePartBig, 0, dimension.width, dimension.height, redBorderPaint);
		}
		else {
			if (mode != 1) canvas.drawRect(dimension.width-borderSizePartBig, 0, dimension.width, dimension.height, borderPaint);
		}
	}

	protected void drawBorderLeft(int mode){
		if (date.isFirstDayInMonth() && isToday){
			if (redBorderColor == monthBorderColor || !differentColorsTodayBorder)	{
				canvas.drawRect(0, 0, Math.max(redBorderSize, monthBorderSize), dimension.height, monthBorderPaint);
			}
			else {
				canvas.drawRect(0, 0, monthBorderSize, dimension.height, monthBorderPaint);
				canvas.drawRect(monthBorderSize+differentColorsPadding, 0, monthBorderSize+redBorderSize+differentColorsPadding, dimension.height, redBorderPaint);
			}
		}
		else if (date.isFirstDayInMonth() && !isFirst){
			canvas.drawRect(0, 0, monthBorderSize, dimension.height, monthBorderPaint);
		}
		else if (mode != 1 && date.isNext(today)){
			canvas.drawRect(0, 0, redBorderSizePartLitte, dimension.height, redBorderPaint);
		}
		else if (mode != 1 && isToday){
			canvas.drawRect(0, 0, redBorderSizePartBig, dimension.height, redBorderPaint);
		}
		else {
			if (mode != 1) canvas.drawRect(0, 0, borderSizePartLittle, dimension.height, borderPaint);
		}
	}
	private static int calcPartLittle(int border){
		return border/2;
	}
	private static int calcPartBig(int border){
		return (int) Math.ceil((double)border/(double)2);
	}
	
	private float calcDebtTextSize(){
		return this.dimension.width * debtTextSizePercent;
	}
	
	private float calcPrepayTextSize(){
		return this.dimension.width * prepayTextSizePercent;
	}
	
	private float calcDebtMarginTop(){
		return this.dimension.height * debtMarginTopPercent;
	}
	
	private float calcPrepayMarginTop(){
		return this.dimension.height * prepayMarginTopPercent;
	}
	
	private float calcDebtMargin(){
		return this.dimension.width * debtMarginPercent;
	}
	
	private float calcPrepayMargin(){
		return this.dimension.width * prepayMarginPercent;
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public Drawable getBackground(){
		return this.background;
	}
}
