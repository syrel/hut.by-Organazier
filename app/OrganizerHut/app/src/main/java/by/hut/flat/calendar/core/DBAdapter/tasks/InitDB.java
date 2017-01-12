package by.hut.flat.calendar.core.DBAdapter.tasks;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbUpdate;
import by.hut.flat.calendar.core.DBAdapter.tables.QuerySyntaxException;
import by.hut.flat.calendar.core.DBAdapter.tables.TableAlreadyExistsException;
import by.hut.flat.calendar.core.DBAdapter.tables.TableNotExistsException;
import by.hut.flat.calendar.internal.Task;

public class InitDB extends Task{
	
	private DBAdapter dbAdapter;
	private dbUpdate db;
	private Context context;
	public InitDB(Context context) {
		super(context);
		this.context = context;
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		if (!Config.INST.DATABASE.INIT){
			dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			db = new dbUpdate(dbAdapter);
			try {
				if (db.isClean()){
					db.dbTablesCreate();
					db.dbTablesInsert();
				}
				else {
					db.dbFullFormat();
				}
			} catch (TableAlreadyExistsException e){
				e.printStackTrace();
				return false;
			} catch (QuerySyntaxException e) {
				e.printStackTrace();
				return false;
			} catch (TableNotExistsException e) {
				e.printStackTrace();
				return false;
			}
			dbAdapter.close();
			Config.INST.DATABASE.setDatabaseInitialized();
			return true;
		}
		else {
			return false;
		}
	}

}
