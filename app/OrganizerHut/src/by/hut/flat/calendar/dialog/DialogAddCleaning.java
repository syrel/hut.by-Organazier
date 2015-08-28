package by.hut.flat.calendar.dialog;

import android.content.Context;
import android.content.Intent;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.core.DBAdapter.tasks.AddCleaning;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.utils.Print;

public class DialogAddCleaning extends DialogThread {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogAddCleaning";
	
	private Date date;
	private int FlatID;
	private int IntervalID;
	private int[] maidens;
	private int MaidenID;
	
	@Override
	public void implementTask(){
		if (Do.equals(ACTION_ADD_CLEANING)){
			FlatID = Utils.Int(this.getParam(PARAM_FLAT_ID));
			String dateStr = this.getParam(PARAM_DATE);
			String intervalID = extras.getString(PARAM_INTERVAL_ID);
			int iid = (intervalID != null) ? Utils.Int(extras.getString(PARAM_INTERVAL_ID)) : 0;
			
			assert FlatID >= 0;
			assert dateStr != null;
			assert dateStr.length() > 0;
			
			date = new Date(dateStr);
			
			DBAdapter db = new DBAdapter(context);
			int dayOfWeek = new CalendarHelper(date).getDayOfWeek();
			
			db.open();
			
			Interval intervalDB = new Interval(db);
			Maiden maidenDB = new Maiden(db);
			
			int[] intervals = intervalDB.findIntervals(FlatID, date);
			maidens = maidenDB.getMaidenAtDay(dayOfWeek);
			db.close();
			
			/* if we add to custom interval */
			if (iid > 0){
				IntervalID = iid;
				showMaidenDialog();
				return;
			}
			
			if (intervals.length > 1){
				/* let's ask for event id to remove */
				DialogSelectInterval.showForResult(
					this,
					DIALOG_SELECT_INTERVAL,
					new String[]{ACTION_QUESTION,PARAM_FLAT_ID,PARAM_DATE},
					new String[]{"Select event to bind with",""+FlatID,date.toString()}
				);
			}
			/* when no sence to show dialog moving to next step */
			else {
				if (intervals.length == 1){
					IntervalID = intervals[0];
				}
				else if (intervals.length == 0){
					IntervalID = -1;
				}
				showMaidenDialog();
			}
		}
	}
	
	private void showMaidenDialog(){
		if (maidens.length > 1){
			DialogSelectMaiden.showForResult(
					this,
					DIALOG_SELECT_MAIDEN,
					new String[]{ACTION_QUESTION,PARAM_DATE},
					new String[]{"Select Maiden",date.toString()}
				);
		}
		
		/* when no sence to show maiden dialog */
		else if (maidens.length == 1){
			MaidenID = maidens[0];
			mAsyncTaskManager.setupTask(new AddCleaning(context,FlatID,IntervalID,MaidenID,date));
		}
		else {
			Print.err("No maidens to select!");
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case DIALOG_SELECT_MAIDEN:{
				if (resultCode == RESULT_OK){
					MaidenID = data.getExtras().getInt(PARAM_MAIDEN_ID);
					mAsyncTaskManager.setupTask(new AddCleaning(context,FlatID,IntervalID,MaidenID,date));
				}
				else if (resultCode == DialogSelect.RESULT_ERROR){
					finish();
				}
				else if(resultCode == RESULT_CANCELED){
					finish();
				}
				break;
			}
			case DIALOG_SELECT_INTERVAL:{
				if (resultCode == RESULT_OK){
					IntervalID = data.getExtras().getInt(PARAM_INTERVAL_ID);
					showMaidenDialog();
				}
				else if (resultCode == DialogSelect.RESULT_ERROR){
					finish();
				}
				else if(resultCode == RESULT_CANCELED){
					finish();
				}
				else if (resultCode == RESULT_NOTHING_TO_SELECT){
					IntervalID = 0;
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
		Intent newIntent = new Intent(context, DialogAddCleaning.class);
		newIntent.putExtra(ACTION_DO,ACTION_ADD_CLEANING);
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
