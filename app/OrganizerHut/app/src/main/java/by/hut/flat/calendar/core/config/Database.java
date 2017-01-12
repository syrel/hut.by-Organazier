package by.hut.flat.calendar.core.config;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.dialog.DialogBackupCreate;
import by.hut.flat.calendar.dialog.DialogInitDB;
import by.hut.flat.calendar.utils.Print;
import android.content.Context;
import android.widget.Toast;

public class Database extends Preferences {
	protected static final String TAG = "database";

	public static final String INIT_NAME = "init";
	public static final String LAST_BACKUP_NAME = "backup";
	
	public boolean INIT = false;
	public long LAST_BACKUP = 0;
	
	private OnDatabaseInitializedListener mOnDatabaseInitializedListener;
	
	public interface OnDatabaseInitializedListener{
		public void onDatabaseInitialized();
	}
	
	public Database(Context context,OnDatabaseInitializedListener l){
		super(TAG,context);
		this.mOnDatabaseInitializedListener = l;
		this.INIT = readInit();
		this.LAST_BACKUP = readLastBackup();
		
		if (INIT){
			if(mOnDatabaseInitializedListener != null)mOnDatabaseInitializedListener.onDatabaseInitialized();
		}
		else {
			initDatabase();
		}
		check();
	}

	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void reInit() {}
	/*------------------------------------------------------------
	-------------------------- C H E C K S -----------------------
	------------------------------------------------------------*/
	@Override
	public void check() {
		if (Config.INST.REINIT){
			reInit();
		}
		
		if (INIT) {
			long unixtime = java.lang.System.currentTimeMillis() / 1000L;
			if (unixtime - LAST_BACKUP > 60*60*24) {
				save(LAST_BACKUP_NAME,unixtime);
				backupDatabase();
				Toast.makeText(context,"Бэкап сделан!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	/*------------------------------------------------------------
	-------------------------- P U B L I C  ----------------------
	------------------------------------------------------------*/	
	public void setDatabaseInitialized(){
		save(INIT_NAME,true);
		this.INIT = true;
		if (mOnDatabaseInitializedListener != null){
			mOnDatabaseInitializedListener.onDatabaseInitialized();
		}
	}
	/*------------------------------------------------------------
	----------------------------- R E A D ------------------------
	------------------------------------------------------------*/
	
	private boolean readInit() {
		return this.getBoolean(INIT_NAME);
	}
	
	private long readLastBackup() {
		return this.getLong(LAST_BACKUP_NAME);
	}
	
	/*------------------------------------------------------------
	----------------------------- I N I T ------------------------
	------------------------------------------------------------*/
	private void initDatabase(){
		if (!INIT){
			DialogInitDB.show(context,
					new String[]{Dialog.ACTION_QUESTION,Dialog.ACTION_ASK},
					new String[]{"Creating tables...",Dialog.ACTION_NO}
			);
		}
		else {
			Print.err("Database is already initialized");
		}
	}
	
	private void backupDatabase(){
		DialogBackupCreate.show(context,
				new String[]{Dialog.ACTION_ASK, Dialog.ACTION_AUTO_HIDE},
				new String[]{Dialog.ACTION_NO, Dialog.ACTION_YES});
	}
}
