package by.hut.flat.calendar.core.DBAdapter.tables;

import java.util.ArrayList;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.source.sqlite.SQLiteCursor;
import by.hut.flat.calendar.source.sqlite.SQLiteDatabase;
import by.hut.flat.calendar.source.sqlite.SQLiteException;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class IntervalAnketa extends Table {
	public static final String NAME = "intervalAnketa";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"iid integer NOT NULL," +
			"type integer NOT NULL," +
			"telephone_left varchar(255) NOT NULL DEFAULT ''," +
			"telephone_right varchar(255) NOT NULL DEFAULT ''," +
			"name_left varchar(255) NOT NULL DEFAULT ''," +
			"name_right varchar(255) NOT NULL DEFAULT ''," +
			"anketa text NOT NULL DEFAULT ''," +
			"settlement varchar(255) NOT NULL DEFAULT ''," +
			"eviction varchar(255) NOT NULL DEFAULT ''," +
			"else_info text NOT NULL DEFAULT ''," +
			"CONSTRAINT interval_anketa_fkey FOREIGN KEY (iid) " +
			"REFERENCES interval(iid) MATCH FULL " +
			"ON UPDATE CASCADE ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"iid",					// 0
		"type",					// 1
		"telephone_left",		// 2
		"telephone_right",		// 3
		"name_left",			// 4
		"name_right",			// 5
		"anketa",				// 6
		"settlement",			// 7
		"eviction",				// 8
		"else_info"				// 9
	};
	
	public static final int IID = 0, TYPE = 1,TELEPHONE_LEFT = 2, TELEPHONE_RIGHT = 3, NAME_LEFT = 4, NAME_RIGHT = 5,
			ANKETA = 6, SETTLEMENT = 7, EVICTION = 8, ELSE_INFO = 9,INTERVAL_DATE_FROM = 10, INTERVAL_DATE_TO = 11,
			FLAT_ADDRESS = 12, FLAT_SHORT_ADDRESS = 13, FLAT_FID = 14,PAYMENT_PRICE = 15,PAYMENT_AMOUNT = 16,MAIDEN_BACKGROUND = 17,INTERVAL_TODAY = 18;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public IntervalAnketa(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}

	public long addIntervalAnketa(int IntervalID, int type){
		return this.insert(new int[]{IID,TYPE}, new String[]{""+IntervalID,""+type});
	}
	
	public String[] getData(int IntervalID){
		db.open();
		String query = "SELECT ia.*,i."+Interval.DB[Interval.DATE_FROM]+",i."+Interval.DB[Interval.DATE_TO]+",f."+
						Flat.DB[Flat.ADDRESS]+",f."+Flat.DB[Flat.SHORT_ADDRESS]+",f."+Flat.DB[Flat.FID]+",p."+
						Payment.DB[Payment.PRICE]+",m."+Money.DB[Money.AMOUNT]+" FROM "+NAME+" ia INNER JOIN "+
						Interval.NAME+" i, "+
						IntervalFlat.NAME+" if, "+
						Flat.NAME+" f, " +
						IntervalMoney.NAME+" im,"+
						Money.NAME+" m,"+
						Payment.NAME+" p " +
						"ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid AND im.iid = i.iid AND m.mid = p.mid " +
						"WHERE i."+Interval.DB[Interval.IID]+" = ?";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+IntervalID});
		String[] result = Utils.getRow(getResultString(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public ArrayList<String[]> searchAnkets(Date date,String searchQuery){
		SQLiteDatabase nativeDB = null;
		try  {
			nativeDB = new SQLiteDatabase(db.getDB().getPath());		
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		String query = "SELECT ia.*,i.date_from,i.date_to,f.address,f.short_address,f.fid,p.price,m.amount, " +
				"CASE WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) > 1 THEN -2 " +
				"WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) = 0 THEN 0 " +
				"ELSE (SELECT m.background FROM cleanings c INNER JOIN maiden m ON m.eid=c.eid WHERE c.iid=i.iid AND c.date = ?) " +
				"END AS background, " +
				"(SELECT COUNT(*) FROM intervalToday it WHERE it.iid=i.iid) AS today "+
				"FROM intervalAnketa ia " +
				"INNER JOIN interval i, intervalFlat if, flat f, intervalMoney im,money m,payment p " +
				"ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid " +
				"AND im.iid = i.iid AND m.mid = p.mid " +
				"WHERE ia.telephone_left LIKE ? OR ia.telephone_right LIKE ? OR ia.name_left LIKE ? " +
				"OR ia.name_right LIKE ? OR ia.anketa LIKE ? OR ia.else_info LIKE ?";
		SQLiteCursor cursor = null;
		try {
			cursor = nativeDB.query(query,new Object[]{""+date.toInt(),""+date.toInt(),""+date.toInt(),"%"+searchQuery+"%","%"+searchQuery+"%","%"+searchQuery+"%","%"+searchQuery+"%","%"+searchQuery+"%","%"+searchQuery+"%"});
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		
		final ArrayList<String[]> result = new ArrayList<String[]>();
        final int columnCount = cursor.count();
        int row = 0;
    	try {
			while (cursor.next()){
				result.add(new String[columnCount]);
		        for (int i = 0; i < columnCount; i++) {
					result.get(row)[i] = cursor.stringValue(i);
		        }
		        row++;
		    }
    	} catch (SQLiteException e) {
			e.printStackTrace();
		}
	    cursor.dispose();
		nativeDB.close();
		return result;
	}
	
	public void setType(int IntervalID,int type){
		db.open();
		this.updateByID(IID, IntervalID, new int[]{TYPE}, new String[]{""+type});
		if(!initOpen)db.close();
	}
	
	public String[][] getSettlementDataByDate(Date date){
		db.open();
		String query = "SELECT ia.*,i.date_from,i.date_to,f.address,f.short_address,f.fid,p.price,m.amount, " +
				"CASE WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) > 1 THEN -2 " +
				"WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) = 0 THEN 0 " +
				"ELSE (SELECT m.background FROM cleanings c INNER JOIN maiden m ON m.eid=c.eid " +
				"WHERE c.iid=i.iid AND c.date = ?) END AS background, " +
				"(SELECT COUNT(*) FROM intervalToday it WHERE it.iid=i.iid) AS today "+
				"FROM intervalAnketa ia " +
				"INNER JOIN interval i, intervalFlat if, flat f, intervalMoney im,money m,payment p " +
				"ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid AND " +
				"im.iid = i.iid AND m.mid = p.mid WHERE i.date_from = ? ORDER BY position";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt(),""+date.toInt(),""+date.toInt()});
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public String[][] getEvictionDataByDate(Date date){
		db.open();
		String query = "SELECT ia.*,i.date_from,i.date_to,f.address,f.short_address,f.fid,p.price,m.amount, " +
				"CASE WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) > 1 THEN -2 " +
				"WHEN (SELECT COUNT(*) FROM cleanings c WHERE c.iid=i.iid AND c.date = ?) = 0 THEN 0 " +
				"ELSE (SELECT m.background FROM cleanings c INNER JOIN maiden m ON m.eid=c.eid " +
				"WHERE c.iid=i.iid AND c.date = ?) END AS background, " +
				"(SELECT COUNT(*) FROM intervalToday it WHERE it.iid=i.iid) AS today "+
				"FROM intervalAnketa ia " +
				"INNER JOIN interval i, intervalFlat if, flat f, intervalMoney im,money m,payment p " +
				"ON ia.iid = i.iid AND ia.iid = if.iid AND if.fid = f.fid AND im.mid = p.mid AND " +
				"im.iid = i.iid AND m.mid = p.mid WHERE i.date_to = ? ORDER BY position";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{""+date.toInt(),""+date.toInt(),""+date.toInt(),""+date.toInt()});
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[] getIntervalIDsMissName(){
		db.open();
		String query = "SELECT ia.iid FROM "+NAME+" ia WHERE (ia.name_left = ? OR ia.name_left = ?) AND (ia.name_right = ? OR ia.name_right = ?)";
		int[] result = Utils.getRow(Utils.transpose(getResultInt(db.getDB().rawQuery(query,new String[]{"NULL","","NULL",""}))),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[] getIntervalIDsMissPhone(){
		db.open();
		String query = "SELECT ia.iid FROM "+NAME+" ia WHERE (ia.telephone_left = ? OR ia.telephone_left = ?) AND (ia.telephone_right = ? OR ia.telephone_right = ?)";
		int[] result = Utils.getRow(Utils.transpose(getResultInt(db.getDB().rawQuery(query,new String[]{"NULL","","NULL",""}))),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int[] getIntervalIDsMissPhoneName(){
		db.open();
		String query = "SELECT ia.iid FROM "+NAME+" ia WHERE ((ia.telephone_left = ? OR ia.telephone_left = ?) AND (ia.telephone_right = ? OR ia.telephone_right = ?)) OR ((ia.telephone_left = ? OR ia.telephone_left = ?) AND (ia.telephone_right = ? OR ia.telephone_right = ?))";
		int[] result = Utils.getRow(Utils.transpose(getResultInt(db.getDB().rawQuery(query,new String[]{"NULL","","NULL",""}))),0);
		if(!initOpen)db.close();
		return result;
	}
	
}
