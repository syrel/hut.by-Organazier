package by.hut.flat.calendar.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tasks.Convert;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;

public class DialogConvert extends DialogThread {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogConvert";
	private int FlatID;
	private int IntervalID;
	private DBAdapter db;
	
	@SuppressLint("NewApi")
	@Override
	public void implementTask(){
		int[] intervals = new int[0];
		FlatID = Utils.Int(this.getParam(PARAM_FLAT_ID));
		if (FlatID <= 0) {
			finish();
			return;
		}
		String date = this.getParam(PARAM_DATE);
		
		if (Do.equals(ACTION_CONVERT)){
			if (date == null || date.length() == 0){
				finish();
				return;
			}
			db = new DBAdapter(context);
			db.open();
			Interval intervalDB = new Interval(db);
			intervals = intervalDB.findIntervals(FlatID, new Date(date));
			db.close();
		}
		else if (Do.equals(ACTION_CONVERT_INTERVAL)){
			int intervalID = extras.getInt(Dialog.PARAM_INTERVAL_ID);
			if (intervalID > 0){
				intervals = new int[]{intervalID};
			}
		}

		if (intervals.length == 0){
			finish();
			return;
		}
		if (intervals.length == 1){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) this.getWindow().setDimAmount(0);
			IntervalID = intervals[0];
			mAsyncTaskManager.setupTask(new Convert(context,FlatID,IntervalID));
			return;
		}
		
		/* let's ask for event id to remove */
		DialogSelectInterval.showForResult(
			this,
			DIALOG_SELECT_INTERVAL,
			new String[]{ACTION_QUESTION,PARAM_FLAT_ID,PARAM_DATE},
			new String[]{"Select event to convert:",""+FlatID,date}
		);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case DIALOG_SELECT_INTERVAL:{
				if (resultCode == RESULT_OK){
					IntervalID = data.getExtras().getInt(PARAM_INTERVAL_ID);
					mAsyncTaskManager.setupTask(new Convert(context,FlatID,IntervalID));
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
		Intent newIntent = new Intent(context, DialogConvert.class);
		newIntent.putExtra(ACTION_DO,ACTION_CONVERT);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_NO);
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_YES);
		newIntent.putExtra(ACTION_HIDDEN,ACTION_YES);
		
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		context.startActivity(newIntent);
	}
	
	public static void show(Context context, String FlatID,int IntervalID){
		Intent newIntent = new Intent(context, DialogConvert.class);
		newIntent.putExtra(ACTION_DO,ACTION_CONVERT_INTERVAL);
		newIntent.putExtra(ACTION_LOG,ACTION_NO);
		newIntent.putExtra(ACTION_ASK,ACTION_NO);
		newIntent.putExtra(ACTION_AUTO_HIDE,ACTION_YES);
		newIntent.putExtra(ACTION_HIDDEN,ACTION_YES);
		newIntent.putExtra(PARAM_FLAT_ID,FlatID);
		newIntent.putExtra(PARAM_INTERVAL_ID,IntervalID);
		context.startActivity(newIntent);
	}
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}
}
