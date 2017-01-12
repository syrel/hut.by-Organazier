package by.hut.flat.calendar.core.DBAdapter.tables;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;

public class Programmer extends Table{
	public static final String NAME = "programmer";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"eid INTEGER," +
			"FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"eid"			// 0
	};
	public static final int EID = 0;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Programmer(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
}
