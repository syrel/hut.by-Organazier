package by.hut.flat.calendar.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.utils.Utils;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class StatisticsActivity extends SherlockFragmentActivity {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.statistics.StatisticsActivity";
	
	public static final int CLEANING_FIRST_DAY = 21;
	public static final int CLEANING_LAST_DAY = 20;
	
	Context context = this;
	LinearLayout maidenContainer;
	LinearLayout profitContainer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
		}
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_striped));
		getSupportActionBar().setTitle(Utils.getString(this, R.string.activity_statistics_name));

		StatisticPageBuild();
	}

	public static void show(Context context){
		Intent intent = new Intent(context, StatisticsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		context.startActivity(intent);
	}

	private void StatisticPageBuild(){
		setContentView(R.layout.statistics_activity);
		
		initView();
	}
	
	private void initView(){
		maidenContainer = (LinearLayout)findViewById(R.id.maiden);
		profitContainer = (LinearLayout)findViewById(R.id.profit);
		maidenContainer.addView(new StatisticsMaiden(context));
		maidenContainer.addView(new StatisticsProfit(context));
	}
	
	
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		Config.INST.removeFromRuningActivitiesList(ACTIVITY_TAG);
		super.onDestroy();
	}
}
