package by.hut.flat.calendar.dialog;

import android.content.Context;
import android.content.Intent;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Cleanings;
import by.hut.flat.calendar.core.DBAdapter.tasks.Free;
import by.hut.flat.calendar.utils.Utils;

public class DialogFree extends DialogThread {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogFree";
	
	public static final boolean CLEANINGS_REMOVE_ASK = false;
	
	private int FlatID;
	private DBAdapter db;
	private int[][] cleaningData;
	private int[] removeCleaningIDs;
	private int IntervalID;
	@Override
	public void implementTask(){
		if (Do.equals(ACTION_FREE)){
			FlatID = Utils.Int(this.getParam(PARAM_FLAT_ID));
			String date = this.getParam(PARAM_DATE);
			assert FlatID >= 0;
			assert date != null;
			assert date.length() > 0;

			/* let's ask for event id to remove */
			DialogSelectInterval.showForResult(
				this,
				DIALOG_SELECT_INTERVAL,
				new String[]{ACTION_QUESTION,PARAM_FLAT_ID,PARAM_DATE},
				new String[]{this.getParam(ACTION_QUESTION),""+FlatID,date}
			);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case DIALOG_SELECT_INTERVAL:{
				if (resultCode == RESULT_OK){
					dialogSelectIntervalResult(data);
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


	private void dialogSelectIntervalResult(Intent data) {
		/* getting event id to remove */
		IntervalID = data.getExtras().getInt(PARAM_INTERVAL_ID);
		
		db = new DBAdapter(context);
		db.open();
		
		Cleanings cleaningsDB = new Cleanings(db);
		/* getting all cleanings that are binded to interval */
		cleaningData = cleaningsDB.findCleaningsData(IntervalID);
		db.close();
		
		/* collecting cleanings' ids */
		removeCleaningIDs = Utils.getRow(Utils.transpose(cleaningData),0);

		mAsyncTaskManager.setupTask(new Free(context,FlatID,IntervalID,removeCleaningIDs));
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
		Intent newIntent = new Intent(context, DialogFree.class);
		newIntent.putExtra(ACTION_DO,ACTION_FREE);
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
