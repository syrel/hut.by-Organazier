package by.hut.flat.calendar.core.config;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Print;
import android.content.Context;

public class Sausage extends Preferences{
	private static final String TAG = "sausage";
	private static final int ADDITIONAL_SAUSAGES = 2;
	
	private static final String MONTH_BEFORE_TODAY_NAME = "months_before_today";
	private static final String MONTH_AFTER_TODAY_NAME = "months_after_today";
	private static final String CELLS_NUM_BEFORE_TODAY_NAME = "cells_num_before_today";
	private static final String CELLS_NUM_ON_THE_SCREEN_NAME = "cells_num_on_the_screen";
	private static final String BUTTON_WIDTH_NAME = "button_width";
	private static final String BUTTON_HEIGHT_NAME = "button_height";
	private static final String CELL_WIDTH_NAME = "cell_width";
	private static final String CELL_HEIGHT_NAME = "cell_height";
	private static final String INFO_BAR_HEIGHT_NAME = "footer_height";
	private static final String INFO_BAR_TEXT_SIZE_NAME = "info_bar_text_size";
	private static final String INFO_BAR_TEXT_SIZE_PERCENT_NAME = "info_bar_text_size_percent";
	private static final String SAUSAGE_HEIGHT_NAME = "sausage_height";
	private static final String CELL_TEXT_SIZE_NAME = "cell_text_size";
	private static final String CELL_TEXT_SIZE_PERCENT_NAME = "cell_text_size_percent";
	
	public int MONTHS_BEFORE_TODAY;
	public int MONTHS_AFTER_TODAY;
	public int CELLS_NUM_BEFORE_TODAY;
	public int CELLS_NUM_ON_THE_SCREEN;
	public int BUTTON_WIDTH;
	public int BUTTON_HEIGHT;
	public int CELL_WIDTH;
	public int CELL_HEIGHT;
	public int INFO_BAR_HEIGHT;
	public int INFO_BAR_TEXT_SIZE;
	public int INFO_BAR_TEXT_SIZE_PERCENT;
	public int SAUSAGE_HEIGHT;
	public int CELL_TEXT_SIZE;
	public int CELL_TEXT_SIZE_PERCENT;
	
	public final boolean OK;
	
	public Sausage(Context context){
		super(TAG,context);
		this.CELLS_NUM_BEFORE_TODAY = readCellsNumBeforeToday();
		this.CELLS_NUM_ON_THE_SCREEN = readCellsNumOnScreen();
		this.BUTTON_WIDTH = readButtonWidth();
		this.INFO_BAR_HEIGHT = readFooterHeight();
		this.INFO_BAR_TEXT_SIZE_PERCENT = readInfoBarTextSizePercent();
		this.INFO_BAR_TEXT_SIZE = readInfoBarTextSize();
		this.MONTHS_BEFORE_TODAY = readMonthsBeforeToday();
		this.MONTHS_AFTER_TODAY = readMonthsAfterToday();
		this.CELL_TEXT_SIZE_PERCENT = readCellTextSizePercent();
		this.SAUSAGE_HEIGHT = readSausageHeight();
		this.CELL_WIDTH = readCellWidth();
		this.CELL_HEIGHT = readCellHeight();
		this.BUTTON_HEIGHT = readButtonHeight();
		this.CELL_TEXT_SIZE = readCellTextSize();
		check();
		this.OK = true;
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void reInit() {
		setNull();
		this.CELLS_NUM_BEFORE_TODAY = initCellsNumBeforeToday();
		this.CELLS_NUM_ON_THE_SCREEN = initCellsNumOnScreen();
		this.BUTTON_WIDTH = initButtonWidth();
		this.INFO_BAR_HEIGHT = initFooterHeight();
		this.INFO_BAR_TEXT_SIZE_PERCENT = initInfoBarTextSizePercent();
		this.INFO_BAR_TEXT_SIZE = initInfoBarTextSize();
		this.MONTHS_BEFORE_TODAY = initMonthsBeforeToday();
		this.MONTHS_AFTER_TODAY = initMonthsAfterToday();
		this.CELL_TEXT_SIZE_PERCENT = initCellTextSizePercent();
		this.SAUSAGE_HEIGHT = initSausageHeight();
		this.CELL_WIDTH = initCellWidth();
		this.CELL_HEIGHT = initCellHeight();
		this.BUTTON_HEIGHT = initButtonHeight();
		this.CELL_TEXT_SIZE = initCellTextSize();
	}
	
	public void setNull(){
		this.CELLS_NUM_BEFORE_TODAY = 0;
		this.CELLS_NUM_ON_THE_SCREEN = 0;
		this.BUTTON_WIDTH = 0;
		this.INFO_BAR_HEIGHT = 0;
		this.INFO_BAR_TEXT_SIZE_PERCENT = 0;
		this.INFO_BAR_TEXT_SIZE = 0;
		this.MONTHS_BEFORE_TODAY = 0;
		this.MONTHS_AFTER_TODAY = 0;
		this.CELL_TEXT_SIZE_PERCENT = 0;
		this.SAUSAGE_HEIGHT = 0;
		this.CELL_WIDTH = 0;
		this.CELL_HEIGHT = 0;
		this.BUTTON_HEIGHT = 0;
		this.CELL_TEXT_SIZE = 0;
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
	private int readCellsNumBeforeToday(){
		int cellsNumBeforeToday = this.getInt(CELLS_NUM_BEFORE_TODAY_NAME);
		if (cellsNumBeforeToday == NONE) cellsNumBeforeToday = this.initCellsNumBeforeToday();
		return cellsNumBeforeToday;
	}
	
	private int readCellsNumOnScreen(){
		int cellsNumOnScreen = this.getInt(CELLS_NUM_ON_THE_SCREEN_NAME);
		if (cellsNumOnScreen == NONE) cellsNumOnScreen = this.initCellsNumOnScreen();
		return cellsNumOnScreen;
	}
	
	private int readButtonWidth(){
		int buttonWidth = this.getInt(BUTTON_WIDTH_NAME);
		if (buttonWidth == NONE) buttonWidth = this.initButtonWidth();
		return buttonWidth;
	}
	
	private int readFooterHeight(){
		int footerHeight = this.getInt(INFO_BAR_HEIGHT_NAME);
		if (footerHeight == NONE) footerHeight = this.initFooterHeight();
		return footerHeight;
	}
	
	private int readMonthsBeforeToday(){
		int monthsBeforeToday = this.getInt(MONTH_BEFORE_TODAY_NAME);
		if (monthsBeforeToday == NONE) monthsBeforeToday = this.initMonthsBeforeToday();
		return monthsBeforeToday;
	}
	
	private int readMonthsAfterToday(){
		int monthsAfterToday = this.getInt(MONTH_AFTER_TODAY_NAME);
		if (monthsAfterToday == NONE) monthsAfterToday = this.initMonthsAfterToday();
		return monthsAfterToday;
	}

	private int readCellTextSizePercent(){
		int cellTextSizePercent = this.getInt(CELL_TEXT_SIZE_PERCENT_NAME);
		if (cellTextSizePercent == NONE) cellTextSizePercent = this.initCellTextSizePercent();
		return cellTextSizePercent;
	}
	
	private int readInfoBarTextSizePercent(){
		int infoBarTextSizePercent = this.getInt(INFO_BAR_TEXT_SIZE_PERCENT_NAME);
		if (infoBarTextSizePercent == NONE) infoBarTextSizePercent = this.initInfoBarTextSizePercent();
		return infoBarTextSizePercent;
	}
	
	private int readInfoBarTextSize(){
		int infoBarTextSize = this.getInt(INFO_BAR_TEXT_SIZE_NAME);
		if (infoBarTextSize == NONE) infoBarTextSize = this.initInfoBarTextSize();
		return infoBarTextSize;
	}
	
	private int readSausageHeight(){
		int sausageHeight = this.getInt(SAUSAGE_HEIGHT_NAME);
		if (sausageHeight == NONE) sausageHeight = this.initSausageHeight();
		return sausageHeight;
	}
	
	private int readCellWidth(){
		int cellWidth = this.getInt(CELL_WIDTH_NAME);
		if (cellWidth == NONE) cellWidth = this.initCellWidth();
		return cellWidth;
	}
	
	private int readCellHeight(){
		int cellHeight = this.getInt(CELL_HEIGHT_NAME);
		if (cellHeight == NONE) cellHeight = this.initCellHeight();
		return cellHeight;
	}
	
	private int readButtonHeight(){
		int buttonHeight = this.getInt(BUTTON_HEIGHT_NAME);
		if (buttonHeight == NONE) buttonHeight = this.initButtonHeight();
		return buttonHeight;
	}
	
	private int readCellTextSize(){
		int cellTextSize = this.getInt(CELL_TEXT_SIZE_NAME);
		if (cellTextSize == NONE) cellTextSize = this.initCellTextSize();
		return cellTextSize;
	}
	
	/*------------------------------------------------------------
	----------------------------- I N I T ------------------------
	------------------------------------------------------------*/
	private int initCellsNumOnScreen(){
		int cellsNumOnTheScreen = resources.getInteger(R.integer.default_sausage_cell_num_on_the_screen);
		save(CELLS_NUM_ON_THE_SCREEN_NAME, cellsNumOnTheScreen);
		return cellsNumOnTheScreen;
	}
	
	private int initButtonWidth(){
		int buttonWidth = (int) resources.getDimension(R.dimen.default_sausage_button_width);
		save(BUTTON_WIDTH_NAME,buttonWidth);
		return buttonWidth;
	}
	
	private int initFooterHeight(){
		int footerHeight = (int) resources.getDimension(R.dimen.default_sausage_footer_height);
		save(INFO_BAR_HEIGHT_NAME,footerHeight);
		return footerHeight;
	}
	
	private int initMonthsBeforeToday(){
		int monthsBeforeToday = resources.getInteger(R.integer.default_sausage_months_before_today);
		save(MONTH_BEFORE_TODAY_NAME, monthsBeforeToday);
		return monthsBeforeToday;
	}
	
	private int initMonthsAfterToday(){
		int monthsAfterToday = resources.getInteger(R.integer.default_sausage_months_after_today);
		save(MONTH_AFTER_TODAY_NAME, monthsAfterToday);
		return monthsAfterToday;
	}
	
	private int initCellsNumBeforeToday(){
		int cellsNumBeforeToday = resources.getInteger(R.integer.default_sausage_cell_num_before_today);
		save(CELLS_NUM_BEFORE_TODAY_NAME, cellsNumBeforeToday);
		return cellsNumBeforeToday;
	}

	private int initCellTextSizePercent(){
		int cellTextSizePercent = resources.getInteger(R.integer.default_sausage_cell_text_size_percent);
		save(CELL_TEXT_SIZE_PERCENT_NAME,cellTextSizePercent);
		return cellTextSizePercent;
	}
	
	private int initInfoBarTextSizePercent(){
		int infoBarTextSizePercent = resources.getInteger(R.integer.default_sausage_infobar_text_size_percent);
		save(INFO_BAR_TEXT_SIZE_PERCENT_NAME,infoBarTextSizePercent);
		return infoBarTextSizePercent;
	}
	
	private int initInfoBarTextSize(){
		if (this.INFO_BAR_HEIGHT <= 0 || this.INFO_BAR_TEXT_SIZE_PERCENT <= 0){
			//TODO throw exception here
			Print.err("INFO_BAR_HEIGHT and INFO_BAR_TEXT_SIZE_PERCENT must be initialized before INFO_BAR_TEXT_SIZE_PERCENT!");
		}
		int infoBarTextSize = (int)((float)INFO_BAR_HEIGHT*((float)INFO_BAR_TEXT_SIZE_PERCENT/(float)100));
		save(INFO_BAR_TEXT_SIZE_NAME,infoBarTextSize);
		return infoBarTextSize;
	}
	
	private int initSausageHeight(){
		Print.log(this.INFO_BAR_HEIGHT);
		if (Config.INST.DISPLAY == null || Config.INST.MAIN == null || this.INFO_BAR_HEIGHT <= 0){
			//TODO throw exception here
			Print.err("FOOTER_HEIGHT, DISPLAY and MAIN configs must be initialized before SAUSAGE_HEIGHT!");
		}
		
		int sausageHeight = Config.INST.DISPLAY.HEIGHT-Config.INST.DISPLAY.STATUS_BAR_HEIGHT-
				Config.INST.MAIN.TAB_HOST_HEIGHT-INFO_BAR_HEIGHT;
		save(SAUSAGE_HEIGHT_NAME,sausageHeight);
		return sausageHeight;
	}
	
	private int initCellWidth(){
		if (Config.INST.DISPLAY == null || BUTTON_WIDTH <= 0 || CELLS_NUM_ON_THE_SCREEN <= 0){
			//TODO throw exception here
			Print.err("Display config, BUTTON_WIDTH and CELLS_NUM_ON_THE_SCREEN must be initialized before CELL_WIDTH!");
		}
		int cellWidth = (int)((Config.INST.DISPLAY.WIDTH-BUTTON_WIDTH) / CELLS_NUM_ON_THE_SCREEN);
		save(CELL_WIDTH_NAME,cellWidth);
		return cellWidth;
	}
	
	private int initCellHeight(){
		if (Config.INST.SYSTEM == null || SAUSAGE_HEIGHT <= 0){
			//TODO throw exception here
			Print.err("System config and SAUSAGE_HEIGHT must be initialized before CELL_HEIGHT!");
		}
		int cellHeight = (int) (SAUSAGE_HEIGHT / (Config.INST.SYSTEM.FLATS_NUM+ADDITIONAL_SAUSAGES));
		save(CELL_HEIGHT_NAME,cellHeight);
		return cellHeight;
	}
	
	private int initButtonHeight(){
		if (CELL_HEIGHT <= 0){
			//TODO throw exception here
			Print.err("CELL_HEIGHT must be initialized before BUTTON_HEIGHT!");
		}
		int buttonHeight = this.CELL_HEIGHT;
		save(BUTTON_HEIGHT_NAME,buttonHeight);
		return buttonHeight;
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
