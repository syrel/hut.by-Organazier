package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.core.DBAdapter.tasks.BackupCreate;
import android.content.Context;
import android.content.Intent;

public class DialogBackupCreate extends DialogThread {
	public static final String ACTIVITY_TAG = DialogBackupCreate.class.getName();	
	
	@Override
	public void implementTask() {
		mAsyncTaskManager.setupTask(new BackupCreate(context));
	}
	
	public static void show(Context context){
		Intent newIntent = new Intent(context, DialogBackupCreate.class);
		newIntent.putExtra(ACTION_DO,ACTION_BACKUP);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_YES);
		newIntent.putExtra(ACTION_QUESTION, "Сделать бэкап?");
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_NO);
		context.startActivity(newIntent);
	}
	
	public static void show(Context context, String[] keys, String[] params){
		Intent newIntent = new Intent(context, DialogBackupCreate.class);
		newIntent.putExtra(ACTION_DO,ACTION_BACKUP);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_YES);
		newIntent.putExtra(ACTION_QUESTION, "Сделать бэкап?");
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_NO);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		context.startActivity(newIntent);
	}
	
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}

}
