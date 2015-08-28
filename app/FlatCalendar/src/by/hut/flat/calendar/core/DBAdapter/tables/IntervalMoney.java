package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class IntervalMoney extends Table{
	public static final String NAME = "intervalMoney";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"iid integer NOT NULL," +
			"mid integer NOT NULL UNIQUE," +
			"CONSTRAINT interval_money_iid_fkey FOREIGN KEY (iid)" +
			"REFERENCES interval(iid) MATCH FULL ON DELETE CASCADE," +
			"CONSTRAINT interval_money_mid_fkey FOREIGN KEY (mid)" +
			"REFERENCES money(mid) MATCH FULL ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"iid",			// 0
		"mid"			// 1
	};
	public static final int IID = 0,MID = 1;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public IntervalMoney(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public void addMoney(int IntervalID, int MoneyID){
		this.insert(new int[]{IID,MID}, new String[]{""+IntervalID,""+MoneyID});
	}
	
	public int[][] getDataByIntervalID(int IntervalID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, DB[IID]+" = ?", new String[]{""+IntervalID}, null, null, DB[IID]);
		int[][] result = getResultInt(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[] getDataByMoneyID(int MoneyID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, DB[MID]+" = ?", new String[]{""+MoneyID}, null, null, DB[MID]);
		int[] result = Utils.getRow(getResultInt(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	protected void removeIntervalMoneyByMoneyID(int MoneyID){
		this.removeByID(MID, MoneyID);
	}
	
	public void removeMoneyByIntervalID(int IntervalID){
		Money moneyDB = new Money(db);
		int[] moneyIDs = Utils.getRow(Utils.transpose(this.getDataByIntervalID(IntervalID)),MID);
		for (int i = 0,length = moneyIDs.length; i<length; i++){
			moneyDB.removeMoney(moneyIDs[i]);
		}
	}
}
