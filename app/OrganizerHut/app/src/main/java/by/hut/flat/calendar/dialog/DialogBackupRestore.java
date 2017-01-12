package by.hut.flat.calendar.dialog;

import android.content.Context;
import android.content.Intent;
import by.hut.flat.calendar.core.DBAdapter.tasks.BackupRestore;

public class DialogBackupRestore extends DialogThread {
	public static final String ACTIVITY_TAG = DialogBackupRestore.class.getName();	
	
	public static final int DIALOG_FILE_SELECT = 1;
	
	@Override
	public void implementTask() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	    intent.setType("file/*");
	    startActivityForResult(intent, DIALOG_FILE_SELECT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {finish(); return;}
		if (resultCode != RESULT_OK) {
			finish();
			return;
		}

		switch (requestCode){
			case DIALOG_FILE_SELECT:{
                mAsyncTaskManager.setupTask(new BackupRestore(context, data.getData().getPath()));
				break;
			}
		}
	}
	
	@Override
	public void closeDialog() {
		super.closeDialog();
	}
	
	public static void show(Context context){
		Intent newIntent = new Intent(context, DialogBackupRestore.class);
		newIntent.putExtra(ACTION_DO,ACTION_RESTORE);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_YES);
		newIntent.putExtra(ACTION_QUESTION, "Восстановить бэкап?");
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_YES);
		context.startActivity(newIntent);
	}
	
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}

}
