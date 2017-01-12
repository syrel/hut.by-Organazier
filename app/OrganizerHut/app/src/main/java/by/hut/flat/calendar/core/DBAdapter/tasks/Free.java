package by.hut.flat.calendar.core.DBAdapter.tasks;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.Task;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Date;

public class Free extends Task{
	private DBAdapter db;
	private Context context;
	private int FlatID;
	private int IntervalID;
	private int[] removeCleanings;
	
	public Free(Context context, int FlatID, int IntervalID, int[] removeCleanings) {
		super(context);
		this.context = context;
		this.FlatID = FlatID;
		this.IntervalID = IntervalID;
		this.removeCleanings = removeCleanings;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		db = new DBAdapter(context);
		db.open();
		Interval intervalDB = new Interval(db);
		Cleanings cleaningsDB = new Cleanings(db);
		int[] iData = intervalDB.getData(IntervalID);
		
		Date dateFrom = new Date(iData[Interval.DATE_FROM]);
		Date dateTo = new Date(iData[Interval.DATE_TO]);

		if (removeCleanings.length > 0)cleaningsDB.remove(removeCleanings);
		intervalDB.removeInterval(IntervalID);
		
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
