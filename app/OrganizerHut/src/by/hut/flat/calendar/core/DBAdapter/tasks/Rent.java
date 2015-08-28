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

public class Rent extends Task{
	private static final int state = 2;	//rent = 2
	
	private DBAdapter db;
	private Interval dbInterval;
	private Cleanings dbCleanings;
	private Context context;
	private int FlatID;
	private int MaidenID;
	private Date firstDate;
	private Date lastDate;
	
	public Rent (Context context, int FlatID, Date firstDate, Date lastDate,int MaidenID) {
		super(context);
		this.context = context;
		this.FlatID = FlatID;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
		this.MaidenID = MaidenID;
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0) {
		db = new DBAdapter(context);
		db.open();
		dbInterval = new Interval(db);
		dbCleanings = new Cleanings(db);

		long IntervalID = dbInterval.addInterval(FlatID, state, firstDate, lastDate);
		if (MaidenID > 0) dbCleanings.addCleaning((int)IntervalID, FlatID, MaidenID, lastDate);
		
		db.close();
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,firstDate.toString(),lastDate.toString()});
		BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,firstDate.toString(),lastDate.toString()});
		
		return true;
	}
}
