package by.hut.flat.calendar.dialog.floating.select.dates;

import android.content.Context;
import android.os.AsyncTask;
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

public class DatesToTask extends AsyncTask<Void,Void,Void>{
	private Context context;
	private int IntervalID;
	private Date newDateTo;
	
	private DBAdapter db;

	public DatesToTask(Context context,int IntervalID,Date newDateTo){
		this.context = context;
		this.IntervalID = IntervalID;
		this.newDateTo = newDateTo;
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
		Date dateFrom = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_FROM]));
		Date oldDateTo = new Date(Utils.Int(iData[IntervalAnketa.INTERVAL_DATE_TO]));
		
		if (oldDateTo.equals(newDateTo)) {
			db.close();
			return null;
		}
		
		int[][] evictionCleanings = cleaningsDB.getDataByDateIntervalID(IntervalID, oldDateTo);
		cleaningsDB.remove(Utils.getRow(Utils.transpose(evictionCleanings),Cleanings.CID));
		
		int[] removeCleaningsIDs = Utils.getRow(Utils.transpose(cleaningsDB.findCleaningsThatWillNotGetInNewInterval(IntervalID, dateFrom, newDateTo)),Cleanings.CID); 
				
		cacheDB.removeDebt(IntervalID);
		cacheDB.removePrepay(IntervalID);
		cacheDB.removeDayState(FlatID, type, dateFrom, oldDateTo);
		cleaningsDB.remove(removeCleaningsIDs);
		
		intervalDB.setNewDates(IntervalID, dateFrom, newDateTo);
		cacheDB.setDayState(FlatID, type, dateFrom, newDateTo);
		cacheDB.setDebt(IntervalID);
		cacheDB.setPrepay(IntervalID);
		
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),((oldDateTo.compareTo(newDateTo) == 1) ? oldDateTo : newDateTo).toString()});
		BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),((oldDateTo.compareTo(newDateTo) == 1) ? oldDateTo : newDateTo).toString()});
		
		
		return null;
	}
}
