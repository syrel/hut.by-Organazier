package by.hut.flat.calendar.cell;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import by.hut.flat.calendar.anketa.AnketaActivity;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.Day;
import by.hut.flat.calendar.flat.FlatCalendar;
import by.hut.flat.calendar.internal.CacheLayoutProvider;
import by.hut.flat.calendar.internal.FastTextView;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;

public class FlatCell extends FastTextView implements OnClickListener, OnLongClickListener{
	
	private final int selectedColor = Color.MAGENTA;
	
	private Day day;
	private Dimension dimension;
	private Context context;
	private FlatCalendar flatCalendar;
	
	
	private boolean invariant(){
		return day != null
				&& dimension != null;
	}
	
	public FlatCell(FlatCalendar flatCalendar,Context context, Day day) {
		super(context);
		this.context = context;
		this.flatCalendar = flatCalendar;
		this.day = day;
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
		this.setOnClickListener(this);
		this.setOnLongClickListener(this);
	}
	private void initSize(){
		this.setLayoutProvider(new CacheLayoutProvider());
		width = this.dimension.width;
		height = this.dimension.height;
	}
	
	private void initText(){
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setTextSize(Config.INST.CALENDAR.CELL_TEXT_SIZE);
		this.setTextColor(day.textColor);
		this.setText(""+day.date.getDay());
	}
	
	@SuppressWarnings("deprecation")
	private void initBackground(){
		FlatCellBackground cellBackground = new FlatCellBackground(dimension,day);
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
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onClick(View arg0) {
		this.flatCalendar.selectCell(this);		
	}
	
	@Override
	public boolean onLongClick(View v) {
		if (day.state != 0) {
			AnketaActivity.show(context, day.FlatID, day.date.toString());
			return true;
		}
		return false;
	}
	
	public void select(){
		this.setTextColor(selectedColor);
		invalidate();
	}
	public void unselect(){
		this.setTextColor(day.textColor);
		invalidate();
	}
	public void reDraw(Day day){
		assert day != null;
		this.day = day;
		initView();
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
		FlatCell flatCell = (FlatCell) obj;
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
