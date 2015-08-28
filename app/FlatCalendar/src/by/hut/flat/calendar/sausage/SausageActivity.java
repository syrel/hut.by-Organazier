package by.hut.flat.calendar.sausage;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
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

public class SausageActivity extends Activity {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.sausage.SausageActivity";
	public static final String ACTION_FLAT_CALENDAR_LOADED = "flat_calendar_loaded";
	
	
	private SausageCalendar calendar;
	private boolean receiverRegistered = false;
	private SausageInfoBar infoBar;
		
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override 
		public void onReceive(Context context, Intent intent) { 
			String action = intent.getStringExtra("do");
			Print.log("SausageActivity recieve: "+action);
			if (action.equals("refresh_flat")){
				int FlatID = Utils.Int(intent.getStringExtra("FlatID")); 
				String firstDate = intent.getStringExtra("firstDate");
				String lastDate = intent.getStringExtra("lastDate");
				assert FlatID >= 0;
				assert firstDate != null;
				assert firstDate.length() > 0;
				assert lastDate != null;
				assert lastDate.length() > 0;
				reDraw(FlatID,new Date(firstDate),new Date(lastDate));
				infoBar.refresh();
			}
			else if (action.equals("refresh_warnings")){
				infoBar.refresh();
			}
			else if (action.equals("scroll_to_today")){
				calendar.scrollToToday();
			}
			else if (action.equals(ACTION_FLAT_CALENDAR_LOADED)){
				calendar.setFlatCalendarLoaded();
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
		initView();
	}
	
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		this.setContentView(R.layout.sausage_activity);
		initInfoBar();
		initCalendar();
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
	private void initCalendar(){
		calendar = (SausageCalendar) this.findViewById(R.id.sausage_calendar);
	}
	
	private void initInfoBar(){
		infoBar = (SausageInfoBar) findViewById(R.id.info_bar);
		infoBar.setHeight(Config.INST.SAUSAGE.INFO_BAR_HEIGHT);
		infoBar.setActivity(this);
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
		calendar.reDraw(FlatID,firstDate,lastDate);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		infoBar.onActivityResult(requestCode, resultCode, data);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	BroadcastSender.send(this, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"Menu"});
	    }
	    return super.onKeyDown(keyCode, event);
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
