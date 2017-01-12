package by.hut.flat.calendar.core;

import java.util.ArrayList;
import java.util.LinkedList;

import by.hut.flat.calendar.core.config.Calendar;
import by.hut.flat.calendar.core.config.Database;
import by.hut.flat.calendar.core.config.IPreferences;
import by.hut.flat.calendar.core.config.Main;
import by.hut.flat.calendar.core.config.Sausage;
import by.hut.flat.calendar.core.config.Display;
import by.hut.flat.calendar.core.config.Strings;
import by.hut.flat.calendar.core.config.System;

import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.utils.Print;
import android.content.Context;

public enum Config implements Database.OnDatabaseInitializedListener{
	INST;
	private Context context;
	
	public static final String PACKAGE_NAME = "by.hut.flat.calendar";
	
	private Config(){}

	public System SYSTEM;
	public Strings STRINGS;
	public Display DISPLAY;
	public Main MAIN;
	public Sausage SAUSAGE;
	public Calendar CALENDAR;
	public Database DATABASE;
	
	public boolean REINIT = false;
	
	private LinkedList<String> runningActivities;
	private ArrayList<IPreferences> preferences;
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	public void init(Context context){
		this.context = context;
		assert context != null;
		runningActivities = new LinkedList<String>();
		preferences = new ArrayList<IPreferences>();
		
		this.SYSTEM = new System(context);
		this.STRINGS = new Strings(context);
		this.DISPLAY = new Display(context);
		this.MAIN = new Main(context);
		this.SAUSAGE = new Sausage(context);
		this.CALENDAR = new Calendar(context);
		this.DATABASE = new Database(context,this);
		
		preferences.add(SYSTEM);
		preferences.add(STRINGS);
		preferences.add(DISPLAY);
		preferences.add(MAIN);
		preferences.add(SAUSAGE);
		preferences.add(CALENDAR);
		preferences.add(DATABASE);
	}
	/*------------------------------------------------------------
	-------------------------- C H E C K S -----------------------
	------------------------------------------------------------*/

	public boolean isRunning(Context context,String activityName){
		if (runningActivities == null) {
			init(context);
			Print.log("Reinit Config from: "+activityName);
		}
		return runningActivities.contains(activityName);
	}

	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onDatabaseInitialized() {
		askMainActivityToContinueLoading();
	}
	

	public void addToRuningActivitiesList(String activityName){
		assert !this.runningActivities.contains(activityName);
		Print.log("Starting: "+activityName);
		this.runningActivities.add(activityName);
	}
	
	public void removeFromRuningActivitiesList(String activityName){
		Print.log("Destroying: "+activityName);
		this.runningActivities.remove(activityName);
	}
	
	public void finishApp(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void reInit(){
		for (IPreferences preference : preferences){
			preference.reInit();
		}
		BroadcastSender.send(context, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{MainActivity.ACTION_RESTART});
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	private void askMainActivityToContinueLoading(){
		Print.log("asking main to start");
		BroadcastSender.send(this.context, MainActivity.ACTIVITY_TAG,
				new String[]{"do"}, new String[]{"continueLoading"});
	}
	
	public String toString(){
		return "";
	}
}
