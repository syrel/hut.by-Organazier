package by.hut.flat.calendar.core.config;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Print;
import android.content.Context;

public class Calendar extends Preferences{
	private static final String TAG  = "calendar";

	private static final int ROWS = 7;
	private static final int COLUMNS = 7;
	
	private static final String HEADER_HEIGHT_NAME = "header_height";
	private static final String FOOTER_HEIGHT_NAME = "footer_height";
	private static final String CELL_WIDTH_NAME = "cell_width";
	private static final String CELL_HEIGHT_NAME = "cell_height";
	private static final String CALENDAR_HEIGHT_NAME = "calendar_height";
	private static final String CELL_TEXT_SIZE_NAME = "cell_text_size";
	private static final String CELL_TEXT_SIZE_PERCENT_NAME = "cell_text_size_percent";
	
	public int HEADER_HEIGHT;
	public int FOOTER_HEIGHT;
	public int CELL_WIDTH;
	public int CELL_HEIGHT;
	public int CALENDAR_HEIGHT;
	public int CELL_TEXT_SIZE;
	public int CELL_TEXT_SIZE_PERCENT;
	
	public final boolean OK;
	
	public Calendar(Context context) {
		super(TAG, context);

		this.HEADER_HEIGHT = readHeaderHeight();
		this.FOOTER_HEIGHT = readFooterHeight();
		this.CELL_TEXT_SIZE_PERCENT = readCellTextSizePercent();
		this.CALENDAR_HEIGHT = readCalendarHeight();
		this.CELL_WIDTH = readCellWidth();
		this.CELL_HEIGHT = readCellHeight();
		this.CELL_TEXT_SIZE = readCellTextSize();
		
		check();
		this.OK = true;
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void reInit() {
		this.HEADER_HEIGHT = initHeaderHeight();
		this.FOOTER_HEIGHT = initFooterHeight();
		this.CELL_TEXT_SIZE_PERCENT = initCellTextSizePercent();
		this.CALENDAR_HEIGHT = initCalendarHeight();
		this.CELL_WIDTH = initCellWidth();
		this.CELL_HEIGHT = initCellHeight();
		this.CELL_TEXT_SIZE = initCellTextSize();
	}
	/*------------------------------------------------------------
	-------------------------- C H E C K S -----------------------
	------------------------------------------------------------*/
	@Override
	public void check() {
		if (Config.INST.REINIT){
			reInit();
		}
	}
	/*------------------------------------------------------------
	----------------------------- R E A D ------------------------
	------------------------------------------------------------*/
	private int readHeaderHeight(){
		int headerHeight = this.getInt(HEADER_HEIGHT_NAME);
		if (headerHeight == NONE) headerHeight = this.initHeaderHeight();
		return headerHeight;
	}
	private int readFooterHeight(){
		int footerHeight = this.getInt(FOOTER_HEIGHT_NAME);
		if (footerHeight == NONE) footerHeight = this.initFooterHeight();
		return footerHeight;
	}
	
	private int readCellTextSizePercent(){
		int cellTextSizePercent = this.getInt(CELL_TEXT_SIZE_PERCENT_NAME);
		if (cellTextSizePercent == NONE) cellTextSizePercent = this.initCellTextSizePercent();
		return cellTextSizePercent;
	}
	
	private int readCalendarHeight(){
		int calendarHeight = this.getInt(CALENDAR_HEIGHT_NAME);
		if (calendarHeight == NONE) calendarHeight = this.initCalendarHeight();
		return calendarHeight;
	}
	
	private int readCellWidth(){
		int cellWidth = this.getInt(CELL_WIDTH_NAME);
		if (cellWidth == NONE) cellWidth = this.initCellWidth();
		return cellWidth;
	}
	
	private int readCellHeight(){
		int calendarHeight = this.getInt(CELL_HEIGHT_NAME);
		if (calendarHeight == NONE) calendarHeight = this.initCellHeight();
		return calendarHeight;
	}
	
	private int readCellTextSize(){
		int cellTextSize = this.getInt(CELL_TEXT_SIZE_NAME);
		if (cellTextSize == NONE) cellTextSize = this.initCellTextSize();
		return cellTextSize;
	}
	/*------------------------------------------------------------
	----------------------------- I N I T ------------------------
	------------------------------------------------------------*/
	private int initHeaderHeight(){
		int headerHeight = (int) resources.getDimension(R.dimen.default_flat_calendar_header_height);
		save(HEADER_HEIGHT_NAME, headerHeight);
		return headerHeight;
	}
	private int initFooterHeight(){
		int footerHeight = (int) resources.getDimension(R.dimen.default_flat_calendar_footer_height);
		save(FOOTER_HEIGHT_NAME, footerHeight);
		return footerHeight;
	}

	private int initCellTextSizePercent(){
		int cellTextSizePercent = resources.getInteger(R.integer.default_calendar_cell_text_size_percent);
		save(CELL_TEXT_SIZE_PERCENT_NAME,cellTextSizePercent);
		return cellTextSizePercent;
	}
	
	private int initCalendarHeight(){
		if (Config.INST.DISPLAY == null || Config.INST.MAIN == null || this.FOOTER_HEIGHT <= 0){
			//TODO throw exception here
			Print.err("FOOTER_HEIGHT, DISPLAY and MAIN configs must be initialized before CALENDAR_HEIGHT!");
		}
		
		int calendarHeight = Config.INST.DISPLAY.HEIGHT-Config.INST.DISPLAY.STATUS_BAR_HEIGHT-
				Config.INST.MAIN.TAB_HOST_HEIGHT-HEADER_HEIGHT-FOOTER_HEIGHT;
		save(CALENDAR_HEIGHT_NAME,calendarHeight);
		return calendarHeight;
	}

	private int initCellWidth(){
		if (Config.INST.DISPLAY == null){
			//TODO throw exception here
			Print.err("DISPLAY config must be initialized before CELL_WIDTH!");
		}
		int cellWidth = (int)(Config.INST.DISPLAY.WIDTH/COLUMNS);
		save(CELL_WIDTH_NAME,cellWidth);
		return cellWidth;
	}
	
	private int initCellHeight(){
		if (CALENDAR_HEIGHT <= 0){
			//TODO throw exception here
			Print.err("CALENDAR_HEIGHT must be initialized before CELL_HEIGHT!");
		}
		int cellHeight = (int)(CALENDAR_HEIGHT/ROWS);
		save(CELL_HEIGHT_NAME,cellHeight);
		return cellHeight;
	}
	
	private int initCellTextSize(){
		if (this.CELL_WIDTH <= 0 || this.CELL_HEIGHT <= 0 || this.CELL_TEXT_SIZE_PERCENT <= 0){
			//TODO throw exception here
			Print.err("CELL_WIDTH, CELL_HEIGHT and CELL_TEXT_SIZE_PERCENT must be initialized before CELL_TEXT_SIZE!");
		}
		int cellTextSize = (int)((float)Math.min(CELL_WIDTH, CELL_HEIGHT)*((float)CELL_TEXT_SIZE_PERCENT/(float)100));
		save(CELL_TEXT_SIZE_NAME,cellTextSize);
		return cellTextSize;
	}

}
