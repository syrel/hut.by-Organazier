package by.hut.flat.calendar.cell;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.Day;
import by.hut.flat.calendar.flat.FlatCalendar;
import by.hut.flat.calendar.internal.CacheLayoutProvider;
import by.hut.flat.calendar.internal.FastTextView;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class FlatCellDay extends FastTextView{
		
	private Day day;
	private Dimension dimension;
	private FlatCalendar flatCalendar;
	private int dayOfWeek;
	
	private boolean invariant(){
		return day != null
				&& dimension != null;
	}
	
	public FlatCellDay(Context context,int dayOfWeek) {
		super(context);
		this.dayOfWeek = dayOfWeek;
		this.dimension = new Dimension(Config.INST.CALENDAR.CELL_WIDTH,Config.INST.CALENDAR.CELL_HEIGHT);
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
	}
	
	private void initText(){
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setTextSize(Config.INST.CALENDAR.CELL_TEXT_SIZE);
		this.setText(Config.INST.STRINGS.DAYS_OF_WEEK_SHORT[dayOfWeek]);
	}
	
	@SuppressWarnings("deprecation")
	private void initBackground(){
		FlatCellBackground cellBackground = new FlatCellDayBackground(dimension,dayOfWeek);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			this.setBackground(cellBackground.background);
		}
		else {
			this.setBackgroundDrawable(cellBackground.background);
		}
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public Date getDate(){
		return day.date;
	}
	public int getCellID(){
		return day.id;
	}
	public int getCellState(){
		return day.state;
	}
	public int getCellBackground(){
		return day.background;
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	public boolean equals(Object obj){
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Date))
		      return false;
		FlatCellDay flatCell = (FlatCellDay) obj;
		return (flatCell.day == this.day && flatCell.flatCalendar == this.flatCalendar);
	}
	public int hashCode() {
		final int prime = 31;
		int hash = 1;
		hash = hash * prime + day.hashCode();
		hash = hash * prime + dimension.hashCode();
		hash = hash * prime + flatCalendar.hashCode();
		return hash;
	}
	
}
