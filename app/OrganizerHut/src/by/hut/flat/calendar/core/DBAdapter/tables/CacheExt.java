package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class CacheExt extends Table{
	public static final String NAME = "calendarCacheExt";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"cid integer," +
			"book_am integer NOT NULL DEFAULT '0'," +
			"book_pm integer NOT NULL DEFAULT '0'," +
			"rent_am integer NOT NULL DEFAULT '0'," +
			"rent_pm integer NOT NULL DEFAULT '0'," +
			"book_settlement integer NOT NULL DEFAULT '0'," +
			"book_eviction integer NOT NULL DEFAULT '0'," +
			"rent_settlement integer NOT NULL DEFAULT '0'," +
			"rent_eviction integer NOT NULL DEFAULT '0'," +
			"CONSTRAINT calendar_cache_ext_fkey FOREIGN KEY (cid) " +
			"REFERENCES calendarCache(cid)  MATCH FULL " +
			"ON UPDATE CASCADE ON DELETE CASCADE" +
		");";
	
	public static final String[] DB = {
		"cid",					// 0
		"book_am",				// 1
		"book_pm",				// 2
		"rent_am",				// 3
		"rent_pm",				// 4
		"book_settlement",		// 5
		"book_eviction",		// 6
		"rent_settlement",		// 7
		"rent_eviction"			// 8
	};
	
	public static final int CID = 0, BOOK_AM = 1, BOOK_PM = 2, RENT_AM = 3, RENT_PM = 4, BOOK_SETTLEMENT = 5, BOOK_EVICTION = 6,
			RENT_SETTLEMENT = 7, RENT_EVICTION = 8;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public CacheExt(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	protected void addCacheExt(int CacheID, int bookAM,int bookPM, int rentAM, int rentPM, int bookSettlement,int bookEviction,int rentSettlement,int rentEviction){
		this.insert(new int[]{CID,BOOK_AM,BOOK_PM,RENT_AM,RENT_PM,BOOK_SETTLEMENT,BOOK_EVICTION,RENT_SETTLEMENT,RENT_EVICTION},
				new String[]{""+CacheID,""+bookAM,""+bookPM,""+rentAM,""+rentPM,""+bookSettlement,""+bookEviction,""+rentSettlement,""+rentEviction});
	}
	
	protected void updateCacheExt(int CacheID, int bookAM,int bookPM, int rentAM, int rentPM, int bookSettlement,int bookEviction,int rentSettlement,int rentEviction){
		this.updateByID(CID, CacheID, new int[]{BOOK_AM,BOOK_PM,RENT_AM,RENT_PM,BOOK_SETTLEMENT,BOOK_EVICTION,RENT_SETTLEMENT,RENT_EVICTION},
				new String[]{""+bookAM,""+bookPM,""+rentAM,""+rentPM,""+bookSettlement,""+bookEviction,""+rentSettlement,""+rentEviction});
	}
	
	protected int[] getData(int CacheID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "cid = ?", new String[]{""+CacheID}, null, null, null);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
}
