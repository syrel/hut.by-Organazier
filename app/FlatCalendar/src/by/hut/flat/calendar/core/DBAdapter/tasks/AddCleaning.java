package by.hut.flat.calendar.core.DBAdapter.tasks;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.Task;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Date;

public class AddCleaning extends Task{

	private DBAdapter db;
	private Context context;
	private int FlatID;
	private int IntervalID;
	private int MaidenID;
	private Date date;
	
	public AddCleaning(Context context, int FlatID, int eventID, int maidenID,Date date) {
		super(context);
		this.context = context;
		this.FlatID = FlatID;
		this.IntervalID = eventID;
		this.MaidenID = maidenID;
		this.date = date;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		db = new DBAdapter(context);
		db.open();

		Cleanings cleaningsDB = new Cleanings(db);
		cleaningsDB.addCleaning(IntervalID, FlatID, MaidenID, date);
		
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,date.toString(),date.toString()});
		BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
				new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"refresh_flat",""+FlatID,date.toString(),date.toString()});
		
		db.close();
		return true;
	}
}
