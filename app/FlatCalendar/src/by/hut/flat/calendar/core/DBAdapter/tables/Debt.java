package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class Debt extends Table {
	public static final String NAME = "debt";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"mid integer NOT NULL UNIQUE," +
			"CONSTRAINT debt_fkey FOREIGN KEY (mid)" +
			"REFERENCES money(mid) MATCH FULL ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"mid"			// 0
	};
	public static final int MID = 0, MONEY_DEBT = 1, INTERVAL_IID = 2;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Debt(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addDebt(int IntervalID, int debt) {
		Money moneyDB = new Money(db);
		Cache cacheDB = new Cache(db);
		long mid = moneyDB.addMoney(IntervalID,debt);
		this.insert(new int[]{MID}, new String[]{""+mid});
		cacheDB.setDebt(IntervalID);
		return mid;
	}

	protected void removeDebt(int MoneyID) {
		Cache cacheDB = new Cache(db);
		
		int[] debtData = getData(MoneyID);
		if (debtData.length > 0){
			this.removeByID(MID, MoneyID);
			cacheDB.removeDebt(debtData[INTERVAL_IID]);
		}
	}

	private int[] getData(int MoneyID) {
		db.open();
		String query = "SELECT d.*,m."+Money.DB[Money.AMOUNT]+",im.iid FROM "+NAME+" d INNER JOIN "+Money.NAME+" m,"+IntervalMoney.NAME+" im ON d.mid=m.mid AND d.mid=im.mid WHERE d.mid = ?";
		int[] result = Utils.getRow(getResultInt(db.getDB().rawQuery(query, new String[]{""+MoneyID})),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public void updateDebt(int MoneyID, int debt) {
		Money moneyDB = new Money(db);
		Cache cacheDB = new Cache(db);
		IntervalMoney intervalMoneyDB = new IntervalMoney(db);
		int IntervalID = intervalMoneyDB.getDataByMoneyID(MoneyID)[IntervalMoney.IID];
		moneyDB.updateAmount(MoneyID, debt);
		cacheDB.setDebt(IntervalID);
	}
	
	/**
	 * Use MONEY_DEBT to get cost after sql join
	 * @param IntervalID
	 * @return
	 */
	public int[][] getDebtsByIntervalID(int IntervalID) {
		db.open();
		String query = "SELECT d.*,m.amount  FROM "+NAME+" d INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" i ON d.mid = m.mid AND i.mid = m.mid WHERE i.iid = ?;";
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+IntervalID}));
		if(!initOpen)db.close();
		if (result.length == 0) return null;
		return result;
	}
	
	public int[][] getDebtsByDate(Date date) {
		db.open();
		String query = "SELECT d.*,m."+Money.DB[Money.AMOUNT]+" FROM "+Money.NAME+" m INNER JOIN "+Debt.NAME+" d, "+Interval.NAME+" i, "+IntervalMoney.NAME+" im ON d.mid=m.mid AND d.mid=im.mid AND im.iid=i.iid WHERE i."+Interval.DB[Interval.DATE_FROM]+" <= ? AND i."+Interval.DB[Interval.DATE_TO]+" >= ? ORDER BY m."+Money.DB[Money.TIMESTAMP];
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt()}));
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getDebtsByDateWithoutInterval(int IntervalID, Date date) {
		db.open();
		String query = "SELECT d.*,m."+Money.DB[Money.AMOUNT]+" FROM "+Money.NAME+" m INNER JOIN "+Debt.NAME+" d, "+Interval.NAME+" i, "+IntervalMoney.NAME+" im ON d.mid=m.mid AND d.mid=im.mid AND im.iid=i.iid WHERE i."+Interval.DB[Interval.DATE_FROM]+" <= ? AND i."+Interval.DB[Interval.DATE_TO]+" >= ? AND i."+Interval.DB[Interval.IID]+" != ? ORDER BY m."+Money.DB[Money.TIMESTAMP]+" DESC";
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt(),""+IntervalID}));
		if(!initOpen)db.close();
		return result;
	}
	
	public int getDebtsCountBeforeDate(Date date) {
		db.open();
		String query = "SELECT count(*) FROM money m INNER JOIN debt d, interval i, intervalMoney im ON d.mid=m.mid AND m.mid=im.mid AND im.iid=i.iid WHERE i.date_to < ?";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+date.toInt()});
		int result = Utils.getRow(getResultInt(cursor),0)[0];
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getDebts() {
		db.open();
		String query = "SELECT d.*,m.amount,im.iid FROM money m INNER JOIN debt d, interval i, intervalMoney im ON d.mid=m.mid AND m.mid=im.mid AND im.iid=i.iid;";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
}
