package by.hut.flat.calendar.core.DBAdapter.tasks;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.core.DBAdapter.tables.Cache;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.Task;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class Convert extends Task {
	private DBAdapter db;
	private Context context;
	private int FlatID;
	private int IntervalID;
	
	public Convert(Context context, int FlatID, int intervalID) {
		super(context);
		this.context = context;
		this.FlatID = FlatID;
		this.IntervalID = intervalID;
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		db = new DBAdapter(context);
		db.open();
		
		Interval intervalDB = new Interval(db);
		IntervalAnketa intervalAnketaDB = new IntervalAnketa(db);
		Cache cacheDB  = new Cache(db);
		int[] iData = intervalDB.getData(IntervalID);
		
		int typeOld = Utils.Int(intervalAnketaDB.getData(IntervalID)[IntervalAnketa.TYPE]);
		int typeNew = (typeOld == dbStructure.BOOK) ?  dbStructure.RENT : dbStructure.BOOK;
		
		Date dateFrom = new Date(iData[Interval.DATE_FROM]);
		Date dateTo = new Date(iData[Interval.DATE_TO]);
		
		intervalAnketaDB.setType(IntervalID, typeNew);
		cacheDB.removeDayState(FlatID, typeOld, dateFrom, dateTo);
		cacheDB.setDayState(FlatID, typeNew, dateFrom, dateTo);
		
		db.close();
		
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),dateTo.toString()});
		BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),dateTo.toString()});
		

		return true;
	}
}
