package by.hut.flat.calendar.core.DBAdapter;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public final class DBAdapter{  
	int id = 0;
	public int state = 0;

	private final Context context;  

	private DatabaseHelper DBHelper;  
	private SQLiteDatabase db;
	public DBAdapter(Context ctx){ 
		this.context = ctx;  
		DBHelper = new DatabaseHelper(context);  
	}  

	private static class DatabaseHelper extends SQLiteOpenHelper  {  
		DatabaseHelper(Context context) {  
			super(context, dbStructure.DATABASE_NAME, null, dbStructure.DATABASE_VERSION);  
		}
		@Override  
		public void onCreate(SQLiteDatabase db) {
			System.out.println("Create db");
		}
		@Override  
		public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {  
			Log.w(dbStructure.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		}
	}
	public SQLiteDatabase getDB(){
		return db;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	/********************************************************************************************************************/
	/****************************************************S T A T I C*****************************************************/
	/********************************************************************************************************************/
  
	public DBAdapter open() throws SQLException  {
		if (state == 0){
			db = DBHelper.getWritableDatabase();
			state = 1;
		}
		return this;  
	}
 
	public void close(){
		if (state == 1){
			DBHelper.close();  
			state = 0;
		}
	}
	
	public boolean isOpen(){
		return state == 1;
	}
	
	public void releaseMemory(){
		SQLiteDatabase.releaseMemory();
	}
	
	public void exec(String str){
		db.rawQuery(str,null);
	}
	
}


