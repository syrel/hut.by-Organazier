package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.utils.Print;

public class Cleanings extends Table{
	public static final String NAME = "cleanings";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"cid integer," +
			"iid integer," +
			"eid integer NOT NULL," +
			"fid integer NOT NULL," +
			"date integer NOT NULL," +
			"CONSTRAINT interval_fkey FOREIGN KEY (iid)" +
			"REFERENCES interval(iid) MATCH FULL ON DELETE SET NULL," +
			"CONSTRAINT employee_fkey FOREIGN KEY (eid)" +
			"REFERENCES employee(eid) MATCH FULL ON DELETE CASCADE," +
			"CONSTRAINT flat_fkey FOREIGN KEY (fid)" +
			"REFERENCES flat(fid) MATCH FULL ON DELETE CASCADE," +
			"CONSTRAINT cleanings_pkey PRIMARY KEY (cid)" +
		");";
	public static final String[] DB = {
		"cid",			// 0
		"iid",			// 1
		"eid",			// 2
		"fid",			// 3
		"date"			// 4
	};
	public static final int CID = 0,IID = 1, EID = 2, FID = 3,DATE = 4, MAIDEN_BACKGROUND = 5;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Cleanings(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addCleaning(int IntervalID,int FlatID,int EmployeeID, Date date) {
		db.open();
		Cache dbCache = new Cache(this.getDB());
		long CleaningID = this.insert(new int[]{IID,EID,FID,DATE}, new String[]{(IntervalID != -1 )? ""+IntervalID : "NULL",""+EmployeeID,""+FlatID,""+date.toInt()});
		dbCache.setBackground((int)CleaningID);
		if(!initOpen)db.close();
		return CleaningID;
	}
	
	public int[] getData(int CleaningID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "cid = ?", new String[]{""+CleaningID}, null, null, null);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	/**
	 * Extended output with background. Please use MAIDEN_BACKGROUND to get neccessary data.
	 * This method returns data for ids without param CleaningIDs.
	 * @param CleaningIDs
	 * @return
	 */
	public int[][] getDataInDateByID(int[] CleaningIDs){
		db.open();
		String query = "SELECT c.*,m."+Maiden.DB[Maiden.BACKGROUND]+" FROM "+NAME+" c INNER JOIN "+Maiden.NAME+" m ON c.eid=m.eid WHERE c.date IN (SELECT date from "+NAME+" WHERE cid IN ("+Print.toString(CleaningIDs, ',')+")) AND c.cid NOT IN ("+Print.toString(CleaningIDs, ',')+") ORDER BY c.date;";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getDataByDate(int FlatID, Date date){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "fid = ? AND date = ?", new String[]{""+FlatID,""+date.toInt()}, null, null, null);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getDataByDateIntervalID(int IntervalID,Date date){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "iid = ? AND date = ?", new String[]{""+IntervalID,""+date.toInt()}, null, null, null);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
		
	public int getCleaningsNum(int MaidenID,Date today){
		db.open();
		String query = "SELECT COUNT(*) FROM "+NAME+" WHERE eid = ? AND date <= ?";
		int result = Utils.getRow(getResultInt(db.getDB().rawQuery(query, new String[]{""+MaidenID,""+today.toInt()})),0)[0];
		if(!initOpen)db.close();
		return result;
	}
	
	public int getCleaningsNum(int MaidenID, Date dateFrom,Date dateTo, Date dateToday){
		db.open();
		String query = "SELECT COUNT(*) FROM "+NAME+" WHERE date >= ? AND date <= ? and date <= ? AND eid = ?";
		int result = Utils.getRow(getResultInt(db.getDB().rawQuery(query, new String[]{""+dateFrom.toInt(),""+dateTo.toInt(),""+dateToday.toInt(),""+MaidenID})),0)[0];
		if(!initOpen)db.close();
		return result;
	}
	
	public void nullIID(int IntervalID){
		assert IntervalID > 0;
		db.open();
		String query = "UPDATE " + NAME + " SET iid = NULL WHERE iid = ?";
		db.getDB().execSQL(query, new String[]{""+IntervalID});
		if(!initOpen)db.close();
	}
	
	/**
	 * removes cleanings and sets background in cache to 0
	 * @param CleaningIDs
	 */
	public void remove(int[] CleaningIDs){
		db.open();
		Cache cacheDB = new Cache(db);
		for (int i = 0,length = CleaningIDs.length;i < length; i++){
			cacheDB.removeCleaning(CleaningIDs[i]);
			this.removeByID(CID, CleaningIDs[i]);
		}
		
		if(!initOpen)db.close();
	}
	
	public int[] findCleanings(int IntervalID){
		assert IntervalID > 0;
		db.open();
		Cursor cursor = db.getDB().query(NAME, new String[]{DB[CID]},DB[IID] + " = ?",new String[]{""+IntervalID}, null, null, DB[CID] + " DESC");
		int[] result = Utils.getRow(Utils.transpose(getResultInt(cursor)),0);
		if(!initOpen)db.close();
		return result;
	}
	public int[][] findCleaningsData(int IntervalID){
		assert IntervalID > 0;
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB,DB[IID] + " = ?",new String[]{""+IntervalID}, null, null, DB[CID] + " DESC");
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] findAllCleaningsData(int IntervalID){
		assert IntervalID > 0;
		db.open();
		Interval intervalDB = new Interval(db);
		int[] iData = intervalDB.getData(IntervalID);
		Cursor cursor = db.getDB().query(NAME, DB,DB[DATE] + " BETWEEN ? AND ?",new String[]{""+iData[Interval.DATE_FROM],""+iData[Interval.DATE_TO]}, null, null, DB[DATE]);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getAnotherCleaningsWithDifferentBackgroundsByInterval(int IntervalID){
		assert IntervalID > 0;
		db.open();
		String query = "SELECT c.*,m.background FROM cleanings c INNER JOIN intervalFlat if,maiden m "+
				"ON c.fid=if.fid AND m.eid=c.eid WHERE c.date IN (SELECT c.date FROM cleanings c WHERE c.iid = ?)" +
				"AND if.iid=? AND c.iid != if.iid GROUP BY c.date,m.background ORDER BY c.date,m.background;";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+IntervalID,""+IntervalID});
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] findCleaningsThatWillNotGetInNewInterval(int IntervalID, Date dateFrom,Date dateTo){
		assert IntervalID > 0;
		db.open();
		String query = "SELECT c.* FROM cleanings c INNER JOIN interval i,intervalFlat if ON i.iid=c.iid AND if.iid=i.iid WHERE c.iid=? AND (c.date < ? OR c.date > ?) AND c.fid=if.fid";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+IntervalID,""+dateFrom.toInt(),""+dateTo.toInt()});
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
}
