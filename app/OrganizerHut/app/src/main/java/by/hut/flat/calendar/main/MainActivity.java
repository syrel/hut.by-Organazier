package by.hut.flat.calendar.main;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.advanced.AdvancedActivity;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.Timer;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Print;
import by.hut.flat.calendar.utils.Utils;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.main.MainActivity";
	public static final String ACTION_FLAT_CALENDAR_LOADED = "flat_calendar_loaded";
	public static final String ACTION_RESTART = "restart";
	
	public static final int SAUSAGE = 0;
	public static final int CALENDAR = 1;
	public static final int MENU = 2;
	
	private TabHost mTabHost;
	private TabSpec sausageTab;
	private TabSpec calendarTab;
	private TabSpec menuTab;
	
	private Context context;
	private boolean receiverRegistered = false;
	private boolean configInitialized = false;
	private boolean viewInitialized = false;
	private Timer timer;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override 
		public void onReceive(Context context, Intent intent) { 
			String action = intent.getStringExtra("do"); 
			Print.log("MainActivity recieve: "+action);
			if (action.equals("continueLoading")){
				initView();
			}
			else if (action.equals("timeElapsed")){
				Print.log("Load time: "+timer.timeElapsed()+"ms");
			}
			else if (action.equals("Sausage")){
	        	mTabHost.setCurrentTab(SAUSAGE);
	        }
	        else if (action.equals("Calendar")){
	        	mTabHost.setCurrentTab(CALENDAR);
	        }
	        else if (action.equals("Menu")){
	        	mTabHost.setCurrentTab(MENU);
	        }
	        else if (action.equals(ACTION_FLAT_CALENDAR_LOADED)){
	        	setCalendarTabActiv();
	        }
			else if (action.equals(ACTION_RESTART)){
				recreate();
	        }
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timer = new Timer();
		this.context = this;
		initReceiver(); 	// must be run first!
		initConfig();		// must be run after reciever
		
		/* make sure only one instance is running */
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
		}
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
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
	private void initConfig(){
		if (!configInitialized){
			Config.INST.init(context);
			System.out.println(Config.INST);
			configInitialized = true;
		}
	}
	private void initView(){
		if (!viewInitialized) {
			setContentView(R.layout.main_tab_host);
			setupTabHost();
			viewInitialized = true;
		}
	}
	
	/*------------------------------------------------------------
	--------------------------- S E T U P ------------------------
	------------------------------------------------------------*/
	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
		// Setting tab host height
		LayoutParams layoutParams = mTabHost.getTabWidget().getLayoutParams();
		layoutParams.height = Config.INST.MAIN.TAB_HOST_HEIGHT;
		mTabHost.getTabWidget().setLayoutParams(layoutParams);
		

		//add tabs and activities
		initSausageTab();
		initCalendarTab();
		initMenuTab();
		
		mTabHost.getTabWidget().setVisibility(View.VISIBLE);
		
	}
	
	private void initSausageTab(){
		String name = Utils.getString(context, R.string.main_tab_host_sausage_name);
		View sausageTabView = createTabView(mTabHost.getContext(), name);
		Intent intent = new Intent().setClass(this, SausageActivity.class);
		sausageTab = mTabHost.newTabSpec(""+SAUSAGE).setIndicator(sausageTabView).setContent(intent);
		mTabHost.addTab(sausageTab);
	}
	
	private void initCalendarTab(){
		String name = Utils.getString(context, R.string.main_tab_host_calendar_name);
		View calendarTabView = createTabView(mTabHost.getContext(), name);
		Intent intent = new Intent().setClass(this, FlatActivity.class);
		calendarTab = mTabHost.newTabSpec(""+CALENDAR).setIndicator(calendarTabView).setContent(intent);
		mTabHost.addTab(calendarTab);
		mTabHost.setCurrentTab(CALENDAR);
		mTabHost.setCurrentTab(SAUSAGE);
		mTabHost.getTabWidget().getChildTabViewAt(CALENDAR).setEnabled(false);
	}
	
	private void initMenuTab(){
		String name = Utils.getString(context, R.string.main_tab_host_advanced_name);
		View menuTabView = createTabView(mTabHost.getContext(), name);
		Intent intent = new Intent().setClass(this, AdvancedActivity.class);
		menuTab = mTabHost.newTabSpec(name).setIndicator(menuTabView).setContent(intent);
		mTabHost.addTab(menuTab);
	}

	private void setCalendarTabActiv(){
		mTabHost.getTabWidget().getChildTabViewAt(CALENDAR).setEnabled(true);
	}
	

	/**
	 * Creates view of tabs.
	 * @param context
	 * @param text
	 * @return
	 */
	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.main_tab_host_bg, null);
		TextView title = (TextView) view.findViewById(R.id.tab_title);
		title.setText(text);
		return view;
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	@Override
    protected void onResume() {
        initReceiver();
        BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"scroll_to_today"});
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
