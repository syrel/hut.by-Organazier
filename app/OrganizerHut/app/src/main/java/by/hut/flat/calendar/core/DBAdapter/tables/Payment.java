package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;

import java.util.Arrays;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Print;
import by.hut.flat.calendar.utils.Utils;

public class Payment extends Table{
	public static final String NAME = "payment";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"mid integer NOT NULL UNIQUE," +
			"price integer," +
			"CONSTRAINT payment_fkey FOREIGN KEY (mid)" +
			"REFERENCES money(mid) MATCH FULL ON DELETE CASCADE," +
			"CONSTRAINT payment_check CHECK ((price >= '0' OR price IS NULL))" +
		");";
	public static final String[] DB = {
		"mid",			// 0
		"price"			// 1
	};
	public static final int MID = 0, PRICE = 1, MONEY_SUMM = 2,INTERVAL_IID = 3;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Payment(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addPayment(int IntervalID,int price, int summ){
		Money moneyDB = new Money(db);
		long mid = moneyDB.addMoney(IntervalID,summ);
		this.insert(new int[]{MID,PRICE}, new String[]{""+mid,(price > 0) ? ""+price : "NULL"});
		return mid;
	}
	
	protected void removePayment(int MoneyID){
		this.removeByID(MID, MoneyID);
	}
	
	public void updatePrice(int MoneyID, int price){
		Money moneyDB = new Money(db);
		this.updateByID(MID, MoneyID, new int[]{PRICE}, new String[]{""+price});
		moneyDB.updateByID(Money.MID, MoneyID, new int[]{Money.TIMESTAMP},new String[]{""+new java.util.Date().getTime()});
	}
	
	public void updateSumm(int MoneyID, int summ){
		Money moneyDB = new Money(db);
		moneyDB.updateAmount(MoneyID, summ);
	}
	
	public int[][] getWrongPayments(){
		db.open();
		String query = "SELECT p.*,m.amount,im.iid FROM "+NAME+" p INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" im, "+IntervalAnketa.NAME+" ia ON p.mid = m.mid AND im.mid = m.mid AND im.iid=ia.iid WHERE ia.type=? AND ((p.price IS ?) OR (p.price = ?) OR (m.amount IS ?) OR (m.amount = ?));";
		int[][] result = getResultInt(db.getDB().rawQuery(query,new String[]{""+dbStructure.RENT,"NULL","0","NULL","0"}));
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getWrongPrices(){
		db.open();
		String query = "SELECT p.*,m.amount,im.iid FROM "+NAME+" p INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" im, "+IntervalAnketa.NAME+" ia ON p.mid = m.mid AND im.mid = m.mid AND im.iid=ia.iid WHERE ia.type=? AND ((p.price IS ?) OR (p.price = ?));";
		int[][] result = getResultInt(db.getDB().rawQuery(query,new String[]{""+dbStructure.RENT,"NULL","0"}));
		if(!initOpen)db.close();
		return result;
	}
	
	public int[][] getWrongSumms(){
		db.open();
		String query = "SELECT p.*,m.amount,im.iid FROM "+NAME+" p INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" im, "+IntervalAnketa.NAME+" ia ON p.mid = m.mid AND im.mid = m.mid AND im.iid=ia.iid WHERE ia.type=? AND ((m.amount IS ?) OR (m.amount = ?));";
		int[][] result = getResultInt(db.getDB().rawQuery(query,new String[]{""+dbStructure.RENT,"NULL","0"}));
		if(!initOpen)db.close();
		return result;
	}
	
	/**
	 * Use MONEY_SUMM to get cost after sql join
	 * @param IntervalID
	 * @return
	 */
	public int[][] getPaymentsByIntervalID(int IntervalID){
		db.open();
		String query = "SELECT p.*,m.amount FROM "+NAME+" p INNER JOIN "+Money.NAME+" m, "+IntervalMoney.NAME+" i ON p.mid = m.mid AND i.mid = m.mid WHERE i.iid = ?;";
		int[][] result = getResultInt(db.getDB().rawQuery(query, new String[]{""+IntervalID}));
		if(!initOpen)db.close();
		return result;
	}
	
	public String getProfit(int FlatID, Date dateFrom, Date dateTo, Date today) {
		db.open();
		String query = "SELECT sum(profit.sum) " +
				"FROM (SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num " +
				"FROM calendarCache c " +
				"INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p, " +
				"(SELECT i.iid as iid,COUNT(*)-1 as num FROM calendarCache c " +
				"INNER JOIN intervalFlat if, intervalAnketa ia, interval i " +
				"ON if.fid = c.fid AND i.iid = if.iid AND ia.iid=i.iid " +
				"WHERE (? <= i.date_from OR ? <= i.date_to) " +
				"AND (? >= i.date_from OR ? >= i.date_to) " +
				"AND if.fid = ? AND c.date BETWEEN i.date_from AND i.date_to " +
				//"AND ia.type = ? "+
                "GROUP BY i.iid) days ON if.fid = c.fid AND i.iid = if.iid " +
				"AND im.iid=i.iid AND im.mid = m.mid AND m.mid = p.mid AND days.iid=i.iid " +
				"WHERE i.iid = days.iid AND c.date BETWEEN ? AND ? " +
				"AND c.date >= i.date_from AND c.date < i.date_to " +
				"AND c.date <= ? " +
				"GROUP BY c.date ORDER BY c.date) as profit;";

		Cursor cursor = db.getDB().rawQuery(query,new String[]{
				""+dateFrom.toInt(),
				""+dateFrom.toInt(),
				""+dateTo.toInt(),
				""+dateTo.toInt(),
				""+FlatID,
				//""+dbStructure.RENT,
				""+dateFrom.toInt(),
				""+dateTo.toInt(),
				""+today.toInt()
			});

        String[][] resultArray = Table.getResultString(cursor);
		String result = Utils.getRow(resultArray,0)[0];
		if (result == null || result.length() == 0) result = "0";
		if(!initOpen) db.close();
		return result;
	}
	
	public String getProfit(Date dateFrom, Date dateTo, Date today){
		db.open();
		String query = "SELECT sum(profit.sum) " +
				"FROM (SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num " +
				"FROM calendarCache c " +
				"INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p, " +
				"(SELECT i.iid as iid,COUNT(*)-1 as num FROM calendarCache c " +
				"INNER JOIN intervalFlat if, intervalAnketa ia, interval i " +
				"ON if.fid = c.fid AND i.iid = if.iid AND ia.iid=i.iid " +
				"WHERE (? <= i.date_from OR ? <= i.date_to) " +
				"AND (? >= i.date_from OR ? >= i.date_to) " +
				"AND c.date BETWEEN i.date_from AND i.date_to " +
				//"AND ia.type = ? "+
                "GROUP BY i.iid) days ON if.fid = c.fid AND i.iid = if.iid " +
				"AND im.iid=i.iid AND im.mid = m.mid AND m.mid = p.mid AND days.iid=i.iid " +
				"WHERE i.iid = days.iid AND c.date BETWEEN ? AND ? " +
				"AND c.date >= i.date_from AND c.date < i.date_to " +
				"AND c.date <= ? " +
				"GROUP BY c.date ORDER BY c.date) as profit;";
		Cursor cursor = db.getDB().rawQuery(query,new String[]{
				""+dateFrom.toInt(),
				""+dateFrom.toInt(),
				""+dateTo.toInt(),
				""+dateTo.toInt(),
				//""+dbStructure.RENT,
				""+dateFrom.toInt(),
				""+dateTo.toInt(),
				""+today.toInt()
			});
		String result = Utils.getRow(Table.getResultString(cursor),0)[0];
		if (result == null || result.length() == 0) result = "0";
		if(!initOpen) db.close();
		return result;
	}
	
	public String getProfit(int FlatID, Date today){
		db.open();
		String query = "SELECT sum(profit.sum) " +
				"FROM (SELECT c.date,sum(m.amount*1.0/days.num) as sum,days.num " +
				"FROM calendarCache c " +
				"INNER JOIN interval i, intervalFlat if, intervalMoney im, money m, payment p, " +
				"(SELECT i.iid as iid,COUNT(*)-1 as num FROM calendarCache c " +
				"INNER JOIN intervalFlat if, intervalAnketa ia, interval i " +
				"ON if.fid = c.fid AND i.iid = if.iid AND ia.iid=i.iid " +
				"WHERE if.fid = ? AND c.date BETWEEN i.date_from AND i.date_to " +
				//"AND ia.type = ? "+
                "GROUP BY i.iid) days " +
				"ON if.fid = c.fid AND i.iid = if.iid AND im.iid=i.iid AND im.mid = m.mid " +
				"AND m.mid = p.mid AND days.iid=i.iid WHERE i.iid = days.iid " +
				"AND c.date >= i.date_from AND c.date < i.date_to " +
				"AND c.date <= ? " +
				"GROUP BY c.date ORDER BY c.date) as profit;";
		Cursor cursor = db.getDB().rawQuery(query,new String[]{
				""+FlatID,
				//""+dbStructure.RENT,
				""+today.toInt()
			});
		String result = Utils.getRow(Table.getResultString(cursor),0)[0];
		if (result == null || result.length() == 0) result = "0";
		if(!initOpen)db.close();
		return result;
	}
}
