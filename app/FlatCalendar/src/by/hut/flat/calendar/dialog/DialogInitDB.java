package by.hut.flat.calendar.dialog;

import android.content.Context;
import android.content.Intent;
import by.hut.flat.calendar.core.DBAdapter.tasks.InitDB;

public class DialogInitDB  extends DialogThread{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogInitDB";

	
	@Override
	public void implementTask(){
		if (Do.equals(ACTION_INIT_DB)){
			mAsyncTaskManager.setupTask(new InitDB(context));
		}
	}
	
	public static void show(Context context, String[] keys,String[] params){
		Intent newIntent = new Intent(context, DialogInitDB.class);
		newIntent.putExtra(ACTION_DO,ACTION_INIT_DB);
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_YES);
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
