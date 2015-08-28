package by.hut.flat.calendar.core.DBAdapter;

import java.util.ArrayList;

import by.hut.flat.calendar.core.DBAdapter.tables.Cache;
import by.hut.flat.calendar.core.DBAdapter.tables.CacheExt;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tables.Debt;
import by.hut.flat.calendar.core.DBAdapter.tables.Director;
import by.hut.flat.calendar.core.DBAdapter.tables.Employee;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.ITable;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalFlat;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalMoney;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalToday;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.core.DBAdapter.tables.Manager;
import by.hut.flat.calendar.core.DBAdapter.tables.Money;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.core.DBAdapter.tables.Prepay;
import by.hut.flat.calendar.core.DBAdapter.tables.Programmer;
import by.hut.flat.calendar.core.DBAdapter.tables.State;

public class dbStructure {
	public static final String TAG = "DBAdapter";
    
	public static final String DATABASE_NAME = "FlatCalendar";  
	public static final int DATABASE_VERSION = 3;
    
	public static final String WHERE_DELIMITER = " = ?";
	public static final String AND_CLAUSE = " AND ";
	
	public static final int RENT = 2;
	public static final int BOOK = 3;
	
	private DBAdapter db;
	private ArrayList<ITable> tables;
	
	private boolean invariant(){
		return db != null && tables != null;
	}
	
	public dbStructure(DBAdapter adapter){
		assert adapter != null;
		this.db = adapter;
		initTableList();
		assert invariant();
	}
	
	private void initTableList(){
		tables = new ArrayList<ITable>();
		tables.add(new Flat(db));
		tables.add(new Interval(db));
		tables.add(new IntervalAnketa(db));
		tables.add(new IntervalFlat(db));
		tables.add(new IntervalToday(db));
		tables.add(new Money(db));
		tables.add(new Payment(db));
		tables.add(new Debt(db));
		tables.add(new Prepay(db));
		tables.add(new IntervalMoney(db));
		tables.add(new Employee(db));
		tables.add(new Director(db));
		tables.add(new Manager(db));
		tables.add(new Programmer(db));
		tables.add(new Maiden(db));
		tables.add(new Cleanings(db));
		tables.add(new Cache(db));
		tables.add(new CacheExt(db));
		tables.add(new State(db));
	}
	/*------------------------------------------------------------
	------------------------- G E T T E R S ----------------------
	------------------------------------------------------------*/
    
	public ArrayList<ITable> getTables(){
		assert invariant();
		return this.tables;
	}
}
