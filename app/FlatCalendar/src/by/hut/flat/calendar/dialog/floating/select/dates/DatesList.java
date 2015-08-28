package by.hut.flat.calendar.dialog.floating.select.dates;
import android.content.Context;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.List;

public class DatesList extends List{
	public static final int DATE_FROM = 1;
	public static final int DATE_TO = 2;
	
	private int IntervalID;
	private Context context;
	private DBAdapter db;
	private IntervalAnketa intervalAnketaDB;
	private DatesEntry dateFrom;
	private DatesEntry dateTo;
	
	public DatesList(Context context,int IntervalID) {
		super(context);
		this.IntervalID = IntervalID;
		this.context = context;
		db = new DBAdapter(context);
		initList();
	}

	@Override
	protected void initList() {
		db.open();
		intervalAnketaDB = new IntervalAnketa(db);
		String[] iData = intervalAnketaDB.getData(IntervalID);
		db.close();
		
		dateTo = new DatesEntry(context,DATE_TO);
		dateTo.setType(Utils.getString(context, R.string.dialog_datetime_to));
		dateTo.setDate(new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_TO])));
		
		dateFrom = new DatesEntry(context,DATE_FROM);
		dateFrom.setType(Utils.getString(context, R.string.dialog_datetime_from));
		dateFrom.setDate(new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_FROM])));
		dateFrom.hideLine();
		
		this.add(dateTo);
		this.add(dateFrom);
	}
	
	public DatesEntry getEntry(int id){
		switch(id){
			case DATE_FROM:{
				return dateFrom;
			}
			case DATE_TO:{
				return dateTo;
			}
			default:{
				return null;
			}
		}
	}

}
