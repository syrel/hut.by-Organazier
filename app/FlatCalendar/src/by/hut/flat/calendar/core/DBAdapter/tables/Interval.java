package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class Interval extends Table {
	public static final String NAME = "interval";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"iid integer," +
			"date_from integer NOT NULL," +
			"date_to integer NOT NULL," +
			"CONSTRAINT interval_pkey PRIMARY KEY (iid)" +
		");";
	public static final String[] DB = {
		"iid",			// 0
		"date_from",	// 1
		"date_to"		// 2
	};
	public static final int IID = 0, DATE_FROM = 1, DATE_TO = 2;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Interval(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public int[] getData(int IntervalID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "iid = ?", new String[]{""+IntervalID}, null, null, null);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public long addInterval(int FlatID,int type,Date dateFrom, Date dateTo){
		long iid = this.insert(new int[]{DATE_FROM,DATE_TO}, new String[]{""+dateFrom.toInt(),""+dateTo.toInt()});
		IntervalFlat dbIF = new IntervalFlat(db);
		IntervalAnketa dbIA = new IntervalAnketa(db);
		Payment dbP = new Payment(db);
		Cache dbCache = new Cache(db);
		dbIF.addIntervalFlat(FlatID, (int)iid);
		dbIA.addIntervalAnketa((int)iid, type);
		dbP.addPayment((int)iid,0, 0);
		dbCache.setDayState(FlatID, type, dateFrom, dateTo);
		return iid;
	}
	
	public void removeInterval(int IntervalID){
		IntervalFlat dbIF = new IntervalFlat(db);
		IntervalAnketa dbIA = new IntervalAnketa(db);
		IntervalMoney dbIM = new IntervalMoney(db);
		
		Cache dbCache = new Cache(db);
		Cleanings dbCleanings = new Cleanings(db);
		int[] iData = this.getData(IntervalID);
		int FlatID = dbIF.getData(IntervalID)[IntervalFlat.FID];
		int state = Utils.Int(dbIA.getData(IntervalID)[IntervalAnketa.TYPE]);
		Date dateFrom = new Date(iData[Interval.DATE_FROM]);
		Date dateTo = new Date(iData[Interval.DATE_TO]);
		dbCache.removeDayState(FlatID, state, dateFrom, dateTo);
		dbIM.removeMoneyByIntervalID(IntervalID);
		this.removeByID(IID, IntervalID);
		dbIF.removeByID(IntervalFlat.IID, IntervalID);
		dbIA.removeByID(IntervalAnketa.IID, IntervalID);
		dbCleanings.nullIID(IntervalID);
	}
	
	public int[] findIntervals(int FlatID, Date date){
		assert FlatID > 0;
		db.open();
		String query = "SELECT DISTINCT i.iid FROM "+NAME+" i INNER JOIN "+IntervalFlat.NAME+" if ON i.iid=if.iid WHERE if.fid = ? AND i.date_from <= ? AND i.date_to >= ? ORDER BY i.iid DESC;";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+FlatID,""+date.toInt(),""+date.toInt()});
		int[] result = Utils.getRow(Utils.transpose(getResultInt(cursor)),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] findNotConvertedIntervals(Date date){
		db.open();
		String query = "SELECT i.* FROM interval i INNER JOIN intervalAnketa ia ON ia.iid=i.iid WHERE i.date_from < ? AND ia.type = ?";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+dbStructure.BOOK});
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public void setNewDates(int IntervalID, Date dateFrom, Date dateTo){
		this.updateByID(IID, IntervalID, new int[]{DATE_FROM,DATE_TO}, new String[]{""+dateFrom.toInt(),""+dateTo.toInt()});
	}
	
	public Date getFirstDate(){
		db.open();
		String query = "SELECT date_from FROM interval ORDER BY date_from LIMIT 1";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		if (result.length == 0 || result[0] <= 0) return new Date();
		return new Date(result[0]);
	}
}
