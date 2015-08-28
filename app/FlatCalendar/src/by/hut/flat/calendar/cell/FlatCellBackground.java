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

public class FlatCellBackground {
	protected Paint bgPaint = new Paint();
	protected static final Paint borderPaint = new Paint();
	protected static final Paint redBorderPaint = new Paint();
	protected static final Paint debtPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
	protected static final Paint prepayPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
	
	private static final int borderSize = 2;
	private static final int redBorderSize = 4;
	private static final int redBorderPadding = -1;
	private static float debtTextSizePercent = 0.2f;
	private static float prepayTextSizePercent = 0.2f;
	private static float debtMarginTopPercent = 0.0f;
	private static float prepayMarginTopPercent = 0.0f;
	private static float debtMarginPercent = 0.04f;
	private static float prepayMarginPercent = 0.04f;
	
	private static final int borderSizePartLittle = calcPartLittle(borderSize);;
	private static final int borderSizePartBig = calcPartBig(borderSize);
	private float debtTextSize;
	private float prepayTextSize;
	private float debtMarginTop;
	private float prepayMarginTop;
	private float debtMargin;
	private float prepayMargin;
	
	private static final int borderColor = Color.LTGRAY;
	private static final int redBorderColor = 0xffff0000;
	private static final int debtColor = Color.BLACK;
	private static final int prepayColor = Color.BLACK;

	static {
		borderPaint.setColor(borderColor);
		redBorderPaint.setColor(redBorderColor);
		debtPaint.setColor(debtColor);
		prepayPaint.setColor(prepayColor);
	}
	
	private Day day;
	private Date date;
	private Date today;
	protected final Dimension dimension;
	protected final boolean isToday;
	protected int dayOfWeek = 0;
	protected Drawable background;
	protected Canvas canvas;
	
	private boolean invariant(){
		return day != null
				&& dimension != null;
	}
	
	public FlatCellBackground(Dimension dimension,Day day){
		this.day = day;
		this.date = this.day.date;
		this.today = Config.INST.SYSTEM.TODAY;
		this.dimension = dimension;
		this.isToday = date.isEqual(today);
		this.bgPaint.setColor(day.bgColor);
		
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

	public FlatCellBackground(Dimension dimension, int dayOfWeek) {
		this.dimension = dimension;
		this.isToday = false;
		this.dayOfWeek = dayOfWeek;
		initBackground();
	}

	private void initBackground(){
		Bitmap bitmap = Bitmap.createBitmap(dimension.width, dimension.height, Bitmap.Config.RGB_565);
		canvas = new Canvas(bitmap);
		
		drawBackground();
		drawBorder();
		
		background = new FastBitmapDrawable(bitmap);
	}
	/* Draw background */
	protected void drawBackground(){
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
	private void drawBorder(){
		drawBorderTop();		// should be drawn before left and right
		drawBorderBottom();	// should be drawn before left and right
		drawBorderRight();	// should be drawn after top and bottom
		drawBorderLeft();		// should be drawn after top and bottom
	}
	
	private void drawBorderTop(){
		canvas.drawRect(0, 0, dimension.width, borderSizePartLittle, borderPaint);
		if (isToday){
			int left = borderSizePartLittle+redBorderPadding+1;
			int top = borderSizePartLittle+redBorderPadding;
			int right = dimension.width-borderSizePartBig-redBorderPadding-1;
			int bottom = borderSizePartLittle+redBorderPadding+redBorderSize;
			canvas.drawRect(left, top, right,bottom , redBorderPaint);
		}
	}
	
	private void drawBorderBottom(){
		canvas.drawRect(0, dimension.height-borderSizePartBig, dimension.width, dimension.height, borderPaint);
		if (isToday){
			int left = borderSizePartLittle+redBorderPadding+1;
			int top = dimension.height-borderSizePartBig-redBorderPadding-redBorderSize;
			int right = dimension.width-borderSizePartBig-redBorderPadding-1;
			int bottom = dimension.height-borderSizePartBig-redBorderPadding;
			canvas.drawRect(left, top, right,bottom , redBorderPaint);
		}
	}
	
	private void drawBorderRight(){
		canvas.drawRect(dimension.width-borderSizePartBig, 0, dimension.width, dimension.height, borderPaint);
		if (isToday){
			int left = dimension.width-borderSizePartBig-redBorderPadding-redBorderSize;
			int top = borderSizePartLittle + redBorderPadding + 1;
			int right = dimension.width-borderSizePartBig-redBorderPadding;
			int bottom = dimension.height - borderSizePartBig - redBorderPadding - 1;
			canvas.drawRect(left, top, right,bottom , redBorderPaint);
		}
	}

	private void drawBorderLeft(){
		canvas.drawRect(0, 0, borderSizePartLittle, dimension.height, borderPaint);
		if (isToday){
			int left = borderSizePartLittle+redBorderPadding;
			int top = borderSizePartLittle + redBorderPadding + 1;
			int right = borderSizePartLittle+redBorderPadding + redBorderSize;
			int bottom = dimension.height - borderSizePartBig - redBorderPadding - 1;
			canvas.drawRect(left, top, right,bottom , redBorderPaint);
		}
	}
	private static int calcPartLittle(int border){
		return (int) Math.floor((double)border/(double)2);
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
