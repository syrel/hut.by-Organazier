package by.hut.flat.calendar.cell;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.Day;
import by.hut.flat.calendar.internal.CacheLayoutProvider;
import by.hut.flat.calendar.internal.FastTextView;
import by.hut.flat.calendar.utils.Dimension;

public class SausageCell extends FastTextView {
	private final int monthMargin = 5;
	
	public Day day;
	private Dimension dimension;
	
	private boolean invariant(){
		return day != null
				&& dimension != null;
	}
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public SausageCell(Context context, Day day) {
		super(context);
		this.day = day;
		this.dimension = new Dimension(Config.INST.SAUSAGE.CELL_WIDTH,Config.INST.SAUSAGE.CELL_HEIGHT);
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
		if (day.id != 0 && day.date.isFirstDayInMonth()) {
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
			layoutParams.leftMargin = monthMargin;
			this.setLayoutParams(layoutParams);
			layoutParams = null;
		}
	}
	
	private void initText(){
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setTextSize(Config.INST.SAUSAGE.CELL_TEXT_SIZE);
		this.setTextColor(day.textColor);
		this.setText(""+day.date.day);
	}
	
	@SuppressWarnings("deprecation")
	private void initBackground(){
		SausageCellBackground cellBackground = new SausageCellBackground(dimension,day);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			this.setBackground(cellBackground.background);
		}
		else {
			this.setBackgroundDrawable(cellBackground.background);
		}
		cellBackground = null;
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	public void reDraw(Day day){
		assert day != null;
		this.day = day;
		initView();
	}
}
