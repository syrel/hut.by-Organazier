package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class AdvancedFlatsActivity extends Activity{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.advanced.flats.AdvancedFlatsActivity";
	
	private Context context;
	private LinearLayout container;
	private AdvancedFlatListController flatListController;
	
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
		this.setContentView(R.layout.advanced_flats_activity);
		context = this;
		flatListController = new AdvancedFlatListController(context);
		container = (LinearLayout) this.findViewById(R.id.container);
		container.addView(flatListController);
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(!flatListController.actOnKeyDown()){
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
		Intent intent = new Intent(context, AdvancedFlatsActivity.class);
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
