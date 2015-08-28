package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class Maiden extends Table{
	public static final String NAME = "maiden";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"eid INTEGER," +
			"mon integer NOT NULL DEFAULT '0'," +
			"tue integer NOT NULL DEFAULT '0'," +
			"wed integer NOT NULL DEFAULT '0'," +
			"thu integer NOT NULL DEFAULT '0'," +
			"fri integer NOT NULL DEFAULT '0'," +
			"sat integer NOT NULL DEFAULT '0'," +
			"sun integer NOT NULL DEFAULT '0'," +
			"background integer NOT NULL DEFAULT '1'," +
			"FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"eid",			// 0
		"mon",			// 1
		"tue",			// 2
		"wed",			// 3
		"thu",			// 4
		"fri",			// 5
		"sat",			// 6
		"sun",			// 7
		"background"	// 8
	};
	public static final int EID = 0,MON = 1, TUE = 2, WED = 3, THU = 4, FRI = 5, SAT = 6, SUN = 7, BACKGROUND = 8,EMPLOYEE_FIO = 9;
	
	private static final String[] COLUMN_WEEK_DAY = {DB[MON],DB[TUE],DB[WED],DB[THU],DB[FRI],DB[SAT],DB[SUN]};
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Maiden(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addMaiden(String name){
		db.open();
		Employee employee = new Employee(this.getDB());
		long id = employee.insert(new int[]{Employee.FIO}, new String[]{name});
		employee.updateEmployee((int)id,new int[]{Employee.POSITION},new String[]{""+id});
		this.insert(new int[]{EID}, new String[]{""+id});
		if(!initOpen)db.close();
		return id;
	}
	
	public int[] getMaidenIDs(boolean active){
		db.open();
		String query = "SELECT DISTINCT m.eid FROM "+NAME+" m INNER JOIN "+Employee.NAME+" e ON e.eid=m.eid"+((active) ? " WHERE e.active = '1'" : "")+" ORDER BY e.position;";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[] result = Utils.getRow(Utils.transpose(getResultInt(cursor)),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public int getMaidenNum(){
		db.open();
		String query = "SELECT COUNT(*) FROM "+NAME;
		int result = Utils.getRow(getResultInt(db.getDB().rawQuery(query, null)),0)[0];
		if(!initOpen)db.close();
		return result;
	}
	
	public String[] getData(int MaidenID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "eid = ?", new String[]{""+MaidenID}, null, null, "eid");
		String[] result = Utils.getRow(getResultString(cursor),0);
		if(!initOpen)db.close();
		return result;
	}
	
	public String[][] getData(boolean active){
		db.open();
		String query = "SELECT m.*,e."+Employee.DB[Employee.FIO]+" FROM "+Employee.NAME+" e INNER JOIN "+NAME+" m ON m.eid = e.eid WHERE e."+Employee.DB[Employee.ACTIVE]+" = ?";
		Cursor cursor = db.getDB().rawQuery(query, new String[]{(active)?"1":"0"});
		String[][] result = getResultString(cursor);
		if(!initOpen)db.close();
		return result;
	}
	
	public void updateMaiden(int MaidenID,int[] paramIDs, String[] paramValues){
		this.updateByID(EID, MaidenID, paramIDs, paramValues);
	}
	
	@Override
	public void insert() throws TableNotExistsException{
		db.open();
		String[] names = this.getDB().getContext().getResources().getStringArray(R.array.db_default_maiden_names);
		for (int i = 0; i < names.length; i++){
			addMaiden(names[i]);
		}
		if(!initOpen)db.close();
	}

	public int[] getMaidenAtDay(int dayOfWeek) {
		assert dayOfWeek >= 0;
		assert dayOfWeek < 7;
		db.open();
		String query = "SELECT DISTINCT m.eid FROM "+NAME+" m INNER JOIN "+Employee.NAME+" e ON e.eid=m.eid WHERE e.active = '1' AND m."+COLUMN_WEEK_DAY[dayOfWeek]+" = '1' ORDER BY e.position;";
		Cursor cursor = db.getDB().rawQuery(query, null);
		int[] result = Utils.getRow(Utils.transpose(getResultInt(cursor)),0);
		if(!initOpen)db.close();
		return result;
	}
}
