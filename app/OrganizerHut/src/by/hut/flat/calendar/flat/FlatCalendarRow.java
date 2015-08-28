package by.hut.flat.calendar.flat;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.cell.FlatCell;
import by.hut.flat.calendar.core.DBParser;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FlatCalendarRow extends LinearLayout{
	private FlatCalendar flatCalendar;
	protected final int cellsNum = 7;
	private DBParser dbParser;
	private Context context;
	private FlatCell[] cells;
	private int rowID;
	private int FlatID;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public FlatCalendarRow(FlatCalendar flatCalendar,Context context,DBParser dbParser,int FlatID,int rowID) {
		super(context);
		this.flatCalendar = flatCalendar;
		this.context = context;
		this.dbParser = dbParser;
		this.rowID = rowID;
		this.FlatID = FlatID;
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
		cells = new FlatCell[cellsNum];
		
		for (int i = 0; i < cellsNum; i++){
			FlatCell cell = new FlatCell(flatCalendar,context,dbParser.getDay(FlatID,cellsNum*rowID+i));
			initCell(cell,i);
		}
	}
	private void initCell(FlatCell cell, int pos){
		assert cell != null;
		cells[pos] = cell;
	}
	private void addCells(){
		for (int i = 0; i < cellsNum; i++){
			this.addView(cells[i]);
		}
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	protected FlatCell getCell(int index){
		assert index >=0 && index < cells.length;
		return cells[index];
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	
}
