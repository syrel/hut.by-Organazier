package by.hut.flat.calendar.flat;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.cell.FlatCellDay;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FlatCalendarRowDay extends LinearLayout {
	protected final int cellsNum = 7;
	private Context context;
	private FlatCellDay[] cells;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public FlatCalendarRowDay(Context context) {
		super(context);
		this.context = context;
		initView();
		initCells();
		addCells();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_calendar_line, this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
		this.setLayoutParams(layoutParams);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}
	
	private void initCells(){
		cells = new FlatCellDay[cellsNum];
		int dayOfWeek = 0;
		for (int i = 0; i < cellsNum; i++){
			if (dayOfWeek > 6) dayOfWeek = 0;
			FlatCellDay cell = new FlatCellDay(context,dayOfWeek);
			initCell(cell,i);
			dayOfWeek++;
		}
	}
	
	private void initCell(FlatCellDay cell, int pos){
		assert cell != null;
		cells[pos] = cell;
	}
	
	private void addCells(){
		for (int i = 0; i < cellsNum; i++){
			this.addView(cells[i]);
		}
	}
}
