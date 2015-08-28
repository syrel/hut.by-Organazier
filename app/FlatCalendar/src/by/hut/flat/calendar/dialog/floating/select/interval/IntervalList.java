package by.hut.flat.calendar.dialog.floating.select.interval;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.List;

public class IntervalList extends List{
	private Context context;
	private DBAdapter db;
	
	private int[] intervalIDs;
	private IntervalAnketa intervalAnketaDB;
	
	public IntervalList(Context context,int[] intervalIDs) {
		super(context);
		assert intervalIDs != null;
		this.context = context;
		db = new DBAdapter(context);
		this.intervalIDs = intervalIDs;
		this.initList();
	}

	@Override
	protected void initList() {
		db.open();
		intervalAnketaDB = new IntervalAnketa(db);
		for (int index = 0,length = intervalIDs.length; index < length; index++){
			IntervalEntry entry = new IntervalEntry(context,intervalIDs[index]);
			initEntry(entry,index);
			add(entry);
		}
		db.close();
	}
	
	private void initEntry(IntervalEntry entry, int index){
		String[] iData = intervalAnketaDB.getData(intervalIDs[index]);
		StringBuilder address = new StringBuilder();
		String shortAddress = iData[IntervalAnketa.FLAT_SHORT_ADDRESS];
		address.append((shortAddress.length() > 0) ? shortAddress : iData[IntervalAnketa.FLAT_ADDRESS]);
		address.append(':');
		entry.setFlatAddress(address.toString());
		
		Date dateFrom = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_FROM]));
		Date dateTo = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_TO]));
		StringBuilder date = new StringBuilder();
		date.append(dateFrom.day);
		date.append('-');
		date.append(dateTo.day);
		date.append(' ');
		date.append(dateTo.getMonthText());
		date.append(' ');
		date.append(dateTo.year);
		entry.setDate(date.toString());
		
		if (index == intervalIDs.length-1){
			entry.hideLine();
		}
	}
}
