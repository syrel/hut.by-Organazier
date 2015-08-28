package by.hut.flat.calendar.flat;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.dialog.DialogAddCleaning;
import by.hut.flat.calendar.dialog.DialogRemoveCleaning;
import by.hut.flat.calendar.dialog.floating.select.cleanings.DialogFloatingSelectCleaning;
import by.hut.flat.calendar.flat.FlatCalendarPager.OnPrepareMenuListener;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Print;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class FlatActivity extends Activity implements OnPrepareMenuListener{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.flat.FlatActivity";

	private FlatCalendarPager calendarPager;
	private boolean receiverRegistered = false;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override 
		public void onReceive(Context context, Intent intent) { 
			String action = intent.getStringExtra("do");
			Print.log("FlatActivity recieve: "+action);
			if (action.equals("set_current_flat")){
				int FlatID = Utils.Int(intent.getStringExtra("FlatID")); 
				assert FlatID >= 0;
				calendarPager.setCurrentItem(FlatID);
			}
			else if (action.equals("refresh_flat")){
				int FlatID = Utils.Int(intent.getStringExtra("FlatID")); 
				String firstDate = intent.getStringExtra("firstDate");
				String lastDate = intent.getStringExtra("lastDate");
				assert FlatID >= 0;
				assert firstDate != null;
				assert firstDate.length() > 0;
				assert lastDate != null;
				assert lastDate.length() > 0;
				reDraw(FlatID,new Date(firstDate),new Date(lastDate));
			}
			else if (action.equals("load")){
				initView();
			}
		} 
	};
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* make sure only one instance running */
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
		}
		initReceiver();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		this.setContentView(R.layout.flat_activity);
		calendarPager = (FlatCalendarPager) findViewById(R.id.flat_calendar_pager);
		calendarPager.setOnPrepareMenuListener(this);
	}
	private void initReceiver(){
		if (!receiverRegistered){
			IntentFilter ifilt = new IntentFilter(ACTIVITY_TAG); 
			registerReceiver(mReceiver, ifilt);
			receiverRegistered = true;
		}
	}
	private void unInitReceiver(){
		if (receiverRegistered){
			unregisterReceiver(mReceiver);
			receiverRegistered = false;
		}
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	/**
	 * Redraws cells from firstDate to lastDate
	 * @param FlatID
	 * @param firstDate
	 * @param lastDate
	 */
	private void reDraw(int FlatID,Date firstDate,Date lastDate){
		calendarPager.reDraw(FlatID,firstDate,lastDate);
	}
	
	/*------------------------------------------------------------
	---------------------------- M E N U -------------------------
	------------------------------------------------------------*/

	@Override
	public void onPrepareMenu(View view,int menuID) {
		 switch (menuID){
		 	case R.id.cleaning:{
		 		DialogFloatingSelectCleaning.show(this, view, new Bundle());
		 		break;
		 	}
		 }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		switch (requestCode){
			case Dialog.DIALOG_FLOATING_SELECT_CLEANINGS:{
				if (resultCode == RESULT_OK){
					int CleaningID = data.getExtras().getInt(Dialog.PARAM_CLEANING_ID);
					switch(CleaningID){
						case R.id.cleaning_add:{
							addCleaning();
							break;
						}
						case R.id.cleaning_delete:{
							removeCleaning();
							break;
						}
					}
				}
				break;
			}
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (calendarPager.getSelectedCellNumber() > 0){
	    		calendarPager.unmarkSelected();
	    		return true;
	    	}
	    	else {
	    		BroadcastSender.send(this, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"Sausage"});
	    		return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	private void addCleaning(){
		assert calendarPager.getSelectedCellNumber() == 1;
		Date date = calendarPager.getFirstSelectedCellDate();
		int flatID = calendarPager.getCurrentFlatID();
		DialogAddCleaning.show(this, new String[]{Dialog.PARAM_FLAT_ID,Dialog.PARAM_DATE}, new String[]{""+flatID,date.toString()});
	}
	private void removeCleaning(){
		assert calendarPager.getSelectedCellNumber() == 1;
		if (calendarPager.getFirstCellBackground() != 0){
			Date date = calendarPager.getFirstSelectedCellDate();
			int flatID = calendarPager.getCurrentFlatID();
			DialogRemoveCleaning.show(this, new String[]{Dialog.PARAM_FLAT_ID,Dialog.PARAM_DATE}, new String[]{""+flatID,date.toString()});
		}
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	@Override
    protected void onResume() {
        initReceiver();
        super.onResume();
    }
	@Override
	protected void onPause(){
		super.onPause();
	}
	@Override
	protected void onStart(){
		initReceiver();
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		unInitReceiver();
		Config.INST.removeFromRuningActivitiesList(ACTIVITY_TAG);
		super.onDestroy();
	}
}
