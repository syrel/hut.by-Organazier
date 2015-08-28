package by.hut.flat.calendar.statistics;

import android.content.Context;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.widget.layout.HiddableLayout;

/**
 * Statistics class that contains cleanings of all months in specified year 
 * @author hata.pc
 */
public class HiddableMaidenYearByMonth extends HiddableLayout{
		
	private int year;
	public DBAdapter db;
	private String[][] maidenData;
	private Maiden maidenDB;
	private Cleanings cleaningsDB;
	
	public HiddableMaidenYearByMonth(Context context, int year) {
		super(context);
		this.year = year;
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
		initYear();
	}

	private void initYear(){
		Date dateFrom = new Date(StatisticsMaiden.CLEANING_FIRST_DAY,11,year);
		Date dateTo = new Date(StatisticsMaiden.CLEANING_LAST_DAY,12,year);
		
		if (year == Config.INST.SYSTEM.TODAY.year){
			Date today = Config.INST.SYSTEM.TODAY.copy();
			if (today.day >= StatisticsMaiden.CLEANING_FIRST_DAY){
				dateTo = today.copy().setDay(StatisticsMaiden.CLEANING_LAST_DAY).jumpMonth(1);
				dateFrom = today.copy().setDay(StatisticsMaiden.CLEANING_FIRST_DAY);
			}
			else {
				dateTo = today.copy().setDay(StatisticsMaiden.CLEANING_LAST_DAY);
				dateFrom = today.copy().setDay(StatisticsMaiden.CLEANING_FIRST_DAY).jumpMonth(-1);
			}
		}
		
		while (true){
			String title = "Статистика уборок за "+Config.INST.STRINGS.MONTHS_OF_YEAR[dateTo.month-1]+":";
			addView(new MaidenInterval(getContext(), dateFrom, dateTo, cleaningsDB, maidenData, title));
			dateFrom.jumpMonth(-1);
			dateTo.jumpMonth(-1);
			if (dateFrom.year != year){
				break;
			}
		}
		
		dateFrom = new Date(StatisticsMaiden.CLEANING_FIRST_DAY,12,year-1);
		dateTo = new Date(StatisticsMaiden.CLEANING_LAST_DAY,12,year);
		addView(new MaidenInterval(getContext(),dateFrom, dateTo, cleaningsDB, maidenData, "Уборок за весь "+year+":"));
	}
	
	@Override
	protected void onPostInitialize() {
		db.close();
	}
}
