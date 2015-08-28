package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class Flat extends Table {
	
	public static final String NAME = "flat";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"fid integer," +
			"address varchar(255)," +
			"short_address varchar(255) DEFAULT ''," +
			"position integer UNIQUE," +
			"use_short_address_sausage integer NOT NULL DEFAULT 1," +
			"active integer NOT NULL DEFAULT 1," +
			"CONSTRAINT flat_pkey PRIMARY KEY (fid)" +
		");";
	public static final String[] DB = {
		"fid",							// 0
		"address",						// 1
		"short_address",				// 2
		"position",						// 3
		"use_short_address_sausage", 	// 4
		"active"						// 5
	};
	public static final int FID = 0, ADDRESS = 1, SHORT_ADDRESS = 2, POSITION = 3, USE_SHORT_ADDRESS_SAUSAGE = 4, ACTIVE = 5;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Flat(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public int[] getFlatIDs(boolean active){
		db.open();
		String query = "SELECT DISTINCT fid FROM " + NAME + ((active) ? " WHERE active = '1'" : "") +" ORDER BY position";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[] result = Utils.getRow(Utils.transpose(getResultInt(cursor)),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int getFlatNum(boolean active){
		return getFlatIDs(active).length;
	}
	
	public long addFlat(String address){
		db.open();
		long id = this.insert(new int[]{ADDRESS}, new String[]{address});
		updateFlat((int)id,new int[]{POSITION},new String[]{""+id});
		if(!initOpen)db.close();
		return id;
	}
	
	public void updateFlat(int FlatID,int[] paramIDs, String[] paramValues){
		this.updateByID(FID, FlatID, paramIDs, paramValues);
	}
	
	public String[] getData(int FlatID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "fid = ?", new String[]{""+FlatID}, null, null, DB[FID]);
		String[] result = Utils.getRow(getResultString(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public String[][] getData(boolean active){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, (active)?"active = ?":"", (active)?new String[]{"1"}:null, null, null, DB[FID]);
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public String[][] getDataOrderByPosition(boolean active){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, (active)?"active = ?":"", (active)?new String[]{"1"}:null, null, null, DB[POSITION]);
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public void swapPositions(int FlatID1, int FlatID2) throws QuerySyntaxException {
		assert FlatID1 != FlatID2;
		db.open();
		String[] firstData = this.getData(FlatID1);
		String[] secondData = this.getData(FlatID2);
		updateFlat(FlatID2,new int[]{POSITION},new String[]{"-1"});
		updateFlat(FlatID1,new int[]{POSITION},new String[]{secondData[POSITION]});
		updateFlat(FlatID2,new int[]{POSITION},new String[]{firstData[POSITION]});
		String[] firstDataCheck = this.getData(FlatID1);
		String[] secondDataCheck = this.getData(FlatID2);
		if (!firstDataCheck[POSITION].equals(secondData[POSITION]) || !secondDataCheck[POSITION].equals(firstData[POSITION])){
			throw new QuerySyntaxException(this, "Error swaping positions of flat " + FlatID1 + " and " + FlatID2 + "!");
		}
		if(!initOpen)db.close();
	}
	
	
}
