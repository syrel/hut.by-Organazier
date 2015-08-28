package by.hut.flat.calendar.sausage;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.cell.SausageCellMonth;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Dimension;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class SausageMonth extends LinearLayout implements ISausage{
	private static final int  WORDS_NUM = 6;
	
	protected Context context;
	protected SausageCellMonth[] cells;
	private Date startDate;
	private Date finishDate;
	
	/* tmp Views */
	private SausageCellMonth cell;
	
	protected boolean invariant(){
		return this.context != null
			&& cells != null
			&& cells.length > 0
			&& startDate != null
			&& finishDate != null;
	}
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public SausageMonth(Context context,Date startDate,Date finishDate){
		super(context);
		assert startDate != null;
		assert finishDate != null;
		assert startDate.compareTo(finishDate) == -1;
		assert startDate.daysUntil(finishDate) > 0;
		
		this.context = context;
		this.startDate = startDate;
		this.finishDate = finishDate;
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
	
	private void initCell(SausageCellMonth cell, int pos){
		assert cell != null;
		cells[pos] = cell;
	}
	
	/*------------------------------------------------------------
	----------------------- I N T E R F A C E --------------------
	------------------------------------------------------------*/
	/**
	 * Uses {@code Runnable} for long parsing and add cell init
	 */
	@Override
	public void initCells() {
		
		int monthNum = startDate.monthsUntil(finishDate)+1;

		cells = new SausageCellMonth[monthNum*WORDS_NUM];

		int[] width = new int[monthNum];
		int[] month = new int[monthNum];
		int[] deltaWidth = new int[monthNum];
		Date[] dates = new Date[monthNum];
		
		calcMeasure(monthNum, width, month, deltaWidth, dates);

		/* creating and drawing cells */
		int k = 0,i = 0,j = 0;
		Dimension dim = null;
		Dimension dimExt = null;
		
		for (i = 0; i < monthNum; i++){
			dim = new Dimension(width[i],Config.INST.SAUSAGE.CELL_HEIGHT);
			for (j = 0; j < WORDS_NUM; j++){
				if (j == 0){
					dimExt = new Dimension(width[i]+deltaWidth[i],Config.INST.SAUSAGE.CELL_HEIGHT);
					cell = new SausageCellMonth(context,dates[i],dimExt,month[i],(i == 0) ? true : false, false);
				}
				else {
					if (j == WORDS_NUM - 1){
						cell = new SausageCellMonth(context,dates[i].findDate(dates[i].getDaysInMonth()-1),dim,month[i],false, (i == monthNum - 1) ? true : false);
					}
					else {
						cell = new SausageCellMonth(context,dates[i].findDate(2),dim,month[i],false,false);
					}
				}
				initCell(cell,k);
				k++;
			}
		}

		monthNum = 0;
		width = null;
		month = null;
		deltaWidth = null;
		dates = null;
		dim = null;
		dimExt = null;
		
		assert invariant();
	}
	
	/**
	 * Adds cells to the layout
	 */
	@Override
	public void addCells(){
		for (int i = 0; i < cells.length; i++){
			addView(cells[i]);
		}
	}
	
	/*------------------------------------------------------------
	-------------------------- P R I V A T E ---------------------
	------------------------------------------------------------*/
	private void calcMeasure(int monthNum, int[] width, int[] month,int[] deltaWidth, Date[] dates) {
		Date date = new Date(startDate.day,startDate.month,startDate.year);
		int daysInMonth = 0;
		int monthWidth = 0;
		for (int i = 0; i < monthNum; i++){
			daysInMonth = date.getDaysInMonth();
			monthWidth = daysInMonth*Config.INST.SAUSAGE.CELL_WIDTH;
			width[i] =  monthWidth / WORDS_NUM;
			deltaWidth[i] = monthWidth - width[i] * WORDS_NUM;
			month[i] = date.month;
			dates[i] = date;
			date = date.findDate(daysInMonth);
		}
		
		date = null;
	}
}
