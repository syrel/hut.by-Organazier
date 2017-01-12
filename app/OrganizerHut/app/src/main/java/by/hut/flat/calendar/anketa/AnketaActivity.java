package by.hut.flat.calendar.anketa;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.anketa.AnketaPager.OnLoadListener;
import by.hut.flat.calendar.anketa.AnketaPager.OnModeChangedListener;
import by.hut.flat.calendar.anketa.AnketaPager.OnPageSelectedListener;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalFlat;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.dialog.DialogConvert;
import by.hut.flat.calendar.dialog.floating.select.dates.DialogFloatingSelectDates;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AnketaActivity extends Activity implements OnClickListener, OnPageSelectedListener, OnLoadListener{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.anketa.AnketaActivity";
	public static final String PARAM_FLATID = "FlatID";
	public static final String PARAM_DATE = "Date";
	public static final String PARAM_INTERVAL_ID = "IntervalID";
	public static final String PARAM_AUTO_EDIT = "AutoEdit";
	
	private static final String BUTTON_EDIT_TAG = "edit";
	private static final String BUTTON_SAVE_TAG = "save";
		
	private Context context;
	private int FlatID;
	private DBAdapter db;
	private Button main;
	private Button edit;
	private Button dates;
	private Button convert;
	private AnketaPager pager;
	
	/**
	 * 0 - view, 1 - edit
	 */
	private int[] pageStates;
	private int[] intervalIDs;
	private Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindow();
		/* make sure only one instance is running */
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
			return;
		}
		extras = getIntent().getExtras();
		if (extras == null) {
			finish();
			return;
		}
		this.context = this;
		db = new DBAdapter(this.context);
		
		int IntervalID = extras.getInt(PARAM_INTERVAL_ID);
		if (IntervalID > 0) initIntervalID(IntervalID);
		else initIntervalIDs();
		initView();
	}
	
	private void initWindow(){
		
	}
	
	private void initIntervalID(int IntervalID){
		db.open();
		IntervalFlat intervalFlatDB = new IntervalFlat(db);
		FlatID = intervalFlatDB.getData(IntervalID)[IntervalFlat.FID];
		intervalIDs = new int[]{IntervalID};
		db.close();
	}
	
	private void initIntervalIDs(){
		FlatID = extras.getInt(PARAM_FLATID);
		if (FlatID <= 0) finish();
		Date date = new Date(extras.getString(PARAM_DATE));
		db.open();
		Interval intervalDB = new Interval(db);
		intervalIDs = intervalDB.findIntervals(FlatID, date);
		db.close();
	}
	
	
	/*********************************************************************************************/
	/********************************A N K E T A   P A G E****************************************/
	/*********************************************************************************************/

	/********************************ANKETA PAGE*************************************/
	public void initView(){
		if (intervalIDs.length == 0) {
			Toast.makeText(this, "Нет анкет", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		pageStates = new int[intervalIDs.length];
		setContentView(R.layout.anketa_activity);

		pager = (AnketaPager) this.findViewById(R.id.anketa_pager);
		pager.setOnLoadListener(this);
		pager.initPages(FlatID, intervalIDs);
		pager.setOnPageSelectedListener(this);
		pager.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				if (edit.getTag().equals(BUTTON_EDIT_TAG)){
					editAction();
				}
				return false;
			}
		});
		pager.setOnModeChangedListener(new OnModeChangedListener(){
			@Override
			public void onModeChanged(int mode, int pageIndex) {
				if (mode == 0)initEditButtonEdit(pageIndex);
				else initEditButtonSave(pageIndex);
			}
			
		});

		initNavigation();
	}
	
	private void initNavigation(){
		main = (Button) this.findViewById(R.id.main);
		edit = (Button) this.findViewById(R.id.edit);
		dates = (Button) this.findViewById(R.id.dates);
		convert = (Button) this.findViewById(R.id.convert);
		main.setOnClickListener(this);
		edit.setOnClickListener(this);
		setEditButtonEdit();
		dates.setOnClickListener(this);
		convert.setOnClickListener(this);
	}
	
	private void setEditButtonEdit(){
		initEditButtonEdit(pager.getCurrentPage());
	}
	
	private void setEditButtonSave(){
		initEditButtonSave(pager.getCurrentPage());
	}
	
	private void initEditButtonEdit(int index){
		if (index == pager.getCurrentPage()){
			edit.setTag(BUTTON_EDIT_TAG);
			edit.setText(context.getResources().getString(R.string.anketa_button_edit));
		}
		pageStates[index] = 0;
	}
	
	private void initEditButtonSave(int index){
		if (index == pager.getCurrentPage()){
			edit.setTag(BUTTON_SAVE_TAG);
			edit.setText(context.getResources().getString(R.string.anketa_button_save));
		}
		pageStates[index] = 1;
	}
	
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onLoad(AnketaPager pager) {
		if(extras.getBoolean(PARAM_AUTO_EDIT)){
			editAction();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.main:{
				mainAction();
				break;
			}
			case R.id.edit:{
				if (v.getTag().equals(BUTTON_EDIT_TAG)){
					editAction();
				}
				else if (v.getTag().equals(BUTTON_SAVE_TAG)){
					saveAction();
				}
				break;
			}
			case R.id.dates:{
				datesAction();
				break;
			}
			case R.id.convert:{
				convertAction();
				break;
			}
		}
	}

	@Override
	public void onPageSelected(int index) {
		switch (pageStates[index]){
			case 0:{
				setEditButtonEdit();
				break;
			}
			case 1:{
				setEditButtonSave();
				break;
			}
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (pageStates[pager.getCurrentPage()]){
				case 0:{
					break;
				}
				case 1:{
					saveAction();
					return true;
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (pageStates[pager.getCurrentPage()] == 1){
				saveAction();
			}
			else if (pageStates[pager.getCurrentPage()] == 0){
				mainAction();
			}
		}
	    return super.onKeyDown(keyCode, event);
	}
	
	private void mainAction(){
		BroadcastSender.send(context, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"Sausage"});
		finish();
	}
	
	private void editAction(){
		assert pager != null;
		assert pageStates[pager.getCurrentPage()] == 0;
		pager.switchToEdit();
		setEditButtonSave();
	}
	
	private void saveAction(){
		assert pager != null;
		assert pageStates[pager.getCurrentPage()] == 1;
		Utils.hideKeyboard(this);
		pager.save();
		setEditButtonEdit();
	}
	
	private void datesAction(){
		Bundle extra = new Bundle();
		extra.putInt(Dialog.PARAM_INTERVAL_ID, intervalIDs[pager.getCurrentPage()]);
		DialogFloatingSelectDates.show(this, dates, extra);
	}
	
	private void convertAction(){
		DialogConvert.show(context, ""+FlatID, intervalIDs[pager.getCurrentPage()]);
		pager.convertType(pager.getCurrentPage());
	}
	/*------------------------------------------------------------
	------------------------- D E F A U L T ----------------------
	------------------------------------------------------------*/
	
	public static void show(Context context,int FlatID, String date){
		Intent intent = new Intent(context, AnketaActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.putExtra(PARAM_FLATID, FlatID);
		intent.putExtra(PARAM_DATE, date);
		context.startActivity(intent);
	}
	
	public static void show(Context context,int IntervalID,boolean autoEdit){
		Intent intent = new Intent(context, AnketaActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.putExtra(PARAM_INTERVAL_ID, IntervalID);
		intent.putExtra(PARAM_AUTO_EDIT, autoEdit);
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
