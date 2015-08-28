package by.hut.flat.calendar.advanced.maiden;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class AdvancedMaidenActivity extends Activity{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.advanced.maiden.AdvancedMaidenActivity";
	
	private Context context;
	private AdvancedMaidenListController maidenListController;
	private LinearLayout container;
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
		this.setContentView(R.layout.advanced_maiden_activity);
		context = this;
		maidenListController = new AdvancedMaidenListController(context);
		container = (LinearLayout) this.findViewById(R.id.container);
		container.addView(maidenListController);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(!maidenListController.actOnKeyDown()){
	    		finish();
	    	}
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	public static void show(Context context){
		Intent intent = new Intent(context, AdvancedMaidenActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		context.startActivity(intent);
	}
	
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
}
