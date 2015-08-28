package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.core.DBAdapter.tasks.Rent;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.content.Context;
import android.content.Intent;

public class DialogRent extends DialogThread {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogRent";
	
	private Date firstDate;
	private Date lastDate;
	private int FlatID;
	
	@Override
	public void implementTask(){
		if (Do.equals(ACTION_RENT)){
			FlatID = Utils.Int(this.getParam(PARAM_FLAT_ID));
			String firstDateStr = this.getParam(PARAM_FIRSTDATE);
			String lastDateStr = this.getParam(PARAM_LASTDATE);
			assert FlatID >= 0;
			assert firstDateStr != null;
			assert firstDateStr.length() > 0;
			assert lastDateStr != null;
			assert lastDateStr.length() > 0;
			
			firstDate = new Date(firstDateStr);
			lastDate = new Date(lastDateStr);
			
			int dayOfWeek = new CalendarHelper(lastDate).getDayOfWeek();
			
			DBAdapter db = new DBAdapter(context);
			db.open();
			Maiden dbMaiden = new Maiden(db);
			int[] maidens = dbMaiden.getMaidenAtDay(dayOfWeek);
			db.close();
			
			if (maidens.length > 1){
				DialogSelectMaiden.showForResult(
						this,
						DIALOG_SELECT_MAIDEN,
						new String[]{ACTION_QUESTION,PARAM_DATE},
						new String[]{this.getParam(ACTION_QUESTION),lastDate.toString()}
					);
			}
			else if (maidens.length == 1){
				int maidenID = maidens[0];
				mAsyncTaskManager.setupTask(new Rent(context,FlatID,firstDate,lastDate,maidenID));
			}
			else {
				int maidenID = -1;
				mAsyncTaskManager.setupTask(new Rent(context,FlatID,firstDate,lastDate,maidenID));
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case DIALOG_SELECT_MAIDEN:{
				if (resultCode == RESULT_OK){
					int maidenID = data.getExtras().getInt(PARAM_MAIDEN_ID);
					mAsyncTaskManager.setupTask(new Rent(context,FlatID,firstDate,lastDate,maidenID));
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
	
	public static void show(Context context, String[] keys,String[] params){
		Intent newIntent = new Intent(context, DialogRent.class);
		newIntent.putExtra(ACTION_DO,ACTION_RENT);
		newIntent.putExtra(ACTION_QUICK_HIDE,ACTION_YES);
		newIntent.putExtra(ACTION_AUTO_HIDE, ACTION_YES);
		newIntent.putExtra(ACTION_ASK,ACTION_NO);
		newIntent.putExtra(ACTION_LOG, ACTION_NO);
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
