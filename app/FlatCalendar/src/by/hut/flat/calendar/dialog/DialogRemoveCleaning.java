package by.hut.flat.calendar.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tasks.RemoveCleaning;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class DialogRemoveCleaning extends DialogThread{
public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogRemoveCleaning";
	
	private DBAdapter db;
	private Date date;
	private int FlatID;
	private int[] removeCleaningsID;
	private int[] allCleaningsID;
	private int[][] allCleaningsInfo;
	
	@Override
	public void implementTask(){
		if (Do.equals(ACTION_REMOVE_CLEANING)){
			FlatID = Utils.Int(this.getParam(PARAM_FLAT_ID));
			String dateStr = this.getParam(PARAM_DATE);
			assert FlatID >= 0;
			assert dateStr != null;
			assert dateStr.length() > 0;
			
			date = new Date(dateStr);
			db = new DBAdapter(context);
			
			db.open();
			Cleanings cleaningsDB = new Cleanings(db);
			allCleaningsInfo = cleaningsDB.getDataByDate(FlatID, date);
			db.close();
			
			allCleaningsID = Utils.getRow(Utils.transpose(allCleaningsInfo),Cleanings.CID);
			
			if (allCleaningsID.length == 0){
				finish();
				return;
			}
			
			if (allCleaningsID.length == 1){
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)this.getWindow().setDimAmount(0);
				removeCleaningsID = new int[]{allCleaningsID[0]};
				startRemoving();
				return;
			}
			
			if (allCleaningsID.length > 0){
				/* asking which cleanings to remove */
				DialogSelectRemoveCleanings.showForResult(this, DIALOG_SELECT_REMOVE_CLEANINGS,
					new String[]{ACTION_QUESTION},
					new String[]{"Select cleanings to remove:"},
					allCleaningsID
				);
			}
			
			
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case DIALOG_SELECT_REMOVE_CLEANINGS:{
				if (resultCode == RESULT_OK){
					removeCleaningsID = data.getExtras().getIntArray(PARAM_CLEANING_IDS);
					/* if user selected nothing, exit*/
					if (removeCleaningsID.length == 0){
						finish();
						break;
					}
					startRemoving();
				}
				else if (resultCode == DialogSelect.RESULT_ERROR){
					finish();
				}
				else if(resultCode == RESULT_CANCELED){
					finish();
				}
				break;
			}
		}
	}
	private void startRemoving(){
		mAsyncTaskManager.setupTask(new RemoveCleaning(context,FlatID,date,removeCleaningsID));
	}
	
	/**
	 * <p><b><i>user should provide:</br>
	 * -PARAM_FLAT_ID</br>
	 * -PARAM_DATE
	 * </p></b></i>
	 * @param context
	 * @param keys
	 * @param params
	 */
	public static void show(Context context, String[] keys,String[] params){
		Intent newIntent = new Intent(context, DialogRemoveCleaning.class);
		newIntent.putExtra(ACTION_DO,ACTION_REMOVE_CLEANING);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_NO);
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_YES);
		newIntent.putExtra(ACTION_HIDDEN,ACTION_YES);
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
