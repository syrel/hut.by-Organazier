package by.hut.flat.calendar.dialog;

import java.util.LinkedList;
import by.hut.flat.calendar.core.Config;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public abstract class Dialog extends Activity implements IDialog{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.Dialog";
	
	public static final int DIALOG_SELECT_INTERVAL = 1;
	public static final int DIALOG_SELECT_MAIDEN = 2;
	public static final int DIALOG_SELECT_REMOVE_CLEANINGS = 3;
	public static final int DIALOG_SELECT_CLEANING = 4;
	public static final int DIALOG_FLOATING_SELECT_INTERVAL = 5;
	public static final int DIALOG_FLOATING_SELECT_CLEANINGS = 6;
	public static final int DIALOG_FLOATING_SELECT_DATES = 7;
	public static final int DIALOG_BACKUP_CREATE = 8;
	
	public static final String PARAM_FLAT_ID = "FlatID";
	public static final String PARAM_INTERVAL_ID = "IntervalID";
	public static final String PARAM_MAIDEN_ID = "MaidenID";
	public static final String PARAM_DATE = "date";
	public static final String PARAM_FIRSTDATE = "firstDate";
	public static final String PARAM_LASTDATE = "lastDate";
	public static final String PARAM_CLEANING_IDS = "cleaningIDs";
	public static final String PARAM_CLEANING_ID = "cleaningID";
	
	public static final String ACTION_DO = "do";
	public static final String ACTION_QUESTION = "question";
	public static final String ACTION_LOG = "log";
	public static final String ACTION_ASK = "ask";
	public static final String ACTION_AUTO_HIDE = "autohide";
	public static final String ACTION_HIDDEN = "hidden";
	public static final String ACTION_PROGRESS = "progress";
	public static final String ACTION_QUICK_HIDE = "quick_hide";
	
	public static final String ACTION_SELECT_INTERVAL = "select_interval";
	public static final String ACTION_SELECT_MAIDEN = "select_maiden";
	public static final String ACTION_SELECT_CLEANING = "select_cleaning";
	public static final String ACTION_SELECT_REMOVE_CLEANINGS = "select_remove_cleanings";
	public static final String ACTION_ADD_CLEANING = "add_cleaning";
	public static final String ACTION_REMOVE_CLEANING = "remove_cleaning";
	public static final String ACTION_BOOK = "book";
	public static final String ACTION_RENT = "rent";
	public static final String ACTION_FREE = "free";
	public static final String ACTION_CONVERT = "convert";
	public static final String ACTION_CONVERT_INTERVAL = "convert_interval";
	public static final String ACTION_INIT_DB = "init_db";
	public static final String ACTION_OK_CANCEL = "ok_cancel";
	public static final String ACTION_BACKUP = "backup";
	public static final String ACTION_RESTORE = "restore";
	
	public static final String ACTION_YES = "yes";
	public static final String ACTION_NO = "no";
	
	public final static int RESULT_ERROR = -404;
	public final static int RESULT_NOTHING_TO_SELECT = -503;
	
	private boolean receiverRegistered = false;
	protected Context context = this;
	protected Bundle extras;
	private LinkedList<String> paramKeys;
	
	protected String Do;
	protected String Question;
	/* default values */
	protected boolean Ask = true;
	protected boolean Log = true;
	protected boolean Progress = true;
	protected boolean AutoHide = false;
	protected boolean Hidden = false;
	protected boolean QuickHide = false;
	
	protected LinearLayout dialogLayout;
	protected TextView logText;
	protected ScrollView logScroll;
	private String log = "";
	
	protected Button ok;
	protected Button cancel;
	protected Button close;
	
	private boolean copy = false;
	
	private boolean invariant(){
		return context != null;
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() { 
		@Override 
		public void onReceive(Context context, Intent intent) { 
			String action = intent.getStringExtra(ACTION_DO);
			if (action == null) return;
			if (action.equals(ACTION_LOG)){
				String l = intent.getStringExtra(ACTION_LOG); 
				log = Join(log,l,"\n");
				if (logText != null){
					logText.setText(log);
					logScroll.post(new Runnable() {
						@Override
						public void run() {
							logScroll.fullScroll(ScrollView.FOCUS_DOWN);
						}
					});
				}
			}
		} 
	};
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* make sure only one instance running */
		if (!Config.INST.isRunning(this,getActivityTag())){
			Config.INST.addToRuningActivitiesList(getActivityTag());
		}
		else {
			this.copy = true;
			finish();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) this.setFinishOnTouchOutside(false);
		initDialog();
		
		assert invariant();
	}
	
	private void initDialog(){
		initReceiver();
		if (initParams()){
			initDo();
			initQuestion();
			initAsk();
			initLog();
			initAutoHide();
			initHidden();
			initProgress();
			initQuickHide();
		}
		initView();
	}
	
	/**
	 * Initializes initial view of dialog
	 */
	@SuppressLint("NewApi")
	private void initView(){
		//Hidding title
		if (Hidden || !Ask){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)this.getWindow().setDimAmount(0);
		}
		setTitle(Question);
		
		onInitContentView();
		
		// Hidding window
		if ((Hidden || !Ask) && dialogLayout != null) {
			dialogLayout.setVisibility(8);
			View view = this.getWindow().getDecorView();
			view.getRootView().setVisibility(8);
		    view.setBackgroundColor(0x00000000);
			this.setVisible(false);
		}
		
		onInitElements();
	}
	
	/**
	 * Initializes receiver. If receiver is already initialized, nothing happens.
	 */
	private void initReceiver(){
		if (!receiverRegistered){
			IntentFilter ifilt = new IntentFilter(ACTIVITY_TAG); 
			registerReceiver(mReceiver, ifilt);
			receiverRegistered = true;
		}
	}
	/**
	 * Uninitializes receiver. If receiver is already uninitialized, nothing happens.
	 */
	private void unInitReceiver(){
		if (receiverRegistered){
			unregisterReceiver(mReceiver);
			receiverRegistered = false;
		}
	}
	/**
	 * Initializes params, that was bekommen by dialog.
	 */
	private boolean initParams(){
		extras = getIntent().getExtras();
		if (extras == null) {
			return false;
		}
		paramKeys = new LinkedList<String>(extras.keySet());
		if (paramKeys.size() == 0) return false;
		return true;
	}
	
	/**
	 * Returns {@code String} object holidng parameter value. 
	 * @param key - {@code String} param's key.
	 * @return {@code String} - param value.
	 */
	protected String getParam(String key){
		assert key != null;
		assert key.length() > 0;
		return extras.getString(key);
	}
	
	/**
	 * Gets 'do' parameter. This parameter always should be passed.
	 */
	private void initDo(){
		Do = getParam(ACTION_DO);
	}
	
	/**
	 * Gets 'Question' that will be displayed as title of dialog.
	 * It hepls user to understand what will be task doing.
	 */
	private void initQuestion(){
		if (paramKeys.contains(ACTION_QUESTION)){
			Question = getParam(ACTION_QUESTION);
		}
		else {
			Question = "";
		}
	}
	
	/**
	 * Gets 'Ask' boolean parameter. It says whether user
	 * should be asked if he acepts action.
	 */
	private void initAsk(){
		if (paramKeys.contains(ACTION_ASK)){
			String param = getParam(ACTION_ASK);
			if (param.equals(ACTION_YES)){
				Ask = true;
			}
			else if (param.equals(ACTION_NO)){
				Ask = false;
			}
			else {
				System.err.println(ACTION_ASK+" - Unknown param: "+param);
			}
		}
	}
	
	/**
	 * Gets 'Log' parameter. If Log is {@code True} log will be displayed,
	 * otherwise no.
	 */
	private void initLog(){
		if (paramKeys.contains(ACTION_LOG)){
			String param = getParam(ACTION_LOG);
			if (param.equals(ACTION_YES)){
				Log = true;
			}
			else if (param.equals(ACTION_NO)){
				Log = false;
			}
			else {
				System.err.println(ACTION_LOG+" - Unknown param: "+param);
			}
		}
	}
	/**
	 * Gets 'AutoHide' parameter. If AutoHide is {@code True} dialog will hide after task ist complete,
	 * otherwise no.
	 */
	private void initAutoHide(){
		if (paramKeys.contains(ACTION_AUTO_HIDE)){
			String param = getParam(ACTION_AUTO_HIDE);
			if (param.equals(ACTION_YES)){
				AutoHide = true;
			}
			else if (param.equals(ACTION_NO)){
				AutoHide = false;
			}
			else {
				System.err.println(ACTION_AUTO_HIDE+" - Unknown param: "+param);
			}
		}
	}
	/**
	 * Gets 'Hidden' parameter. If Hidden is {@code True} dialog will be hidden,
	 * otherwise no.
	 */
	private void initHidden(){
		if (paramKeys.contains(ACTION_HIDDEN)){
			String param = getParam(ACTION_HIDDEN);
			if (param.equals(ACTION_YES)){
				Hidden = true;
			}
			else if (param.equals(ACTION_NO)){
				Hidden = false;
			}
			else {
				System.err.println(ACTION_HIDDEN+" - Unknown param: "+param);
			}
		}
	}
	
	/**
	 * Gets 'Progress' parameter. If Progress is {@code True} progress bar will be visible,
	 * otherwise no.
	 */
	private void initProgress(){
		if (paramKeys.contains(ACTION_PROGRESS)){
			String param = getParam(ACTION_PROGRESS);
			if (param.equals(ACTION_YES)){
				Progress = true;
			}
			else if (param.equals(ACTION_NO)){
				Progress = false;
			}
			else {
				System.err.println(ACTION_PROGRESS+" - Unknown param: "+param);
			}
		}
	}
	
	/**
	 * Gets 'Progress' parameter. If Progress is {@code True} progress bar will be visible,
	 * otherwise no.
	 */
	private void initQuickHide(){
		if (paramKeys.contains(ACTION_QUICK_HIDE)){
			String param = getParam(ACTION_QUICK_HIDE);
			if (param.equals(ACTION_YES)){
				QuickHide = true;
			}
			else if (param.equals(ACTION_NO)){
				QuickHide = false;
			}
			else {
				System.err.println(ACTION_QUICK_HIDE+" - Unknown param: "+param);
			}
		}
	}
	/*------------------------------------------------------------
	--------------------- T O   O V E R R I D E ------------------
	------------------------------------------------------------*/

	public abstract void onInitContentView();

	public abstract void onInitElements();
	
	public abstract void onInitButtons();
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	private String Join(String line, String add,String delimiter){
		return (line.length() == 0) ? add : line+delimiter+add;
	}
	/**
	 * Deactivates close by back button
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
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
		if (!copy)Config.INST.removeFromRuningActivitiesList(getActivityTag());
		super.onDestroy();
	}

	public abstract String getActivityTag();
	
	@Override
	public LinearLayout getLayout(){
		return this.dialogLayout;
	}
}
