package by.hut.flat.calendar.sausage;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.cell.SausageCell;
import by.hut.flat.calendar.core.DBParser;
import by.hut.flat.calendar.utils.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


public class Sausage extends LinearLayout implements ISausage{
	
	protected Context context;
	protected SausageCell[] cells;
	private DBParser dbParser;
	private Date startDate;
	private Date finishDate;
	private int cellsNum;
	private int FlatID;
	
	/* tmp Views */
	private SausageCell cell;
	
	protected boolean invariant(){
		return this.context != null
				&& cells != null
				&& cells.length > 0
				&& cells.length == this.cellsNum
				&& startDate != null
				&& finishDate != null;
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Sausage(DBParser parser, Context context, int FlatID,Date startDate,Date finishDate){
		super(context);
		assert startDate != null;
		assert finishDate != null;
		assert startDate.compareTo(finishDate) == -1;
		assert startDate.daysUntil(finishDate) > 0;
		
		this.dbParser = parser;
		this.context = context;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.cellsNum = startDate.daysUntil(finishDate)+1;
		this.FlatID = FlatID;
		
		initView();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sausage_calendar_line, this);
        inflater = null;
	}
	
	private void initCell(SausageCell cell, int pos){
		assert cell != null;
		cells[pos] = cell;
	}
	
	
	/*------------------------------------------------------------
	----------------------- I N T E R F A C E --------------------
	------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see by.hut.flat.calendar.sausage.ISausage#initCells()
	 */
	@Override
	public void initCells() {
		cells = new SausageCell[cellsNum];
		/* creating and drawing cells */
		for (int i = 0; i < cellsNum; i++){
			cell = new SausageCell(context,dbParser.getDay(FlatID,i));
			initCell(cell,i);
			cell = null;
		}
		assert invariant();
	}
	
	/* (non-Javadoc)
	 * @see by.hut.flat.calendar.sausage.ISausage#addCells()
	 */
	@Override
	public void addCells(){
		for (int i = 0; i < cellsNum; i++){
			addView(cells[i]);
		}
	}
	/*------------------------------------------------------------
	-------------- P R O T E C T E D   G E T T E R S -------------
	------------------------------------------------------------*/
	protected SausageCell getCell(int index){
		assert index >= 0;
		assert index < cells.length;
		return cells[index];
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	protected void reDraw(Date firstDate, Date lastDate) {
		int[] indexes = dbParser.reParseDB(this.FlatID,firstDate, lastDate);
		if(indexes == null) return;
		for (int i = indexes[0]; i <= indexes[1]; i++){
			this.getCell(i).reDraw(dbParser.getDay(this.FlatID,i));
		}
	}
}
