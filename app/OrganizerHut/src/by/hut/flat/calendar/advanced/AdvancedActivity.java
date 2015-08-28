package by.hut.flat.calendar.advanced;

import java.util.ArrayList;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.widget.list.simple.IBackButtonObserver;
import by.hut.flat.calendar.widget.list.simple.IBackButtonSubject;
import by.hut.flat.calendar.widget.list.simple.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class AdvancedActivity extends Activity implements IBackButtonSubject{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.advanced.AdvancedActivity";

	private ArrayList<IBackButtonObserver> observers;
	private Context context;
	
	private List menu;
	private LinearLayout menuContainer;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/* make sure only one instance running */
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
		}
		initView();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		this.setContentView(R.layout.advanced_activity);
		observers = new ArrayList<IBackButtonObserver>();
		context = this;
		menuContainer = (LinearLayout) this.findViewById(R.id.container);
		menu = new Menu(context);
		menu.registerSubject(this);
		menuContainer.addView(menu.getView());
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (menu.isExpanded()){
	    		notifyObserversBackButton();
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
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	@Override
    protected void onResume() {
        super.onResume();
    }
	@Override
	protected void onPause(){
		super.onPause();
	}
	@Override
	protected void onStart(){
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		Config.INST.removeFromRuningActivitiesList(ACTIVITY_TAG);
		super.onDestroy();
	}
	@Override
	public void registerObserver(IBackButtonObserver observer) {
		assert observers != null;
		assert observer != null;
		assert !observers.contains(observer);
		observers.add(observer);
	}
	
	@Override
	public void removeObserver(IBackButtonObserver observer) {
		assert observers != null;
		assert observers.contains(observer);
		observers.remove(observer);
	}
	@Override
	public void notifyObserversBackButton() {
		assert observers != null;
		for (IBackButtonObserver observer : observers){
			observer.notifyBackButton();
		}
	}
}