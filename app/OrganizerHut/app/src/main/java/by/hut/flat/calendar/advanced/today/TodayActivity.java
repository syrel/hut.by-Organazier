package by.hut.flat.calendar.advanced.today;

import java.util.ArrayList;
import java.util.Arrays;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.DateView;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.anketa.AnketaList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class TodayActivity extends SherlockFragmentActivity {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.advanced.today.TodayActivity";

	private Context context;
	private DBAdapter db;
	private IntervalAnketa iaDB;
	private LinearLayout container;
	
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
		getSupportActionBar().setTitle(Utils.getString(this, R.string.activity_today_name)+" "+DateView.toAnketa(Config.INST.SYSTEM.TODAY));
		init();
	}
	
	private void init(){
		setContentView(R.layout.today_activity);
		this.context = this;
		db = new DBAdapter(context);
		iaDB = new IntervalAnketa(db);
		container = (LinearLayout) findViewById(R.id.container);
		ArrayList<String[]> result = new ArrayList<String[]>(Arrays.asList(iaDB.getEvictionDataByDate(new Date())));
		result.addAll(new ArrayList<String[]>(Arrays.asList(iaDB.getSettlementDataByDate(new Date()))));
		AnketaList list = new AnketaList(context,result);
		container.addView(list.getView());
	}
	
	public static void show(Context context){
		Intent intent = new Intent(context, TodayActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		context.startActivity(intent);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
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
