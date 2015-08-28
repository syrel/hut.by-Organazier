package by.hut.flat.calendar.core.DBAdapter.tables;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class Prepay extends Table {
	public static final String NAME = "prepay";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"mid integer NOT NULL UNIQUE," +
			"CONSTRAINT prepay_fkey FOREIGN KEY (mid)" +
			"REFERENCES money(mid) MATCH FULL ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"mid"			// 0
	};
	public static final int MID = 0, MONEY_PREPAY = 1, INTERVAL_MONEY_IID = 2;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Prepay(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}

	public long addPrepay(int IntervalID, int prepay){
		Money moneyDB = new Money(db);
		Cache cacheDB = new Cache(db);
		long mid = moneyDB.addMoney(IntervalID,prepay);
		this.insert(new int[]{MID}, new String[]{""+mid});
		cacheDB.setPrepay(IntervalID);
		return mid;
	}
	
	protected void removePrepay(int MoneyID){
		Cache cacheDB = new Cache(db);
		
		int[] prepayData = getData(MoneyID);
		if (prepayData.length > 0){
			this.removeByID(MID, MoneyID);
			cacheDB.removePrepay(prepayData[INTERVAL_MONEY_IID]);
		}
	}
	
	private int[] getData(int MoneyID) {
		db.open();
		String query = "SELECT p.*,m."+Money.DB[Money.AMOUNT]+",im.iid FROM "+NAME+" p INNER JOIN "+Money.NAME+" m,"+IntervalMoney.NAME+" im ON p.mid=m.mid AND p.mid=im.mid WHERE p.mid = ?";
		int[] result = Utils.getRow(getResultInt(db.getDB().rawQuery(query, new String[]{""+MoneyID})),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getData(){
		db.open();
		String query = "SELECT p.*,m."+Money.DB[Money.AMOUNT]+",im.iid FROM "+NAME+" p INNER JOIN "+Money.NAME+" m,"+Interval.NAME+" i, "+IntervalMoney.NAME+" im ON p.mid=m.mid AND p.mid=im.mid AND im.iid=i.iid ORDER BY i."+Interval.DB[Interval.DATE_FROM] + " DESC";
		int[][] result = getResultInt(db.getDB().rawQuery(query, null));
		if(!initOpen)db.close();
		return result;
	}
	
	public void updatePrepay(int MoneyID, int prepay){
		Money moneyDB = new Money(db);
		Cache cacheDB = new Cache(db);
		IntervalMoney intervalMoneyDB = new IntervalMoney(db);
		int IntervalID = intervalMoneyDB.getDataByMoneyID(MoneyID)[IntervalMoney.IID];
		moneyDB.updateAmount(MoneyID, prepay);
		cacheDB.setPrepay(IntervalID);
	}
	
	/**
	 * Use MONEY_PREPAY to get cost after sql join
	 * @param IntervalID
	 * @return
	 */
	public int[][] getPrepaysByIntervalID(int IntervalID){
		db.open();
		String query = "SELECT p.*,m.amount FROM "+NAME+" p INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" i ON p.mid = m.mid AND i.mid = m.mid WHERE i.iid = ?;";
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+IntervalID}));
		if(!initOpen)db.close();
		if (result.length == 0) return null;
		return result;
	}
	
	public int[][] getPrepaysByDate(Date date){
		db.open();
		String query = "SELECT p.*,m."+Money.DB[Money.AMOUNT]+" FROM "+Money.NAME+" m INNER JOIN "+Prepay.NAME+" p, "+Interval.NAME+" i, "+IntervalMoney.NAME+" im ON p.mid=m.mid AND p.mid=im.mid AND im.iid=i.iid WHERE i."+Interval.DB[Interval.DATE_FROM]+" <= ? AND i."+Interval.DB[Interval.DATE_TO]+" >= ? ORDER BY m."+Money.DB[Money.TIMESTAMP];
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt()}));
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getPrepaysByDateWithoutInterval(int IntervalID, Date date){
		db.open();
		String query = "SELECT p.*,m."+Money.DB[Money.AMOUNT]+" FROM "+Money.NAME+" m INNER JOIN "+Prepay.NAME+" p, "+Interval.NAME+" i, "+IntervalMoney.NAME+" im ON p.mid=m.mid AND p.mid=im.mid AND im.iid=i.iid WHERE i."+Interval.DB[Interval.DATE_FROM]+" <= ? AND i."+Interval.DB[Interval.DATE_TO]+" >= ? AND i."+Interval.DB[Interval.IID]+" != ? ORDER BY m."+Money.DB[Money.TIMESTAMP]+" DESC";
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt(),""+IntervalID}));
		if(!initOpen)db.close();
		return result;
	}
}
