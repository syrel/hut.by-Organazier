package by.hut.flat.calendar.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class SausageCellMonth extends TextView{
	private final int monthMargin = 5;
	private static final int textColor = Color.BLACK;
	
	private Date today;
	private Date date;
	private int month;
	private boolean isFirst;
	private boolean isLast;
	private Dimension dimension;
	
	private boolean invariant(){
		return date != null
			&& today != null
			&& dimension != null;
	}
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public SausageCellMonth(Context context, Date date,Dimension dimension,int month,boolean isFirst, boolean isLast) {
		super(context);
		this.today = Config.INST.SYSTEM.TODAY;
		this.date = date;
		this.month = month;
		this.dimension = dimension;
		this.isFirst = isFirst;
		this.isLast = isLast;
		
		assert invariant();
		initView();
	}
	
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		initSize();
		initText();
		initBackground();
	}
	private void initSize(){
		this.setWidth(this.dimension.width);
		this.setHeight(this.dimension.height);

		this.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
		if (date.isFirstDayInMonth() && !isFirst) {
			layoutParams.setMargins(monthMargin, 0, 0, 0); //(left, top, right, bottom);
		}
		this.setLayoutParams(layoutParams);
		
		layoutParams = null;
	}
	private void initText(){
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX,Config.INST.SAUSAGE.CELL_TEXT_SIZE);
		this.setTextScaleX((float) 2.2);
		this.setTextColor(textColor);
		this.setText(Config.INST.STRINGS.MONTHS_OF_YEAR[month-1]);
	}
	@SuppressWarnings("deprecation")
	private void initBackground(){
		SausageCellMonthBackground cellBackground = new SausageCellMonthBackground(dimension,date,today,0,this.isFirst,this.isLast,false);
		this.setBackgroundDrawable(cellBackground.background);
		cellBackground = null;
	}

}
