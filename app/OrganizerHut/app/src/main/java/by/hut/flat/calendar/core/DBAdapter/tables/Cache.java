package by.hut.flat.calendar.core.DBAdapter.tables;

import android.content.ContentValues;
import android.database.Cursor;
import by.hut.flat.calendar.core.DBParser;
import by.hut.flat.calendar.core.DayState;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Date;

public class Cache extends Table{
	public static final String NAME = "calendarCache";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"cid integer," +
			"date integer NOT NULL," +
			"fid integer NOT NULL," +
			"state integer NOT NULL DEFAULT '0'," +
			"background integer NOT NULL DEFAULT '0'," +
			"prepay integer NOT NULL DEFAULT '0'," +
			"debt integer NOT NULL DEFAULT '0'," +
			"casus integer NOT NULL DEFAULT '0'," +
			"CONSTRAINT calendar_cache_pkey PRIMARY KEY (cid)," +
			"CONSTRAINT calendar_cache_fkey FOREIGN KEY (fid) " +
			"REFERENCES flat(fid)  MATCH FULL " +
			"ON UPDATE CASCADE ON DELETE CASCADE," +
			"CONSTRAINT uc_dateFid UNIQUE (date,fid)" +
		");";
	public static final String[] DB = {
		"cid",			// 0
		"date",			// 1
		"fid",			// 2
		"state",		// 3
		"background",	// 4
		"prepay",		// 5
		"debt",			// 6
		"casus"			// 7
	};
	public static final int CID = 0, DATE = 1, FID = 2, STATE = 3, BACKGROUND = 4, PREPAY = 5, DEBT = 6, CASUS = 7;
	public static final int RENT = 2, BOOK = 3;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Cache(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public String[][] readCache(Date startDate, Date finishDate) {
		db.open();
		String query = "SELECT c.* FROM calendarCache c INNER JOIN flat f ON f.fid=c.fid WHERE f.active = '1' AND c.date BETWEEN ? AND ? ORDER BY f.position, c.date;";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+startDate.toInt(),""+finishDate.toInt()});
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public String[][] readCache(int FlatID, Date startDate, Date finishDate) {
		db.open();
		String query = "SELECT c.* FROM calendarCache c INNER JOIN flat f ON f.fid=c.fid WHERE f.fid = ? AND f.active = '1' AND c.date BETWEEN ? AND ? ORDER BY c.date;";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+FlatID,""+startDate.toInt(),""+finishDate.toInt()});
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	/******************************************************************************************/
    /***************************************setDayState****************************************/
	/******************************************************************************************/
    /*************************************************************/
    /**        Sets new event from date_1 to date_2 in          **/
    /**        flat 'flat_id' with state 'state'                **/
    /*************************************************************/
	/**(warning)  THIS IS THE MAIN METHOD IN PROGRAMM  (warning)**/
	
	/**
	 * 
	 * @param FlatID - {@code int} flat id [1...]
	 * @param date_1 - {@code String} first date. YYYY_mm_dd
	 * @param date_2 - {@code String} last date. YYYY_mm_dd
	 * @param state - {@code int} new state (1 - free, 2 - rent, 3- book)
	 * @throws TableNotExistsException 
	 */
    public void setDayState(int FlatID, int state, Date startDate, Date finishDate){
    	assert FlatID > 0;
    	assert state == BOOK || state == RENT;
    	assert startDate != null;
    	assert finishDate != null;
    	
    	int daysNum = startDate.daysUntil(finishDate)+1;
    	assert daysNum >= 1;
    	
    	DBParser dbParser = new DBParser(this.getDB(),FlatID,startDate,finishDate);
		DayState dState = new DayState();
		State dbState = new State(this.getDB());
		CacheExt dbCacheExt = new CacheExt(this.getDB());
		
		Date tmpDate = startDate.copy();
		
    	for (int index = 0; index < daysNum; index++){
    		if (dbParser.getDay(FlatID, index).clearDay){ /* if day is clean */
    			/* create new day in cache */
    			addClearDay(FlatID, state, daysNum, dState, dbState, dbCacheExt, tmpDate, index);
    		}
    		
    		else { /* if day was in cache */
    			/* update day in cache */
    			updateCacheDayAdd(FlatID, state, daysNum, dbParser, dState,dbState, dbCacheExt, index);
    			
    		}
    		tmpDate.next();
    	}
    }
    
    public void removeDayState(int FlatID,int state, Date startDate, Date finishDate){
    	assert FlatID > 0;
    	assert state == BOOK || state == RENT;
    	assert startDate != null;
    	assert finishDate != null;
    	
    	int daysNum = startDate.daysUntil(finishDate)+1;
    	assert daysNum >= 1;
    	
    	DBParser dbParser = new DBParser(this.getDB(),FlatID,startDate,finishDate);
		DayState dState = new DayState();
		State dbState = new State(this.getDB());
		CacheExt dbCacheExt = new CacheExt(this.getDB());

		for (int index = 0; index < daysNum; index++){
			int CacheID = dbParser.getDay(FlatID, index).CacheID;
			assert CacheID > 0;
			int[] ceData = dbCacheExt.getData(CacheID);
			int bookAM = ceData[CacheExt.BOOK_AM];
			int bookPM = ceData[CacheExt.BOOK_PM];
			int rentAM = ceData[CacheExt.RENT_AM];
			int rentPM = ceData[CacheExt.RENT_PM];
			int bookSettlement = ceData[CacheExt.BOOK_SETTLEMENT];
			int bookEviction = ceData[CacheExt.BOOK_EVICTION];
			int rentSettlement = ceData[CacheExt.RENT_SETTLEMENT];
			int rentEviction = ceData[CacheExt.RENT_EVICTION];
			if (index == 0){
    			if (state == BOOK) {
    				bookPM--;
    				bookSettlement--;
    			}
    			if (state == RENT) {
    				rentPM--;
    				rentSettlement--;
    			}
    		}
    		else if (index == daysNum - 1){
    			if (state == BOOK) {
    				bookAM--;
    				bookEviction--;
    			}
    			if (state == RENT) {
    				rentAM--;
    				rentEviction--;
    			}
    		}
    		else {
    			if (state == BOOK) {
    				bookAM--;
    				bookPM--;
    			}
    			if (state == RENT) {
    				rentAM--;
    				rentPM--;
    			}
    		}
			dbCacheExt.updateCacheExt(CacheID, bookAM, bookPM, rentAM, rentPM, bookSettlement, bookEviction, rentSettlement, rentEviction);
			int DayState = dbState.getNewDayState(dState.getAmPmState(bookAM, rentAM), dState.getAmPmState(bookPM, rentPM), dState.getSettEvictState(bookSettlement+rentSettlement), dState.getSettEvictState(bookEviction+rentEviction));
			this.updateByID(CID, CacheID, new int[]{STATE}, new String[]{""+DayState});
		}
    }

    /**
     * 
     * @param FlatID
     * @param state
     * @param daysNum
     * @param dbParser
     * @param dState
     * @param dbState
     * @param dbCacheExt
     * @param index
     */
	private void updateCacheDayAdd(int FlatID, int state, int daysNum,DBParser dbParser, DayState dState, State dbState,CacheExt dbCacheExt, int index) {
		int CacheID = dbParser.getDay(FlatID, index).CacheID;
		assert CacheID > 0;
		int[] ceData = dbCacheExt.getData(CacheID);
		int bookAM = ceData[CacheExt.BOOK_AM];
		int bookPM = ceData[CacheExt.BOOK_PM];
		int rentAM = ceData[CacheExt.RENT_AM];
		int rentPM = ceData[CacheExt.RENT_PM];
		int bookSettlement = ceData[CacheExt.BOOK_SETTLEMENT];
		int bookEviction = ceData[CacheExt.BOOK_EVICTION];
		int rentSettlement = ceData[CacheExt.RENT_SETTLEMENT];
		int rentEviction = ceData[CacheExt.RENT_EVICTION];
		if (index == 0){
			if (state == BOOK) {
				bookPM++;
				bookSettlement ++;
			}
			if (state == RENT) {
				rentPM++;
				rentSettlement ++;
			}
		}
		else if (index == daysNum - 1){
			if (state == BOOK) {
				bookAM++;
				bookEviction ++;
			}
			if (state == RENT) {
				rentAM ++;
				rentEviction ++;
			}
		}
		else {
			if (state == BOOK) {
				bookAM++;
				bookPM++;
			}
			if (state == RENT) {
				rentAM ++;
				rentPM++;
			}
		}
		dbCacheExt.updateCacheExt(CacheID, bookAM, bookPM, rentAM, rentPM, bookSettlement, bookEviction, rentSettlement, rentEviction);
		
		int DayState = dbState.getNewDayState(dState.getAmPmState(bookAM, rentAM), dState.getAmPmState(bookPM, rentPM), dState.getSettEvictState(bookSettlement+rentSettlement), dState.getSettEvictState(bookEviction+rentEviction));
		this.updateByID(CID, CacheID, new int[]{STATE}, new String[]{""+DayState});
	}

	/**
	 * 
	 * @param FlatID
	 * @param state
	 * @param daysNum
	 * @param dState
	 * @param dbState
	 * @param dbCacheExt
	 * @param tmpDate
	 * @param index
	 * @throws TableNotExistsException
	 */
	private void addClearDay(int FlatID, int state, int daysNum,DayState dState, State dbState, CacheExt dbCacheExt, Date tmpDate,int index){
		int bookAM = 0,bookPM = 0,rentAM = 0,rentPM = 0,bookSettlement = 0,bookEviction = 0,rentSettlement = 0,rentEviction = 0;
		if (index == 0){
			if (state == BOOK) {
				bookPM++;
				bookSettlement ++;
			}
			if (state == RENT) {
				rentPM++;
				rentSettlement ++;
			}
		}
		else if (index == daysNum - 1){
			if (state == BOOK) {
				bookAM++;
				bookEviction ++;
			}
			if (state == RENT) {
				rentAM ++;
				rentEviction ++;
			}
		}
		else {
			if (state == BOOK) {
				bookAM++;
				bookPM++;
			}
			if (state == RENT) {
				rentAM ++;
				rentPM++;
			}
		}
		int DayState = dbState.getNewDayState(dState.getAmPmState(bookAM, rentAM), dState.getAmPmState(bookPM, rentPM), dState.getSettEvictState(bookSettlement+rentSettlement), dState.getSettEvictState(bookEviction+rentEviction));
		long CacheID = this.addCache(tmpDate, FlatID, DayState);
		dbCacheExt.addCacheExt((int)CacheID, bookAM, bookPM, rentAM, rentPM, bookSettlement, bookEviction, rentSettlement, rentEviction);
	}
    
	/**
	 * 
	 * @param date
	 * @param FlatID
	 * @param state
	 * @return
	 * @throws TableNotExistsException
	 */
    private long addCache(Date date, int FlatID,int state){
    	return this.insert(new int[]{DATE,FID,STATE}, new String[]{""+date.toInt(),""+FlatID,""+state});
    }
    
    /**
     * Checks if cache day exists
     * @param FlatID
     * @param date
     * @return
     */
    private boolean isExists(int FlatID, Date date){
    	db.open();
    	Cursor cursor = db.getDB().query(NAME, new String[]{DB[CID]}, DB[FID]+" = ? AND "+DB[DATE]+" = ?", new String[]{""+FlatID,""+date.toInt()}, null, null, null);
    	boolean result = false;
    	if (cursor.getCount() > 0) result = true;
    	if(!initOpen)db.close();
    	cursor.close();
    	return result;
    }
    
    /*------------------------------------------------------------
	--------------------- B A C K G R O U N D --------------------
	------------------------------------------------------------*/
    /**
     * Sets background of cleaning in cache and if not exists create cache day
     * @param CleaningID
     */
    public void setBackground(int CleaningID){
    	db.open();
    	Cleanings cleaningsDB = new Cleanings(db);
    	CacheExt dbCacheExt = new CacheExt(this.getDB());
    	
    	int[] cData = cleaningsDB.getData(CleaningID);
    	int FlatID = cData[Cleanings.FID];
    	Date date = new Date(cData[Cleanings.DATE]);
    	
    	if (!isExists(FlatID, date)){
    		long CacheID = this.addCache(date,FlatID,0);
    		dbCacheExt.addCacheExt((int)CacheID,0,0,0,0,0,0,0,0);
    	}
    	
		String query = "UPDATE calendarCache SET background = (SELECT CASE " +
				"WHEN count(background) > 1 THEN -2 " +
				"WHEN count(background) = 0 THEN 0 " +
				"ELSE background END AS background FROM ((" +
				"SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid " +
				"WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid = ?) " +
				"AND c.fid = (SELECT c.fid FROM cleanings c WHERE c.cid = ?)))) " +
				"WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.cid = ?) " +
				"AND date = (SELECT c.date as date FROM cleanings c WHERE c.cid = ?);";
		String cid = ""+CleaningID;
		db.getDB().execSQL(query, new String[]{cid,cid,cid,cid});
		if(!initOpen)db.close();
    }
    
    /**
     * must be executed before actual remove of cleaning
     * @param CleaningID
     */
    public void removeCleaning(int CleaningID){
    	db.open();
    	String query = "UPDATE calendarCache SET background = (SELECT CASE " +
    			"WHEN count(background) > 1 THEN -2 " +
    			"WHEN count(background) = 0 THEN 0 " +
    			"ELSE background END AS background FROM ((" +
    			"SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid " +
    			"WHERE c.date = (SELECT c.date FROM cleanings c WHERE c.cid = ?) " +
    			"AND c.fid = (SELECT c.fid FROM cleanings c WHERE c.cid = ?) AND c.cid != ?))) " +
    			"WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.cid = ?) " +
    			"AND date = (SELECT c.date as date FROM cleanings c WHERE c.cid = ?);";
    	String cid = ""+CleaningID;
    	db.getDB().execSQL(query, new String[]{cid,cid,cid,cid,cid});
    	if(!initOpen)db.close();
    }
    
    
    /**
     * Sets background at date,flat to 0
     * @param FlatID
     * @param date
     */
    public void removeBackground(int FlatID, Date date){
    	db.open();
    	ContentValues args = new ContentValues();
		args.put(DB[BACKGROUND], 0);
		db.getDB().update(NAME, args, DB[FID]+" = ? AND " + DB[DATE] + " = ?", new String[]{""+FlatID,""+date.toInt()});
		if(!initOpen)db.close();
    }
    
    public void updateBackground(int IntervalID,Date date){
    	db.open();
    	String query = "UPDATE calendarCache SET background = (SELECT CASE " +
    			"WHEN count(background) > 1 THEN -2 " +
    			"WHEN count(background) = 0 THEN 0 " +
    			"ELSE background END AS background FROM ((" +
    			"SELECT background FROM cleanings as c INNER JOIN maiden m ON m.eid=c.eid " +
    			"WHERE c.date = ? AND c.iid = ?))) " +
    			"WHERE fid = (SELECT c.fid as fid FROM cleanings c WHERE c.iid = ? LIMIT 1) " +
    			"AND date = ?;";
    	String iid = ""+IntervalID;
    	db.getDB().execSQL(query, new String[]{""+date.toInt(),iid,iid,""+date.toInt()});
    	if(!initOpen)db.close();
    }
    /*------------------------------------------------------------
	---------------------------- D E B T -------------------------
	------------------------------------------------------------*/
    
    private void updateDebt(int date, int FlatID,int debt){
    	assert debt >= -1;
    	ContentValues args = new ContentValues();
		args.put(DB[DEBT], debt);
		db.getDB().update(NAME, args, DB[FID]+" = ? AND " + DB[DATE] + " = ?", new String[]{""+FlatID,""+date});
    }
    
    /**
     * Removes debt info from cache. Must be run before removing interval
     * @param IntervalID
     */
    public void removeDebt(int IntervalID){
    	db.open();
    	String query = "SELECT l.day as date,l.flat as fid,r.debt as debt " +
    			"FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c " +
    			"INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid = ? AND c.date BETWEEN i.date_from AND i.date_to)  as l " +
    			"LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as debt " +
    			"FROM calendarCache c INNER JOIN intervalMoney im, money m, debt d, intervalFlat if, interval i " +
    			"ON i.iid=im.iid AND im.mid=m.mid AND m.mid=d.mid AND if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid IN (SELECT i.iid FROM interval i," +
    			"(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish " +
    			"FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = ?) date " +
    			"INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) " +
    			"AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid != date.interval) " +
    			"AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = ?) " +
    			"AND date <= (SELECT i.date_to FROM interval i WHERE i.iid = ?) GROUP BY c.date ORDER BY c.date,m.timestamp) as r " +
    			"ON l.day=r.day;";
    	String iid = ""+IntervalID;
    	Cursor cursor = db.getDB().rawQuery(query, new String[]{iid,iid,iid,iid});
    	int[][] newData = getResultInt(cursor);
    	for (int i = 0,length = newData.length; i < length; i++){
    		updateDebt(newData[i][0],newData[i][1],newData[i][2]); //date,fid,debt
    	}
    	if(!initOpen)db.close();
    }
    
    public void setDebt(int IntervalID){
    	db.open();
    	String query = "SELECT l.day as date,l.flat as fid,r.debt as debt " +
    			"FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c " +
    			"INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid = ? AND c.date BETWEEN i.date_from AND i.date_to)  as l " +
    			"LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as debt " +
    			"FROM calendarCache c INNER JOIN intervalMoney im, money m, debt d, intervalFlat if, interval i " +
    			"ON i.iid=im.iid AND im.mid=m.mid AND m.mid=d.mid AND if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid IN (SELECT i.iid FROM interval i," +
    			"(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish " +
    			"FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = ?) date " +
    			"INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) " +
    			"AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat) " +
    			"AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = ?) " +
    			"AND date <= (SELECT i.date_to FROM interval i WHERE i.iid = ?) GROUP BY c.date ORDER BY c.date,m.timestamp) as r " +
    			"ON l.day=r.day;";
    	String iid = ""+IntervalID;
    	Cursor cursor = db.getDB().rawQuery(query, new String[]{iid,iid,iid,iid});
    	int[][] newData = getResultInt(cursor);
    	for (int i = 0,length = newData.length; i < length; i++){
    		updateDebt(newData[i][0],newData[i][1],newData[i][2]); //date,fid,debt
    	}
    	if(!initOpen)db.close();
    }
    
    /*------------------------------------------------------------
	-------------------------- P R E P A Y -----------------------
	------------------------------------------------------------*/
    
    private void updatePrepay(int date, int FlatID,int prepay){
    	assert prepay >= -1;
    	ContentValues args = new ContentValues();
		args.put(DB[PREPAY], prepay);
		db.getDB().update(NAME, args, DB[FID]+" = ? AND " + DB[DATE] + " = ?", new String[]{""+FlatID,""+date});
    }
    
    public void removePrepay(int IntervalID){
    	db.open();
    	String query = "SELECT l.day as date,l.flat as fid,r.prepay as prepay " +
    			"FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c " +
    			"INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid = ? AND c.date BETWEEN i.date_from AND i.date_to)  as l " +
    			"LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as prepay " +
    			"FROM calendarCache c INNER JOIN intervalMoney im, money m, prepay p, intervalFlat if, interval i " +
    			"ON i.iid=im.iid AND im.mid=m.mid AND m.mid=p.mid AND if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid IN (SELECT i.iid FROM interval i," +
    			"(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish " +
    			"FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = ?) date " +
    			"INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) " +
    			"AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat AND i.iid != date.interval) " +
    			"AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = ?) " +
    			"AND date <= (SELECT i.date_to FROM interval i WHERE i.iid = ?) GROUP BY c.date ORDER BY c.date,m.timestamp) as r " +
    			"ON l.day=r.day;";
    	String iid = ""+IntervalID;
    	Cursor cursor = db.getDB().rawQuery(query, new String[]{iid,iid,iid,iid});
    	int[][] newData = getResultInt(cursor);
    	for (int i = 0,length = newData.length; i < length; i++){
    		updatePrepay(newData[i][0],newData[i][1],newData[i][2]); //date,fid,debt
    	}
    	if(!initOpen)db.close();
    }
    
    public void setPrepay(int IntervalID){
    	db.open();
    	String query = "SELECT l.day as date,l.flat as fid,r.prepay as prepay " +
    			"FROM (SELECT if.fid as flat, c.date as day FROM calendarCache c " +
    			"INNER JOIN intervalFlat if, interval i ON if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid = ? AND c.date BETWEEN i.date_from AND i.date_to)  as l " +
    			"LEFT OUTER JOIN (SELECT c.date as day, SUM(m.amount) as prepay " +
    			"FROM calendarCache c INNER JOIN intervalMoney im, money m, prepay p, intervalFlat if, interval i " +
    			"ON i.iid=im.iid AND im.mid=m.mid AND m.mid=p.mid AND if.fid = c.fid AND i.iid = if.iid " +
    			"WHERE i.iid IN (SELECT i.iid FROM interval i," +
    			"(SELECT i.iid as interval,if.fid as flat, i.date_from as start,i.date_to as finish " +
    			"FROM interval i INNER JOIN intervalFlat if ON if.iid=i.iid WHERE i.iid = ?) date " +
    			"INNER JOIN intervalFlat if ON if.iid=i.iid WHERE (start <= i.date_from OR start <= i.date_to) " +
    			"AND (finish >= i.date_from OR finish >= i.date_to) AND if.fid = date.flat) " +
    			"AND c.date BETWEEN i.date_from AND i.date_to AND c.date >= (SELECT i.date_from FROM interval i WHERE i.iid = ?) " +
    			"AND date <= (SELECT i.date_to FROM interval i WHERE i.iid = ?) GROUP BY c.date ORDER BY c.date,m.timestamp) as r " +
    			"ON l.day=r.day;";
    	String iid = ""+IntervalID;
    	Cursor cursor = db.getDB().rawQuery(query, new String[]{iid,iid,iid,iid});
    	int[][] newData = getResultInt(cursor);
    	for (int i = 0,length = newData.length; i < length; i++){
    		updatePrepay(newData[i][0],newData[i][1],newData[i][2]); //date,fid,debt
    	}
    	if(!initOpen)db.close();
    }
    /*------------------------------------------------------------
	------------------- C H A N G E   D A T E S ------------------
	------------------------------------------------------------*/

    
}
