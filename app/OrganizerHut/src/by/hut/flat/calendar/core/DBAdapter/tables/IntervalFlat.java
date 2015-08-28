package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class IntervalFlat extends Table {
	public static final String NAME = "intervalFlat";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"fid integer NOT NULL," +
			"iid integer NOT NULL," +
			"CONSTRAINT interval_flat_fid_fkey FOREIGN KEY (fid)" +
			"REFERENCES flat(fid) MATCH FULL ON DELETE CASCADE," +
			"CONSTRAINT interval_flat_iid_fkey FOREIGN KEY (iid) " +
			"REFERENCES interval(iid) MATCH FULL ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"fid",		// 0
		"iid"		// 1
	};
	public static final int FID = 0, IID = 1;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public IntervalFlat(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addIntervalFlat(int FlatID, int IntervalID){
		return this.insert(new int[]{FID,IID}, new String[]{""+FlatID,""+IntervalID});
	}
	
	public int[] getData(int IntervalID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "iid = ?", new String[]{""+IntervalID}, null, null, null);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
}
