package by.hut.flat.calendar.flat;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.cell.FlatCell;
import by.hut.flat.calendar.core.DBParser;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FlatCalendar extends LinearLayout{
	
	public final static int rowsNum = 6;
	private Context context;
	
	private int FlatID;
	private DBParser dbParser;
	private FlatCalendarRowDay calendarDay;
	private FlatCalendarRow[] calendarRows;
	protected FlatCell firstCell;
	protected FlatCell lastCell;
	private Date initDate;
	
	private boolean invariant(){
		return context != null
			&& FlatID >= 0
			&& calendarRows != null
			&& calendarRows.length == rowsNum
			&& dbParser != null;
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public FlatCalendar(Context context) {
		super(context);
	}
	
	public FlatCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	
	public void initialize(int FlatID,DBParser dbParser){
		this.FlatID = FlatID;
		this.dbParser = dbParser;
        initDate = new Date().setDay(1);
		initRows();
        addRows();
        assert invariant();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flat_calendar, this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
		this.setLayoutParams(layoutParams);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
	}
	
	private void initRows(){
		calendarDay = new FlatCalendarRowDay(context);
        calendarRows = new FlatCalendarRow[rowsNum];
               
        for (int i = 0; i < rowsNum; i++){
        	FlatCalendarRow calendarRow = new FlatCalendarRow(this,context,dbParser,FlatID,i);
        	initRow(calendarRow,i);
        }
	}
	
	private void initRow(FlatCalendarRow calendarRow, int pos){
		assert calendarRow != null;
		calendarRows[pos] = calendarRow;
	}
	
	private void addRows(){
		assert calendarRows != null;
		addView(calendarDay);
		for (int i = 0; i < rowsNum; i++){
			addView(calendarRows[i]);
		}
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	public void selectCell(FlatCell cell){
		assert cell != null;
		if (getSelectedCellNumber() == 0){
			firstCell = cell;
			markSelected();
		}
		else if (getSelectedCellNumber() == 1){
			assert lastCell == null;
			int compare = firstCell.getDate().compareTo(cell.getDate());
			if (compare == 1) { // selected date is smaller then already selected. inverting
				lastCell = firstCell;
				firstCell = cell;
				markSelected();
			}
			else if (compare == -1){ // selected date is bigger then already selected
				lastCell = cell;
				markSelected();
			}
			else if (compare == 0){ // select already selected date. unselect
				unmarkSelected(); // must be before unselecting
				firstCell = null;
			}
			
		}
		else if (getSelectedCellNumber() == 2){ // recelect date as first
			unmarkSelected();
			firstCell = cell;
			lastCell = null;
			markSelected();
		}
	}
	protected void reDraw(Date firstDate, Date lastDate) {
		int[] indexes = dbParser.reParseDB(this.FlatID,firstDate, lastDate);
		if (indexes == null) return;
		reDrawParsed(indexes[0], indexes[1]);
	}
	
	protected void reDrawParsed(int firstDateID, int lastDateID) {
		for (int i = firstDateID; i <= lastDateID; i++){
			this.getCell(i).reDraw(dbParser.getDay(FlatID,i));
		}
		resetSelection();
	}
	/*------------------------------------------------------------
	-------------- P R O T E C T E D   G E T T E R S -------------
	------------------------------------------------------------*/
	protected int getSelectedCellNumber(){
		return ((firstCell != null) ? 1 : 0) + ((lastCell != null) ? 1 : 0);
	}
	protected Date getFirstSelectedCellDate(){
		return this.firstCell.getDate();
	}
	protected Date getLastSelectedCellDate(){
		return this.lastCell.getDate();
	}
	protected void resetSelection(){
		firstCell = null;
		lastCell = null;
	}
	
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public int getMonth(){
		return this.initDate.month;
	}
	public int getYear(){
		return this.initDate.year;
	}
	/*------------------------------------------------------------
	-------------------------- S E T T E R S ---------------------
	------------------------------------------------------------*/
	public void nextMonth(){
		initDate.jumpMonth(1);
		Date startDate = new CalendarHelper(initDate).getFirstCalendarDate();
		Date finishDate = startDate.findDate(FlatCalendar.rowsNum*7-1);
		dbParser.getDB().open();
		dbParser.reInitDB(FlatID, startDate, finishDate);
		dbParser.getDB().close();
		reDraw(startDate, finishDate);
	}
	
	public void prevMonth(){
		initDate.jumpMonth(-1);
		Date startDate = new CalendarHelper(initDate).getFirstCalendarDate();
		Date finishDate = startDate.findDate(FlatCalendar.rowsNum*7-1);
		dbParser.getDB().open();
		dbParser.reInitDB(FlatID, startDate, finishDate);
		dbParser.getDB().close();
		reDraw(startDate, finishDate);
	}
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	private void markSelected(){
		if (getSelectedCellNumber() != 0){
			if (getSelectedCellNumber() == 1){
				firstCell.select();
			}
			else {
				int firstID = firstCell.getCellID();
				int lastID = lastCell.getCellID();
				for (int i = firstID; i <= lastID; i++){
					this.getCell(i).select();
				}
			}
		}
	}
	protected void unmarkSelected(){
		if (getSelectedCellNumber() != 0){
			if (getSelectedCellNumber() == 1){
				firstCell.unselect();
			}
			else {
				int firstID = firstCell.getCellID();
				int lastID = lastCell.getCellID();
				for (int i = firstID; i <= lastID; i++){
					this.getCell(i).unselect();
				}
			}
		}
	}
	private FlatCell getCell(int index){
		int cellsNum = calendarRows[0].cellsNum;
		assert index >= 0 && index < cellsNum*rowsNum;
		int rowId = (int) Math.floor((double)index/(double)cellsNum);
		assert rowId >= 0 && rowId < calendarRows.length;
		int cellId = index - rowId*cellsNum;
		return calendarRows[rowId].getCell(cellId);
	}
}
