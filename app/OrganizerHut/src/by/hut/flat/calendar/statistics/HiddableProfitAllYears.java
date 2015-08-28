package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.widget.layout.HiddableLayout;

public class HiddableProfitAllYears extends HiddableLayout{

	private DBAdapter db;
	private Interval intervalDB;
	
	public HiddableProfitAllYears(Context context) {
		super(context);
	}

	@Override
	protected void onPreInitialize() {
		db = new DBAdapter(getContext());
		db.open();
		intervalDB = new Interval(db);
	}
	
	private void initYears(){
		int firstYear = intervalDB.getFirstDate().year;
		int lastYear = Config.INST.SYSTEM.TODAY.year;
		CollapsableProfitYearByMonth profit = null;
		for (int year = lastYear; year >= firstYear; year--){
			profit = new CollapsableProfitYearByMonth(getContext(),year);
			addView(profit);
		}
		profit = null;
		addView(new CollapsableProfitAllTime(getContext()));
	}
	
	@Override
	protected void doInInitialization() {
		initYears();
	}

	@Override
	protected void onPostInitialize() {
		db.close();
	}

}
