package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.layout.HiddableLayout;

/**
 * Statistics class that contains cleanings in current and previous months
 * @author hata.pc
 */
public class HiddableMaidenCurrentPreviousMonths extends HiddableLayout{

	public DBAdapter db;
	private String[][] maidenData;
	private Maiden maidenDB;
	private Cleanings cleaningsDB;
	
	public HiddableMaidenCurrentPreviousMonths(Context context) {
		super(context);
	}

	@Override
	protected void onPreInitialize() {
		db = new DBAdapter(getContext());
		db.open();
		maidenDB = new Maiden(db);
		cleaningsDB = new Cleanings(db);
	}

	@Override
	protected void doInInitialization() {
		maidenData = maidenDB.getData(true);
		currentMonth();
		previousMonth();
	}

	/**
	 * Initializes current month and adds to view arraylist
	 */
	private void currentMonth(){
		Date[] dates = calcMonthDatesFromToday(0);
		
		String title = "Уборок в текущем месяце:";
		addView(new MaidenInterval(getContext(), dates[0], dates[1], cleaningsDB, maidenData, title));
	}
	
	/**
	 * Initializes previous month and adds to view arraylist
	 */
	private void previousMonth(){
		Date[] dates = calcMonthDatesFromToday(-1);
		
		String title = "Уборок в предыдущем месяце:";
		addView(new MaidenInterval(getContext(), dates[0], dates[1], cleaningsDB, maidenData, title));
	}
	
	/**
	 * Calculates dateFrom and dateTo to month, that stays from today at delta
	 */
	private Date[] calcMonthDatesFromToday(int deltaMonth){
		Date today = Config.INST.SYSTEM.TODAY.copy();
		Date dateTo = null;
		Date dateFrom = null;

		if (today.day >= StatisticsMaiden.CLEANING_FIRST_DAY){
			dateTo = today.copy().setDay(StatisticsMaiden.CLEANING_LAST_DAY).jumpMonth(deltaMonth+1);
			dateFrom = today.copy().setDay(StatisticsMaiden.CLEANING_FIRST_DAY).jumpMonth(deltaMonth);
		}
		else {
			dateTo = today.copy().setDay(StatisticsMaiden.CLEANING_LAST_DAY).jumpMonth(deltaMonth);
			dateFrom = today.copy().setDay(StatisticsMaiden.CLEANING_FIRST_DAY).jumpMonth(deltaMonth-1);
		}
		return new Date[]{dateFrom,dateTo};
	}
	
	@Override
	protected void onPostInitialize() {
		db.close();
	}

}
