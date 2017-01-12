package by.hut.flat.calendar.core.DBAdapter.tables;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;

public class IntervalToday extends Table{
	public static final String NAME = "intervalToday";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"iid integer NOT NULL UNIQUE, " +
			"CONSTRAINT interval_today_fkey FOREIGN KEY (iid) " +
			"REFERENCES interval(iid) MATCH FULL " +
			"ON UPDATE CASCADE ON DELETE CASCADE" +
		");";
	public static final String[] DB = {
		"iid"					// 0
	};
	
	public static final int IID = 0;
	
	public IntervalToday(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public void addToday(int IntervalID){
		this.insert(new int[]{IID}, new String[]{""+IntervalID});
	}
	
	public void removeToday(int IntervalID){
		this.removeByID(IID, IntervalID);
	}

}
