package by.hut.flat.calendar.core;

import java.util.ArrayList;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cache;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.utils.AssociativeList;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.graphics.Color;

public class DBParser {
	protected final static int[] textColor = {
		Color.BLACK,0xFF2bc0ff,0xFFb06e2c,0xFF00d100,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFF2bc0ff,0xFFb06e2c,0xFFb06e2c,0xFF00d100,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,Color.RED,0xFFb06e2c,0xFFb06e2c,Color.RED,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,Color.RED,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c,0xFFb06e2c
	};
	protected final static int[] bgColor = {
		Color.WHITE,0xFFe0feff,0xFFbafeff,0xFFe2ffdb,0xFFc7ffc9,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe0e7,0xFFffcfcf,0xFFffcfcf,0xFFffe0e7,0xFFffcfcf,0xFFffcfcf,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFe0feff,0xFFbafeff,0xFFbafeff,0xFFe2ffdb,0xFFc7ffc9,0xFFc7ffc9,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe0e7,0xFFffcfcf,0xFF58c951,0xFF5ac761,0xFFffe0e7,0xFFffe0e7,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFbafeff,0xFFbafeff,0xFFbafeff,0xFFc7ffc9,0xFFc7ffc9,0xFFc7ffc9,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe0e7,0xFFffe0e7,0xFFffe0e7,0xFFffe0e7,0xFFffe0e7,0xFFffe0e7,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffcfcf,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba,0xFFffe3ba
	};
	
	
	protected int[] daysOfWeek;

	private int flatNum;
	private int initDaysNum;
	
	private final DBAdapter db;
	private final Date startDate;
	private final Date finishDate;

	private int[] fIDs;
	private String[][] flatData;
	private final AssociativeList<ArrayList<String[]>> cachePerFlat;
	protected AssociativeList<Day[]> days;
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public DBParser(DBAdapter db, Date startDate,Date finishDate) {
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.initDaysNum = this.startDate.daysUntil(this.finishDate)+1;
		this.db = db;
		this.cachePerFlat = new AssociativeList<ArrayList<String[]>>();
		this.days = new AssociativeList<Day[]>();
		parseDB();
	}
	
	public DBParser(DBAdapter db, int FlatID,Date startDate,Date finishDate) {
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.initDaysNum = this.startDate.daysUntil(this.finishDate)+1;
		this.db = db;
		this.cachePerFlat = new AssociativeList<ArrayList<String[]>>();
		this.days = new AssociativeList<Day[]>();
		parseDB(FlatID);
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	public void parseDB(){
		Cache cache = new Cache(db);
		Flat flat = new Flat(db);
		String[][] cData = cache.readCache(startDate, finishDate);
		fIDs = flat.getFlatIDs(true);
		flatData = flat.getDataOrderByPosition(true);
		flatNum = fIDs.length;
		for (int i = 0,length = fIDs.length; i < length; i++){
			assert !cachePerFlat.containsKey(fIDs[i]);
			assert !days.containsKey(fIDs[i]);
			cachePerFlat.add(new ArrayList<String[]>(), fIDs[i]);
			days.add(new Day[initDaysNum], fIDs[i]);
		}
		for (int i = 0,length = cData.length; i < length; i++){
			cachePerFlat.get(Utils.Int(cData[i][Cache.FID])).add(cData[i]);
		}
		initDays();
	}
	
	public void parseDB(int FlatID){
		Cache cache = new Cache(db);
		String[][] cData = cache.readCache(FlatID,startDate, finishDate);
		fIDs = new int[]{FlatID};
		flatNum = fIDs.length;
		for (int i = 0,length = fIDs.length; i < length; i++){
			assert !cachePerFlat.containsKey(fIDs[i]);
			assert !days.containsKey(fIDs[i]);
			cachePerFlat.add(new ArrayList<String[]>(), fIDs[i]);
			days.add(new Day[initDaysNum], fIDs[i]);
		}
		for (int i = 0,length = cData.length; i < length; i++){
			cachePerFlat.get(Utils.Int(cData[i][Cache.FID])).add(cData[i]);
		}
		initDays();
	}
	
	public void reInitDB(int FlatID,Date startDate,Date finishDate){
		Cache cache = new Cache(db);
		String[][] cData = cache.readCache(FlatID,startDate, finishDate);
		assert cachePerFlat.containsKey(FlatID);
		assert days.containsKey(FlatID);
		cachePerFlat.get(FlatID).clear();
		for (int i = 0,length = cData.length; i < length; i++){
			cachePerFlat.get(FlatID).add(cData[i]);
		}
		initDays(FlatID, startDate,finishDate);
	}
	
	public int[] reParseDB(int FlatID, Date dateFrom, Date dateTo){
		assert cachePerFlat.containsKey(FlatID);
		assert days.containsKey(FlatID);
		if (days.get(FlatID).length < 1) return null;
		if (days.get(FlatID)[0].date.compareTo(dateTo) == 1) return null;
		if (days.get(FlatID)[days.get(FlatID).length-1].date.compareTo(dateFrom) == -1) return null;
		if (days.get(FlatID)[0].date.compareTo(dateFrom) == 1) dateFrom = days.get(FlatID)[0].date.copy();
		if ((days.get(FlatID)[days.get(FlatID).length-1]).date.compareTo(dateTo) == -1) dateTo = (days.get(FlatID)[days.get(FlatID).length-1]).date.copy();
		
		Cache cache = new Cache(db);
		String[][] cData = cache.readCache(FlatID,dateFrom, dateTo);
		
		int startIndex = days.get(FlatID)[0].date.daysUntil(dateFrom);
		int finishIndex = startIndex + dateFrom.daysUntil(dateTo);
		assert startIndex >= 0;
		assert finishIndex >= 0;
		int[] resultIndex = new int[]{startIndex,finishIndex};
		
		Date date = dateFrom.copy();
		int cursor = 0;
		Date cacheDate = null;
		for (int cellID = startIndex; cellID <= finishIndex; cellID++){
			if (cData.length == 0){
				cursor = -1;
			}
			if (cursor != -1){
				if (cacheDate == null){ /* create initial date */
					cacheDate = new Date(Utils.Int(cData[cursor][Cache.DATE]));
				}
				if (cacheDate.equals(date)){ /* create day from cache */
					int CacheID = Utils.Int(cData[cursor][Cache.CID]);
					reCreateCacheDay(date, cData,cursor, cellID, FlatID,CacheID);
					cursor++;
					if (cData.length == cursor){
						cursor = -1;
					}
					else {
						cacheDate = new Date(Utils.Int(cData[cursor][Cache.DATE]));
					}
				}
				else {	/* create clear day */
					createClearDay(date, cellID, FlatID);
				}
			}
			else {	/* create clear day */
				createClearDay(date, cellID, FlatID);
			}
			date.next();
		}
		return resultIndex;
	}
	
	private void initDays(){
		assert days.getSize() == this.flatNum;
		CalendarHelper calendar = new CalendarHelper();
		Date date = startDate.copy();
		int[] cursor = new int[flatNum];
		Date[] cacheDates = new Date[flatNum];
		daysOfWeek = new int[initDaysNum];
		for (int cellID = 0; cellID < initDaysNum; cellID++){
			calendar.setDate(date);
			daysOfWeek[cellID] = calendar.dayOfWeek;
			for (int flat = 0; flat < flatNum; flat++){
				if (cachePerFlat.get(fIDs[flat]).size() == 0){
					cursor[flat] = -1;
				}
				
				if (cursor[flat] != -1){
				
					if (cacheDates[flat] == null){ /* create initial date */
						cacheDates[flat] = new Date(Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.DATE]));
					}
					if (cacheDates[flat].equals(date)){ /* create day from cache */
						int CacheID = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.CID]);
						createCacheDay(date, cursor, cellID, flat,CacheID);
						cursor[flat]++;
						if (cachePerFlat.get(fIDs[flat]).size() == cursor[flat]){
							cursor[flat] = -1;
						}
						else {
							cacheDates[flat] = new Date(Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.DATE]));
						}
					}
					else {	/* create clear day */
						createClearDay(date, cellID, fIDs[flat]);
					}
				}
				else {	/* create clear day */
					createClearDay(date, cellID, fIDs[flat]);
				}
			}
			date.next();
		}
	}
	
	private void initDays(int FlatID, Date startDate,Date finishDate){
		CalendarHelper calendar = new CalendarHelper();
		Date date = startDate.copy();
		int cursor = 0;
		int initDaysNum = startDate.daysUntil(finishDate)+1;
		Date cacheDate = null;
		int[] daysOfWeek = new int[initDaysNum];
		for (int cellID = 0; cellID < initDaysNum; cellID++){
			calendar.setDate(date);
			daysOfWeek[cellID] = calendar.dayOfWeek;
			if (cachePerFlat.get(FlatID).size() == 0){
				cursor = -1;
			}
			if (cursor != -1){
				if (cacheDate == null){ /* create initial date */
					cacheDate = new Date(Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.DATE]));
				}
				if (cacheDate.equals(date)){ /* create day from cache */
					int CacheID = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.CID]);
					createCacheDay(date, daysOfWeek,initDaysNum,cursor, cellID, FlatID,CacheID);
					cursor++;
					if (cachePerFlat.get(FlatID).size() == cursor){
						cursor = -1;
					}
					else {
						cacheDate = new Date(Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.DATE]));
					}
				}
				else {	/* create clear day */
					createClearDay(date, cellID, FlatID);
				}
			}
			else {	/* create clear day */
				createClearDay(date, cellID, FlatID);
			}
			date.next();
		}
	}
	
	private void createCacheDay(Date date, int[] cursor, int cellID, int flat,int CacheID) {
		int state = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.STATE]);
		int background = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.BACKGROUND]);
		int prepay = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.PREPAY]);
		int debt = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.DEBT]);
		int casus = Utils.Int(cachePerFlat.get(fIDs[flat]).get(cursor[flat])[Cache.CASUS]);
		days.get(fIDs[flat])[cellID] = new Day(
				cellID,									// cell id
				fIDs[flat],								// flat id
				date.copy(),							// date
				daysOfWeek[cellID],						// day of week
				state,									// state
				background,								// background
				prepay,									// prepay
				debt,									// debt
				casus,									// casus
				getTextColor(state),					// text color
				getBgColor(state),						// bg color
				(cellID == this.initDaysNum-1)			// if day is last
		).setCacheDay().setCacheID(CacheID);
	}
	
	private void createCacheDay(Date date, int[] daysOfWeek, int initDaysNum, int cursor, int cellID, int FlatID,int CacheID) {
		int state = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.STATE]);
		int background = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.BACKGROUND]);
		int prepay = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.PREPAY]);
		int debt = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.DEBT]);
		int casus = Utils.Int(cachePerFlat.get(FlatID).get(cursor)[Cache.CASUS]);
		days.get(FlatID)[cellID] = new Day(
				cellID,									// cell id
				FlatID,								// flat id
				date.copy(),							// date
				daysOfWeek[cellID],						// day of week
				state,									// state
				background,								// background
				prepay,									// prepay
				debt,									// debt
				casus,									// casus
				getTextColor(state),					// text color
				getBgColor(state),						// bg color
				(cellID == initDaysNum-1)			// if day is last
		).setCacheDay().setCacheID(CacheID);
	}
	
	private void createClearDay(Date date, int cellID, int FlatID) {
		days.get(FlatID)[cellID] = new Day(
				cellID,								// cell id
				FlatID,								// flat id
				date.copy(),						// date
				daysOfWeek[cellID],					// day of week
				0,									// state
				0,									// background
				0,									// prepay
				0,									// debt
				0,									// casus
				getTextColor(0),					// text color
				getBgColor(0),						// bg color
				(cellID == this.initDaysNum-1)		// if day is last
		);
	}
	
	private void reCreateCacheDay(Date date, String[][] cData,int cursor, int cellID, int FlatID,int CacheID) {
		int state = Utils.Int(cData[cursor][Cache.STATE]);
		int background = Utils.Int(cData[cursor][Cache.BACKGROUND]);
		int prepay = Utils.Int(cData[cursor][Cache.PREPAY]);
		int debt = Utils.Int(cData[cursor][Cache.DEBT]);
		int casus = Utils.Int(cData[cursor][Cache.CASUS]);
		days.get(FlatID)[cellID] = new Day(
				cellID,									// cell id
				FlatID,									// flat id
				date.copy(),							// date
				daysOfWeek[cellID],						// day of week
				state,									// state
				background,								// background
				prepay,									// prepay
				debt,									// debt
				casus,									// casus
				getTextColor(state),					// text color
				getBgColor(state),						// bg color
				(cellID == this.initDaysNum-1)			// if day is last
		).setCacheDay().setCacheID(CacheID);
	}
	
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	
	public int getFlatNum(){
		return this.flatNum;
	}

	public Day getDay(int FlatID,int index) {
		assert days.containsKey(FlatID);
		assert index >=0 && index < this.initDaysNum;
		return days.get(FlatID)[index];
	}
	
	public int getFlatID(int index){
		assert index >=0 && index < this.fIDs.length;
		return this.fIDs[index];
	}
	
	public String getFlatShortAddress(int index){
		assert index >=0 && index < this.fIDs.length;
		return this.flatData[index][Flat.SHORT_ADDRESS];
	}
	
	public boolean getFlatUseShortAddress(int index){
		assert index >=0 && index < this.fIDs.length;
		return Utils.Int(this.flatData[index][Flat.USE_SHORT_ADDRESS_SAUSAGE]) == 1;
	}
	
	public int getIndex(int FlatID){
		assert FlatID > 0;
		int index = -1;
		for (int i = 0, length = this.fIDs.length; i < length; i++){
			if (fIDs[i]==FlatID){
				index = i;
				return index;
			}
		}
		assert index >= 0;
		return index;
	}
	
	protected int getTextColor(int state){
	if (state == -1)return Color.LTGRAY;
		if (state >= textColor.length) return Color.LTGRAY;
		return textColor[state];
	}
	protected int getBgColor(int state){
		if (state == -1)return Color.WHITE;
		if (state >= bgColor.length)return Color.WHITE;
		return bgColor[state];
	}
	
	public DBAdapter getDB(){
		assert db != null;
		return db;
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	public static String Str(int integer){
		return ""+integer;
	}
	public void alertL(String str){ 
		android.util.Log.d("DBParser", str);	
	}
}
