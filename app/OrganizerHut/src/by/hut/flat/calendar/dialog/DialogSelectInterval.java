package by.hut.flat.calendar.dialog;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

public class DialogSelectInterval extends DialogSelect implements OnCheckedChangeListener{
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.dialog.DialogSelectEvent";
	
	private static final int BOOK_BGCOLOR = 0x3300FF00;
	private static final int RENT_BGCOLOR = 0x332bc0ff;
	
	private RadioGroup radioGroup;
	private int radioButtonChoice;
	private ScrollView scroll;
	private DBAdapter db;
	private int FlatID;
	private Date date;
	private int[] intervals;
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	/**
	 * <p><b><i>user should provide:</br>
	 * -PARAM_FLAT_ID</br>
	 * -PARAM_DATE
	 * </p></b></i>
	 * @param activity
	 * @param requestCode
	 * @param keys
	 * @param params
	 */
	public static void showForResult(Activity activity,int requestCode,String[] keys,String[] params){	
		Intent newIntent = new Intent(activity, DialogSelectInterval.class);
		newIntent.putExtra(ACTION_DO,ACTION_SELECT_INTERVAL);
		for (int i = 0; i < keys.length; i++){
			newIntent.putExtra(keys[i], params[i]);
		}
		activity.startActivityForResult(newIntent,requestCode);
	}
	

	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	@Override
	protected void initSelectLayout(){
		initDialog();
		initRadioGroup();
		initRadioButtons();
	}
	
	private void initDialog(){
		db = new DBAdapter(context);
		FlatID = Utils.Int(getParam(PARAM_FLAT_ID));
		date = new Date(getParam(PARAM_DATE));
	}
	private void initRadioGroup(){
		ok.setClickable(false);
		ok.setTextColor(INACTIVE_COLOR);
		scroll = new ScrollView(context);
		radioGroup = new RadioGroup(context);
		radioGroup.setOnCheckedChangeListener(this);
		scroll.addView(radioGroup);
		container.addView(scroll);
	}
	private void initRadioButtons(){
		db.open();
		Interval intervalDB = new Interval(db);
		IntervalAnketa intervalAnketaDb = new IntervalAnketa(db);
		/* get all events at this day */
		intervals = intervalDB.findIntervals(FlatID, date);
		if (intervals.length == 0){
			db.close();
			Intent intent = new Intent();
			setResult(RESULT_NOTHING_TO_SELECT, intent);
			finish();
		}
		
		for (int i = 0; i < intervals.length;i++){
			RadioButton radioButton = new RadioButton(context);
			int[] iData = intervalDB.getData(intervals[i]);
			String[] aData = intervalAnketaDb.getData(intervals[i]);
			
			Date dateFrom = new Date(iData[Interval.DATE_FROM]);
			Date dateTo = new Date(iData[Interval.DATE_TO]);
			
			String text = " "+dateFrom.day+"-"+dateTo.day+"."+dateTo.month+
					"  " + aData[IntervalAnketa.NAME_LEFT] + " "+aData[IntervalAnketa.TELEPHONE_LEFT];
			radioButton.setText(text);
			
			if (Utils.Int(aData[IntervalAnketa.TYPE]) == dbStructure.RENT){
				radioButton.setBackgroundColor(RENT_BGCOLOR);
			}
			else if(Utils.Int(aData[IntervalAnketa.TYPE]) == dbStructure.BOOK){
				radioButton.setBackgroundColor(BOOK_BGCOLOR);
			}
			
			radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			radioButton.setButtonDrawable(R.drawable.btn_radio_holo_dark);
			
			radioButton.setTag(""+intervals[i]);
					
			addButton(radioButton);
			
			/* if there is only one event, checking it */
			if (intervals.length == 1){
				radioButton.setChecked(true);
			}
		}
		db.close();
	}
	private void addButton(RadioButton radioButton){
		radioGroup.addView(radioButton);
	}


	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	protected void okClick(){
		Intent intent = new Intent();
		int eventID = Utils.Int((String) radioGroup.findViewById(radioButtonChoice).getTag());
		if (eventID >= 0){
			intent.putExtra(PARAM_INTERVAL_ID, eventID);
			setResult(RESULT_OK, intent);
			finish();
		}
		else {
			finish();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		if (checkedId != 0){
			radioButtonChoice = checkedId;
			ok.setClickable(true);
			ok.setTextColor(Color.WHITE);
		}
		else {
			ok.setClickable(false);
			ok.setTextColor(INACTIVE_COLOR);
		}
		
	}
	
	@Override
	public String getActivityTag() {
		return ACTIVITY_TAG;
	}
	
	
}
