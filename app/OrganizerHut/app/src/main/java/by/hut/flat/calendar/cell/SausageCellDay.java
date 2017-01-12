package by.hut.flat.calendar.cell;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.internal.CacheLayoutProvider;
import by.hut.flat.calendar.internal.FastTextView;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

public class SausageCellDay extends FastTextView {
	private static final int monthMargin = 5;
	private static final int textColor = Color.BLACK;
	private static final int todayTextColor = Color.WHITE;
	
	public final Date date;
	private Date today;
	private boolean isFirst;
	private boolean isLast;
	private boolean isToday;
	private Dimension dimension;
	private int dayOfWeek;
	
	private boolean invariant(){
		return date != null
			&& today != null
			&& dimension != null;
	}
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public SausageCellDay(Context context, Date date, boolean isFirst, boolean isLast, int dayOfWeek) {
		super(context);
		this.date = date;
		this.today = Config.INST.SYSTEM.TODAY;
		this.dimension = new Dimension(Config.INST.SAUSAGE.CELL_WIDTH,Config.INST.SAUSAGE.CELL_HEIGHT);
		this.isFirst = isFirst;
		this.isLast = isLast;
		this.isToday = date.isEqual(today);

		this.dayOfWeek = dayOfWeek;
		
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
		this.setLayoutProvider(new CacheLayoutProvider());
		width = this.dimension.width;
		height = this.dimension.height;
		if (date.isFirstDayInMonth() && !isFirst) {
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
			layoutParams.leftMargin = monthMargin;
			this.setLayoutParams(layoutParams);
		}
	}

	private void initText(){
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setTextSize(Config.INST.SAUSAGE.CELL_TEXT_SIZE);
		this.setTextColor(isToday ? todayTextColor: textColor);
		this.setText(Config.INST.STRINGS.DAYS_OF_WEEK_SHORT[dayOfWeek]);
	}

	private void initBackground(){
		SausageCellDayBackground cellBackground = new SausageCellDayBackground(dimension,date,today,dayOfWeek,this.isFirst,this.isLast,this.isToday);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			this.setBackground(cellBackground.background);
		}
		else {
			this.setBackgroundDrawable(cellBackground.background);
		}
	}
}
