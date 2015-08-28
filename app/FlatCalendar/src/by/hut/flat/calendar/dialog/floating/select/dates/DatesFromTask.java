package by.hut.flat.calendar.dialog.floating.select.dates;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cache;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;

public class DatesFromTask extends AsyncTask<Void,Void,Void>{

	private Context context;
	private int IntervalID;
	private Date newDateFrom;
	
	private DBAdapter db;
	
	public DatesFromTask(Context context,int IntervalID,Date newDateFrom){
		this.context = context;
		this.IntervalID = IntervalID;
		this.newDateFrom = newDateFrom;
		this.db = new DBAdapter(this.context);
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		db.open();
		Interval intervalDB = new Interval(db);
		IntervalAnketa intervalAnketaDB = new IntervalAnketa(db);
		Cache cacheDB = new Cache(db);
		Cleanings cleaningsDB = new Cleanings(db);
		
		String[] iData = intervalAnketaDB.getData(IntervalID);
		int type = Utils.Int(iData[IntervalAnketa.TYPE]);
		int FlatID = Utils.Int(iData[IntervalAnketa.FLAT_FID]);
		Date oldDateFrom = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_FROM]));
		Date dateTo = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_TO]));
		
		if (oldDateFrom.equals(newDateFrom)) {
			db.close();
			return null;
		}
		
		int[] removeCleaningsIDs = Utils.getRow(Utils.transpose(cleaningsDB.findCleaningsThatWillNotGetInNewInterval(IntervalID, newDateFrom, dateTo)),Cleanings.CID); 
		
		cacheDB.removeDebt(IntervalID);
		cacheDB.removePrepay(IntervalID);
		cacheDB.removeDayState(FlatID, type, oldDateFrom, dateTo);
		cleaningsDB.remove(removeCleaningsIDs);
		
		intervalDB.setNewDates(IntervalID, newDateFrom, dateTo);
		cacheDB.setDayState(FlatID, type, newDateFrom, dateTo);
		cacheDB.setDebt(IntervalID);
		cacheDB.setPrepay(IntervalID);
		
		db.close();
		
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,((oldDateFrom.compareTo(newDateFrom) == 1) ? newDateFrom : oldDateFrom).toString(),dateTo.toString()});
		BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,((oldDateFrom.compareTo(newDateFrom) == 1) ? newDateFrom : oldDateFrom).toString(),dateTo.toString()});
		
		return null;
	}

}
