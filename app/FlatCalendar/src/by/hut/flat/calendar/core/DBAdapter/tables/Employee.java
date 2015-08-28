package by.hut.flat.calendar.core.DBAdapter.tables;

import android.database.Cursor;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Utils;

public class Employee extends Table{
	public static final String NAME = "employee";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"eid integer," +
			"fio varchar(255) NOT NULL," +
			"position integer UNIQUE," +
			"active integer NOT NULL DEFAULT 1," +
			"CONSTRAINT employee_pkey PRIMARY KEY (eid)" +
		");";
	public static final String[] DB = {
		"eid",			// 0
		"fio",			// 1
		"position",		// 2
		"active"		// 3
	};
	public static final int EID = 0, FIO = 1 ,POSITION = 2,ACTIVE = 3;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Employee(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public void updateEmployee(int EmployeeID, int[] paramIDs, String[] paramValues){
		this.updateByID(EID, EmployeeID, paramIDs, paramValues);
	}
	
	public String[] getData(int EmployeeID){
		db.open();
		Cursor cursor = db.getDB().query(NAME, DB, "eid = ?", new String[]{""+EmployeeID}, null, null, "eid");
		String[] result = Utils.getRow(getResultString(cursor),0);
		if(!initOpen)db.close();
		return result;
	}

	public void swapPositions(int EmployeeID1, int EmployeeID2) throws QuerySyntaxException {
		assert EmployeeID1 != EmployeeID2;
		db.open();
		String[] firstData = this.getData(EmployeeID1);
		String[] secondData = this.getData(EmployeeID2);
		updateEmployee(EmployeeID2,new int[]{POSITION},new String[]{"-1"});
		updateEmployee(EmployeeID1,new int[]{POSITION},new String[]{secondData[POSITION]});
		updateEmployee(EmployeeID2,new int[]{POSITION},new String[]{firstData[POSITION]});
		String[] firstDataCheck = this.getData(EmployeeID1);
		String[] secondDataCheck = this.getData(EmployeeID2);
		if (!firstDataCheck[POSITION].equals(secondData[POSITION]) || !secondDataCheck[POSITION].equals(firstData[POSITION])){
			throw new QuerySyntaxException(this, "Error swaping positions of employee " + EmployeeID1 + " and " + EmployeeID2 + "!");
		}
		if(!initOpen)db.close();
	}
}
