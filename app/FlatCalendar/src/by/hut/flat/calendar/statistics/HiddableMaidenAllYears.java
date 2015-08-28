package by.hut.flat.calendar.statistics;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.widget.layout.HiddableLayout;

/**
 * Statistics class that contains cleaning in all years that exists in program by year DESC
 * @author hata.pc
 */
public class HiddableMaidenAllYears extends HiddableLayout{
	
	private DBAdapter db;
	private Interval intervalDB;
	
	public HiddableMaidenAllYears(Context context) {
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
		CollapsableMaidenYearByMonth cleanings = null;
		for (int year = lastYear; year >= firstYear; year--){
			cleanings = new CollapsableMaidenYearByMonth(getContext(),year);
			addView(cleanings);
		}
		cleanings = null;
		addView(new CollapsableMaidenAllTime(getContext()));
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
